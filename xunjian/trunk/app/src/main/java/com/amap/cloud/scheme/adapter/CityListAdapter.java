package com.amap.cloud.scheme.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.cloud.scheme.model.City;
import com.swdn.R;

import java.util.ArrayList;


public class CityListAdapter extends BaseAdapter {
	private Context ctx;
	private ArrayList<City> list;

	// private int index;

	public CityListAdapter(Context context, ArrayList<City> list) {
		this.ctx = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(ctx, R.layout.item_city_list, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.letter = (TextView) convertView.findViewById(R.id.letter);
			holder.tv_line_margin = (TextView) convertView.findViewById(R.id.tv_line_margin);
			holder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		City item = list.get(position);
		if (TextUtils.isEmpty(item.letter)) {
			holder.letter.setVisibility(View.INVISIBLE);
			holder.tv_line_margin.setVisibility(View.VISIBLE);
			holder.tv_line.setVisibility(View.GONE);
		} else {
			holder.letter.setVisibility(View.VISIBLE);
			holder.letter.setText(item.letter);
			holder.tv_line_margin.setVisibility(View.GONE);
			holder.tv_line.setVisibility(View.VISIBLE);
		}
		holder.name.setText(item.name);

		return convertView;
	}

	private class ViewHolder {
		TextView name;
		TextView letter;
		TextView tv_line;
		TextView tv_line_margin;
	}
}
