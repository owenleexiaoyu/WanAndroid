# Nav

> 我要在wankmp模块下实现如图中所示的导航页，并替换现在app模块内的导航页。
> 新实现采用新技术栈：
>
> - KMP
> - ComposeMultiplatform
> - Ktrofit 用于实现网络请求
> - MVI 架构
> 选择适合当前项目的依赖版本，请先列出实现方案规划，不要直接写代码

下面是在**不引入 Ktorfit、Kotlin 维持 1.9.22（与当前根工程一致）**前提下的实现规划，仅作方案说明，不涉及具体代码。

1. 目标与边界

网络：只用 Ktor Client（HttpClient + 各平台 Engine），手写请求与响应解析，无 Retrofit/Ktorfit 式接口生成。
语言/构建：不升级 Kotlin，根工程 ext.kotlin_version 与 wankmp 中 Kotlin 依赖保持 1.9.22。
功能：在 wankmp 实现与截图一致的「导航」页（纵向分区 + 每区 FlowRow 标签）；在 app 替换原 NavFragment 的展示与入口。
数据：仍请求 navi/json，模型与现有 Nav / NavItem（name、articles、title、link）对齐即可。
2. 架构（MVI，逻辑放 common）
部分	说明
State
加载中、错误信息、列表数据（按分类的标题 + 链接列表）；可选空态。
Intent / Event
进入页面触发加载、重试、点击某个标签链接。
Effect
「用系统方式打开 URL」（一次性），通过 expect/actual 或从 app 注入 (String) -> Unit 实现。
执行层
在 commonMain 用协程调用封装好的 Ktor 数据层（见下），成功/失败更新 State。
Android 侧可用 ViewModel + StateFlow 持有上述状态；若希望逻辑完全在 common，也可用纯 Kotlin 的 CoroutineScope + MutableStateFlow，由 Fragment 生命周期绑定 scope。

1. 网络层（仅 Ktor）

3.1 依赖组织

在 commonMain：ktor-client-core、ktor-client-content-negotiation、kotlinx-serialization-json（或你更熟悉的 JSON 方案），以及 Ktor 的序列化插件（与 kotlinx.serialization 集成）。
版本选择：选用与 Kotlin 1.9.x 同期维护的 Ktor 2.3.x 一条线（具体小版本以 Maven 上该系列与 Kotlin 1.9 的说明为准）；通过 Ktor BOM（若使用）或显式对齐 ktor-* 与 kotlinx-serialization 版本，避免混用两套不兼容组合。
平台 Engine：androidMain 使用 ktor-client-okhttp（或 ktor-client-android）；iosMain 使用 ktor-client-darwin。仅在对应 source set 声明，不把 Engine 放进 commonMain。
3.2 Base URL 与请求

与现有 App 一致：[https://www.wanandroid.com/](https://www.wanandroid.com/) 为前缀，路径 navi/json。
使用 HttpClient：install(ContentNegotiation) { json(...) }，请求用 client.get { url(...) }，响应 body() 反序列化为 WanResponse<List>（或你定义的 DTO）。
3.3 DTO 与领域模型

用 @Serializable 标注 DTO（字段名与 JSON 一致，或用 @SerialName），与 Gson 版 Nav/NavItem 字段对齐，避免业务层两套含义。
可选：在 common 再映射一层 UI 用的轻量结构（分类名 + 展示标题 + URL），便于 Compose 直接使用。
3.4 错误与线程

统一捕获 ClientRequestException / 序列化异常 / 无网络等，映射为 State 中的 error 或 Snackbar 文案。
Ktor 的调用均在协程中；不要在 UI 线程直接 runBlocking。
4. UI（Compose Multiplatform）
保持现有 wankmp 的 Compose Multiplatform 版本（当前为 1.6.0），不强制为 Ktor 升 Compose；若后续单独升级 Compose，与 Kotlin 1.9.22 的兼容性以 JetBrains 说明为准。
布局：LazyColumn 遍历每个分类；每项内：分类标题（小字、灰色）+ FlowRow 展示标签（圆角背景、clickable）。
顶栏/底栏：若 MainActivity 已有原生顶栏和底部导航，KMP 页面只负责中间内容，避免重复底栏。
5. 与 app 模块的衔接
替换 NavFragment：改为承载 Compose 的 Fragment（或 ComposeView），在 setContent { ... } 中调用 wankmp 暴露的导航页 Composable。
打开链接：与现有详情/Web 行为对齐——通过回调传入 app 里已有打开 WebView/浏览器/详情页的路由。
MainActivity：仅把原 NavFragment.newInstance() 换成新 Fragment 类即可，其它 Tab 不动。
6. 实施顺序建议
在 wankmp 为 common/android/ios 配好 Ktor + Engine + 序列化，先写 仅拉取 navi/json 并打印/单测 的薄封装（验证反序列化正确）。
实现 MVI 状态机 + Repository（或单一 data source） 调用上述封装。
实现 导航页 UI（LazyColumn + FlowRow），接好点击 → Effect/回调。
expect/actual 打开 URL（或从 app 注入）。
app 替换 Fragment 并联调。
异常与空数据、横竖屏等基础回归。
7. 风险与注意点
依赖版本：在 Kotlin 1.9.22 下，Ktor 2.3.x + kotlinx-serialization 配套版本需一次性对齐；升级其中任一而不对齐易导致编译或运行时序列化失败。
混淆：Release 若开启 R8/Proguard，需为 kotlinx.serialization 的生成器及你的 @Serializable 类保留规则（实现阶段再补）。
无 Ktorfit 的代价：接口变多时手写路径与 DTO 会略繁琐；当前仅导航一个接口，成本可控。