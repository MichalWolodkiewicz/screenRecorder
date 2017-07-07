package recorder.mikrosoft.com.screenrecorder;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordSettingsFragment extends Fragment {

    @BindView(R.id.time_picker)
    NumberPicker numberPicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.record_settings_fragment, container, false);
        ButterKnife.bind(this, fragmentView);
        numberPicker.setMinValue(5);
        numberPicker.setMaxValue(180);
        return fragmentView;
    }
}
