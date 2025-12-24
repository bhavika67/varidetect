

https://github.com/user-attachments/assets/e1827fa1-9722-4c0b-b755-f03c87e585ab

# VariDetect – Multi-Modal Deepfake Detection Android App

VariDetect is a **multi-modal deepfake detection Android application** capable of analyzing **images**, **videos**, and **audio** to determine whether the content is real or AI-manipulated.  
It integrates **three separate deep learning models**, each trained for a different media type, and deploys them on-device using **TensorFlow Lite** for fast, offline inference.

This project demonstrates:
- Multi-modal ML systems (image + video + audio)
- CNN & CNN+LSTM architectures
- Audio deepfake detection via spectrograms
- TFLite conversion and on-device inference
- Android development with Java/Kotlin
- End-to-end ML deployment on mobile

---

# 🎯 Features

- **Image Deepfake Detection** using CNN model  
- **Video Deepfake Detection** using CNN + LSTM  
- **Audio Deepfake Detection** using spectrogram-based CNN  
- **On-Device ML Inference** (no server required)  
- **Real/Fake Classification + Confidence Score**  
- **Offline & Privacy-Friendly**  
- **User-Friendly Android UI**  

---

# 🧠 ML Model Architecture

### 🔹 **Image Model – CNN**
- Preprocessing: resize → normalize  
- Architecture: EfficientNetB4 / Custom CNN  
- Output: softmax probability (real vs fake)

---

### 🔹 **Video Model – CNN + LSTM**
- Extract frames from the video  
- CNN → spatial feature extraction  
- LSTM → temporal sequence modeling  
- Aggregate predictions across frames

---

### 🔹 **Audio Model – Spectrogram CNN**
- Convert audio → Mel Spectrogram  
- CNN classifies real vs synthetic voices  
- Supports WAV/MP3 input formats

---

# 🔗 Model Training Repository
All training notebooks, experiments, and TFLite conversion scripts are available here:

👉 **Model Repository:**  
https://github.com/bhavika67/deepfake_ml_model

This includes:
- Image model training  
- Video frame extraction + CNN + LSTM  
- Audio spectrogram model  
- Evaluation metrics  
- TFLite conversion  
- Dataset usage  

---

# 🚀 TensorFlow Lite Deployment

Each model was exported to TFLite using:

```bash
tflite_convert \
 --saved_model_dir=model/ \
 --output_file=model.tflite \
 --optimize=DEFAULT
```

### Android inference uses:
- **TFLite Interpreter**  
- ByteBuffer input conversion  
- Float32 tensors  
- Softmax output decoding  

All inference runs **offline**, making the app faster & more privacy-friendly.

---

# 📁 Project Structure

```
varidetect/
│
├── android_app/
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── assets/              # TFLite models (.tflite)
│   │   │   ├── java/com/vari/varidetect/
│   │   │   │   ├── Activity/        # Splash, Home, OnBoard, etc.
│   │   │   │   ├── Fragment/        # Upload, Result, FAQ, History, etc.
│   │   │   │   ├── Adapter/         # RecyclerView adapters
│   │   │   │   ├── HelperClass/     # ML inference helpers
│   │   │   │   ├── Model/           # Data models
│   │   │   │   └── Utils/           # Utilities & managers
│   │   │   ├── res/                 # Layouts, icons, drawables, fonts
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle
│
├── ml-pipeline/                      # Documentation for the ML models
│   └── README.md
│
├── samples/
│   ├── images/
│   ├── videos/
│   └── audio/
│
└── README.md
```

---

# 🔧 How Detection Works

### 🖼 **Image Detection**
1. Upload/select image  
2. Preprocess (resize → normalize)  
3. Run through image TFLite model  
4. Get probability output  

---

### 🎥 **Video Detection**
1. Extract frames from selected video  
2. Preprocess each frame  
3. CNN generates spatial features  
4. LSTM processes frame sequence  
5. Aggregate predictions  

---

### 🎤 **Audio Detection**
1. Convert audio → Mel Spectrogram  
2. Resize to CNN input dimensions  
3. Run spectrogram through TFLite model  
4. Output = real/fake probability  

---

# 🛠 How to Run the App

### Android Setup
1. Clone this repository  
2. Open `/android_app` in **Android Studio**  
3. Add all `.tflite` models to:

```
android_app/app/src/main/assets/
```

4. Build & run on emulator/device  
5. Choose Image / Video / Audio  
6. View detection result  

---

# 📊 Example Outputs

| Media Type | Output | Confidence |
|------------|--------|------------|
| Image      | Fake   | 0.91       |
| Video      | Real   | 0.73       |
| Audio      | Fake   | 0.88       |

---

# 📚 Datasets Used
- **FaceForensics++** (image/video)  
- **WaveFake / LJ Speech** (audio)  
- Additional synthetic augmentations  

Only **public datasets** were used.

---

# ⚠ Limitations
- Video inference may be slow on low-end devices  
- Audio quality impacts accuracy  
- No multi-modal fusion (models run independently)  
- Larger TFLite models increase app size  

---

# 🧠 What I Learned
- Designing multi-modal ML systems  
- Building CNN, LSTM & spectrogram models  
- TFLite conversion & optimization  
- Preprocessing pipelines for image/video/audio  
- Android ML integration  
- Mobile performance constraints  

---

# 🤝 Contributing
PRs and suggestions are welcome.

---

# 📜 License
MIT License
SE)**.   
