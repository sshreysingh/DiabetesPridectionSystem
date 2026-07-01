# Diabetes Prediction System

A complete full-stack machine learning application for diabetes risk prediction. This repository contains the machine learning model, a Flask REST API backend, and a modern Android application frontend.

## Project Architecture

The project is divided into three main components:

1.  **Machine Learning Model:** A trained scikit-learn model (`diabetes_model.pkl`) trained on the Pima Indians Diabetes Database (`diabetes.csv`). The original training notebook is available in `Model.ipynb`.
2.  **REST API Backend (`/api`):** A Flask server that loads the pickled machine learning model and provides an HTTP endpoint (`POST /predict`) for making predictions. It includes input validation to ensure physiologically plausible values.
3.  **Android Client (`/android`):** A native Android application built with Kotlin and Jetpack Compose. It provides a beautiful, user-friendly interface to input medical data, communicates with the Flask API to get predictions, and saves the history locally using Room database.

## Getting Started

To run the complete system, you need to start the backend API and then run the Android application.

### 1. Start the Backend API

The backend is a Python Flask application.

```bash
cd api
python -m venv venv
source venv/bin/activate  # On Windows use: venv\Scripts\activate
pip install -r requirements.txt
python app.py
```

The API will start on `http://0.0.0.0:5001`.
*For more details, see the [API README](api/README.md).*

### 2. Run the Android App

The Android app is built with modern Android development practices (Compose, Retrofit, Coroutines).

1.  Open the `android/` folder in **Android Studio**.
2.  Ensure your device or emulator is on the same network as your computer.
3.  If testing on a physical device, update the `BASE_URL` in `android/app/build.gradle.kts` to your computer's local IP address (e.g., `http://192.168.1.10:5001/`). If using an emulator, `http://10.0.2.2:5001/` is already configured.
4.  Sync Gradle and click **Run**.

*For more details, see the [Android README](android/README.md).*

## Features

*   **Robust Validation:** Both the Android app and the API validate inputs against physiologically plausible ranges (e.g., Glucose 40-300 mg/dL, Age 1-120 years).
*   **Modern UI:** The Android app features a dark mode, glassmorphism UI with smooth animations.
*   **Local History:** All predictions are saved securely on the Android device using a Room database.
*   **AI-Powered:** Uses a Random Forest classifier for high-accuracy predictions based on 8 clinical features.

## Disclaimer

⚠️ **For informational purposes only.** This application is not a substitute for professional medical advice, diagnosis, or treatment. Always seek the advice of your physician or other qualified health provider with any questions you may have regarding a medical condition.
