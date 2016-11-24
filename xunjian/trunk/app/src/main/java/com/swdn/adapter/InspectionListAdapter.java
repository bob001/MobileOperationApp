package com.swdn.adapter;

import java.util.List;

import com.swdn.R;
import com.swdn.entity.AshamedInfo;
import com.swdn.entity.InspectionPlan;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//www.javaapk.com
public class InspectionListAdapter extends BaseAdapter {

	private List<InspectionPlan> list;
	private Context ctx;

	public InspectionListAdapter(Context ctx, List<InspectionPlan> list) {
		this.list = list;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(final int index, View view, ViewGroup group) {
		final Holder hold;
		if (view == null) {
			hold = new Holder();
			view = View.inflate(ctx, R.layout.inspection_listview_item, null);
			hold.state = (ImageView) view.findViewById(R.id.inspection_item_state);
			hold.type = (TextView) view.findViewById(R.id.inspection_item_type);
			hold.name = (TextView) view.findViewById(R.id.inspection_item_name);
			hold.date = (TextView) view.findViewById(R.id.inspection_item_date);
			view.setTag(hold);
		} else {
			hold = (Holder) view.getTag();
		}
		if(list.get(index).getState() == 1){
			hold.state.setImageResource(R.drawable.finish);
		}else if(list.get(index).getState() == 0){
			hold.state.setImageResource(R.drawable.unfinish);
		}
		hold.type.setText(list.get(index).getType());
		hold.name.setText(list.get(index).getName());
		hold.date.setText(list.get(index).getDate());
		return view;
	}

	/**
	 * 列表项详细结构类
	 * @author wuxian
	 *
	 */
	static class Holder {
		ImageView state;
		TextView type;
		TextView name;
		TextView date;
	}
}
