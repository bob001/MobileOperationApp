package com.swdn.db_xunshi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/11/15.
 */

public class XSOpenHelper extends SQLiteOpenHelper {

    /**
     * TourInfo表建表语句
     */
    public static final String CREATE_TourInfo = "create table tour_info (" +
            "id integer primary key," +
            "tour_category text," +
            "tour_num text," +
            "pre_tour_person text," +
            "pre_tour_date," +
            "bds_id integer," +
            "bds text," +
            "receive_person text," +
            "receive_date text," +
            "weather text," +
            "temperature text," +
            "is_received integer," +
            "is_finished integer," +
            "tour_start_time text," +
            "tour_end_time text," +
            "tour_person text," +   //增加了巡视人字段
            "tour_situation text," +
            "remarks text)";

    public XSOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TourInfo);//创建巡视信息表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("alter table tour_info add column tour_person text");
            default:
        }
    }



}
