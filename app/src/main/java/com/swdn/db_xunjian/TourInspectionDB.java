package com.swdn.db_xunjian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.swdn.model_xunjian.TourInspectionDev;
import com.swdn.model_xunjian.TourInspectionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/7.
 */

public class TourInspectionDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "tour_inspection";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static TourInspectionDB tourInspectionDB;
    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     */
    private TourInspectionDB(Context context) {
        TourInspectionOpenHelper dbHelper = new TourInspectionOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static TourInspectionDB getInstance(Context context) {
        if (tourInspectionDB == null) {
            tourInspectionDB = new TourInspectionDB(context);
        }
        return tourInspectionDB;
    }

    /**
     * 将数据库中的某表内容清空
     */
    public void clearTableContent(String tableName) {
        db.execSQL("delete from " + tableName);
    }

    /**
     * 将TourInspectionTask实例存储到数据库
     */
    public void saveTourInspectionTask(TourInspectionTask tourInspectionTask) {
        if (tourInspectionTask != null) {
            ContentValues values = new ContentValues();
            values.put("id",tourInspectionTask.getId());
            values.put("category",tourInspectionTask.getCategory());
            values.put("write_person",tourInspectionTask.getWritePerson());
            values.put("exec_person",tourInspectionTask.getExecPerson());
            values.put("exec_date",tourInspectionTask.getExecDate());
            values.put("is_finished",tourInspectionTask.getIsFinished());
            values.put("dept",tourInspectionTask.getDept());
            db.insert("tourinspection_task", null, values);
        }
    }

    /**
     * 读取所有的任务信息
     */
    public List<TourInspectionTask> loadTourInspectionTasks() {
        List<TourInspectionTask> list = new ArrayList<TourInspectionTask>();
        Cursor cursor = db.query("tourinspection_task", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TourInspectionTask tourInspectionTask = new TourInspectionTask();
                tourInspectionTask.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tourInspectionTask.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                tourInspectionTask.setDept(cursor.getString(cursor.getColumnIndex("dept")));
                tourInspectionTask.setExecDate(cursor.getString(cursor.getColumnIndex("exec_date")));
                tourInspectionTask.setExecPerson(cursor.getString(cursor.getColumnIndex("exec_person")));
                tourInspectionTask.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                tourInspectionTask.setWritePerson(cursor.getString(cursor.getColumnIndex("write_person")));
                list.add(tourInspectionTask);
            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }


    /**
     * 读取某一任务id的任务信息
     */
    public TourInspectionTask loadTourInspectionTaskById(int id) {
        Cursor cursor = db.query("tourinspection_task", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        TourInspectionTask tourInspectionTask = new TourInspectionTask();
        if (cursor.moveToFirst()) {
            tourInspectionTask.setId(cursor.getInt(cursor.getColumnIndex("id")));
            tourInspectionTask.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            tourInspectionTask.setDept(cursor.getString(cursor.getColumnIndex("dept")));
            tourInspectionTask.setExecDate(cursor.getString(cursor.getColumnIndex("exec_date")));
            tourInspectionTask.setExecPerson(cursor.getString(cursor.getColumnIndex("exec_person")));
            tourInspectionTask.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
            tourInspectionTask.setWritePerson(cursor.getString(cursor.getColumnIndex("write_person")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return tourInspectionTask;
    }

    /**
     * 模糊查询,查询日期中包含date的记录
     * @param date
     * @return
     */
    public List<TourInspectionTask> queryTasksByDate(String date) {
        List<TourInspectionTask> list = new ArrayList<TourInspectionTask>();
        String sql = "select * from tourinspection_task " +
                "where exec_date like ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                TourInspectionTask task = new TourInspectionTask();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                task.setDept(cursor.getString(cursor.getColumnIndex("dept")));
                task.setExecDate(cursor.getString(cursor.getColumnIndex("exec_date")));
                task.setExecPerson(cursor.getString(cursor.getColumnIndex("exec_person")));
                task.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                task.setWritePerson(cursor.getString(cursor.getColumnIndex("write_person")));
                list.add(task);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;

    }

//    public List<TourInspectionTask> queryTasksByDate(String date) {
//        List<TourInspectionTask> list = new ArrayList<TourInspectionTask>();
//        Cursor cursor = db.query("tourinspection_task", null, "exec_date=?", new String[]{date},null,null,null);
//        if (cursor.moveToFirst()) {
//            do {
//                TourInspectionTask task = new TourInspectionTask();
//                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                task.setCategory(cursor.getString(cursor.getColumnIndex("category")));
//                task.setDept(cursor.getString(cursor.getColumnIndex("dept")));
//                task.setExecDate(cursor.getString(cursor.getColumnIndex("exec_date")));
//                task.setExecPerson(cursor.getString(cursor.getColumnIndex("exec_person")));
//                task.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
//                task.setWritePerson(cursor.getString(cursor.getColumnIndex("write_person")));
//                list.add(task);
//            } while (cursor.moveToNext());
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//        return list;
//    }

    /**
     * 查询类型为category的数据
     * @param category
     * @return
     */
    public List<TourInspectionTask> queryTasksByCategory(String category) {
        List<TourInspectionTask> list = new ArrayList<TourInspectionTask>();
        Cursor cursor = db.query("tourinspection_task", null, "category=?", new String[]{category},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                TourInspectionTask task = new TourInspectionTask();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                task.setDept(cursor.getString(cursor.getColumnIndex("dept")));
                task.setExecDate(cursor.getString(cursor.getColumnIndex("exec_date")));
                task.setExecPerson(cursor.getString(cursor.getColumnIndex("exec_person")));
                task.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                task.setWritePerson(cursor.getString(cursor.getColumnIndex("write_person")));
                list.add(task);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 查询类型为category和时间为date的数据
     * @param category
     * @param date
     */
    public List<TourInspectionTask> queryTasksByCateTime(String category,String date){
        List<TourInspectionTask> list = new ArrayList<TourInspectionTask>();
        String sql = "select * from tourinspection_task " +
                "where exec_date like ? and category = ?";
        String[] selectionArgs = new String[]{"%" + date + "%",category};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                TourInspectionTask task = new TourInspectionTask();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                task.setDept(cursor.getString(cursor.getColumnIndex("dept")));
                task.setExecDate(cursor.getString(cursor.getColumnIndex("exec_date")));
                task.setExecPerson(cursor.getString(cursor.getColumnIndex("exec_person")));
                task.setIsFinished(cursor.getInt(cursor.getColumnIndex("is_finished")));
                task.setWritePerson(cursor.getString(cursor.getColumnIndex("write_person")));
                list.add(task);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将TourInspectionDev实例存储到数据库
     */
    public void saveTourInspectionDev(TourInspectionDev tourInspectionDev) {
        if (tourInspectionDev != null) {
            ContentValues values = new ContentValues();
            values.put("id",tourInspectionDev.getId());
            values.put("dev_id",tourInspectionDev.getDevId());
            values.put("dev_name",tourInspectionDev.getDevName());
            values.put("dev_type",tourInspectionDev.getDevType());
            values.put("location",tourInspectionDev.getLocation());
            values.put("pretour_key",tourInspectionDev.getPretourKey());
            values.put("remarks",tourInspectionDev.getRemarks());
            values.put("task_id",tourInspectionDev.getTaskId());
            values.put("tour_date",tourInspectionDev.getTourDate());
            values.put("tour_person",tourInspectionDev.getTourPerson());
            values.put("tour_key",tourInspectionDev.getTourKey());
            values.put("tour_end",tourInspectionDev.getTourEnd());
            values.put("tour_remarks",tourInspectionDev.getTourRemarks());
            values.put("is_commited",tourInspectionDev.getIsCommited());
            db.insert("tourinspection_dev", null, values);
        }
    }

    /**
     * 读取某一任务id下的所有巡检设备信息
     */
    public List<TourInspectionDev> loadTourInspectionDevs(int taskId) {
        List<TourInspectionDev> list = new ArrayList<TourInspectionDev>();
        Cursor cursor = db.query("tourinspection_dev", null, "task_id=?", new String[]{String.valueOf(taskId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TourInspectionDev tourInspectionDev = new TourInspectionDev();
                tourInspectionDev.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tourInspectionDev.setTaskId(cursor.getInt(cursor.getColumnIndex("task_id")));
                tourInspectionDev.setDevId(cursor.getString(cursor.getColumnIndex("dev_id")));
                tourInspectionDev.setDevName(cursor.getString(cursor.getColumnIndex("dev_name")));
                tourInspectionDev.setDevType(cursor.getString(cursor.getColumnIndex("dev_type")));
                tourInspectionDev.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                tourInspectionDev.setPretourKey(cursor.getString(cursor.getColumnIndex("pretour_key")));
                tourInspectionDev.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                tourInspectionDev.setTourDate(cursor.getString(cursor.getColumnIndex("tour_date")));
                tourInspectionDev.setTourPerson(cursor.getString(cursor.getColumnIndex("tour_person")));
                tourInspectionDev.setTourKey(cursor.getString(cursor.getColumnIndex("tour_key")));
                tourInspectionDev.setTourEnd(cursor.getString(cursor.getColumnIndex("tour_end")));
                tourInspectionDev.setTourRemarks(cursor.getString(cursor.getColumnIndex("tour_remarks")));
                tourInspectionDev.setIsCommited(cursor.getInt(cursor.getColumnIndex("is_commited")));
                list.add(tourInspectionDev);
            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 读取某一巡检设备id的信息
     */
    public TourInspectionDev loadTourInspectionDevById(int devId) {
        Cursor cursor = db.query("tourinspection_dev", null, "id=?", new String[]{String.valueOf(devId)}, null, null, null);
        TourInspectionDev tourInspectionDev = new TourInspectionDev();
        if (cursor.moveToFirst()) {
            tourInspectionDev.setId(cursor.getInt(cursor.getColumnIndex("id")));
            tourInspectionDev.setTaskId(cursor.getInt(cursor.getColumnIndex("task_id")));
            tourInspectionDev.setDevId(cursor.getString(cursor.getColumnIndex("dev_id")));
            tourInspectionDev.setDevName(cursor.getString(cursor.getColumnIndex("dev_name")));
            tourInspectionDev.setDevType(cursor.getString(cursor.getColumnIndex("dev_type")));
            tourInspectionDev.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            tourInspectionDev.setPretourKey(cursor.getString(cursor.getColumnIndex("pretour_key")));
            tourInspectionDev.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
            tourInspectionDev.setTourDate(cursor.getString(cursor.getColumnIndex("tour_date")));
            tourInspectionDev.setTourPerson(cursor.getString(cursor.getColumnIndex("tour_person")));
            tourInspectionDev.setTourKey(cursor.getString(cursor.getColumnIndex("tour_key")));
            tourInspectionDev.setTourEnd(cursor.getString(cursor.getColumnIndex("tour_end")));
            tourInspectionDev.setTourRemarks(cursor.getString(cursor.getColumnIndex("tour_remarks")));
            tourInspectionDev.setIsCommited(cursor.getInt(cursor.getColumnIndex("is_commited")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return tourInspectionDev;
    }

    /**
     * 更新某个巡检设备的回填字段设为1
     */
    public void updateBackfill_TIDev(int id,String tourDate,String tourPerson,String tourKey,String tourEnd,
                                     String tourRemarks) {
        ContentValues values = new ContentValues();
        values.put("is_commited", 1);
        values.put("tour_date", tourDate);
        values.put("tour_person", tourPerson);
        values.put("tour_key", tourKey);
        values.put("tour_end", tourEnd);
        values.put("tour_remarks", tourRemarks);
        db.update("tourinspection_dev", values, "id=?", new String[]{String.valueOf(id)});
    }

    /**
     * 更新task表的是否完成字段，设为1
     */
    public void updateIsComplished_TITask(int id) {
        ContentValues values = new ContentValues();
        values.put("is_finished",1);
        db.update("tourinspection_task",values,"id=?",new String[]{String.valueOf(id)});
    }

}
