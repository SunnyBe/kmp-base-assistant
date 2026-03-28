# kmp-base-assistant
Basic Chat App build with KMP and CMP sharing business logic and UI.

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

```text
.
├── gradle/
│   └── libs.versions.toml          # Version Catalog
├── composeApp/                     # The "App Shell" (Android/iOS/Desktop Entry)
│   ├── src/
│   │   ├── androidMain/            # Android Manifest, Application class
│   │   ├── iosMain/                # ViewController factory, SwiftUI entry
│   │   └── commonMain/             # Main Navigation (Voyager/Decompose), App Theme
│
├── core/
│   ├── domain/                     # 100% Pure Kotlin. No Dependencies.
│   │   └── src/commonMain/kotlin/
│   │       ├── model/              # Message, User, Account (Data Classes)
│   │       ├── repository/         # ChatRepository, AccountRepository (Interfaces)
│   │       └── usecase/            # FetchMessagesUseCase
│   │
│   ├── data/                       # Infrastructure & Implementations
│   │   └── src/
│   │       ├── commonMain/kotlin/  # ChatRepositoryImpl, Mappers, Ktor/SQLDelight Setup
│   │       ├── androidMain/        # AndroidSqliteDriver
│   │       └── iosMain/            # NativeSqliteDriver
│   │
│   ├── services/                   # Cross-cutting concerns
│   │   └── src/commonMain/kotlin/
│   │       ├── auth/               # AccountServiceImpl
│   │       ├── notifications/      # NotificationManager
│   │       └── hints/              # HintService
│   │
│   └── ui-kit/                     # Design System
│       └── src/commonMain/kotlin/  # Shared Composables (Buttons, TextStyles)
│
└── features/                       # UI Modules (One per flow)
    ├── chat/
    │   └── src/commonMain/kotlin/  # ChatScreen, ChatViewModel
    └── conversation-list/
        └── src/commonMain/kotlin/  # ListScreen, ListViewModel
```

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
