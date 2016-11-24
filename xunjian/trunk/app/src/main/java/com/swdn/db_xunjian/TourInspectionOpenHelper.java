package com.swdn.db_xunjian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/10/7.
 */

public class TourInspectionOpenHelper extends SQLiteOpenHelper {

    /**
     * 可以在创建表之前判断，这样就不会重新创建，create table if not exists Student(name text primary key, code integer);
     * 比平时多了if not exists
     */

    /**
     * 巡检任务表建表语句
     */
    public static final String CREATE_TOURINSPECTIONTASK = "create table if not exists tourinspection_task (" +
            "id integer primary key," +
            "category text," +
            "write_person text," +
            "exec_person text," +
            "exec_date text," +
            "is_finished integer," +
            "dept text)";

    /**
     * 巡检设备表建表语句
     */
    public static final String CREATE_TOURINSPECTIONDEV = "create table if not exists tourinspection_dev (" +
            "id integer primary key," +
            "dev_id text," +
            "dev_name text," +
            "dev_type text," +
            "location text," +
            "pretour_key text ," +
            "remarks text," +
            "task_id integer," +  //对应巡检任务表的主键，是表的外键
            //下面的5个 回填信息
            "tour_date text," +
            "tour_person text," +
            "tour_key text," +
            "tour_end text," +
            "tour_remarks text," +
            "is_commited integer)";

    public TourInspectionOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TOURINSPECTIONTASK);//创建巡检任务表
        sqLiteDatabase.execSQL(CREATE_TOURINSPECTIONDEV);//创建巡检设备表

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
