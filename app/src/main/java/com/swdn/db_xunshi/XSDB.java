package com.swdn.db_xunshi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swdn.model_qiangxiu.RepairInfo;
import com.swdn.model_xunshi.TourInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/11/15.
 */

public class XSDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "XunShi.db";

    /**
     * 数据库版本
     */
//    public static final int VERSION = 1;
    public static final int VERSION = 2;//在数据库表中增加了tour_person巡视人字段

    private static XSDB xsdb;

    private SQLiteDatabase db;

    /**
     * 私有构造方法
     *
     * @return
     */
    private XSDB(Context context) {
        XSOpenHelper dbHelper = new XSOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static XSDB getInstance(Context context) {
        if (xsdb == null) {
            xsdb = new XSDB(context);
        }
        return xsdb;
    }

    /**
     * 将TourInfo实例存储到数据库XunShi.db下的tour_info表中
     */
    public void saveTourInfo(TourInfo ti) {
        if (ti != null) {
            ContentValues values = new ContentValues();
            values.put("id",ti.getId());
            values.put("tour_category",ti.getTourCategory());
            values.put("tour_num",ti.getTourNum());
            values.put("pre_tour_person",ti.getPreTourPerson());
            values.put("pre_tour_date",ti.getPreTourDate());
            values.put("bds_id",ti.getBdsId());
            values.put("bds",ti.getBds());
            values.put("receive_person",ti.getReceivePerson());
            values.put("receive_date",ti.getReceiveDate());
            values.put("weather",ti.getWeather());
            values.put("temperature",ti.getTemperature());
            values.put("is_received",ti.getIsReceived());
            values.put("is_finished",ti.getIsFinished());
            values.put("tour_start_time",ti.getTourStartTime());
            values.put("tour_end_time",ti.getTourEndTime());
            values.put("tour_person",ti.getTourPerson());
            values.put("tour_situation",ti.getTourSituation());
            values.put("remarks",ti.getRemarks());
            db.insert("tour_info", null, values);
        }
    }

    /**
     * 加载数据库XunShi.db下的tour_info表的所有信息
     */
    public List<TourInfo> loadTourInfos() {
        List<TourInfo> tourInfos = new ArrayList<TourInfo>();
        Cursor cursor = db.query("tour_info", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TourInfo tourInfo = new TourInfo();
                tourInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tourInfo.setTourCategory(cursor.getString(cursor.getColumnIndex("tour_category")));
                tourInfo.setTourNum(cursor.getString(cursor.getColumnIndex("tour_num")));
                tourInfo.setPreTourPerson(cursor.getString(cursor.getColumnIndex("pre_tour_person")));
                tourInfo.setPreTourDate(cursor.getString(cursor.getColumnIndex("pre_tour_date")));
                tourInfo.setBdsId(cursor.getInt(cursor.getColumnIndex("bds_id")));
                tourInfo.setBds(cursor.getString(cursor.getColumnIndex("bds")));
                tourInfo.setReceivePerson(cursor.getString(cursor.getColumnIndex("receive_person")));
                tourInfo.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                tourInfo.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                tourInfo.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
                tourInfo.setIsReceived(cursor.getInt(cursor.getColumnIndex("is_received")));
                tourInfo.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                tourInfo.setTourStartTime(cursor.getString(cursor.getColumnIndex("tour_start_time")));
                tourInfo.setTourEndTime(cursor.getString(cursor.getColumnIndex("tour_end_time")));
                tourInfo.setTourPerson(cursor.getString(cursor.getColumnIndex("tour_person")));
                tourInfo.setTourSituation(cursor.getString(cursor.getColumnIndex("tour_situation")));
                tourInfo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                tourInfos.add(tourInfo);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return tourInfos;
    }

    /**
     * 更新指定id的巡视任务的接单状态和接单时间
     */
    public void updateReceiveStateAndDate(int id,int state,String date) {
        ContentValues values = new ContentValues();
        values.put("is_received",state);
        values.put("receive_date",date);
        db.update("tour_info",values,"id=?",new String[]{String.valueOf(id)});
    }

    /**
     * 更新指定id的抢修单的回填字段和处理状态字段
     */
    public void updateFillbackInfo(TourInfo tourInfo) {
        ContentValues values = new ContentValues();
        values.put("is_finished",tourInfo.getIsFinished());
        values.put("tour_start_time",tourInfo.getTourStartTime());
        values.put("tour_end_time",tourInfo.getTourEndTime());
        values.put("tour_person",tourInfo.getTourEndTime());
        values.put("tour_situation",tourInfo.getTourSituation());
        values.put("remarks",tourInfo.getRemarks());
        db.update("tour_info", values, "id=?",new String[]{String.valueOf(tourInfo.getId())});
    }

    /**
     * 通过时间来查询抢修数据
     */
    public List<TourInfo> queryByDate(String date) {
        List<TourInfo> list = new ArrayList<TourInfo>();
        String sql = "select * from tour_info " +
                "where receive_date like ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                TourInfo tourInfo = new TourInfo();
                tourInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tourInfo.setTourCategory(cursor.getString(cursor.getColumnIndex("tour_category")));
                tourInfo.setTourNum(cursor.getString(cursor.getColumnIndex("tour_num")));
                tourInfo.setPreTourPerson(cursor.getString(cursor.getColumnIndex("pre_tour_person")));
                tourInfo.setPreTourDate(cursor.getString(cursor.getColumnIndex("pre_tour_date")));
                tourInfo.setBdsId(cursor.getInt(cursor.getColumnIndex("bds_id")));
                tourInfo.setBds(cursor.getString(cursor.getColumnIndex("bds")));
                tourInfo.setReceivePerson(cursor.getString(cursor.getColumnIndex("receive_person")));
                tourInfo.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                tourInfo.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                tourInfo.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
                tourInfo.setIsReceived(cursor.getInt(cursor.getColumnIndex("is_received")));
                tourInfo.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                tourInfo.setTourStartTime(cursor.getString(cursor.getColumnIndex("tour_start_time")));
                tourInfo.setTourEndTime(cursor.getString(cursor.getColumnIndex("tour_end_time")));
                tourInfo.setTourPerson(cursor.getString(cursor.getColumnIndex("tour_person")));
                tourInfo.setTourSituation(cursor.getString(cursor.getColumnIndex("tour_situation")));
                tourInfo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                list.add(tourInfo);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 通过类型来查询抢修数据
     */
    public List<TourInfo> queryByCategory(String category) {
        List<TourInfo> list = new ArrayList<TourInfo>();
        Cursor cursor = db.query("tour_info", null, "tour_category=?", new String[]{category},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                TourInfo tourInfo = new TourInfo();
                tourInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tourInfo.setTourCategory(cursor.getString(cursor.getColumnIndex("tour_category")));
                tourInfo.setTourNum(cursor.getString(cursor.getColumnIndex("tour_num")));
                tourInfo.setPreTourPerson(cursor.getString(cursor.getColumnIndex("pre_tour_person")));
                tourInfo.setPreTourDate(cursor.getString(cursor.getColumnIndex("pre_tour_date")));
                tourInfo.setBdsId(cursor.getInt(cursor.getColumnIndex("bds_id")));
                tourInfo.setBds(cursor.getString(cursor.getColumnIndex("bds")));
                tourInfo.setReceivePerson(cursor.getString(cursor.getColumnIndex("receive_person")));
                tourInfo.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                tourInfo.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                tourInfo.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
                tourInfo.setIsReceived(cursor.getInt(cursor.getColumnIndex("is_received")));
                tourInfo.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                tourInfo.setTourStartTime(cursor.getString(cursor.getColumnIndex("tour_start_time")));
                tourInfo.setTourEndTime(cursor.getString(cursor.getColumnIndex("tour_end_time")));
                tourInfo.setTourPerson(cursor.getString(cursor.getColumnIndex("tour_person")));
                tourInfo.setTourSituation(cursor.getString(cursor.getColumnIndex("tour_situation")));
                tourInfo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                list.add(tourInfo);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 通过类型和时间查询抢修数据
     */
    public List<TourInfo> queryByCateDate(String cate, String date) {
        List<TourInfo> list = new ArrayList<TourInfo>();
        String sql = "select * from tour_info " +
                "where receive_date like ? and tour_category = ?";
        String[] selectionArgs = new String[]{"%" + date + "%",cate};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                TourInfo tourInfo = new TourInfo();
                tourInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tourInfo.setTourCategory(cursor.getString(cursor.getColumnIndex("tour_category")));
                tourInfo.setTourNum(cursor.getString(cursor.getColumnIndex("tour_num")));
                tourInfo.setPreTourPerson(cursor.getString(cursor.getColumnIndex("pre_tour_person")));
                tourInfo.setPreTourDate(cursor.getString(cursor.getColumnIndex("pre_tour_date")));
                tourInfo.setBdsId(cursor.getInt(cursor.getColumnIndex("bds_id")));
                tourInfo.setBds(cursor.getString(cursor.getColumnIndex("bds")));
                tourInfo.setReceivePerson(cursor.getString(cursor.getColumnIndex("receive_person")));
                tourInfo.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                tourInfo.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                tourInfo.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
                tourInfo.setIsReceived(cursor.getInt(cursor.getColumnIndex("is_received")));
                tourInfo.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                tourInfo.setTourStartTime(cursor.getString(cursor.getColumnIndex("tour_start_time")));
                tourInfo.setTourEndTime(cursor.getString(cursor.getColumnIndex("tour_end_time")));
                tourInfo.setTourPerson(cursor.getString(cursor.getColumnIndex("tour_person")));
                tourInfo.setTourSituation(cursor.getString(cursor.getColumnIndex("tour_situation")));
                tourInfo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                list.add(tourInfo);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 清空巡视信息表中的数据
     */
    public void clearTableContent(String tableName) {
        db.execSQL("delete from " + tableName);
    }

}
