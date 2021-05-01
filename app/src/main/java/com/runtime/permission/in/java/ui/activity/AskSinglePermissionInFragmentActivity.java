package com.runtime.permission.in.java.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.runtime.permission.in.java.R;
import com.runtime.permission.in.java.fragmentutils.ManageFragment;
import com.runtime.permission.in.java.ui.fragment.AskSinglePermissionFragment;

public class AskSinglePermissionInFragmentActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ManageFragment manageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Single Permission In Fragment Activity");

        setContentView(R.layout.activity_ask_single_permission_in_fragment);
        initializeObject();
    }

    protected void initializeObject() {
        fragmentManager = getSupportFragmentManager();
        manageFragment  = new ManageFragment(fragmentManager, R.id.fragmentContainer);

        Bundle profileBundle = new Bundle();
        profileBundle.putString("bundle_key", "Ask Single Permission Fragment");

        AskSinglePermissionFragment askSinglePermissionFragment = AskSinglePermissionFragment.newInstance(profileBundle);
        manageFragment.addFragment(askSinglePermissionFragment, null, true);
    }
}