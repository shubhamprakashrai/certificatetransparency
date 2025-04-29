import 'dart:io';

import 'package:flutter/services.dart';
class CertificateTransparency {
  static const MethodChannel _channel = MethodChannel('certificate_transparency');

  static Future<Map<String, dynamic>> verifyUrl(
    String url,{ 
      List<String> includeHosts = const [], 
      List<String> excludeHosts = const []
    }) async {
      if (Platform.isIOS) {
      return {
        'isValidCertificate': true,
        'certificateStatus': 'iOS Safe by Default',
        'trustedLogs': [],
        'networkStatus': 'SKIPPED_ON_IOS',
        'responseCode': 0,
        'responseMessage': null,
        'errorMessage': null,
        'errorType': null,
      };
    }
    final result = await _channel.invokeMethod(
      'verifyUrl', { 
        'url': url, 
        'includeHosts': includeHosts,
        'excludeHosts': excludeHosts, });
    return Map<String, dynamic>.from(result);
  }
}
