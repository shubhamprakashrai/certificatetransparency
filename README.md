# Certificate Transparency Plugin for Flutter

A Flutter plugin to verify [Certificate Transparency](https://certificate.transparency.dev/) (CT) compliance of HTTPS connections on Android.  
Useful for security-focused apps that want to ensure SSL/TLS certificates are logged in public CT logs.

> ⚠️ iOS is **skipped by default** since Apple handles CT internally.

---

## ✨ Features

- ✅ Verify CT compliance of HTTPS URLs
- ✅ Include or exclude specific hosts
- ✅ View certificate logs and validation result
- ✅ Full OkHttp + CT log integration on Android

---

## 📦 Installation

Add the following to your `pubspec.yaml`:

```yaml
dependencies:
  certificate_transparency_plugin: ^1.0.0  # Replace with latest

Then run:
      flutter pub get

Import the plugin:
   import 'package:certificate_transparency_plugin/certificate_transparency_plugin.dart';


Then use it like this:
final result = await CertificateTransparency.verifyUrl(
  "https://www.google.com",
  includeHosts: ["google.com"],
  excludeHosts: [],
);
print(result);



Example App
Below is a full Flutter app that checks CT status for https://www.google.com:

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'package:certificate_transparency_plugin/certificate_transparency_plugin.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: CertificateTransparencyTestPage(),
    );
  }
}

class CertificateTransparencyTestPage extends StatefulWidget {
  const CertificateTransparencyTestPage({super.key});
  @override
  State<CertificateTransparencyTestPage> createState() => _CertificateTransparencyTestPageState();
}

class _CertificateTransparencyTestPageState extends State<CertificateTransparencyTestPage> {
  String result = "Tap to check";

  Future<void> _checkCT() async {
    final response = await CertificateTransparency.verifyUrl(
      "https://www.google.com",
      includeHosts: ["google.com"],
      excludeHosts: [],
    );
    setState(() {
      result = const JsonEncoder.withIndent('  ').convert(response);
    });
    if (kDebugMode) {
      print(result);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Center(child: Text("CT Test"))),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Text(result),
            ),
            ElevatedButton(
              onPressed: _checkCT,
              child: const Text("Verify CT"),
            ),
          ],
        ),
      ),
    );
  }
}

🧩 Platform Support

Platform	Support
Android	✅ Yes (API 24+)
iOS	🚫 Skipped
Web	❌ Not supported


License
MIT License © 2025 Shubham Rai