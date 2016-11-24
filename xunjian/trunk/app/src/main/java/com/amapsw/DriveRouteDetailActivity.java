package com.amapsw;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.cloud.scheme.adapter.DriveSegmentListAdapter;
import com.amap.cloud.scheme.constant.BundleFlag;
import com.amap.cloud.scheme.constant.Const;
import com.amap.cloud.scheme.util.SchemeUtil;
import com.amapsw.model.AppInfo;
import com.amapsw.widget.ActionSheetDialog;
import com.amapsw.widget.AlertDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.swdn.R;

/**
 * 点击路线规划页面中的 车辆tab下面的 详情 进入的页面 用来通过文字列表显示车辆导航的路线规划
 *
 * @author lingxiang.wang
 *
 */

public class DriveRouteDetailActivity extends Activity {
    private DrivePath mDrivePath;
    private DriveRouteResult mDriveRouteResult;
    private TextView mTitleDesTv;
    private TextView mTitleDriveRoute, mDesDriveRoute;
    private PullToRefreshListView mDriveSegmentList;
    private DriveSegmentListAdapter mDriveSegmentListAdapter;
    private String mDriveTargetName;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private ImageView mTitleMap; //使用本机导航
    private  double  chufajingdu;
    private  double  chufaweidu;
    private  double  daodajingdu;
    private  double  daodaweidu;

