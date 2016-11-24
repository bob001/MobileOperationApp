package com.swdn.util;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.swdn.entity.InspectionPlan;

/**
 * Json字符串解析工具类
 * 
 * @author 苦涩
 */

public class MyJson {
	//www.javaapk.com
	// 解析糗事方法
	public List<InspectionPlan> getInspectionPlanList(String value) {
		List<InspectionPlan> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<InspectionPlan>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				InspectionPlan info = new InspectionPlan();
				info.setState(job.getInt("STATE"));
				info.setType(job.getString("TYPE"));
				info.setName(job.getString("NAME"));
				info.setDate(job.getString("DATE"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}

	

}
