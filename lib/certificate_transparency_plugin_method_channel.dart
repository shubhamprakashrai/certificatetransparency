import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'certificate_transparency_plugin_platform_interface.dart';

/// An implementation of [CertificateTransparencyPluginPlatform] that uses method channels.
class MethodChannelCertificateTransparencyPlugin extends CertificateTransparencyPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('certificate_transparency_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
