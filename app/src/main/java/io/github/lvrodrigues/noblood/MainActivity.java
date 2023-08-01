package io.github.lvrodrigues.noblood;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 11000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView info = findViewById(R.id.info);
        info.setText(Html.fromHtml(getString(R.string.info), Html.FROM_HTML_MODE_LEGACY));
    }

    private void startServiceExecute() {
        Snackbar snack = Snackbar.make(this.findViewById(android.R.id.content), R.string.main_service_starting, BaseTransientBottomBar.LENGTH_SHORT);
        snack.show();
        Intent service = new Intent(this, NoBloodService.class);
        startForegroundService(service);
    }

    public void startService(View view) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (manager.areNotificationsEnabled()) {
            startServiceExecute();
        } else {
            verifyAndRequestNotificationPermission();
        }
    }

    private void verifyAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            verifyAndRequestNotificationTiramisu();
        } else {
            verifyAndRequestNotificationLegacy();
        }
    }

    private void verifyAndRequestNotificationLegacy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.request_notification_title);
        builder.setMessage(R.string.request_notification_message);
        builder.setPositiveButton("Sim", (dialog, which) -> showNotificationIntentSettings());
        builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showNotificationIntentSettings() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    private void verifyAndRequestNotificationTiramisu() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            startServiceExecute();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.request_notification_title);
                builder.setMessage(R.string.request_notification_message);
                builder.setPositiveButton(R.string.yes, (dialog, which) -> requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE));
                builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startServiceExecute();
            } else {
                // TODO Verificar como abrir configurações de notificações...
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.request_notification_title);
                builder.setMessage(R.string.request_notification_failure);
                builder.setPositiveButton("Sim", (dialog, which) -> showNotificationIntentSettings());
                builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        }
    }

    public void stopService(View view) {
        Intent service = new Intent(this, NoBloodService.class);
        stopService(service);
    }
}