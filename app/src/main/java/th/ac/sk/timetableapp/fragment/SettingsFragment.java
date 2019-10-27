package th.ac.sk.timetableapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
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
        ((SwitchMaterial)v.findViewById(R.id.toggle_switch)).setChecked(
                DataSaveHandler.SharedPreferencesHelper.getBoolean("settings_hide_no_class"));
        ((SwitchMaterial)v.findViewById(R.id.toggle_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DataSaveHandler.SharedPreferencesHelper.setBoolean(b,"settings_hide_no_class");
                DataSaveHandler.saveMaster();
            }
        });
    }
}