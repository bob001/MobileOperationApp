package com.swdn.db_qiangxiu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/11/9.
 */

public class QXOpenHelper extends SQLiteOpenHelper {

    /**
     * RepairInfo表建表语句
     */
    public static final String CREATE_RepairInfo = "create table repair_info (" +
            "id integer primary key," +
            "alarm_id integer," +
            "manager text," +
            "category text," +
            "address text," +
            "task_code text," +//版本3增加的列
            "group_info text," +
            "process_state integer," +
            "receive_state integer," +
            "lon real," +             //版本2增加的列
            "lat real," +             //版本2增加的列
            "receiver text," +
            "receive_date text," +
            "arrival_time text," +
            "end_time text," +
            "repair_persons text," +
            "failure_cause text," +
            "execute_situation text," +
            "remarks text)";

    public QXOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RepairInfo);//创建RepairInfo表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("alter table repair_info add column lon real");
                db.execSQL("alter table repair_info add column lat real");
            case 2:
                db.execSQL("alter table repair_info add column task_code text");
            default:
        }
    }
}