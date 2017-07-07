package recorder.mikrosoft.com.screenrecorder.repository;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import recorder.mikrosoft.com.screenrecorder.model.history.RecordHistory;
import recorder.mikrosoft.com.screenrecorder.repository.history.RecordHistoryRepository;

@Database(entities = {RecordHistory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordHistoryRepository recordHistoryRepository();

    public static AppDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "screen_records").addMigrations(getMigrations()).build();
    }

    private static Migration[] getMigrations() {
        Migration[] migrations = new Migration[1];
        migrations[0] = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("create table `records_history` (`uid` INTEGER, `create_time` INTEGER, `name` VARCHAR(50) PRIMARY KEY(`id`))");
            }
        };
        return migrations;
    }

}
