package th.ac.sk.timetableapp.tool;

import android.content.Context;

import th.ac.sk.timetableapp.database.DataSaveHandler;

public class InstantiateHelper {
    public static void instantiate(Context context) {
        StaticUtil.getInstance().applyContext(context);
        DataSaveHandler.getInstance();
    }
}
