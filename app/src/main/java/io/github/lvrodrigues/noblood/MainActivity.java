package io.github.lvrodrigues.noblood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        personalUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        personalUpdate();
    }

    private void personalUpdate() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("name", "");
        TextView personal = findViewById(R.id.personal);
        String text = getString(R.string.personal);
        text = text.replace("$NAME$", name);
        personal.setText(text);
    }
}