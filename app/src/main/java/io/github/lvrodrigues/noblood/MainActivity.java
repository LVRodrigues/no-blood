package io.github.lvrodrigues.noblood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton config = findViewById(R.id.config);
        config.setOnClickListener(view -> {
            Intent settings = new Intent(view.getContext(), SettingsActivity.class);
            startActivity(settings);
        });

        TextView info = findViewById(R.id.info);
        info.setText(Html.fromHtml(getString(R.string.info), Html.FROM_HTML_MODE_LEGACY));

        personalUpdate();
        attorneyMainUpdate();
        attorneyAlternativeUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        personalUpdate();
        attorneyMainUpdate();
        attorneyAlternativeUpdate();
    }

    private void personalUpdate() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("name", getString(R.string.settings_empty_sample));
        TextView personal = findViewById(R.id.personal);
        String text = getString(R.string.personal);
        text = text.replace("$NAME$", name);
        personal.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
    }

    private void attorneyMainUpdate() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enabled = sharedPreferences.getBoolean("attorney_main_enabled", false);
        TextView view = findViewById(R.id.attorney_main);
        if (enabled) {
            String name = sharedPreferences.getString("attorney_main_name", "");
            String phone = sharedPreferences.getString("attorney_main_phone", "");
            String text = getString(R.string.attorney);
            text = text.replace("$NAME$", name);
            text = text.replace("$PHONE$", phone);
            view.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            view.setText("");
        }
    }

    private void attorneyAlternativeUpdate() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enabled = sharedPreferences.getBoolean("attorney_alternative_enabled", false);
        TextView view = findViewById(R.id.attorney_alternative);
        if (enabled) {
            String name = sharedPreferences.getString("attorney_alternative_name", "");
            String phone = sharedPreferences.getString("attorney_alternative_phone", "");
            String text = getString(R.string.attorney);
            text = text.replace("$NAME$", name);
            text = text.replace("$PHONE$", phone);
            view.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            view.setText("");
        }
    }
}