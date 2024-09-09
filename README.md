# Seoul Public Bike Rental Information & Kakao Map Integration Android App

This Android app provides real-time information on Seoul's public bike rental system (따릉이), including the location of bike rental stations and the number of available bikes at each station. The app also integrates Kakao Map's bike navigation feature, helping users find the most efficient cycling routes.

## Features

- **Bike Station Locator**: Displays the location of all Seoul public bike rental stations on a map.
- **Real-Time Bike Availability**: Shows the number of available bikes and docks at each station.
- **Kakao Map Integration**: Provides bike navigation through Kakao Map, guiding users to the nearest bike rental station or a selected destination.
- **User-Friendly Interface**: Easy-to-use UI with search and filter options for bike stations based on availability and distance.

## App Screenshots

<img src="https://github.com/user-attachments/assets/1337fc25-6133-46d8-b0e3-e0860248ced2" alt="image" width="300"/>


## Getting Started

### Prerequisites

- Android Studio (latest version)
- Kakao Developers API key
- Internet connection to retrieve real-time bike rental data

### Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/JinNamil/SeoulCycleNavi.git
   ```
2. Open the Project in Android Studio
  - Open Android Studio.
  - Select File > Open.
  - Navigate to the directory where the project is cloned, and open the folder.
3. Configure Kakao Map API Key
    To enable the Kakao Map navigation feature, you need to add your Kakao Developers API Key:
    - Open the AndroidManifest.xml file located in the app/src/main/ directory.
    - Add your Kakao API key within the <application> tag as shown below:
      ```bash
      <meta-data
       android:name="com.kakao.sdk.AppKey"
       android:value="YOUR_KAKAO_API_KEY"/>
      ```
      Replace YOUR_KAKAO_API_KEY with the actual API key from your Kakao Developers account.
4. Sync Gradle

    After adding the API key, sync the Gradle files by clicking on the "Sync Now" notification at the top of Android Studio or by selecting File > Sync Project with Gradle Files.
5. Build and Run the App

    - Connect an Android device to your development machine or use an Android emulator.
    - Click the Run button (or press Shift + F10) in Android Studio to build and run the project.
    If the build is successful, the app should launch on the device or emulator.
### Usage
1. View Bike Stations

    Once the app is open, you will see a map with markers representing all the bike rental stations in Seoul. You can zoom in and tap on any marker to view real-time details such as:

    - Number of available bikes
    - Number of available docks
2. Search for Nearby Stations

Use the search bar at the top to find the closest bike stations based on your current location or an address you input.
3. Navigate with Kakao Map

  - After selecting a bike station, tap the "Navigate" button.
  - This will launch Kakao Map with bike-friendly routes guiding you to your destination.

### API Integration
The app relies on two main APIs:

- Seoul Public Bike System API: Provides the location and availability data for bike rental stations.
- Kakao Map API: Offers bike navigation features integrated directly into the app.
To ensure the app functions correctly, make sure your API keys for both services are valid and properly configured in the app.

### Troubleshooting
If you encounter any issues during installation or while running the app, consider the following:

- Build Errors: Ensure all dependencies are properly installed and the gradle files are synced.
- API Key Issues: Double-check that your API key is correct and has the necessary permissions enabled.
- App Crashes: Review logcat output in Android Studio for detailed error logs.

### Contributing
We welcome contributions! Please follow the steps below to contribute:

1. Fork the repository.
2. Create a new branch (git checkout -b feature/your-feature-name).
3. Commit your changes (git commit -m 'Add some feature').
4. Push to the branch (git push origin feature/your-feature-name).
5. Open a pull request and explain your changes.

### License
This project is licensed under the MIT License. See the LICENSE file for details.
```bash
This extends the previous markdown with more detail, structured sections, and practical steps to set up, run, and contribute to the project. Feel free to modify any part to fit your exact project setup!
```
