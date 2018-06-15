package us.zhicheng.administrator.mymap.gaode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import us.zhicheng.administrator.mymap.R;

public class MainActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener{

    private MapView mMapView;
    AMap aMap;
    private int a = 0;
    private int b = 0;
    private Button btn01;
    private Button btn02;
    private Button btn03;
    private Marker marker;
    private TextView tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn01 = (Button) findViewById(R.id.btn01);
        btn02 = (Button) findViewById(R.id.btn02);
        btn03 = (Button) findViewById(R.id.btn03);
        tv_location = (TextView) findViewById(R.id.tv_location);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        test();
        initLogoMarker();

    }
    //动态申请权限的测试方法
    public void test() {
        // 要申请的权限 数组 可以同时申请多个权限
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE};

        if (Build.VERSION.SDK_INT >= 23) {
            //如果超过6.0才需要动态权限，否则不需要动态权限
            //如果同时申请多个权限，可以for循环遍历
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //写入你需要权限才能使用的方法
                initMap();
            } else {
                //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            //写入你需要权限才能使用的方法
            initMap();
        }
    }
    private void initLogoMarker() {
        //北工商坐标有了
        LatLng bubaLatLng = new LatLng(40.239437,116.133239);
        //为北工商这个位置提供自定义图标的marker
        BitmapDescriptor buba_logo = BitmapDescriptorFactory.fromResource(R.mipmap.buba_logo);
        marker = aMap.addMarker(new MarkerOptions().position(bubaLatLng).icon(buba_logo));

        //为这个Marker提供一个(自定义的)infowindow
        //创建一个自定义的infowindow
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = View.inflate(MainActivity.this, R.layout.bubu_infowindow, null);
                ImageView infowindow_icon = v.findViewById(R.id.infowindow_icon);
                TextView infowindow_title = v.findViewById(R.id.infowindow_title);
                TextView infowindow_content = v.findViewById(R.id.infowindow_content);
                infowindow_icon.setBackgroundResource(R.mipmap.buba_logo);
                infowindow_content.setText("我是详细信息");
                infowindow_title.setText("我是标题");
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        //给infowindow添加点击事件
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                }
            }
        });
        //给map添加点击事件
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (MainActivity.this.marker.isInfoWindowShown()) {
                    MainActivity.this.marker.hideInfoWindow();
                }
            }
        });
        //给marker添加点击事件  如果处理的话 就返回true
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();
                }
                return true;
            }
        });

    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //显示比例,数值越大,越精确
        //aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //自定义定位蓝点图标
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        myLocationStyle.interval(20000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Location location = aMap.getMyLocation();
                if (location==null){
                    tv_location.setText("定位中...");
                }else{
                    //tv_location.setText("定位中");
                    tv_location.setText(location.getExtras().get("City")+"");
                }
            }
        },1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //回调，判断用户到底点击是还是否。
        //如果同时申请多个权限，可以for循环遍历
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //写入你需要权限才能使用的方法
            initMap();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要获得XXX权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.btn01:
                initMap();
                aMap.setTrafficEnabled(false);
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                btn02.setText("开启铁路图");
                btn03.setText("开启夜间模式");
                break;
            case R.id.btn02:
                if (a%2==0){
                    btn02.setText("关闭铁路图");
                    aMap.setTrafficEnabled(true);
                }else{
                    btn02.setText("开启铁路图");
                    aMap.setTrafficEnabled(false);
                }
                a++;
                break;
            case R.id.btn03:
                if (b%2==0){
                    btn03.setText("关闭夜间模式");
                    aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                }else{
                    btn03.setText("开启夜间模式");
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }
                b++;
                break;
        }

    }

    //通过实现AMap的onMyLocationChangeListener回调方法,获取定位蓝点的经纬度,通过经纬度再获取地址信息
    //当位置发生变化时候触发该方法
    @Override
    public void onMyLocationChange(Location location) {
        //Toast.makeText(this,"经度:"+location.getLatitude()+"\n"+"纬度:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
        tv_location.setText(location.getExtras().get("City")+"");
    }
}
