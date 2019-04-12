package th.ac.sk.timetableapp.settings;

import android.content.Context;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.util.DialogBuilder;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
        Preference importPref = findPreference("import");
        Objects.requireNonNull(importPref).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogBuilder.getImportDataDialog(requireActivity());
                return true;
            }
        });
        Preference exportPref = findPreference("export");
        Objects.requireNonNull(exportPref).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogBuilder.getExportDataDialog(requireActivity());
                return true;
            }
        });
        Preference wipePref = findPreference("wipe");
        Objects.requireNonNull(wipePref).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogBuilder.getWipeDataDialog(requireActivity());
                return true;
            }
        });
        Preference rollbackPref = findPreference("rollback");
        Objects.requireNonNull(rollbackPref).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogBuilder.getRollbackDialog(requireActivity());
                return true;
            }
        });

    }
}
