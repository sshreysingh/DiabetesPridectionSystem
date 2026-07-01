# Diabetes Prediction API

Flask REST API that serves the pre-trained diabetes prediction ML model.

## Quick Setup

```bash
# 1. Navigate to the api folder
cd api/

# 2. Create a virtual environment
python -m venv venv
source venv/bin/activate       # Mac/Linux
# venv\Scripts\activate        # Windows

# 3. Install dependencies
pip install -r requirements.txt

# 4. Start the server
python app.py
```

The server starts at `http://localhost:5000`

---

## Endpoints

### `GET /health`
Returns server status.

**Response:**
```json
{ "status": "ok", "model_loaded": true, "version": "1.0.0" }
```

### `POST /predict`
Returns diabetes risk prediction.

**Request body:**
```json
{
  "pregnancies": 6,
  "glucose": 148,
  "blood_pressure": 72,
  "skin_thickness": 35,
  "insulin": 0,
  "bmi": 33.6,
  "diabetes_pedigree_function": 0.627,
  "age": 50
}
```

**Response:**
```json
{
  "prediction": 1,
  "probability": 0.8523,
  "message": "Diabetic",
  "risk_percentage": 85.2
}
```

---

## Android App Connection

Change the API base URL in `android/app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BASE_URL", "\"http://<YOUR_IP>:5000/\"")
```

| Device          | URL                             |
|-----------------|---------------------------------|
| Emulator        | `http://10.0.2.2:5000/`        |
| Physical Device | `http://192.168.x.x:5000/`     |

To find your local IP on Mac: run `ipconfig getifaddr en0` in Terminal.

---

## Cloud Deployment (Render)

1. Push this project to GitHub
2. Create a new Web Service on [render.com](https://render.com)
3. Set **Build Command**: `pip install -r api/requirements.txt`
4. Set **Start Command**: `gunicorn api.app:app`
5. Update `BASE_URL` in Android app to your Render URL
