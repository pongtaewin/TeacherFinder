package th.ac.sk.timetableapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.tool.DialogBuilder;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        v.findViewById(R.id.choice_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.getImportDataDialog(requireActivity()).show();
            }
        });
        v.findViewById(R.id.choice_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.getExportDataDialog(requireActivity()).show();
            }
        });
        v.findViewById(R.id.choice_wipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.getWipeDataDialog(requireActivity()).show();
            }
        });
        v.findViewById(R.id.choice_rollback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.getRollbackDialog(requireActivity()).show();
            }
        });
    }
}