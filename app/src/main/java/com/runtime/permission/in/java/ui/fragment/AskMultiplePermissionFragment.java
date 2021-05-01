package com.runtime.permission.in.java.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.runtime.permission.in.java.R;
import com.runtime.permission.in.java.fragmentutils.IFragment;
import com.runtime.permission.in.java.permissionutils.ManagePermission;
import com.runtime.permission.in.java.permissionutils.PermissionDialog;
import com.runtime.permission.in.java.permissionutils.PermissionName;

public class AskMultiplePermissionFragment extends Fragment implements IFragment {

    private static final String TAG = AskMultiplePermissionFragment.class.getSimpleName();

    private String bundleData;

    private View rootView;

    private Button askMultiplePermissionButton;

    private static final int MULTIPLE_PERMISSION_REQUEST_CODE                   = 1001;
    private static final int MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE     = 2001;

    public static final String[] MULTIPLE_PERMISSIONS                           =
            {
                    PermissionName.WRITE_CONTACTS,
                    PermissionName.CALL_PHONE,
                    PermissionName.MANAGE_EXTERNAL_STORAGE
            };

    private ManagePermission managePermission;

    public AskMultiplePermissionFragment() {
    }

    public static AskMultiplePermissionFragment newInstance(Bundle bundle) {
        AskMultiplePermissionFragment askMultiplePermissionFragment = new AskMultiplePermissionFragment();
        askMultiplePermissionFragment.setArguments(bundle);
        return askMultiplePermissionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleData = getArguments().getString("bundle_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ask_multiple_permission, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeView();
        initializeObject();
        setOnClickListener();
    }

    protected void initializeView(){
        askMultiplePermissionButton = rootView.findViewById(R.id.askMultiplePermissionButton);
    }

    protected void initializeObject(){
        managePermission = new ManagePermission(getActivity());
    }

    protected void setOnClickListener(){
        askMultiplePermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (managePermission.hasPermission(MULTIPLE_PERMISSIONS))
                {
                    Log.e(TAG, "permission already granted");

                    createContactAndCall();
                }
                else
                {
                    Log.e(TAG, "permission is not granted, request for permission");

                    AskMultiplePermissionFragment.this.requestPermissions(
                            MULTIPLE_PERMISSIONS,
                            MULTIPLE_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    for (int i = 0; i < grantResults.length; i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            String permission = permissions[i];

                            if(permission.equalsIgnoreCase(PermissionName.WRITE_CONTACTS))
                            {
                                boolean showRationale = managePermission.shouldShowRequestPermissionRationale(permission);
                                if (showRationale)
                                {
                                    Log.e(TAG, "write contact permission denied");

                                    AskMultiplePermissionFragment.this.requestPermissions(
                                            MULTIPLE_PERMISSIONS,
                                            MULTIPLE_PERMISSION_REQUEST_CODE);
                                    return;
                                }
                                else
                                {
                                    Log.e(TAG, "write contact permission denied and don't ask for it again");

                                    PermissionDialog.permissionDeniedWithNeverAskAgain(
                                            getActivity(),
                                            this,
                                            R.drawable.permission_ic_contacts,
                                            "Contacts Permission",
                                            "Kindly allow Contact Permission from Settings, without this permission the app is unable to provide create contact feature. Please turn on permissions at [Setting] -> [Permissions]>",
                                            permission,
                                            MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                                    return;
                                }
                            }

                            if(permission.equalsIgnoreCase(PermissionName.CALL_PHONE))
                            {
                                boolean showRationale = managePermission.shouldShowRequestPermissionRationale(permission);
                                if (showRationale)
                                {
                                    Log.e(TAG, "call phone permission denied");

                                    AskMultiplePermissionFragment.this.requestPermissions(
                                            MULTIPLE_PERMISSIONS,
                                            MULTIPLE_PERMISSION_REQUEST_CODE);
                                    return;
                                }
                                else
                                {
                                    Log.e(TAG, "call phone permission denied and don't ask for it again");

                                    PermissionDialog.permissionDeniedWithNeverAskAgain(
                                            getActivity(),
                                            this,
                                            R.drawable.permission_ic_phone,
                                            "Phone Permission",
                                            "Kindly allow Phone Permission from Settings, without this permission the app is unable to provide calling feature. Please turn on permissions at [Setting] -> [Permissions]>",
                                            permission,
                                            MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                                    return;
                                }
                            }

                            if(permission.equalsIgnoreCase(PermissionName.MANAGE_EXTERNAL_STORAGE))
                            {
                                boolean showRationale = managePermission.shouldShowRequestPermissionRationale(permission);
                                if (showRationale)
                                {
                                    Log.e(TAG, "manage external storage permission denied");

                                    AskMultiplePermissionFragment.this.requestPermissions(
                                            MULTIPLE_PERMISSIONS,
                                            MULTIPLE_PERMISSION_REQUEST_CODE);
                                    return;
                                }
                                else
                                {
                                    Log.e(TAG, "manage external storage permission denied and don't ask for it again");

                                    PermissionDialog.permissionDeniedWithNeverAskAgain(
                                            getActivity(),
                                            this,
                                            R.drawable.permission_ic_storage,
                                            "Manage Storage Permission",
                                            "Kindly allow Manage Storage Permission from Settings, without this permission the app is unable to provide file read write feature. Please turn on permissions at [Setting] -> [Permissions]>",
                                            permission,
                                            MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                                    return;
                                }
                            }
                        }
                    }

                    Log.e(TAG, "all permission granted, do the task");
                    createContactAndCall();
                }
                else
                {
                    Log.e(TAG, "Unknown Error");
                }
                break;
            default:
                throw new RuntimeException("unhandled permissions request code: " + requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE)
        {
            if (managePermission.hasPermission(MULTIPLE_PERMISSIONS))
            {
                Log.e(TAG, "permission granted from settings");

                createContactAndCall();
            }
            else
            {
                Log.e(TAG, "permission is not granted, request for permission, from settings");

                AskMultiplePermissionFragment.this.requestPermissions(
                        MULTIPLE_PERMISSIONS,
                        MULTIPLE_PERMISSION_REQUEST_CODE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createContactAndCall() {
        Log.e(TAG, "create contact and call require, Contact and Phone Permission");
        createContact();
        call();
    }

    private void createContact() {
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:9753100453"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}