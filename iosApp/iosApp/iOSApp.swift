import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinStarterKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}