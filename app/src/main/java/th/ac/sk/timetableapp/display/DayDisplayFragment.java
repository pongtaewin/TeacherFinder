package th.ac.sk.timetableapp.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import th.ac.sk.timetableapp.MasterActivity;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;

public class DayDisplayFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) args = new Bundle();
        args.putString(MasterActivity.TAG_SCREEN, MasterActivity.SCREEN_MAIN);
        setArguments(args);
        return inflater.inflate(R.layout.fragment_day_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        DataSaveHandler.loadMaster();
        v.findViewById(R.id.c1).setOnClickListener(new DayClickListener(1));
        v.findViewById(R.id.c2).setOnClickListener(new DayClickListener(2));
        v.findViewById(R.id.c3).setOnClickListener(new DayClickListener(3));
        v.findViewById(R.id.c4).setOnClickListener(new DayClickListener(4));
        v.findViewById(R.id.c5).setOnClickListener(new DayClickListener(5));
    }

    class DayClickListener implements View.OnClickListener {
        @IntRange(from = 1, to = 5)
        private final int day;

        DayClickListener(@IntRange(from = 1, to = 5) int day) {
            this.day = day;
        }

        @Override
        public void onClick(View v) {
            DataSaveHandler.loadMaster();
            Bundle args = new Bundle();
            args.putInt(PeriodDisplayFragment.DAY, day);
            args.putString(MasterActivity.TAG_SCREEN, PeriodDisplayFragment.getActionBarText(day));
            Navigation.findNavController(v).navigate(R.id.action_display_period, args);
        }
    }
}
