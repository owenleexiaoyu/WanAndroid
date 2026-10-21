import UIKit
import SwiftUI
import WanKMPKit

struct HomeComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.HomeViewController(
            onSearch: { showHomeMessage("搜索页尚未接入 iOS 宿主") },
            onArticle: { _ in showHomeMessage("文章详情页尚未接入 iOS 宿主") },
            onLogin: { showHomeMessage("请先登录，iOS 登录页尚未接入") }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

private func showHomeMessage(_ text: String) {
    guard let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
          let root = scene.windows.first(where: { $0.isKeyWindow })?.rootViewController else { return }
    var presenter = root
    while let presented = presenter.presentedViewController { presenter = presented }
    let alert = UIAlertController(title: nil, message: text, preferredStyle: .alert)
    alert.addAction(UIAlertAction(title: "确定", style: .default))
    presenter.present(alert, animated: true)
}

struct NavComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.NavViewController(
            onOpenUrl: { _ in showHomeMessage("链接详情页尚未接入 iOS 宿主") }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DiscoverComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.DiscoverViewController(
            onOpenUrl: { _ in showHomeMessage("链接详情页尚未接入 iOS 宿主") },
            onMessage: { showHomeMessage($0) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SettingsComposeView: UIViewControllerRepresentable {
    let onBack: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.SettingsViewController(
            onBack: onBack,
            onMessage: { showHomeMessage($0) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct MineComposeView: UIViewControllerRepresentable {
    let onSettings: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MineViewController(
            onSettings: onSettings,
            onMessage: { showHomeMessage($0) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

private struct PlaceholderScreen: View {
    let title: String

    var body: some View {
        NavigationView {
            Text(title)
                .font(.title2)
                .foregroundColor(.secondary)
                .navigationBarTitle(title, displayMode: .inline)
        }.navigationViewStyle(.stack)
    }
}

struct ContentView: View {
    @State private var showSettings = false

    var body: some View {
        TabView {
            HomeComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .tabItem {
                    Label("首页", systemImage: "house")
                }

            PlaceholderScreen(title: "体系")
                .tabItem {
                    Label("体系", systemImage: "square.grid.2x2")
                }

            DiscoverComposeView()
                .ignoresSafeArea(.keyboard)
                .tabItem {
                    Label("发现", systemImage: "safari")
                }

            NavComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .tabItem {
                    Label("导航", systemImage: "map")
                }

            MineComposeView(onSettings: { showSettings = true })
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .tabItem {
                    Label("我的", systemImage: "person")
                }
        }
        .fullScreenCover(isPresented: $showSettings) {
            SettingsComposeView(onBack: { showSettings = false })
                .ignoresSafeArea(.keyboard)
        }
    }
}
