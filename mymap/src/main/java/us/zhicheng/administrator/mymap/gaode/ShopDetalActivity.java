package us.zhicheng.administrator.mymap.gaode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;

import us.zhicheng.administrator.mymap.R;

public class ShopDetalActivity extends AppCompatActivity {

    private ImageView shop_icon;
    private TextView tv_location;
    private LatLng lation;
    private String title;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detal);
        initView();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent!=null){
            title = intent.getStringExtra("title");
            des = intent.getStringExtra("des");
            lation = intent.getParcelableExtra("lation");
            tv_location.setText(title);
            tv_location.append(des);
        }
    }

    private void initView() {
        shop_icon = (ImageView) findViewById(R.id.shop_icon);
        tv_location = (TextView) findViewById(R.id.tv_location);
    }

    public void click(View view) {
        //路线规划
        Intent intent = new Intent(this,LineActivity.class);
        intent.putExtra("lation", lation);
        intent.putExtra("title", title);
        intent.putExtra("des", des);
        startActivity(intent);
    }
}