    private  ListView  maplist;//本机安装导航列表
    List<AppInfo> appInfoListlist=new ArrayList<AppInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveroute_detail);
        mTitleMap = (ImageView) findViewById(R.id.title_right_img);
        mTitleMap.setVisibility(View.VISIBLE);

        getIntentData();

        setUpInteractiveControls();
    }
    private void setUpInteractiveControls() {
        mTitleDesTv = (TextView) findViewById(R.id.title_des_text);
        mTitleDesTv.setText(Const.Drive_ROUTE_DETAIL);
        mTitleDriveRoute = (TextView) findViewById(R.id.title_bus_route);
        mDesDriveRoute = (TextView) findViewById(R.id.des_bus_route);
        mTitleMap = (ImageView) findViewById(R.id.title_right_img); //使用本机导航
        mTitleMap.setVisibility(View.VISIBLE);
        String dur = SchemeUtil.getFriendlyTime((int) mDrivePath.getDuration());
        String dis = SchemeUtil.getFriendlyLength((int) mDrivePath
                .getDistance());
        mTitleDriveRoute.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
        SpannableStringBuilder spanabledes = SchemeUtil.getRouteDes(
                this.getApplication(), taxiCost);
        mDesDriveRoute.setText(spanabledes);
        configureListView();
    }

    private void configureListView() {
        mDriveSegmentList = (PullToRefreshListView) findViewById(R.id.bus_segment_list);
        mDriveSegmentList.setMode(Mode.DISABLED);
        ListView actualListView = mDriveSegmentList.getRefreshableView();
        mDriveSegmentListAdapter = new DriveSegmentListAdapter(
                this.getApplicationContext(), mDrivePath.getSteps(),
                mDriveTargetName);
        actualListView.setAdapter(mDriveSegmentListAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mDrivePath = intent.getParcelableExtra(BundleFlag.DRIVE_PATH);
        mDriveRouteResult = intent.getParcelableExtra(BundleFlag.DRIVE_RESULT);
        mDriveTargetName = intent.getStringExtra(BundleFlag.DRIVE_TARGET_NAME);
        chufajingdu=intent.getDoubleExtra("chufajingdu",0d);
        chufaweidu=intent.getDoubleExtra("chufaweidu",0d);
        daodajingdu=intent.getDoubleExtra("daodajingdu",0d);
        daodaweidu=intent.getDoubleExtra("daodaweidu",0d);
    }

    public void onBackClick(View view) {
        this.finish();
    }


    /**
     * 右上角的地图按钮点击事件 使用本机导航
     *
     * @param view
     */
    public void onTitleRightClick(View view) {
        mapios7Dialog();
    }

    /**
     * 判断是否安装地图,若安装则调用地图,调用顺序为读取安装包中的顺序
     *
     * @return
     */
    // 没装地图
    public static final int MAP_UNINSTALLED = -1;
    // 谷歌地图
    public static final int MAP_GOOGLE = 0;
    // 谷歌(BRUT)地图
    public static final int MAP_BRUT = 1;
    // 百度地图
    public static final int MAP_BAIDU = 2;
    // 图吧地图
    public static final int MAP_BAR = 3;
    // 高德地图
    public static final int MAP_AUTONAVI = 4;
    // 描述地图所属包的信息集合
    private static List<String> mapPackage = new ArrayList<String>(6);
    static {
       mapPackage.add("uninstalled");
       mapPackage.add("com.google.android.apps.maps");
       mapPackage.add("brut.googlemaps");
       mapPackage.add("com.baidu.BaiduMap");
       mapPackage.add("com.mapbar.android.mapbarmap");
       mapPackage.add("com.autonavi.minimap");
    }

    /*
        获得所有启动Activity的信息，本机已经安装的导航
     */
  public    List<AppInfo>  queryAppInfo2() {
      List<AppInfo>  listappinfo=new ArrayList<AppInfo>();
      //获取packagemanager
      PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
      Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
      mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
      // 通过查询，获得所有ResolveInfo对象.
      List<ResolveInfo> resolveInfos = pm
              .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
      // 调用系统排序 ， 根据name排序
      // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
      Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
      for (ResolveInfo reInfo : resolveInfos) {
          AppInfo   appInfo=new AppInfo();
          String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
          String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
          String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
          Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
         if(mapPackage.get(1).equals(pkgName))
         {
             appInfo.setAppIcon(icon);
             appInfo.setAppLabel(appLabel);
             appInfo.setPkgName(pkgName);
             appInfo.setMapcode(MAP_GOOGLE);
             listappinfo.add(appInfo);
         }
          if(mapPackage.get(2).equals(pkgName))
          {
              appInfo.setAppIcon(icon);
              appInfo.setAppLabel(appLabel);
              appInfo.setPkgName(pkgName);
              appInfo.setMapcode(MAP_BRUT);
              listappinfo.add(appInfo);

          }
          if(mapPackage.get(3).equals(pkgName))
          {
              appInfo.setAppIcon(icon);
              appInfo.setAppLabel(appLabel);
              appInfo.setPkgName(pkgName);
              appInfo.setMapcode(MAP_BAIDU);
              listappinfo.add(appInfo);
          }
          if(mapPackage.get(4).equals(pkgName))
          {
              appInfo.setAppIcon(icon);
              appInfo.setAppLabel(appLabel);
              appInfo.setPkgName(pkgName);
              appInfo.setMapcode(MAP_BAR);
              listappinfo.add(appInfo);
          }
          if(mapPackage.get(5).equals(pkgName))
          {
              appInfo.setAppIcon(icon);
              appInfo.setAppLabel(appLabel);
              appInfo.setPkgName(pkgName);
              appInfo.setMapcode(MAP_AUTONAVI);
              listappinfo.add(appInfo);
          }

      }
          return   listappinfo;
  }


    /*
          获得所有启动Activity的信息，本机已经安装的导航
       */
    public    List<AppInfo>  queryAppInfo() {
        List<AppInfo>  listappinfo=new ArrayList<AppInfo>();
        //获取packagemanager
        final PackageManager packageManager = this.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if(packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                 String pkgName =packageInfos.get(i).packageName; // 获得应用程序的包名
                 AppInfo   appInfo=new AppInfo();
                if(mapPackage.get(1).equals(pkgName))
                {

                    appInfo.setAppLabel("谷歌地图");
                    appInfo.setPkgName(pkgName);
                    appInfo.setMapcode(MAP_GOOGLE);
                    listappinfo.add(appInfo);
                }
                if(mapPackage.get(2).equals(pkgName))
                {
                    appInfo.setAppLabel("谷歌(BRUT)地图");
                    appInfo.setPkgName(pkgName);
                    appInfo.setMapcode(MAP_BRUT);
                    listappinfo.add(appInfo);

                }
                if(mapPackage.get(3).equals(pkgName))
                {
                    appInfo.setAppLabel("百度地图");
                    appInfo.setPkgName(pkgName);
                    appInfo.setMapcode(MAP_BAIDU);
                    listappinfo.add(appInfo);
                }
                if(mapPackage.get(4).equals(pkgName))
                {
                    appInfo.setAppLabel("图吧地图");
                    appInfo.setPkgName(pkgName);
                    appInfo.setMapcode(MAP_BAR);
                    listappinfo.add(appInfo);
                }
                if(mapPackage.get(5).equals(pkgName))
                {
                    appInfo.setAppLabel("高德地图");
                    appInfo.setPkgName(pkgName);
                    appInfo.setMapcode(MAP_AUTONAVI);
                    listappinfo.add(appInfo);
                }

            }
        }
        return   listappinfo;
    }



