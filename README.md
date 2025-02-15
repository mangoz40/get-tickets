# Event Ticket Booking App - Android Application (Kotlin)

## Description

This is the Android application component of the Event Ticket Booking App. Built using Kotlin and Jetpack Compose, this app allows users to:

  * **Browse Available Events:** View a list of events fetched from a backend API.
  * **Book Tickets:** Fill out a simple form to book tickets for selected events.
  * **View Purchased Tickets:** (Basic Implementation) Displays a confirmation screen with a QR code URL after successful booking (further development needed for display like total participants that booked the event).

**Important Note:** This application is designed to work in conjunction with a separate Laravel backend API (instructions for setting up the backend are available in the main project README - if applicable). This app focuses solely on the frontend Android user experience.

## Tech Stack

  * **Language:** Kotlin
  * **UI Framework:** Jetpack Compose
  * **Architecture:** MVVM (Model-View-ViewModel)
  * **Networking:** Retrofit 2 (for API communication)
  * **JSON Serialization/Deserialization:** Kotlinx Serialization with Retrofit Converter
  * **Build Tool:** Gradle (Kotlin DSL - `build.gradle.kts`)

## Architecture

This Android application follows the **MVVM (Model-View-ViewModel)** architectural pattern.

  * **View (Composable Functions):** Responsible for displaying the UI and interacting with the user. Composable functions are used to build the UI using Jetpack Compose.
  * **ViewModel:** Holds the UI-related data and business logic. It fetches data from the API (using Retrofit), processes it, and exposes it to the View via `StateFlow`'s. ViewModels are lifecycle-aware and survive configuration changes.
  * **Model (Data Classes & API Client):**
      * **Data Classes:** Represent the data structures used in the app (e.g., `Event`, `BookingRequest`, `BookingResponse`).
      * **API Client (ApiService & NetworkModule):** Handles communication with the backend API using Retrofit.

## Setup Instructions

Before you begin, ensure you have the following installed:

  * **Android Studio:** Download and install the latest version of Android Studio from [https://developer.android.com/studio](https://www.google.com/url?sa=E&source=gmail&q=https://developer.android.com/studio).
  * **JDK (Java Development Kit):** Android Studio typically bundles a suitable JDK, but ensure you have one installed if needed.

**Steps to set up and run the app:**

1.  **Clone the Repository:**

    ```bash
    git clone https://github.com/mangoz40/get-tickets.git
    cd get-tickets  # Navigate to the Android app directory
    ```

2.  **Open in Android Studio:**

      * Open Android Studio.
      * Select "Open an existing project or Gradle" (or similar option depending on your Android Studio version).
      * Navigate to the cloned repository directory and select the `get-tickets` folder 
      * Click "Open".

3.  **Gradle Sync:**

      * Android Studio will automatically start Gradle sync. Wait for Gradle to sync and build the project successfully.
      * If Gradle sync fails, try the following:
          * **File \> Sync Project with Gradle Files**
          * **File \> Invalidate Caches / Restart... \> Invalidate and Restart**
          * Check your internet connection.
          * Ensure you have the required Android SDK versions installed (Android Studio will usually prompt you if missing).

4.  **Configure Backend API URL:**

      * Open the file: `com/example/gettickets/network/NetworkModule.kt` *
      * Locate the `BASE_URL` constant:
        ```kotlin
        private const val BASE_URL = "http://your_laravel_backend_url/api/" // Replace something like this!
        ```
      * **Replace `"http://your_laravel_backend_url/api/"` with the actual URL of your Laravel backend API.**
          * **If running backend on a different machine or accessible via network:** Use the appropriate IP address or domain name and port, e.g., `http://<your_backend_ip>:<port>/api/` or `https://<your_backend_domain>/api/`.

## Running the App

1.  **Connect a Device or Start Emulator:**

      * **Physical Device:** Connect your Android device to your computer via USB. Ensure USB debugging is enabled on your device (Developer Options).
      * **Emulator:** Create and start an Android emulator in Android Studio (Tools \> Device Manager).

2.  **Run the App:**

      * In Android Studio, select your connected device or emulator in the run configuration dropdown (usually next to the "Run" button).
      * Click the "Run" button (green play icon) or press **Shift + F10**.

3.  **App Launch:**

      * The app should build and install on your device/emulator.
      * Once launched, you should see the Event List screen (if the backend API is running and accessible).

## Future Enhancements (Potential Contributions)

  * Improved UI/UX: Enhance the user interface and user experience with better styling, layouts, and animations.
  * Event Detail Screen: Implement a screen to show detailed information about each event before booking.
  * Full Ticket Display Screen: Develop a dedicated screen to display the purchased ticket with the QR code image properly loaded and potentially with more ticket details.
  * User Authentication: Add user registration and login functionality to manage user bookings and profiles.
  * Payment Integration: Integrate a dummy or real payment gateway for ticket purchases.
  * Testing: Implement UI tests and unit tests for better code quality and stability.
  * Error Handling: Improve error handling and user feedback for network errors and API issues.
  * Offline Support (Caching): Implement caching mechanisms to allow users to browse events offline or with poor network connectivity.



**All the best\!**