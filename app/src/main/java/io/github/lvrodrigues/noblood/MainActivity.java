package io.github.lvrodrigues.noblood;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            startServiceExecute();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getText(R.string.request_notification_title));
                builder.setMessage(getText(R.string.request_notification_message));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getText(R.string.request_notification_title));
                builder.setMessage(getText(R.string.request_notification_failure));
                builder.setNegativeButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        }
    }

    public void stopService(View view) {
        Intent service = new Intent(this, NoBloodService.class);
        stopService(service);
    }
}