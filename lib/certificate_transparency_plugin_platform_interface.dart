import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'certificate_transparency_plugin_method_channel.dart';

abstract class CertificateTransparencyPluginPlatform extends PlatformInterface {
  /// Constructs a CertificateTransparencyPluginPlatform.
  CertificateTransparencyPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static CertificateTransparencyPluginPlatform _instance = MethodChannelCertificateTransparencyPlugin();

  /// The default instance of [CertificateTransparencyPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelCertificateTransparencyPlugin].
  static CertificateTransparencyPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [CertificateTransparencyPluginPlatform] when
  /// they register themselves.
  static set instance(CertificateTransparencyPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
