# GlucoCheck AI — Android App

A premium Android application for diabetes risk prediction powered by a machine learning model.

## Project Structure

```
android/
├── app/
│   └── src/main/
│       ├── java/com/diabetes/prediction/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── api/            ← Retrofit HTTP client
│       │   │   ├── db/             ← Room local database
│       │   │   ├── model/          ← Request/Response data classes
│       │   │   └── repository/     ← Data repository
│       │   ├── viewmodel/          ← PredictionViewModel
│       │   └── ui/
│       │       ├── navigation/     ← NavGraph
│       │       ├── screens/        ← 5 Compose screens
│       │       └── theme/          ← Color, Type, Theme
│       └── res/
│           ├── values/             ← strings, colors, themes
│           ├── drawable/           ← launcher icon assets
│           └── mipmap-anydpi-v26/  ← adaptive icon
├── build.gradle.kts
└── settings.gradle.kts
```

## Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17**
- **Android SDK** API 26+

## Setup Steps

### 1. Start the Flask API

```bash
cd api/
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python app.py
```

The server will start at `http://localhost:5000`.

### 2. Configure the API URL

Open `android/app/build.gradle.kts` and update `BASE_URL`:

| Scenario          | URL                               |
|-------------------|-----------------------------------|
| Android Emulator  | `http://10.0.2.2:5000/`          |
| Physical Device   | `http://192.168.x.x:5000/`       |
| Cloud Deployment  | `https://your-app.onrender.com/` |

To find your local IP on Mac: `ipconfig getifaddr en0`

### 3. Open in Android Studio

1. Open Android Studio
2. **File → Open** → select the `android/` folder
3. Wait for Gradle sync to complete
4. Click **Run ▶** to build and install on emulator or device

## Tech Stack

| Layer       | Technology |
|-------------|-----------|
| UI          | Jetpack Compose + Material3 |
| HTTP        | Retrofit2 + OkHttp |
| Local DB    | Room (SQLite) |
| Navigation  | Navigation Compose |
| State       | StateFlow + Coroutines |
| DI          | Manual Factory (no Hilt) |

## Features

- 🎨 **Dark theme** with glassmorphism-style cards and gradient accents
- ✨ **Smooth animations** — spring bounce splash, ring fill, slide-ins
- 📋 **8-field medical form** with units, hints, and input validation
- 📊 **Animated result screen** with circular risk ring and health tips
- 🕐 **Local history** — all predictions stored in Room, viewable offline
- 🚨 **Graceful error handling** — clear messages when server is unreachable

## Troubleshooting

| Problem | Solution |
|---------|---------|
| "Could not reach prediction server" | Ensure `python app.py` is running and `BASE_URL` in `build.gradle.kts` is correct |
| Gradle sync fails | Check that JDK 17 is configured in Android Studio |
| App crashes on launch | Check Logcat for Room migration issues (clear app data if needed) |
