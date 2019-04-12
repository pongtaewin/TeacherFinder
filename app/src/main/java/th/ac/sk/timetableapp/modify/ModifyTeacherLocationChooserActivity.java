package th.ac.sk.timetableapp.modify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.datamodel.TeacherDetail;
import th.ac.sk.timetableapp.util.DialogBuilder;

public class ModifyTeacherLocationChooserActivity extends AppCompatActivity {

    public RecyclerView rv;
    public ExtendedFloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_teacher_location_chooser);

        final Activity activity = this;
        addFab = findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.getAddTeacherDialog(activity).show();
            }
        });
        rv = findViewById(R.id.recycler);


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TeacherChooserAdapter());
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) addFab.shrink();
                else addFab.extend();
            }
        });
        //if(TeacherLocationDatabase.getTeacherCount()==0)
        //    findViewById(R.id.noData).setVisibility(View.VISIBLE);

    }


    private class TeacherClickListener implements View.OnClickListener {
        int teacherId;

        TeacherClickListener(int teacherId) {
            this.teacherId = teacherId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), ModifyTeacherLocationEditorActivity.class);
            intent.putExtra("teacherId", teacherId);
            startActivity(intent);
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

    private class TeacherChooserAdapter extends RecyclerView.Adapter {

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

            VH.view.setBackgroundColor(getResources().getColor(
                    position % 2 == 0 ? R.color.normalBackground : R.color.tintedBackground));
            VH.text.setText(String.format(Locale.getDefault(), "อ. %s %s", data.name, data.surname));
            VH.view.setOnClickListener(new TeacherClickListener(data.id));
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
