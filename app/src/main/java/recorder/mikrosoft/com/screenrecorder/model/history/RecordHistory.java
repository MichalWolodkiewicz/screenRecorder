package recorder.mikrosoft.com.screenrecorder.model.history;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "records_history")
public class RecordHistory {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "create_time")
    private long createTime;

    @ColumnInfo(name = "name")
    private String name;

    public RecordHistory(long createTime, String name) {
        this.createTime = createTime;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
