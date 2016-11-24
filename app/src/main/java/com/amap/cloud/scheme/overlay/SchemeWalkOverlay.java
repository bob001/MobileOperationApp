package com.amap.cloud.scheme.overlay;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.amap.cloud.scheme.constant.Const;
import com.R;

public class SchemeWalkOverlay extends WalkRouteOverlay {

	public SchemeWalkOverlay(Context arg0, AMap arg1, WalkPath arg2,
			LatLonPoint arg3, LatLonPoint arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BitmapDescriptor getStartBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.route_start);
	}

	@Override
	protected BitmapDescriptor getEndBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.route_end);
	}
	@Override
	protected BitmapDescriptor getWalkBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.cloud_walk);
	}
	@Override
	protected float getBuslineWidth() {
		return Const.ROUTE_LINE_WIDTH;
	}

}
