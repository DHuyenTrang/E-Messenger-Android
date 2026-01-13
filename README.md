# E-Messenger Android

A modern, real-time messaging application for Android that enables users to connect and communicate seamlessly.

## Introduction

E-Messenger is a feature-rich Android messaging application built with modern Android development practices. It supports real-time messaging through WebSocket connections, user authentication, group conversations, contact management, and profile customization. The app provides a smooth and intuitive user experience with Material Design principles.

## Architecture

This project follows **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern to ensure separation of concerns, testability, and maintainability.

### Architecture Layers

```
├── UI Layer (Presentation)
│   ├── Fragments (Views)
│   ├── ViewModels
│   └── Adapters
│
├── Domain Layer
│   ├── Models (Entities)
│   └── Repository Interfaces
│
├── Data Layer
│   ├── Repository Implementations
│   ├── Network (API Services)
│   ├── Request/Response Models
│   └── WebSocket Service
│
└── Dependency Injection
    └── Hilt Modules
```

### Key Architecture Components

- **UI Layer**: Contains Fragments, ViewModels, and custom views. Follows MVVM pattern where ViewModels manage UI state and business logic.
- **Domain Layer**: Contains domain models (User, Message, Conversation) and repository interfaces that define contracts for data operations.
- **Data Layer**: Implements repository interfaces and handles data operations including network calls via Retrofit and real-time messaging via WebSocket/STOMP.
- **Dependency Injection**: Uses Hilt for dependency injection to provide loose coupling and easier testing.

## Tech Stack

### Core Technologies

- **Kotlin**: Primary programming language
- **Android SDK**: Target SDK 35, Min SDK 24

### Architecture & Design Pattern

- **MVVM (Model-View-ViewModel)**: Architectural pattern for UI layer
- **Clean Architecture**: Separation of concerns with clear layer boundaries
- **Hilt**: Dependency injection framework

### UI Components

- **ViewBinding**: Type-safe view access
- **Material Design Components**: Modern UI following Material Design guidelines
- **RecyclerView**: Efficient list display
- **Navigation Component**: Fragment navigation with type-safe arguments
- **CircleImageView**: Circular image displays for avatars

### Networking

- **Retrofit**: RESTful API client (v2.9.0)
- **Gson**: JSON serialization/deserialization
- **OkHttp**: HTTP client with custom interceptors for authentication

### Real-time Communication

- **Java-WebSocket**: WebSocket client implementation
- **STOMP Protocol**: Messaging protocol over WebSocket
- **RxJava 2**: Reactive programming for handling asynchronous events

### Data Persistence

- **SharedPreferences**: Local data storage
- **Security Crypto**: Encrypted SharedPreferences for sensitive data

### Concurrency

- **Kotlin Coroutines**: Asynchronous programming and background tasks
- **Flow**: Reactive stream for state management

### Media & UI Enhancements

- **Glide**: Image loading and caching
- **Waveform SeekBar**: Audio waveform visualization
- **Amplituda**: Audio analysis library
- **Chip Navigation Bar**: Bottom navigation component

### Testing

- **JUnit**: Unit testing framework
- **Espresso**: UI testing framework

## Features

- 🔐 User authentication (Sign in/Sign up)
- 💬 Real-time messaging
- 👥 Contact management
- 👨‍👩‍👧‍👦 Group conversations
- 🤖 Chatbot integration
- 👤 User profile management
- 🔍 Contact search
- 📱 Material Design UI
- 🎙️ Audio message support

## Project Structure

```
app/
├── src/main/java/com/example/e_messengerapplication/
│   ├── data/               # Data layer
│   │   ├── repository/     # Repository implementations
│   │   ├── request/        # API request models
│   │   └── response/       # API response models
│   ├── domain/             # Domain layer
│   │   ├── repository/     # Repository interfaces
│   │   └── [Models]        # Domain entities
│   ├── network/            # Network services
│   │   ├── APIService      # REST API endpoints
│   │   ├── AuthAPIService  # Authentication endpoints
│   │   └── AuthInterceptor # JWT token interceptor
│   ├── ui/                 # UI layer
│   │   ├── auth/           # Authentication screens
│   │   ├── home/           # Home/conversations screen
│   │   ├── chat/           # Chat screen
│   │   ├── contacts/       # Contacts screen
│   │   ├── groups/         # Group management
│   │   ├── profile/        # Profile screens
│   │   ├── search/         # Search functionality
│   │   └── chatbot/        # Chatbot screen
│   ├── di/                 # Dependency injection modules
│   ├── utils/              # Utility classes
│   └── AppStore.kt         # App-wide data store
```

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK with API 24+

### Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Build and run the application

## License

This project is part of a portfolio/educational project.
