package com.artxdev.apk_installer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import org.apache.commons.io.FileUtils;

import java.io.File;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** ApkInstallerPlugin */
public class ApkInstallerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "apk_installer");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("install")) {
      String apkPath = call.argument("apkPath");
      installApk(apkPath);
      result.success(0);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private void installApk(String apkPath) {
    Context context = activity.getApplicationContext();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Uri uri = FileProvider.getUriForFile(context,
              context.getPackageName() + ".provider", new File(apkPath));
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setDataAndType(uri, "application/vnd.android.package-archive");
      context.startActivity(intent);
    } else {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(Uri.fromFile(new File(apkPath)),
              "application/vnd.android.package-archive");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {}

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {}
}
