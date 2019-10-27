package th.ac.sk.timetableapp.fragment;

import android.os.Bundle;
import android.util.Log;
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
import th.ac.sk.timetableapp.tool.DialogBuilder;

public class ModifyTeacherLocationChooserFragment extends Fragment {
    private final TeacherChooserAdapter adapter = new TeacherChooserAdapter(this);
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
        TeacherLocationDatabase.getInstance().setLocationObserver(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
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
            final int teacherId;

            Click(int teacherId) {
                this.teacherId = teacherId;
            }

            @Override
            public void onClick(View v) {
                Log.w("TAG","TeacherId = "+teacherId);
                openEditor(Navigation.findNavController(v), teacherId);
            }
        }

        private static class LongClick implements View.OnLongClickListener {
            final int teacherId;
            final ModifyTeacherLocationChooserFragment fragment;

            LongClick(int teacherId, ModifyTeacherLocationChooserFragment fragment) {
                this.teacherId = teacherId;
                this.fragment = fragment;
            }

            @Override
            public boolean onLongClick(View v) {
                DialogBuilder.getDeleteItemDialog(fragment, TeacherLocationDatabase.getInstance().getDetailAt(teacherId)).show();
                return false;
            }
        }
    }

    private class TeacherChooserViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView text;

        private TeacherChooserViewHolder(@NonNull View v) {
            super(v);
            view = v.findViewById(R.id.view);
            text = v.findViewById(R.id.text);
        }
    }

    class TeacherChooserAdapter extends RecyclerView.Adapter {
        final ModifyTeacherLocationChooserFragment fragment;

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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
            TeacherChooserViewHolder VH = (TeacherChooserViewHolder) holder;
            TeacherDetail data = TeacherLocationDatabase.getInstance().getDetailList().get(pos);
            VH.text.setText(String.format(Locale.getDefault(), "อ. %s %s", data.name, data.surname));

            VH.view.setBackgroundColor(getResources().getColor(
                    pos % 2 == 0 ? R.color.colorBackground : R.color.colorBackgroundTint));
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
                            R.color.colorBackground : R.color.colorBackgroundTint));
        }
    }
}
