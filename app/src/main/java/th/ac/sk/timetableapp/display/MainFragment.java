package th.ac.sk.timetableapp.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import th.ac.sk.timetableapp.MasterActivity;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.modify.ModifyClassroomActivity;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.util.StaticUtil;

public class MainFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        StaticUtil.getInstance().applyContext(getContext());
        DataSaveHandler.getInstance();
        v.findViewById(R.id.c1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(MasterActivity.TAG_SCREEN, MasterActivity.SCREEN_DAY_DISPLAY);
                Navigation.findNavController(v).navigate(R.id.action_open_timetableView, args);
            }
        });
        v.findViewById(R.id.c2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), ModifyClassroomActivity.class));
            }
        });
        v.findViewById(R.id.c3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(MasterActivity.TAG_SCREEN, MasterActivity.SCREEN_MODIFY_TEACHER_LOCATION_CHOOSER);
                Navigation.findNavController(v).navigate(R.id.action_open_teacherLocationChooser, args);
            }
        });
    }
}
