package com.swdn.db_qiangxiu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swdn.model_qiangxiu.RepairInfo;
import com.swdn.model_xunjian.TourInspectionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/11/9.
 */

public class QXDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "QiangXiu.db";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static QXDB qxdb;

    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     */
    private QXDB(Context context) {
        QXOpenHelper dbHelper = new QXOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取QXDB的实例
     */
    public synchronized static QXDB getInstance(Context context) {
        if (qxdb == null) {
            qxdb = new QXDB(context);
        }
        return qxdb;
    }

    /**
     * 将RepairInfo实例存储到数据库qiangxiu.db下的repair_info表中
     */
    public void saveRepairInfo(RepairInfo ri) {
        if (ri != null) {
            ContentValues values = new ContentValues();
            values.put("id", ri.getId());
            values.put("alarm_id", ri.getAlarmId());
            values.put("manager", ri.getManager());
            values.put("category", ri.getCategory());
            values.put("address", ri.getAddress());
            values.put("group_info", ri.getGroupInfo());
            values.put("process_state", ri.getProcessState());
            values.put("receive_state", ri.getReceiveState());
            values.put("receiver", ri.getReceiver());
            values.put("receive_date", ri.getReceiveDate());
            values.put("arrival_time", ri.getArrivalTime());
            values.put("end_time", ri.getEndTime());
            values.put("repair_persons", ri.getRepairPersons());
            values.put("failure_cause", ri.getFailureCause());
            values.put("execute_situation", ri.getExecuteSituation());
            values.put("remarks", ri.getRemarks());
            db.insert("repair_info", null, values);
        }
    }

    /**
     * 加载数据库qiangxiu.db下的repair_info表的所有信息
     */
    public List<RepairInfo> loadRepairInfos() {
        List<RepairInfo> repairInfos = new ArrayList<RepairInfo>();
        Cursor cursor = db.query("repair_info", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RepairInfo ri = new RepairInfo();
                ri.setId(cursor.getInt(cursor.getColumnIndex("id")));
                ri.setAlarmId(cursor.getInt(cursor.getColumnIndex("alarm_id")));
                ri.setManager(cursor.getString(cursor.getColumnIndex("manager")));
                ri.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                ri.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                ri.setGroupInfo(cursor.getString(cursor.getColumnIndex("group_info")));
                ri.setProcessState(cursor.getInt(cursor.getColumnIndex("process_state")));
                ri.setReceiveState(cursor.getInt(cursor.getColumnIndex("receive_state")));
                ri.setReceiver(cursor.getString(cursor.getColumnIndex("receiver")));
                ri.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                ri.setArrivalTime(cursor.getString(cursor.getColumnIndex("arrival_time")));
                ri.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                ri.setRepairPersons(cursor.getString(cursor.getColumnIndex("repair_persons")));
                ri.setFailureCause(cursor.getString(cursor.getColumnIndex("failure_cause")));
                ri.setExecuteSituation(cursor.getString(cursor.getColumnIndex("execute_situation")));
                ri.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                repairInfos.add(ri);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return repairInfos;
    }

    /**
     * 更新指定id的抢修任务的接单状态和接单时间
     */
    public void updateReceiveStateAndDate(int id,int state,String date) {
        ContentValues values = new ContentValues();
        values.put("receive_state",state);
        values.put("receive_date",date);
        db.update("repair_info",values,"id=?",new String[]{String.valueOf(id)});
    }

    /**
     * 更新指定id的抢修单的回填字段和处理状态字段
     */
    public void updateFillbackInfo(RepairInfo repairInfo) {
        ContentValues values = new ContentValues();
        values.put("process_state",repairInfo.getProcessState());
        values.put("arrival_time",repairInfo.getArrivalTime());
        values.put("end_time",repairInfo.getEndTime());
        values.put("repair_persons",repairInfo.getRepairPersons());
        values.put("failure_cause",repairInfo.getFailureCause());
        values.put("execute_situation",repairInfo.getExecuteSituation());
        values.put("remarks",repairInfo.getRemarks());
        db.update("repair_info", values, "id=?",new String[]{String.valueOf(repairInfo.getId())});
    }

    /**
     * 通过时间来查询抢修数据
     */
    public List<RepairInfo> queryByDate(String date) {
        List<RepairInfo> list = new ArrayList<RepairInfo>();
        String sql = "select * from repair_info " +
                "where receive_date like ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                RepairInfo ri = new RepairInfo();
                ri.setId(cursor.getInt(cursor.getColumnIndex("id")));
                ri.setAlarmId(cursor.getInt(cursor.getColumnIndex("alarm_id")));
                ri.setManager(cursor.getString(cursor.getColumnIndex("manager")));
                ri.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                ri.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                ri.setGroupInfo(cursor.getString(cursor.getColumnIndex("group_info")));
                ri.setProcessState(cursor.getInt(cursor.getColumnIndex("process_state")));
                ri.setReceiveState(cursor.getInt(cursor.getColumnIndex("receive_state")));
                ri.setReceiver(cursor.getString(cursor.getColumnIndex("receiver")));
                ri.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                ri.setArrivalTime(cursor.getString(cursor.getColumnIndex("arrival_time")));
                ri.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                ri.setRepairPersons(cursor.getString(cursor.getColumnIndex("repair_persons")));
                ri.setFailureCause(cursor.getString(cursor.getColumnIndex("failure_cause")));
                ri.setExecuteSituation(cursor.getString(cursor.getColumnIndex("execute_situation")));
                ri.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                list.add(ri);
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
    public List<RepairInfo> queryByCategory(String category) {
        List<RepairInfo> list = new ArrayList<RepairInfo>();
        Cursor cursor = db.query("repair_info", null, "category=?", new String[]{category},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                RepairInfo ri = new RepairInfo();
                ri.setId(cursor.getInt(cursor.getColumnIndex("id")));
                ri.setAlarmId(cursor.getInt(cursor.getColumnIndex("alarm_id")));
                ri.setManager(cursor.getString(cursor.getColumnIndex("manager")));
                ri.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                ri.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                ri.setGroupInfo(cursor.getString(cursor.getColumnIndex("group_info")));
                ri.setProcessState(cursor.getInt(cursor.getColumnIndex("process_state")));
                ri.setReceiveState(cursor.getInt(cursor.getColumnIndex("receive_state")));
                ri.setReceiver(cursor.getString(cursor.getColumnIndex("receiver")));
                ri.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                ri.setArrivalTime(cursor.getString(cursor.getColumnIndex("arrival_time")));
                ri.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                ri.setRepairPersons(cursor.getString(cursor.getColumnIndex("repair_persons")));
                ri.setFailureCause(cursor.getString(cursor.getColumnIndex("failure_cause")));
                ri.setExecuteSituation(cursor.getString(cursor.getColumnIndex("execute_situation")));
                ri.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                list.add(ri);
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
    public List<RepairInfo> queryByCateDate(String cate, String date) {
        List<RepairInfo> list = new ArrayList<RepairInfo>();
        String sql = "select * from repair_info " +
                "where receive_date like ? and category = ?";
        String[] selectionArgs = new String[]{"%" + date + "%",cate};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                RepairInfo ri = new RepairInfo();
                ri.setId(cursor.getInt(cursor.getColumnIndex("id")));
                ri.setAlarmId(cursor.getInt(cursor.getColumnIndex("alarm_id")));
                ri.setManager(cursor.getString(cursor.getColumnIndex("manager")));
                ri.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                ri.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                ri.setGroupInfo(cursor.getString(cursor.getColumnIndex("group_info")));
                ri.setProcessState(cursor.getInt(cursor.getColumnIndex("process_state")));
                ri.setReceiveState(cursor.getInt(cursor.getColumnIndex("receive_state")));
                ri.setReceiver(cursor.getString(cursor.getColumnIndex("receiver")));
                ri.setReceiveDate(cursor.getString(cursor.getColumnIndex("receive_date")));
                ri.setArrivalTime(cursor.getString(cursor.getColumnIndex("arrival_time")));
                ri.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                ri.setRepairPersons(cursor.getString(cursor.getColumnIndex("repair_persons")));
                ri.setFailureCause(cursor.getString(cursor.getColumnIndex("failure_cause")));
                ri.setExecuteSituation(cursor.getString(cursor.getColumnIndex("execute_situation")));
                ri.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                list.add(ri);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将数据库中的某表内容清空
     * @param tableName
     */
    public void clearTableContent(String tableName) {
        db.execSQL("delete from " + tableName);
    }

}
