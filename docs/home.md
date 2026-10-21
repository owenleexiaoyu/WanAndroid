# KMP 首页

`wankmp/commonMain/.../home` 包含共享 Article / ArticlePage / Chapter、KtorHomeRepository、HomeViewModel 和 HomeScreen。Android 的 HomeViewModel 仅提供生命周期托管，HomeNavigator 负责复用现有搜索、详情与登录。iOS 的 HomeViewController 暴露对应回调。

## 状态与列表

- 主 Tab 为热门、广场、项目、公众号。项目和公众号分类按接口顺序懒加载。
- FeedKey（Tab + 分类 ID）隔离分页、加载和错误状态。分页成功才推进请求页码；刷新取消上一请求，并用请求代次阻止旧响应覆盖。
- 热门与广场从 0 页开始，项目与公众号从 1 页开始。热门置顶请求允许单独失败，与普通文章按 ID 去重。
- 列表位置通过 Compose 的 SaveableStateHolder 和 LazyListState 保存。重复点击当前主／二级 Tab 只发送共享 UI 内的滚动事件，不触发 Repository 请求；底部导航重复点击无回顶行为。

## Cell

四类列表都使用 ArticleCell。热门、项目在首行显示作者与分类；广场、公众号不显示分类，把作者放在末行时间之前。只有项目显示描述与右侧图片。第一行无置顶、作者或分类时折叠。收藏图标与整项点击分别处理。

## 更新后的交互与 UI 计划

- 标题采用 15sp、最多两行；项目描述采用 13sp、最多两行，超出内容省略。
- Cell 上下内边距为 10dp，首行与标题间距为 8dp，描述与标题间距为 4dp，末行上间距为 4dp；收藏图标为 20dp，按钮为 40dp。
- 列表顶部保留 8dp 间距，隔开首个 Item 与 Tab。
- 项目和公众号二级分类仅通过点击 Tab 切换，不再使用二级 HorizontalPager，也不保留分类边界手势交接逻辑。文章区域左右滑动直接切换一级 Tab；二级 Tab 条仍可横向滚动以查看分类。
- 分类切换保留各列表缓存与滚动位置；再次点击当前一级／二级 Tab 仍只回顶当前列表，不刷新。

此更新替代原方案中“各级 Tab 支持左右滑动切换”和“分类边界切换主 Tab”的要求。下文较早的设备验收记录描述的是更新前版本，新交互需重新验收。

本次更新已通过 Android Debug 构建与 `:wankmp:compileKotlinIosSimulatorArm64` 编译检查；尚未重新进行设备 UI 验收。

## 会话与收藏

SharedNetworkSession 为旧 Retrofit 与新 Ktor 提供同一个持久化 CookieJar。收藏通过共享 Ktor 数据层执行，单文章请求期间禁止重复操作，成功后同步所有缓存列表。旧页面通过 HomeCollectionEvents 发布收藏变化。

登录状态变化刷新已加载列表；登录过期通过 Effect 请求宿主打开登录，不自动补发收藏。iOS 展示入口未接入账户，收藏触发登录回调，搜索／详情也由宿主展示未接入提示。

## 验证入口

```sh
./gradlew :app:assembleDebug :wankmp:testAndroidHostTest
DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer ./gradlew :wankmp:compileKotlinIosSimulatorArm64
```

commonTest 的 HomeRepositoryTest 和 HomeViewModelTest 覆盖请求路径、分页起点、业务失败、可空字段、置顶合并、缓存、失败重试、并发取消、收藏同步及登录过期。真机检查还应覆盖四个 Tab 的布局、各级 Tab 点击／滑动、重复点击回顶、切换后位置保留、搜索／详情、下拉刷新、分页、明暗主题以及独立页面原有悬浮按钮。

## 2026-10-21 真机验收

设备：Redmi K30 Pro，Android 12，1080 × 2400。原安装包签名与本机 Debug 签名不同，使用临时 Gradle 配置生成独立包名 `cc.lixiaoyu.wanandroid.homecheck` 并排安装；原应用及数据保留，项目正式 applicationId 未修改。

已检查：四个主 Tab 与动态分类加载、四类 Cell（包括项目图片和描述、广场／公众号末行作者）、主 Tab 与分类滑动、分类边界切换主 Tab、主／二级 Tab 重复点击回顶、分类切换后位置保留、搜索及详情 Activity 跳转、未登录收藏跳转登录。下拉刷新手势已操作，刷新后列表正常显示。

真机发现并修复：浅色首页状态栏图标不可读、标题残留 mdash 等 HTML 实体，以及同方向嵌套 Pager 在分类边界不能切换主 Tab。

