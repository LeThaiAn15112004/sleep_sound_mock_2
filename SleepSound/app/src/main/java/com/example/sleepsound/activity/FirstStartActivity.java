package com.example.sleepsound.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sleepsound.R;
import com.example.sleepsound.databinding.ActivityFirstStartBinding;

public class FirstStartActivity extends AppCompatActivity {
    private ActivityFirstStartBinding activityFirstStartBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityFirstStartBinding = ActivityFirstStartBinding.inflate(getLayoutInflater());
        setContentView(activityFirstStartBinding.getRoot());
        activityFirstStartBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstStartActivity.this, SecondStartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
