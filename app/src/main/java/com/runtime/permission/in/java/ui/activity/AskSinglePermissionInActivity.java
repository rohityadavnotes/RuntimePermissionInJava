package com.runtime.permission.in.java.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.runtime.permission.in.java.R;
import com.runtime.permission.in.java.permissionutils.PermissionDialog;
import com.runtime.permission.in.java.permissionutils.PermissionName;
import com.runtime.permission.in.java.permissionutils.ManagePermission;
import com.runtime.permission.in.java.saf.SAFRequestCode;
import com.runtime.permission.in.java.saf.SAFUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AskSinglePermissionInActivity extends AppCompatActivity {

    private static final String TAG = AskSinglePermissionInActivity.class.getSimpleName();

    private Button askSinglePermissionButton;

    private static final int SINGLE_PERMISSION_REQUEST_CODE                   = 1001;
    private static final int SINGLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE     = 2001;
    private static final int CAPTURE_IMAGE_REQUEST_CODE                       = 3001;

    private ManagePermission managePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("==============================onCreate(Bundle savedInstanceState)==============================");

        getSupportActionBar().setTitle("Single Permission In Activity");

        setContentView(R.layout.activity_ask_single_permission_in);

        initializeView();
        initializeObject();
        setOnClickListener();
    }

    protected void initializeView() {
        askSinglePermissionButton = findViewById(R.id.askSinglePermissionButton);
    }

    protected void initializeObject() {
        managePermission = new ManagePermission(AskSinglePermissionInActivity.this);
    }

    protected void setOnClickListener() {
        askSinglePermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (managePermission.hasPermission(PermissionName.CAMERA))
                {
                    Log.e(TAG, "permission already granted");

                    captureImageAndSave();
                }
                else
                {
                    Log.e(TAG, "permission is not granted, request for permission");

                    ActivityCompat.requestPermissions(AskSinglePermissionInActivity.this, new String[]{PermissionName.CAMERA}, SINGLE_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case SINGLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    String permission = permissions[0];

                    if (permission.equalsIgnoreCase(PermissionName.CAMERA))
                    {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        {
                            Log.e(TAG, "camera permission granted");

                            captureImageAndSave();
                        }
                        else if (managePermission.shouldShowRequestPermissionRationale(permission))
                        {
                            Log.e(TAG, "camera permission denied");

                            ActivityCompat.requestPermissions(AskSinglePermissionInActivity.this, new String[]{PermissionName.CAMERA}, SINGLE_PERMISSION_REQUEST_CODE);
                        }
                        else
                        {
                            Log.e(TAG, "camera permission denied and don't ask for it again");

                            PermissionDialog.permissionDeniedWithNeverAskAgain(
                                    AskSinglePermissionInActivity.this,
                                    R.drawable.permission_ic_camera,
                                    "Camera Permission",
                                    "Kindly allow Camera Permission from Settings, without this permission the app is unable to provide photo capture feature. Please turn on permissions at [Setting] -> [Permissions]>",
                                    permission,
                                    SINGLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException("unhandled permissions request code: " + requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SINGLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE)
        {
            if (managePermission.hasPermission(PermissionName.CAMERA))
            {
                Log.e(TAG, "permission granted from settings");

                captureImageAndSave();
            }
            else
            {
                Log.e(TAG, "permission is not granted, request for permission, from settings");

                ActivityCompat.requestPermissions(AskSinglePermissionInActivity.this, new String[]{PermissionName.CAMERA}, SINGLE_PERMISSION_REQUEST_CODE);
            }
        }

        if(resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                Uri uri = data.getData();

                if (uri != null)
                {
                    if(SAFRequestCode.SELECT_FOLDER_REQUEST_CODE == requestCode)
                    {
                        /* Save the obtained directory permissions */
                        final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        getContentResolver().takePersistableUriPermission(uri, takeFlags);

                        /* Save uri */
                        SharedPreferences sharedPreferences = getSharedPreferences(SAFUtils.SAF_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(SAFUtils.ALLOW_DIRECTORY, uri.toString());
                        sharedPreferencesEditor.apply();
                    }
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            Log.e(TAG, "Activity canceled");
        }
        else
        {
            Log.e(TAG, "Something want wrong");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void captureImageAndSave() {
        Log.e(TAG, "capture image and save image require, Camera and Storage Permission");

        DocumentFile file = null;
        DocumentFile rootDirectory   = SAFUtils.takeRootDirectoryWithPermission(getApplicationContext(),AskSinglePermissionInActivity.this);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileNameWithExtension = "IMG_" + timeStamp+ ".png";

        if(rootDirectory != null)
        {
            file = createFileOwnDirectory(rootDirectory, "captureImages", fileNameWithExtension);
        }

        if (file != null){
            Uri fileUri = SAFUtils.getUri(file);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }

    private DocumentFile createFileOwnDirectory(DocumentFile rootDirectory, String childDirectoryName, String fileNameWithExtension) {
        DocumentFile childDirectory;
        DocumentFile file;

        if(rootDirectory.findFile(childDirectoryName) == null)
        {
            childDirectory  = SAFUtils.createDirectory(rootDirectory, childDirectoryName);
        }
        else
        {
            childDirectory = rootDirectory.findFile(childDirectoryName);
        }

        assert childDirectory != null;
        file = SAFUtils.createFile(childDirectory, "image/png", fileNameWithExtension);
        return file;
    }

    /* ========================================================================================== */
    /* Life cycle method                                                                          */
    /* ========================================================================================== */
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("==============================onStart()==============================");
    }

    @Override
    protected void onRestart() { /* Only called after onStop() */
        super.onRestart();
        System.out.println("==============================onRestart()==============================");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("==============================onResume()==============================");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("==============================onPause()==============================");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("==============================onStop()==============================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("==============================onDestroy()==============================");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("==============================onBackPressed()==============================");
    }
}