Android Debug 构建通过，Android host 共 11 项测试通过，其中首页新增 8 项。截图保存在 `app/build/reports/home-device/`，独立验收包保存在 `app/build/outputs/apk/homecheck/wanandroid-homecheck.apk`。

未完成的验证：未在验收版输入账户，因此登录后真实收藏／取消收藏尚未做真机闭环（共享状态和请求逻辑有单测）；分页末页、旋转和明暗主题未做完整真机回归。当时 iOS 编译因工具链下载失败中止；后续编译结果见下节。

## 2026-10-21 iOS 编译与验证

环境：Xcode 26.6（17F113），iPhone 17 Pro 模拟器，iOS 26.5，arm64。保留 Kotlin 1.9.22、Compose Multiplatform 1.6.0 和 Ktor 2.3.12。

已修复：

- 为 NSURLSession 的带 completionHandler 重载补充 `platform.Foundation.dataTaskWithURL` 扩展导入，解决 iOS 图片加载代码编译失败。
- 修正 Xcode Release 中指向旧 `shared`／`composeApp` 模块的框架搜索路径，并移除 Debug 的重复路径。
- 传输错误显示简洁提示与重试入口，避免将完整 NSURLSession TLS 诊断文本直接铺满页面；服务端业务错误仍保留文案。新增错误态与重试恢复单测。

已通过：

- `:wankmp:linkDebugFrameworkIosSimulatorArm64`：共享框架编译及链接成功。
- 标准 Xcode 环境下的 `:wankmp:embedAndSignAppleFrameworkForXcode`：框架组装及 Compose 资源同步成功；框架为静态链接，因此嵌入／签名步骤按预期跳过。
- Swift 宿主 Debug 模拟器构建成功，使用工程自身的 `FRAMEWORK_SEARCH_PATHS`，无需命令行覆盖路径。
- `:wankmp:iosSimulatorArm64Test`：11 项通过；Android host 回归测试：12 项通过。
- App 已安装并启动；首页搜索框、四个主 Tab、加载态、简洁失败态和重试入口可见；搜索宿主回调能弹出“搜索页尚未接入 iOS 宿主”提示。

构建命令（正常网络环境）：

```sh
DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer ./gradlew :wankmp:linkDebugFrameworkIosSimulatorArm64 :wankmp:iosSimulatorArm64Test
DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug -sdk iphonesimulator -destination 'platform=iOS Simulator,name=iPhone 17 Pro' CODE_SIGNING_ALLOWED=NO build
```

本机验证曾使用 `/tmp/wanandroid-local-verification.init.gradle` 复用依赖缓存，并手动补齐官方 Kotlin/Native 工具链缓存。Swift 验证阶段先完成框架与资源同步，再设置 `OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED=YES` 跳过重复 Gradle 调用。App 产物位于 `/tmp/wan-home-ios-derived/Build/Products/Debug-iphonesimulator/WanAndroid.app`。

用户调整网络并解锁 Mac 后，继续完成模拟器界面检查：

- 热门真实文章与置顶标记、广场真实文章均正常显示。
- 项目分类与公众号分类加载成功；点击第二个分类后显示对应的文章。跨平台项目列表曾显示失败态，点击重试后恢复真实列表。
- 四种 Cell 已逐一检查：热门／项目作者在首行；广场／公众号作者在第三行时间之前且未在首行重复；项目描述与真实图片显示正常，部分图片失败时显示占位。
- 搜索、文章点击、未登录收藏分别触发对应的 iOS 宿主提示；收藏按钮未触发文章详情。
- 搜索框和 Tab 区域正常显示，首页无回顶悬浮按钮。

截图位于 `app/build/reports/home-ios/`，包括 `hot.png`、`square.png`、`project.png`、`project-second.png`、`wechat.png`、`wechat-second.png` 和宿主回调截图。

初次真实请求曾因系统代理 `127.0.0.1:6864` 出现 TLS -1200／-9819，网络调整后已成功加载上述数据；未关闭证书校验或改用明文请求。

待人工复核：当前自动化工具在模拟器中的拖动被识别为点击，滚轮也未推动列表，因此本轮不能确认 iOS 的纵向滚动、手势切换、下拉刷新、自动分页、滚动位置恢复和重复点击 Tab 的回顶动画。上述交互已在 Android 真机验证，共享分页与缓存逻辑通过单测，但不能替代 iOS 手势验收。明暗主题及 iOS 真机亦未覆盖。iOS 搜索、详情和登录按本次范围仅提供宿主回调，未实现完整页面或登录后收藏闭环。
