package recorder.mikrosoft.com.screenrecorder.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import recorder.mikrosoft.com.screenrecorder.R;
import recorder.mikrosoft.com.screenrecorder.model.history.RecordHistory;
import recorder.mikrosoft.com.screenrecorder.repository.AppDatabase;
import recorder.mikrosoft.com.screenrecorder.view.MainActivity;

public class ScreenRecordService extends Service {

    private static final int ONGOING_NOTIFICATION_ID = 100;
    public static String ACTION_RECORD_END_SUCCESS = "action.record.end.success";
    public static String ACTION_RECORD_ERROR = "action.record.error";
    public static String ACTION_RECORD_START = "action.record.start";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(getText(R.string.record_notification_title))
                .setContentText(getText(R.string.record_notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.record_notification_title))
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    final Process screenRecordProcess = Runtime
                            .getRuntime()
                            .exec("screenrecord --time-limit 5 " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/records/" + fileName + ".mp4");
                    sendBroadcast(new Intent(ACTION_RECORD_START));
                    final int i = screenRecordProcess.waitFor();
                    Log.i(MainActivity.TAG, "wait for result = " + i);
                    Log.i(MainActivity.TAG, "exitValue = " + screenRecordProcess.exitValue());
                    if (i > 0) {
                        Log.w(MainActivity.TAG, readInputStream(screenRecordProcess.getErrorStream()));
                        sendBroadcast(new Intent(ACTION_RECORD_ERROR));
                    } else {
                        Log.i(MainActivity.TAG, "screen record success");
                        final RecordHistory recordHistory = new RecordHistory(System.currentTimeMillis(), fileName);
                        AppDatabase.getInstance(getApplicationContext()).recordHistoryRepository().insertAll(recordHistory);
                        sendBroadcast(new Intent(ACTION_RECORD_END_SUCCESS));
                    }
                    stopForeground(true);
                    stopSelf();
                } catch (IOException | InterruptedException e) {
                    Log.e(MainActivity.TAG, e.getMessage());
                    stopForeground(true);
                    sendBroadcast(new Intent(ACTION_RECORD_ERROR));
                    stopSelf();
                }
            }
        }).start();
        return START_NOT_STICKY;
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}
