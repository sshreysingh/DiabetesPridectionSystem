import os
import numpy as np
import joblib
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Load the trained model
MODEL_PATH = os.path.join(
    os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
    'diabetes_model.pkl'
)

try:
    model = joblib.load(MODEL_PATH)
    print(f"✅ Model loaded successfully from {MODEL_PATH}")
except Exception as e:
    print(f"❌ Error loading model: {e}")
    model = None


@app.route('/health', methods=['GET'])
def health():
    """Health check endpoint."""
    return jsonify({
        'status': 'ok',
        'model_loaded': model is not None,
        'version': '1.0.0'
    })


@app.route('/predict', methods=['POST'])
def predict():
    """
    Predict diabetes risk from input features.

    Expected JSON body:
    {
        "pregnancies": float,
        "glucose": float,
        "blood_pressure": float,
        "skin_thickness": float,
        "insulin": float,
        "bmi": float,
        "diabetes_pedigree_function": float,
        "age": float
    }
    """
    if model is None:
        return jsonify({'error': 'Model not loaded on server'}), 500

    try:
        data = request.get_json()

        if not data:
            return jsonify({'error': 'No JSON data provided'}), 400

        required_fields = [
            'pregnancies', 'glucose', 'blood_pressure', 'skin_thickness',
            'insulin', 'bmi', 'diabetes_pedigree_function', 'age'
        ]

        for field in required_fields:
            if field not in data:
                return jsonify({'error': f'Missing required field: {field}'}), 400

        # Parse to float first, so a bad type (e.g. a string) fails clearly.
        parsed = {}
        for field in required_fields:
            try:
                parsed[field] = float(data[field])
            except (TypeError, ValueError):
                return jsonify({'error': f'Field "{field}" must be a number'}), 400

        # Reject physiologically implausible values instead of feeding
        # garbage into the model and returning a false-confident result.
        errors = []
        for field, (low, high, unit) in FIELD_RANGES.items():
            value = parsed[field]
            if value < low or value > high:
                errors.append(
                    f'{field} = {value} is outside the plausible range '
                    f'({low}–{high} {unit})'
                )

        if errors:
            return jsonify({
                'error': 'One or more values are outside plausible physiological ranges',
                'details': errors
            }), 400

        # Build feature vector in the same order as training data
        features = [
            parsed['pregnancies'],
            parsed['glucose'],
            parsed['blood_pressure'],
            parsed['skin_thickness'],
            parsed['insulin'],
            parsed['bmi'],
            parsed['diabetes_pedigree_function'],
            parsed['age']
        ]

        input_array = np.array([features])

        prediction = int(model.predict(input_array)[0])
        probabilities = model.predict_proba(input_array)[0]
        diabetic_probability = float(probabilities[1])

        return jsonify({
            'prediction': prediction,
            'probability': round(diabetic_probability, 4),
            'message': 'Diabetic' if prediction == 1 else 'Not Diabetic',
            'risk_percentage': round(diabetic_probability * 100, 1)
        })

    except ValueError as e:
        return jsonify({'error': f'Invalid input values: {str(e)}'}), 400
    except Exception as e:
        return jsonify({'error': f'Prediction failed: {str(e)}'}), 500
# Physiologically plausible ranges for each input field.
# These bounds reject impossible/nonsensical values (e.g. BMI=2, BP=22)
# rather than silently feeding them into the model. They're intentionally
# a bit wider than the training data's observed range so real patients
# at the edges of "normal" aren't rejected.
FIELD_RANGES = {
    'pregnancies':                (0,    20,   'times'),
    'glucose':                    (40,   300,  'mg/dL'),
    'blood_pressure':             (40,   200,  'mm Hg'),
    'skin_thickness':             (0,    100,  'mm'),
    'insulin':                    (0,    900,  'µU/mL'),
    'bmi':                        (10,   70,   'kg/m²'),
    'diabetes_pedigree_function': (0.05, 2.5,  'score'),
    'age':                        (1,    120,  'years'),
}

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5001))
    print(f"🚀 Starting Diabetes Prediction API on port {port}")
    print(f"📱 For Android Emulator: use http://10.0.2.2:{port}/")
    print(f"📱 For Physical Device: use http://<your-local-ip>:{port}/")
    debug_mode = os.environ.get('FLASK_DEBUG', 'false').lower() == 'true'
    app.run(debug=debug_mode, host='0.0.0.0', port=port)
