package com.swdn.utils_xunjian;//package com.qiu.listviewoftourinspection.utils;
//
//import com.qiu.listviewoftourinspection.model.TourInspectionDevice;
//import com.qiu.listviewoftourinspection.model.TourInspectionModel;
//import com.qiu.listviewoftourinspection.model.TourInspectionTask;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
///**
// * Created by lenovo on 2016/9/30.
// */
//
//public class GsonUtil_Qiu {
//
//
//    public static ArrayList<TourInspectionModel> parseJSONString(String jsonData) {
//        ArrayList<TourInspectionModel> tiModelList = new ArrayList();
//        try {
//            TourInspectionModel model1 = new TourInspectionModel();
//            TourInspectionModel model2 = new TourInspectionModel();
//            JSONObject jsonObject = new JSONObject(jsonData);
//            JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
//            JSONObject jsonObject_213 = jsonObject_msg.getJSONObject("213");
//            JSONArray jsonArray_213 = jsonObject_213.getJSONArray("xssb");
//            JSONObject jsonObject_215 = jsonObject_msg.getJSONObject("215");
//            JSONArray jsonArray_215 = jsonObject_215.getJSONArray("xssb");
//
//            //213基本信息
//            TourInspectionTask tiTask213 = new TourInspectionTask();
//            tiTask213.setId(jsonObject_213.getInt("id"));
//            tiTask213.setWritePerson(jsonObject_213.getString("bzr"));
//            tiTask213.setExecPerson(jsonObject_213.getString("yxr"));
//            tiTask213.setExecDate(jsonObject_213.getString("bzsj"));
//            tiTask213.setCategory(jsonObject_213.getString("xslx"));
//            tiTask213.setStateBackfill(0);
//            model1.setTi_task(tiTask213);
//            //213任务的需要巡检哪些设备
//            ArrayList<TourInspectionDevice> ti_deviceList213 = new ArrayList<TourInspectionDevice>();
//            for(int i = 0;i < jsonArray_213.length();i++) {
//                JSONObject jsonObj = jsonArray_213.getJSONObject(i);
//                TourInspectionDevice ti_device = new TourInspectionDevice();
//                ti_device.setDevId(jsonObj.getInt("dev_id"));
//                ti_device.setDevName(jsonObj.getString("dev_name"));
//                ti_device.setDevType(jsonObj.getString("dev_type"));
//                ti_device.setLocation(jsonObj.getString("dev_loc"));
//                ti_device.setTourKey(jsonObj.getString("xszd"));
//                ti_device.setRemarks(jsonObj.getString("xsbz"));
//                ti_deviceList213.add(ti_device);
//            }
//            model1.setTi_devices(ti_deviceList213);
//
//            //215基本信息
//            TourInspectionTask tiTask215 = new TourInspectionTask();
//            tiTask215.setId(jsonObject_213.getInt("id"));
//            tiTask215.setWritePerson(jsonObject_213.getString("bzr"));
//            tiTask215.setExecPerson(jsonObject_213.getString("yxr"));
//            tiTask215.setExecDate(jsonObject_213.getString("bzsj"));
//            tiTask215.setCategory(jsonObject_213.getString("xslx"));
//            tiTask215.setStateBackfill(0);
//            model2.setTi_task(tiTask215);
//            //215任务的需要巡检哪些设备
//            ArrayList<TourInspectionDevice> ti_deviceList215 = new ArrayList<TourInspectionDevice>();
//            for(int i = 0;i < jsonArray_215.length();i++) {
//                JSONObject jsonObj = jsonArray_215.getJSONObject(i);
//                TourInspectionDevice ti_device = new TourInspectionDevice();
//                ti_device.setDevId(jsonObj.getInt("dev_id"));
//                ti_device.setDevName(jsonObj.getString("dev_name"));
//                ti_device.setDevType(jsonObj.getString("dev_type"));
//                ti_device.setLocation(jsonObj.getString("dev_loc"));
//                ti_device.setTourKey(jsonObj.getString("xszd"));
//                ti_device.setRemarks(jsonObj.getString("xsbz"));
//                ti_deviceList215.add(ti_device);
//            }
//            model2.setTi_devices(ti_deviceList215);
//            tiModelList.add(model1);
//            tiModelList.add(model2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return tiModelList;
//    }
//}
