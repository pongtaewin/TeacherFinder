package th.ac.sk.timetableapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.database.ImportExportUtil;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.datamodel.TeacherDetail;
import th.ac.sk.timetableapp.modify.ModifyClassroomActivity;
import th.ac.sk.timetableapp.modify.ModifyTeacherLocationChooserFragment;
import th.ac.sk.timetableapp.modify.ModifyTeacherLocationEditorActivity;


public abstract class DialogBuilder {

    @NonNull
    public static AlertDialog getWipeDataDialog(@NonNull final Activity activity) {
        return new MaterialAlertDialogBuilder(activity.getApplicationContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Kanit)
                .setTitle("ต้องการล้างข้อมูลทั้งหมดหรือไม่")
                .setView(inflateDialogMessage(activity, "ข้อมูลทั้งหมดที่บันทึกไว้จะหายไป"))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ModifyClassroomActivity) activity).wipeData();
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @NonNull
    public static AlertDialog getRollbackDialog(@NonNull final Activity activity) {
        return new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Kanit)
                .setTitle("ต้องการกลับไปใช้ข้อมูลของ ม.502 หรือไม่")
                .setView(inflateDialogMessage(activity, "ข้อมูลทั้งหมดที่บันทึกไว้จะถูกแทนที่ด้วยข้อมูลของ ม.502 สำหรับการทดสอบ"))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ModifyClassroomActivity) activity).rollbackData();
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @NonNull
    public static AlertDialog getImportDataDialog(@NonNull final Activity activity) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(activity).inflate(R.layout.dialog_import_data_input, null, false);
        final TextInputEditText text = v.findViewById(R.id.text);
        return new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Kanit)
                .setTitle("กรอกข้อมูลนำเข้า")
                .setView(v)
                .setPositiveButton("เรียบร้อย", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean success = ImportExportUtil.importData(Objects.requireNonNull(text.getText()).toString());
                        Snackbar.make(activity.findViewById(android.R.id.content), success ? "นำเข้าข้อมูลสำเร็จ" : "นำเข้าข้อมูลไม่สำเร็จ", Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    @NonNull
    private static View inflateDialogMessage(@NonNull final Activity activity, String s) {
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_details, null, false);
        TextView tv = v.findViewById(R.id.text);
        tv.setText(s);
        return v;
    }

    @NonNull
    public static AlertDialog getExportDataDialog(@NonNull final Activity activity) {
        return new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Kanit)
                .setTitle("ต้องการส่งออกข้อมูลหรือไม่")
                .setView(inflateDialogMessage(activity, "คุณจะได้ข้อมูลในรูปแบบตัวอักษร เพื่อใช้สำหรับการนำเข้าในโทรศัพท์อื่นๆ " +
                        "ถ้าต้องการนำเข้าข้อมูล ให้มาที่หน้านี้ แล้วเลือก \"นำเข้าข้อมูล\""))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImportExportUtil.shareData(activity);
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    @NonNull
    public static AlertDialog getAddTeacherDialog(@NonNull final Activity activity, final NavController navController) {
        @SuppressLint("InflateParams") final View v = LayoutInflater.from(activity).inflate(R.layout.dialog_add_teacher, null, false);
        final TextInputEditText text = v.findViewById(R.id.text);
        final TextInputEditText text2 = v.findViewById(R.id.text2);
        return new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Kanit)
                .setTitle("กรอกข้อมูลของอาจารย์")
                .setView(v)
                .setPositiveButton("เรียบร้อย", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = TeacherLocationDatabase.getNewDetailId();
                        TeacherLocationDatabase.getInstance().putDetail(id, new TeacherDetail(id,
                                Objects.requireNonNull(text.getText()).toString(), Objects.requireNonNull(text2.getText()).toString()));
                        DataSaveHandler.saveCurrentTeacherLocationData();

                        ModifyTeacherLocationChooserFragment.openEditor(navController, id);
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    @NonNull
    public static AlertDialog getAddTeacherDialog(@NonNull final Activity activity) {
        @SuppressLint("InflateParams") final View v = LayoutInflater.from(activity).inflate(R.layout.dialog_add_teacher, null, false);
        final TextInputEditText text = v.findViewById(R.id.text);
        final TextInputEditText text2 = v.findViewById(R.id.text2);
        return new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Kanit)
                .setTitle("กรอกข้อมูลของอาจารย์")
                .setView(v)
                .setPositiveButton("เรียบร้อย", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = TeacherLocationDatabase.getNewDetailId();
                        TeacherLocationDatabase.getInstance().putDetail(id, new TeacherDetail(id,
                                Objects.requireNonNull(text.getText()).toString(), Objects.requireNonNull(text2.getText()).toString()));
                        //Todo: Start Activity
                        Intent intent = new Intent(v.getContext(), ModifyTeacherLocationEditorActivity.class);
                        intent.putExtra("teacherId", id);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }
}
