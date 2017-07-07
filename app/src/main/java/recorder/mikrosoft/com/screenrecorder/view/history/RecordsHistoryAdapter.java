package recorder.mikrosoft.com.screenrecorder.view.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import recorder.mikrosoft.com.screenrecorder.R;
import recorder.mikrosoft.com.screenrecorder.model.history.RecordHistory;


public class RecordsHistoryAdapter extends BaseAdapter {

    private List<RecordHistory> recordHistory;

    public RecordsHistoryAdapter(List<RecordHistory> recordHistory) {
        this.recordHistory = recordHistory;
    }

    @Override
    public int getCount() {
        return recordHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_history_row, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText(recordHistory.get(position).getName());
        holder.time.setText(String.valueOf(recordHistory.get(position).getCreateTime()));

        return view;
    }

    static class ViewHolder {

        @BindView(R.id.record_history_name)
        TextView name;
        @BindView(R.id.record_history_time)
        TextView time;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
