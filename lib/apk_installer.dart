
import 'dart:async';

import 'package:flutter/services.dart';

class ApkInstaller {
  static const MethodChannel _channel =
      const MethodChannel('apk_installer');

  static Future<int?> installApk(String apkPath) async {
    return await _channel.invokeMethod('install', {
      "apkPath": apkPath
    });
  }
}
