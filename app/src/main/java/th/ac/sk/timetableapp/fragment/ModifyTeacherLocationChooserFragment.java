package th.ac.sk.timetableapp.fragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.tool.DialogBuilder;

public class ModifyTeacherLocationChooserFragment extends Fragment {
    public TeacherChooserAdapter adapter = new TeacherChooserAdapter(this);
    private RecyclerView rv;
    private ExtendedFloatingActionButton addFab;

    public static void openEditor(NavController navController, int teacherId) {
        DataSaveHandler.saveMaster();
        DataSaveHandler.loadMaster();

        Bundle args = new Bundle();
        args.putInt("teacherId", teacherId);
        navController.navigate(R.id.action_open_teacherLocationEditor, args);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modify_teacher_location_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        DataSaveHandler.loadMaster();
        rv = v.findViewById(R.id.recycler);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) addFab.shrink();
                else addFab.extend();
            }
        });

        addFab = v.findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSaveHandler.loadMaster();
                DialogBuilder.getAddTeacherDialog(requireActivity(), Navigation.findNavController(v)).show();
                adapter.notifyDataSetChanged();
            }
        });
        TeacherLocationDatabase.getInstance().setLocationObserver(getViewLifecycleOwner(), new Observer<SparseArray<SparseArray<TeacherLocation>>>() {
            @Override
            public void onChanged(SparseArray<SparseArray<TeacherLocation>> sparseArraySparseArray) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    private static class TeacherListener {
        private static class Click implements View.OnClickListener {
            int teacherId;

            Click(int teacherId) {
                this.teacherId = teacherId;
            }

            @Override
            public void onClick(View v) {
                openEditor(Navigation.findNavController(v), teacherId);
            }
        }

        private static class LongClick implements View.OnLongClickListener {
            int teacherId;
            ModifyTeacherLocationChooserFragment fragment;

            LongClick(int teacherId, ModifyTeacherLocationChooserFragment fragment) {
                this.teacherId = teacherId;
                this.fragment = fragment;
            }

            @Override
            public boolean onLongClick(View v) {
                DialogBuilder.getDeleteItemDialog(fragment, TeacherLocationDatabase.getInstance().getDetail(teacherId)).show();
                return false;
            }
        }
    }

    private class TeacherChooserViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView text;

        private TeacherChooserViewHolder(@NonNull View v) {
            super(v);
            view = v.findViewById(R.id.view);
            text = v.findViewById(R.id.text);
        }
    }

    public class TeacherChooserAdapter extends RecyclerView.Adapter {
        public ModifyTeacherLocationChooserFragment fragment;

        TeacherChooserAdapter(ModifyTeacherLocationChooserFragment fragment) {
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TeacherChooserViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.widget_modify_teacher_location_teacher_detail_display, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TeacherChooserViewHolder VH = (TeacherChooserViewHolder) holder;
            TeacherDetail data = TeacherLocationDatabase.getInstance().getDetail().valueAt(position);
            VH.text.setText(String.format(Locale.getDefault(), "อ. %s %s", data.name, data.surname));

            VH.view.setBackgroundColor(getResources().getColor(
                    position % 2 == 0 ? R.color.normalBackground : R.color.tintedBackground));
            VH.view.setOnClickListener(new TeacherListener.Click(data.id));
            VH.view.setOnLongClickListener(new TeacherListener.LongClick(data.id, fragment));
        }

        @Override
        public int getItemCount() {
            return TeacherLocationDatabase.getTeacherCount();
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            rv.setBackgroundColor(getResources().getColor(
                    TeacherLocationDatabase.getTeacherCount() % 2 == 0 ?
                            R.color.normalBackground : R.color.tintedBackground));
        }
    }
}
