import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:apk_installer/apk_installer.dart';

void main() {
  const MethodChannel channel = MethodChannel('apk_installer');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(null, '42');
  });
}
