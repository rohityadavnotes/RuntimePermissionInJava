package com.runtime.permission.in.java.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.runtime.permission.in.java.R;

public class AfterPermissionGrantedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("After Permission Granted");

        setContentView(R.layout.activity_after_permission_granted);
    }
}