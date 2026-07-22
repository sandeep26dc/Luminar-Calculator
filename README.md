<div align="center">

  <!-- Title & Tagline -->
  <h1>D E V I C E &nbsp; I N S I G H T</h1>
  <p><strong>Minimalist Hardware & System Telemetry Diagnostic Engine</strong></p>

  <!-- Badges -->
  <a href="https://github.com/sandeep26dc/DeviceInfoApp/actions">
    <img src="https://img.shields.io/badge/Build-Passing-10B981?style=for-the-badge&logo=githubactions&logoColor=white" alt="Build Status">
  </a>
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  </a>
  <a href="https://developer.android.com/jetpack/compose">
    <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" alt="Compose">
  </a>
  <a href="https://github.com/sandeep26dc/DeviceInfoApp/releases">
    <img src="https://img.shields.io/badge/Android-API%2026%2B-000000?style=for-the-badge&logo=android&logoColor=3DDC84" alt="Android Version">
  </a>

  <br><br>

  <!-- Screenshot Preview -->
  <img src="docs/screenshots/device_info_preview.png" width="340" alt="Device Info Dashboard" style="border-radius: 16px;">

  <br><br>

  <p width="80%">
    <em>DeviceInfoApp provides clean, real-time hardware diagnostics and operating system specifications. Built with zero cloud dependence, it delivers accurate hardware reporting in a sleek, executive layout.</em>
  </p>

  <a href="https://github.com/sandeep26dc/DeviceInfoApp/releases">
    <img src="https://img.shields.io/badge/⚡_DOWNLOAD_DEBUG_APK-05070A?style=for-the-badge&logo=android&logoColor=white" alt="Download APK">
  </a>

</div>

---

## 🛠️ Diagnostics & Features

* **Real-time CPU & Thermal Metrics:** Monitored via native Android hardware subsystem APIs.
* **Memory & Storage Allocation Breakdown:** Dynamic progress gauges for RAM and internal storage.
* **Display & Graphics Profiling:** Screen resolution, refresh rate, density index, and GPU reporting.
* **Battery Health & Voltage Analytics:** Live power status, charge rate, and cell temperature.

---

## ⚡ Building From Source

```bash
# Clone the repository
git clone [https://github.com/sandeep26dc/DeviceInfoApp.git](https://github.com/sandeep26dc/DeviceInfoApp.git)

# Enter project directory
cd DeviceInfoApp

# Build Debug APK locally
gradle assembleDebug
