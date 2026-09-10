# Copilot instructions for CV01Controller

## Project overview

This repository is a Kotlin Multiplatform project with a shared business layer, a Compose Multiplatform UI layer, and a Ktor server module.

- `core/` holds shared Kotlin code that is intended to be reused across targets. Put common models, utility logic, and cross-platform abstractions in `core/src/commonMain/kotlin`.
- `app/shared/` contains the shared Compose UI used by the client apps. This is the place for UI code that should run across Android, Desktop, Web, and iOS.
- `app/androidApp/`, `app/desktopApp/`, `app/webApp/`, and `app/iosApp/` are target-specific entry points that wire the shared UI into each platform.
- `server/` is a Ktor application. The main entry point is `server/src/main/kotlin/com/rhuertas/cv01controller/Application.kt` and it exposes a simple HTTP route.

The codebase follows the standard Kotlin package layout under `com.rhuertas.cv01controller` and keeps platform-specific code in the matching `androidMain`, `jvmMain`, `jsMain`, `wasmJsMain`, or `iosMain` source sets when needed.

## Build and test commands

Run commands from the repository root using the Gradle wrapper:

- Android app package:
  - `./gradlew :app:androidApp:assembleDebug`
- Desktop app:
  - `./gradlew :app:desktopApp:run`
  - `./gradlew :app:desktopApp:hotRun --auto`
- Server:
  - `./gradlew :server:run`
- Web app:
  - `./gradlew :app:webApp:wasmJsBrowserDevelopmentRun`
  - `./gradlew :app:webApp:jsBrowserDevelopmentRun`

Test commands defined in the project:

- Android host tests:
  - `./gradlew :app:shared:testAndroidHostTest`
- Desktop tests:
  - `./gradlew :app:shared:jvmTest`
- Server tests:
  - `./gradlew :server:test`
- Web tests:
  - `./gradlew :app:shared:wasmJsTest`
  - `./gradlew :app:shared:jsTest`
- iOS simulator tests:
  - `./gradlew :app:shared:iosSimulatorArm64Test`

For a single test, use Gradle's `--tests` filter with any of the above targets. Example:

- `./gradlew :server:test --tests "*Application*"`
- `./gradlew :app:shared:jvmTest --tests "*MyFeatureTest*"`

No repository-specific lint task was identified in the configured Gradle files. Prefer the existing Gradle test/build tasks above when validating changes.

## Key conventions

- Keep shared logic in `commonMain` and platform-specific adapters in target-specific source sets.
- Prefer `core` for reusable Kotlin logic instead of duplicating functionality in UI modules.
- Use Compose Multiplatform conventions when editing the shared app UI, including resource access via generated Compose resources.
- Follow the existing package naming scheme: `com.rhuertas.cv01controller` and subpackages under it.
- When adding new modules or entry points, keep them aligned with the existing multi-project setup in `settings.gradle.kts`.
- For the server, keep the Ktor bootstrapping in `Application.kt` and route logic within the `Application.module()` setup pattern.

## Working style in this repo

- Changes that affect the shared behavior should usually be checked in `core` or `app/shared` before target-specific glue code.
- UI work belongs in the shared Compose module unless the platform requires a specific implementation.
- The repository is structured around Kotlin Multiplatform targets rather than a single JVM-only app, so verify target-specific impact before finalizing changes.
