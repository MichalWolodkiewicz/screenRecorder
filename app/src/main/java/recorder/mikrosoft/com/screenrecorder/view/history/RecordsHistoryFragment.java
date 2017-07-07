package recorder.mikrosoft.com.screenrecorder.view.history;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import recorder.mikrosoft.com.screenrecorder.R;
import recorder.mikrosoft.com.screenrecorder.model.history.RecordHistory;
import recorder.mikrosoft.com.screenrecorder.repository.AppDatabase;

public class RecordsHistoryFragment extends Fragment {

    @BindView(R.id.recordsHistory)
    ListView recordsHistoryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.records_history_fragment, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recordsHistoryList.getAdapter() == null) {
            new AsyncTask<String, Integer, List<RecordHistory>>() {
                @Override
                protected List<RecordHistory> doInBackground(String... params) {
                    return AppDatabase.getInstance(getActivity().getApplicationContext()).recordHistoryRepository().getAll();
                }

                @Override
                protected void onPostExecute(List<RecordHistory> recordHistory) {
                    recordsHistoryList.setAdapter(new RecordsHistoryAdapter(recordHistory));
                }
            }.execute();
        }
    }
}
