package com.runtime.permission.in.java.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
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
import com.runtime.permission.in.java.saf.SAFRequestCode;
import com.runtime.permission.in.java.saf.SAFUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AskSinglePermissionFragment extends Fragment implements IFragment {

    private static final String TAG = AskSinglePermissionFragment.class.getSimpleName();

    private String bundleData;

    private View rootView;

    private Button askSinglePermissionButton;

    private static final int SINGLE_PERMISSION_REQUEST_CODE                   = 1001;
    private static final int SINGLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE     = 2001;
    private static final int CAPTURE_IMAGE_REQUEST_CODE                       = 3001;

    private ManagePermission managePermission;

    public AskSinglePermissionFragment() {
    }

    public static AskSinglePermissionFragment newInstance(Bundle bundle) {
        AskSinglePermissionFragment askSinglePermissionFragment = new AskSinglePermissionFragment();
        askSinglePermissionFragment.setArguments(bundle);
        return askSinglePermissionFragment;
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
        rootView = inflater.inflate(R.layout.fragment_ask_single_permission, container, false);
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
        askSinglePermissionButton = rootView.findViewById(R.id.askSinglePermissionButton);
    }

    protected void initializeObject(){
        managePermission = new ManagePermission(getActivity());
    }

    protected void setOnClickListener(){
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

                    AskSinglePermissionFragment.this.requestPermissions(new String[]{PermissionName.CAMERA}, SINGLE_PERMISSION_REQUEST_CODE);
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

        switch (requestCode) {
            case SINGLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    String permission = permissions[0];
                    Log.e(TAG, "PERMISSION : "+permission);
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

                            AskSinglePermissionFragment.this.requestPermissions(new String[]{PermissionName.CAMERA}, SINGLE_PERMISSION_REQUEST_CODE);
                        }
                        else
                        {
                            Log.e(TAG, "camera permission denied and don't ask for it again");

                            PermissionDialog.permissionDeniedWithNeverAskAgain(
                                    getActivity(),
                                    this,
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                AskSinglePermissionFragment.this.requestPermissions(new String[]{PermissionName.CAMERA}, SINGLE_PERMISSION_REQUEST_CODE);
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

                        getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);

                        /* Save uri */
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SAFUtils.SAF_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
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
        DocumentFile rootDirectory   = SAFUtils.takeRootDirectoryWithPermission(getContext(),getActivity());
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

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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
}