/*
调用百度 ，图吧，高德地图 ,国内地图
 */
    private void  getCNMapIntent(String pck )
    {
                if (chufajingdu !=0 && chufaweidu!=0) {
                float chufajingdu1= (float)chufajingdu;
                float chufaweidu1=(float)chufaweidu;
                float daodajingdu1= (float)daodajingdu;
                float daodaweidu1=(float)daodaweidu;
            // 先默认一个初始的坐标Uri,供百度地图使用
            // geo:xx.xxxxxx,yy.yyyyyy,你的位置?z=15
            StringBuffer sb = new StringBuffer("geo:");
            sb.append(daodajingdu);
            sb.append("," + daodaweidu);
            sb.append(" ,你的位置?z=17");
            Uri uri = Uri.parse(sb.toString());
            Intent i;
            i = new Intent(Intent.ACTION_VIEW, uri);
            // 设置包名,使用确切的包来表示调用指定的地图,否则系统会给出提示选择已经安装的地图中的一个
            i.setPackage(pck);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);

        } else {
            Toast toast2=Toast.makeText(getApplicationContext(), "由于无法获取到您的位置，所以暂时无法提供导航", Toast.LENGTH_SHORT);
            toast2.show();
        }

    }

/*
调用google 地图
 */
    private void  getGMapIntent(String pck )
    {
        if (chufajingdu !=0 && chufaweidu!=0) {
            float chufajingdu1= (float)chufajingdu;
            float chufaweidu1=(float)chufaweidu;
            float daodajingdu1= (float)daodajingdu;
            float daodaweidu1=(float)daodaweidu;
            // 先默认一个初始的坐标Uri,供百度地图使用
            // geo:xx.xxxxxx,yy.yyyyyy,你的位置?z=15
            StringBuffer sb = new StringBuffer(
                    "http://ditu.google.cn/maps?hl=zh&mrt=loc&q=");
            sb.append(daodajingdu);
            sb.append("," + daodaweidu);
            Uri uri = Uri.parse(sb.toString());
            Intent i;
            i = new Intent(Intent.ACTION_VIEW, uri);
            // 设置包名,使用确切的包来表示调用指定的地图,否则系统会给出提示选择已经安装的地图中的一个
            i.setPackage(pck);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);

        } else {
            Toast toast2=Toast.makeText(getApplicationContext(), "由于无法获取到您的位置，所以暂时无法提供导航", Toast.LENGTH_SHORT);
            toast2.show();
        }

    }


    private  void mapios7Dialog() {
        appInfoListlist = queryAppInfo();
        if (appInfoListlist.size() == 0) {
                 new AlertDialog(DriveRouteDetailActivity.this).builder()
                    .setMsg("您的手机未找到可以使用的导航，请下载后再使用(建议下载百度地图或高德地图)")
                    .setNegativeButton("确定",  new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        } else {
            ActionSheetDialog  mapdialog=new ActionSheetDialog(DriveRouteDetailActivity.this);
            mapdialog.builder();
            mapdialog.setTitle("请选择导航软件");
            mapdialog.setCancelable(false);
            mapdialog.setCanceledOnTouchOutside(false);
            for(int i=0;i<appInfoListlist.size();i++)
            {
              final AppInfo appinfo=appInfoListlist.get(i);
                mapdialog.addSheetItem(appinfo.getAppLabel(), ActionSheetDialog.SheetItemColor.Blue,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            if(appinfo.getMapcode()==0)
                            {  //调用goole地图
                                getGMapIntent(appinfo.getPkgName());
                            }
                            else
                            {//调用国内地图：百度，图吧，高德
                                getCNMapIntent(appinfo.getPkgName());
                            }
                        }
                    });

            }
            mapdialog.show();
       }
   }

}


