# 体系 Tab KMP + CMP 迁移方案

## 范围与接口

Android 用 Compose 容器替换 KnowledgeMapFragment 的旧分类列表，iOS 体系 Tab 替换占位页。保留旧知识节点详情 Activity，避免影响其他入口。共享 UI、MVI ViewModel 和 Ktor 数据层放在 `wankmp/commonMain/.../knowledge`。

接口依据 [WanAndroid 官方文档](https://www.wanandroid.com/blog/show/2#47)：

- `GET https://wanandroid.com/tree/json` 获取两级分类；过滤没有二级分类的一级分类，保留服务端顺序。
- `GET https://wanandroid.com/article/list/{page}/json?cid={二级分类ID}`，从第 0 页分页，不合并首页置顶。
- 复用 `WanResponse<T>`、`Article`、`ArticlePage`、`Chapter`（补充递归 children）和首页收藏接口。

## MVI 与状态

KnowledgeState 保存分类加载状态、当前一级分类 ID、各一级分类选中的二级 ID、筛选 Sheet 状态、按二级 ID 隔离的 FeedState 和收藏进行中集合。KnowledgeIntent 接收分类选择、刷新、分页、筛选显隐、文章／收藏点击、会话及收藏变化。KnowledgeEffect 发出详情、登录和提示事件。

首次加载默认选第一个有效一级分类及其第一个二级分类，文章按需加载。各分类独立缓存、保存滚动位置，切换后恢复。刷新替代同分类旧请求，分页成功后推进页码，按服务端 over／curPage／pageCount 判断结束；失败可重试，不重复并发分页，使用代次避免过期响应覆盖。

收藏使用与首页一致的 Ktor 登录会话，按钮请求期间禁用；成功同步体系缓存，并通过共享事件同步首页及详情。未登录或 -1001 请求宿主登录；登录返回刷新已加载数据，不自动补发收藏。

## UI 与交互

- 共享 Compose 标题栏“体系”，右侧过滤图标；Android 隐藏体系 Tab 原生标题栏，避免重复。
- 第一层 ScrollableTabRow + HorizontalPager，支持点击和左右滑动切换一级分类。
- 第二层使用内容宽度胶囊条，仅点击切换二级分类，不使用子 Pager。胶囊无最小宽度，13sp 字号、水平内边距 16dp、垂直内边距 6dp，间距固定 12dp。胶囊条可滚动查看分类，文章区左右滑动切换一级分类。首页项目／公众号也复用该胶囊组件。
- 列表复用首页 ArticleCell 与文章列表组件，按公众号样式显示：作者在时间前，不显示分类、描述和图片；标题最多两行。支持下拉刷新、自动分页、加载／空／错误及重试。
- 重复点击当前一级或二级 Tab 平滑回顶当前列表，不刷新。
- 筛选使用全窗口 Compose Dialog 内的共享 ModalBottomSheetLayout，覆盖原生底部导航，支持返回／遮罩／下滑关闭并跳过半展开状态；Sheet 高度为全窗口可用高度的 90%，展示全部有效一级分类和自动换行的二级胶囊，可纵向滚动。点击二级分类关闭 Sheet，原子更新父子选择并定位到对应一级 Tab；加载对应文章，不先请求错误分类。

## 平台接入

Android 宿主 ViewModel 管理共享对象、客户端和登录／收藏事件订阅，复用现有详情及登录入口。iOS 新增 KnowledgeViewController，连接底部体系 Tab；详情及登录继续显示未接入提示，不打开浏览器。销毁时取消协程、关闭客户端。

## 验证

执行 Android Debug 构建、iOS 框架及 Swift 宿主构建；遵循当前人工验收方式，不执行自动化测试。人工检查短标题胶囊等距、空一级分类过滤、一级滑动／二级点击、Sheet 跨父分类定位及 90% 高度、分页／刷新、收藏和登录回调、切换保留位置及主题。

实现或方案后续变更时同步更新本文档。构建和运行结果在完成后追加。

## 2026-10-23 实现与构建结果

- 已完成共享 KnowledgeRepository／KnowledgeViewModel／KnowledgeScreen，以及 Android Compose 容器与 iOS 体系 Tab 接入。
- Chapter 添加 children，首页与体系共用内容宽度胶囊条、ArticleList 和按展示配置区分的 ArticleCell。收藏成功通过 HomeCollectionEvents 同步另一 Tab。
- 真实分类接口读取到 61 个一级分类，过滤没有二级分类的“速查知识表”后展示 60 个，首个有效分类为“开发环境”；界面顺序以实时服务端返回为准。
- `:app:assembleDebug` 与 `:wankmp:embedAndSignAppleFrameworkForXcode` 构建通过；Swift 宿主 Debug 模拟器构建通过。
- 已更新安装并启动至 iPhone 17 Pro / iOS 26.5 模拟器，供人工验收；未执行自动化测试，也未声称完成设备交互验收。
- Android APK：`app/build/outputs/apk/debug/app-debug.apk`；iOS App：`/tmp/wan-home-ios-derived/Build/Products/Debug-iphonesimulator/WanAndroid.app`。
- 本机构建复用 `/tmp/wanandroid-local-verification.init.gradle` 中的依赖缓存；仓库依赖版本未变。
