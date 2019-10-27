package th.ac.sk.timetableapp.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import th.ac.sk.timetableapp.MasterActivity;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.database.ImportExportUtil;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.fragment.ModifyTeacherLocationChooserFragment;
import th.ac.sk.timetableapp.model.TeacherDetail;


public abstract class DialogBuilder {

    @NonNull
    public static AlertDialog getWipeDataDialog(@NonNull final Activity activity) {
        return new MaterialAlertDialogBuilder(activity, R.style.AppTheme_AlertDialog)
                .setTitle("ต้องการล้างข้อมูลทั้งหมดหรือไม่")
                .setView(inflateDialogMessage(activity, "ข้อมูลทั้งหมดที่บันทึกไว้จะหายไป"))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImportExportUtil.wipeData();
                        Snackbar.make(activity.findViewById(android.R.id.content), "ล้างข้อมูลสำเร็จ", Snackbar.LENGTH_LONG).show();
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
        return new MaterialAlertDialogBuilder(activity, R.style.AppTheme_AlertDialog)
                .setTitle("ต้องการกลับไปใช้ข้อมูลของ ม.502 หรือไม่")
                .setView(inflateDialogMessage(activity, "ข้อมูลทั้งหมดที่บันทึกไว้จะถูกแทนที่ด้วยข้อมูลของ ม.502 สำหรับการทดสอบ"))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImportExportUtil.rollbackToPlaceholderData();
                        Snackbar.make(activity.findViewById(android.R.id.content), "ใช้งานข้อมูลทดสอบสำเร็จ", Snackbar.LENGTH_LONG).show();
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
        return new MaterialAlertDialogBuilder(activity, R.style.AppTheme_AlertDialog)
                .setTitle("กรอกข้อมูลนำเข้า")
                .setView(v)
                .setPositiveButton("เรียบร้อย", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = ImportExportUtil.importData(Objects.requireNonNull(text.getText()).toString());
                        if (intent != null) {
                            intent.setComponent(new ComponentName(activity, MasterActivity.class));
                            activity.startActivityForResult(intent, MasterActivity.REQUEST_IMPORT);
                        } else
                            Snackbar.make(activity.findViewById(android.R.id.content), "นำเข้าข้อมูลไม่สำเร็จ", Snackbar.LENGTH_LONG).show();
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
    private static View inflateDialogMessage(@NonNull final Fragment fragment, String s) {
        return inflateDialogMessage(fragment.requireActivity(), s);
    }

    @NonNull
    public static AlertDialog getExportDataDialog(@NonNull final Activity activity) {
        return new MaterialAlertDialogBuilder(activity, R.style.AppTheme_AlertDialog)
                .setTitle("ต้องการส่งออกข้อมูลหรือไม่")
                .setView(inflateDialogMessage(activity, "คุณจะได้ข้อความชุดหนึ่ง เพื่อใช้สำหรับการนำเข้าข้อมูลในโทรศัพท์อื่นๆ " +
                        "ถ้าต้องการนำเข้าข้อมูล ให้คัดลอกข้อความดังกล่าว แล้วใส่ในหน้า \"นำเข้าข้อมูล\""))
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
        return new MaterialAlertDialogBuilder(activity, R.style.AppTheme_AlertDialog)
                .setTitle("กรอกข้อมูลของอาจารย์")
                .setView(v)
                .setPositiveButton("เรียบร้อย", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSaveHandler.loadMaster();
                        int id = TeacherLocationDatabase.getNewTeacherID();
                        TeacherLocationDatabase.getInstance().putDetailAt(id, new TeacherDetail(id,
                                Objects.requireNonNull(text.getText()).toString(), Objects.requireNonNull(text2.getText()).toString()));
                        DataSaveHandler.saveMaster();
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
    public static AlertDialog getDeleteItemDialog(@NonNull final Fragment fragment, final TeacherDetail detail) {
        return new MaterialAlertDialogBuilder(fragment.requireContext(), R.style.AppTheme_AlertDialog)
                .setTitle("ยืนยันการลบข้อมูล")
                .setView(inflateDialogMessage(fragment, String.format("ข้อมูลของ อ. %s %s จะถูกลบและไม่สามารถกู้คืนกลับมาได้อีก", detail.name, detail.surname)))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TeacherLocationDatabase.getInstance().removeLocationHashAt(detail.id);
                        TeacherLocationDatabase.getInstance().removeDetailAt(detail.id);
                        Snackbar.make(fragment.requireView(), "ลบข้อมูลสำเร็จ", Snackbar.LENGTH_LONG).show();
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
}
