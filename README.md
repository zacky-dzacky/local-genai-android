# Local GenAI Android

This is a Kotlin Multiplatform project that demonstrates how to share business logic between an Android and an iOS application. The project uses a shared `ViewModel` to process user prompts with a local AI model and displays the results in a native UI for each platform.

## Project Structure

The project is organized into three main modules:

- `shared`: A Kotlin Multiplatform module that contains the core business logic, including the `SharedViewModel`, the `HoldToDictateViewModel`, and the platform-agnostic abstractions for services like AI and logging.
- `app`: An Android application that consumes the `shared` module and provides the Android-specific UI.
- `iosApp`: An iOS application that consumes the `shared` module and provides the iOS-specific UI.

## Core Technologies

- **Kotlin Multiplatform**: For sharing code between Android and iOS.
- **Jetpack Compose**: For building the Android UI.
- **SwiftUI**: For building the iOS UI.
- **Koin**: For dependency injection in both the shared and platform-specific modules.
- **MOKO MVVM**: For a platform-agnostic `ViewModel` implementation.
- **Coroutines**: For managing asynchronous operations.

## Getting Started

### Prerequisites

- Android Studio
- Xcode
- CocoaPods

### Building and Running the Android App

1. Open the project in Android Studio.
2. Let Gradle sync and build the project.
3. Select the `app` run configuration.
4. Choose an Android emulator or a connected device.
5. Click the "Run" button.

### Building and Running the iOS App

1. **Install CocoaPods**:
   ```sh
   sudo gem install cocoapods
   ```

2. **Integrate the Shared Framework**:
   - Open a terminal and navigate to the `iosApp` directory within the project.
   - Run the following command:
     ```sh
     pod install
     ```

3. **Open the Xcode Workspace**:
   - In the `iosApp` directory, open the `.xcworkspace` file in Xcode.

4. **Build and Run**:
   - Select an iOS simulator or a connected device.
   - Build and run the app from Xcode.

## Further Development

To continue developing this project, you can:

- **Implement iOS-specific features**: The iOS implementation of the `GenAiService` and the `HoldToDictateViewModel` are currently placeholders. You can replace these with your own native iOS code.
- **Expand the shared logic**: Add more business logic to the `shared` module to be used by both platforms.
- **Enhance the UI**: Improve the UI for both the Android and iOS apps.
