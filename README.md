# Android Jetpack Compose Application

This repository contains an Android application built using **Jetpack Compose**, a modern Android UI toolkit that simplifies and accelerates UI development. The application follows MVVM architecture, leverages Kotlin Coroutines, and integrates common Android libraries such as **Hilt** for dependency injection, **Room** for local data storage, and **Retrofit** for network operations.

## Table of Contents
1. [Features](#features)
2. [Architecture](#architecture)
3. [Technologies Used](#technologies-used)
4. [Project Setup](#project-setup)
5. [Dependencies](#dependencies)
6. [Configuration](#configuration)
7. [How to Build and Run](#how-to-build-and-run)
8. [Unit Testing](#unit-testing)

## Features
- Jetpack Compose UI
- MVVM Architecture
- Dependency Injection using Hilt
- Network requests with Retrofit
- Local data storage with Room
- Kotlin Coroutines and Flow for async operations
- Pagination with Jetpack Paging
- Unit and UI Testing

## Architecture
This project follows **MVVM (Model-View-ViewModel)** architecture pattern, which separates concerns and ensures that the UI is decoupled from business logic.

### Layers:
- **Model**: Responsible for managing the data (local or remote).
- **ViewModel**: Acts as a bridge between the UI (View) and the Model. It fetches data and exposes it to the UI in the form of observable data streams (e.g., `StateFlow`).
- **View**: Composable functions that consume the data from the ViewModel and render the UI accordingly.

### Data Flow:
1. User interacts with the UI.
2. UI delegates actions to ViewModel.
3. ViewModel interacts with Repository/UseCase.
4. Repository handles data operations (fetches from network or local database).
5. The data is exposed to the ViewModel, which updates the UI.

## Technologies Used
- **Kotlin**: Main programming language
- **Jetpack Compose**: Modern UI toolkit for building native Android UIs
- **Hilt**: Dependency injection framework
- **Room**: Local database persistence library
- **Retrofit**: HTTP client for making API requests
- **Coroutines & Flow**: Handling asynchronous tasks
- **Paging 3**: For efficient pagination of data
- **JUnit & Mockk**: Unit testing framework and mocking library
- **Espresso & Jetpack Compose Testing**: UI testing tools

## Project Setup

### Prerequisites
- **Android Studio** (latest stable version)
- **JDK 11** or newer
- **Gradle 8.x** (handled automatically by Android Studio)

### Clone the Repository
```bash
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
```
## Dependencies
All dependencies are handled by **Gradle**. To sync and install them, open the project in **Android Studio** and let it sync automatically.

## Configuration
- **API Keys**: If your app relies on external APIs, make sure to add your API keys in the appropriate `local.properties` file or environment configuration.
- **Build Variants**: Configure different environments (e.g., Debug, Release) in the `build.gradle` file.

## How to Build and Run
1. **Open Android Studio** and import the project.
2. **Sync Gradle** to ensure all dependencies are installed.
3. **Run the app** using the play button or use the following command:
   ```bash
   ./gradlew assembleDebug
   ```
You can also run the app on an emulator or a physical device by selecting your target device in Android Studio.

## Unit Testing
This project includes both **unit tests** and **UI tests**.

### Running Unit Tests
To run unit tests, use the following **Gradle** command:
```bash
./gradlew test
```

### Running UI Tests
To run UI tests, use the following **Gradle** command:
```bash
./gradlew connectedAndroidTest
```
You can also run tests directly from **Android Studio** by right-clicking on the `test` or `androidTest` directory and selecting **Run Tests**.
