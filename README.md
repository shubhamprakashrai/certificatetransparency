# Certificate Transparency Plugin for Flutter

A Flutter plugin to verify [Certificate Transparency](https://certificate.transparency.dev/) (CT) compliance of HTTPS URLs on Android.

> ⚠️ On iOS, CT is skipped by default because Apple handles it internally.

## Features

- Verify CT compliance of HTTPS URLs
- Include or exclude specific hosts
- View certificate logs and results
- Android-only (API 24+)

## Installation

Add to your `pubspec.yaml`:

```yaml
dependencies:
  certificate_transparency_plugin: ^1.0.0  # Use the latest version



Then run:
   flutter pub get


Usage
 
import 'package:certificate_transparency_plugin/certificate_transparency_plugin.dart';

final result = await CertificateTransparency.verifyUrl(
  "https://www.google.com",
  includeHosts: ["google.com"],
  excludeHosts: [],
);

print(result);

Example App



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
      home: CertificateTransparencyTestPage(),
    );
  }
}

class CertificateTransparencyTestPage extends StatefulWidget {
  const CertificateTransparencyTestPage({super.key});
  @override
  State<CertificateTransparencyTestPage> createState() => _State();
}

class _State extends State<CertificateTransparencyTestPage> {
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
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("CT Test")),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.all(10),
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



Platform Support

| Platform | Support    |
|----------|------------|
| Android  | ✅ API 24+ |
| iOS      | 🚫 Skipped |
| Web      | ❌ No      |






