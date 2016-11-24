package com.amap.cloud.scheme.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.swdn.R;
import com.amapsw.model.AppInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class mapAdapter extends ArrayAdapter<AppInfo> {
      private  int resourceid;
    public mapAdapter(Context context, int resource, List<AppInfo> objects) {
        super(context, resource,objects);
        resourceid=resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfo appInfo=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null)
        {
            view=LayoutInflater.from(getContext()).inflate(resourceid,null);
            viewHolder=new ViewHolder();
            viewHolder.title=(TextView)view.findViewById(R.id.item_name);
            view.setTag(viewHolder);
        }else
        {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.title.setText(appInfo.getAppLabel());
        return view;
    }


    private class ViewHolder {
        TextView  title;
        Button ibtn;
    }


}