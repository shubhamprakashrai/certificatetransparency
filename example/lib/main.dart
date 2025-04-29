import 'dart:convert';

import 'package:certificate_transparency_plugin/certificate_transparency_plugin.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
void main() {
  runApp(const MyApp());
}

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
  _CertificateTransparencyTestPageState createState() => _CertificateTransparencyTestPageState();
}

class _CertificateTransparencyTestPageState extends State<CertificateTransparencyTestPage> {
  String result = "Tap to check";
  Future<void> _checkCT() async {
    final response = await CertificateTransparency.verifyUrl(
      "https://www.google.com",
      includeHosts: ["google.com"],
      excludeHosts: []
    );
    setState(() {
      result = response.toString();
    });
    if (kDebugMode) {
      print(const JsonEncoder.withIndent('   ').convert(result));
    }
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Center(child: Text("CT Test"))),
      body: SingleChildScrollView(
        child: Center(
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
              )
            ],
          ),
        ),
      ),
    );
  }
}
