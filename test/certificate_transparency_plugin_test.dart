// import 'package:flutter_test/flutter_test.dart';
// import 'package:certificate_transparency_plugin/certificate_transparency_plugin.dart';
// import 'package:certificate_transparency_plugin/certificate_transparency_plugin_platform_interface.dart';
// import 'package:certificate_transparency_plugin/certificate_transparency_plugin_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';

// class MockCertificateTransparencyPluginPlatform
//     with MockPlatformInterfaceMixin
//     implements CertificateTransparencyPluginPlatform {

//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');
// }

// void main() {
//   final CertificateTransparencyPluginPlatform initialPlatform = CertificateTransparencyPluginPlatform.instance;

//   test('$MethodChannelCertificateTransparencyPlugin is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelCertificateTransparencyPlugin>());
//   });

//   test('getPlatformVersion', () async {
//     CertificateTransparencyPlugin certificateTransparencyPlugin = CertificateTransparencyPlugin();
//     MockCertificateTransparencyPluginPlatform fakePlatform = MockCertificateTransparencyPluginPlatform();
//     CertificateTransparencyPluginPlatform.instance = fakePlatform;

//     expect(await certificateTransparencyPlugin.getPlatformVersion(), '42');
//   });
// }
