# Packager

**Packager** is a minimal yet powerful Android utility app designed to help users instantly launch any installed application by simply entering its package name. Once a package name is saved, Packager will automatically launch that app every time it starts, without showing its own UI. The entire application is implemented in a single file: `MainActivity.kt`, and it uses Jetpack Compose for the UI and SharedPreferences for persistent storage. It also supports launcher shortcuts for resetting the saved package name.

## Concept

There are cases where a user might want to instantly open a specific app through an intermediary launcher. Packager serves as that launcher: once a package name is saved, it launches the app directly without showing any intermediate screen. Additionally, it allows resetting the saved package using a long-press launcher shortcut—without needing to open the app manually.

## Features

- Clean and minimal UI built with Jetpack Compose.
- Save any app’s package name and launch it directly.
- Automatically skips the UI once a package is saved.
- Long-press launcher shortcut to reset the saved package name.
- Error handling for nonexistent packages.
- Entire app logic lives inside a single file: `MainActivity.kt`.

## Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- Tested on Android 15 for compatibility.

## How to Use

1. Launch the app.
2. Enter the package name of any installed app (e.g. `com.instagram.android`).
3. Press **Save**.
4. The app will launch the specified package instantly.
5. On future launches, Packager will auto-launch the saved app without showing its UI.
6. To reset the saved package:
   - Long-press on the Packager icon from your device launcher.
   - Select the shortcut **"Reset Saved Package Name"**.
   - A toast will confirm the reset.

## Key Files

- **MainActivity.kt**  
  Contains the full logic of the app: UI rendering, package saving and retrieval, app launching, error handling, and shortcut action processing.

- **res/xml/shortcuts.xml**  
  Defines the launcher shortcut for resetting the saved package.

- **AndroidManifest.xml**  
  Declares the main activity, shortcut metadata, and intent filters for shortcut execution.

## Example

If you input this package name:  com.instagram.android


The app will attempt to launch it. If the app is not installed, it will display this message:  
> Application 'com.instagram.android' not found

If installed, it will launch the Instagram app immediately.

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/mezoinnit/packager.git

