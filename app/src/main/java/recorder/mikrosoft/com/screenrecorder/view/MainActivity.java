package recorder.mikrosoft.com.screenrecorder.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import recorder.mikrosoft.com.screenrecorder.R;
import recorder.mikrosoft.com.screenrecorder.RecordDoneSuccessFragment;
import recorder.mikrosoft.com.screenrecorder.RecordErrorFragment;
import recorder.mikrosoft.com.screenrecorder.RecordInProgressFragment;
import recorder.mikrosoft.com.screenrecorder.RecordSettingsFragment;
import recorder.mikrosoft.com.screenrecorder.view.history.RecordsHistoryFragment;
import recorder.mikrosoft.com.screenrecorder.service.ScreenRecordService;
import recorder.mikrosoft.com.screenrecorder.WriteExternalStoragePermissionRequiredFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "SCREEN_RECORDER";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 201;
    private RecordActionsReceiver recordActionsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.i(TAG, Environment.getDataDirectory().getAbsolutePath());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_records_history:
                showFragmentAndAddToBackStack(new RecordsHistoryFragment());
                break;
        }
        return true;
    }

    private void showFragmentAndAddToBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForGrantPermissionIfNeeded();
        recordActionsReceiver = new RecordActionsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScreenRecordService.ACTION_RECORD_END_SUCCESS);
        intentFilter.addAction(ScreenRecordService.ACTION_RECORD_START);
        intentFilter.addAction(ScreenRecordService.ACTION_RECORD_ERROR);
        registerReceiver(recordActionsReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(recordActionsReceiver);
    }

    private void requestForGrantPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showFragment(new WriteExternalStoragePermissionRequiredFragment());
            } else {
                requestForWriteToExternalStoragePermission();
            }
        } else {
            showFragment(new RecordSettingsFragment());
        }
    }

    private void requestForWriteToExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                Log.i(TAG, "permission granted");
                showFragment(new RecordSettingsFragment());
            } else {
                Log.i(TAG, "permission denied");
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                showFragment(new WriteExternalStoragePermissionRequiredFragment());
            }
        }
    }

    public void startRecord(View view) {
        startService(new Intent(this, ScreenRecordService.class));
    }

    public void requestForWriteToExternalStoragePermission(View view) {
        requestForWriteToExternalStoragePermission();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragment).commitAllowingStateLoss();
    }

    public void playRecordedVideo(View view) {

    }

    public void showRecordSettingsFragment(View view) {
        showFragment(new RecordSettingsFragment());
    }

    private class RecordActionsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Record action receiver = " + intent.getAction());
            if (intent.getAction().equals(ScreenRecordService.ACTION_RECORD_END_SUCCESS)) {
                showFragment(new RecordDoneSuccessFragment());
            } else if (intent.getAction().equals(ScreenRecordService.ACTION_RECORD_ERROR)) {
                showFragment(new RecordErrorFragment());
            } else if (intent.getAction().equals(ScreenRecordService.ACTION_RECORD_START)) {
                showFragment(new RecordInProgressFragment());
            }
        }
    }
}
