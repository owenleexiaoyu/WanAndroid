import UIKit
import SwiftUI
import WanKMPKit

struct TestComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct NavComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.NavViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct MineComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MineViewController()
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
    var body: some View {
        TabView {
            TestComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .tabItem {
                    Label("首页", systemImage: "house")
                }

            PlaceholderScreen(title: "体系")
                .tabItem {
                    Label("体系", systemImage: "square.grid.2x2")
                }

            PlaceholderScreen(title: "发现")
                .tabItem {
                    Label("发现", systemImage: "safari")
                }

            NavComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .tabItem {
                    Label("导航", systemImage: "map")
                }

            MineComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
                .tabItem {
                    Label("我的", systemImage: "person")
                }
        }
    }
}



