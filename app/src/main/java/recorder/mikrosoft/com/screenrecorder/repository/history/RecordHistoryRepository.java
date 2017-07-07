package recorder.mikrosoft.com.screenrecorder.repository.history;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import recorder.mikrosoft.com.screenrecorder.model.history.RecordHistory;

@Dao
public interface RecordHistoryRepository {

    @Query("select * from records_history")
    List<RecordHistory> getAll();

    @Query("select * from records_history order by create_time desc limit 1")
    RecordHistory findLast();

    @Insert
    void insertAll(RecordHistory... history);
}
