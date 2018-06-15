package us.zhicheng.administrator.mymap.gaode;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import us.zhicheng.administrator.mymap.gaode.overlay.DrivingRouteOverlay;
import us.zhicheng.administrator.mymap.R;

public class LineActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener,INaviInfoCallback{

    private MapView line_map;
    private AMap amap;
    private String title;
    private String des;
    private LatLng lation;
    //定位的自己的位置
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        line_map = (MapView) findViewById(R.id.line_map);
        line_map.onCreate(savedInstanceState);
        amap = line_map.getMap();
        //initLocation();
        initIntent();
        initSearchDriveRoute();
    }

    //开始获取驾驶路线
    private void initSearchDriveRoute() {
        //第 1 步，初始化 RouteSearch 对象
        RouteSearch routeSearch = new RouteSearch(this);
        //第 2 步，设置数据回调监听器
        routeSearch.setRouteSearchListener(this);
        //第 3 步，设置搜索参数
        //参数1 起始位置  参数2 结束为止
        myLocation = MyApplication.getMyLocation();
        if (myLocation != null) {
            //起始位置点
            LatLonPoint startLatLonPoint = new LatLonPoint(myLocation.getLatitude(), myLocation.getLongitude());
            //结束位置点
            LatLonPoint endLatLonPoint = new LatLonPoint(lation.latitude, lation.longitude);

            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startLatLonPoint, endLatLonPoint);
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");

            //第 4 步，发送请求
            routeSearch.calculateDriveRouteAsyn(query);

        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent!=null){
            title = intent.getStringExtra("title");
            des = intent.getStringExtra("des");
            lation = intent.getParcelableExtra("lation");
        }
    }

    //定位
    private void initLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        amap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        //实现定位功能
        amap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //启动定位1秒钟之后去拿到定位
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myLocation = amap.getMyLocation();
                if (myLocation ==null|| myLocation.getExtras().get("City")==null){
                    Toast.makeText( LineActivity.this, "定位失败。。。", Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        line_map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        line_map.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        line_map.onPause();
    }

    //公交
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    //驾车
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int i) {

        amap.clear();// 清理地图上的所有覆盖物
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    final DrivePath drivePath = result.getPaths().get(0);
                    if(drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            LineActivity.this, amap, drivePath,
                            result.getStartPos(),
                            result.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                }

            }
        }

    }

    //步行
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    //骑车
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    public void click(View view) {
        AMapLocation location = MyApplication.getMyLocation();
        Poi start = new Poi(location.getAoiName(), new LatLng(location.getLatitude(),location.getLongitude()), "");
        /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
        Poi end = new Poi(title,lation,"");
        AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER),this);

    }
    @Override
    public void onInitNaviFailure() {
        //导航失败时
    }

    @Override
    public void onGetNavigationText(String s) {
        //拿到导航文字时

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        //当定位变更的时候
    }

    @Override
    public void onArriveDestination(boolean b) {
        //当到达目的地时
    }

    @Override
    public void onStartNavi(int i) {
        //当开始导航时
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //当计算路线成功时
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        //当计算路线失败时
    }

    @Override
    public void onStopSpeaking() {
        //当停止讲话时
    }

    @Override
    public void onReCalculateRoute(int i) {
        //当再次计算路线时
    }

    @Override
    public void onExitPage(int i) {
        //在退出页面时
    }

    @Override
    public void onStrategyChanged(int i) {
        //当导航策略变化时
    }

    @Override
    public View getCustomNaviBottomView() {
        //获得导航底部view
        return null;
    }

    @Override
    public View getCustomNaviView() {
        //获得自定义的的导航view
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {
        //当到达目的地时
    }
}
