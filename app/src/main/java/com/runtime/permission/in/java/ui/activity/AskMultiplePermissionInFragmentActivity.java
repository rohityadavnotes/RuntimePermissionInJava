package com.runtime.permission.in.java.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.runtime.permission.in.java.R;
import com.runtime.permission.in.java.fragmentutils.ManageFragment;
import com.runtime.permission.in.java.ui.fragment.AskMultiplePermissionFragment;

public class AskMultiplePermissionInFragmentActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ManageFragment manageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Multiple Permission In Fragment Activity");

        setContentView(R.layout.activity_ask_multiple_permission_in_fragment);
        initializeObject();
    }

    protected void initializeObject() {
        fragmentManager = getSupportFragmentManager();
        manageFragment  = new ManageFragment(fragmentManager, R.id.fragmentContainer);

        Bundle profileBundle = new Bundle();
        profileBundle.putString("bundle_key", "Ask Multiple Permission Fragment");

        AskMultiplePermissionFragment askMultiplePermissionFragment = AskMultiplePermissionFragment.newInstance(profileBundle);
        manageFragment.addFragment(askMultiplePermissionFragment, null, true);
    }
}