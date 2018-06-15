package us.zhicheng.administrator.mymap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import us.zhicheng.administrator.mymap.baidu.HomeActivity;
import us.zhicheng.administrator.mymap.gaode.CateGrayActivity;

public class BigstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigst);
    }

    public void gaode(View view) {
        startActivity(new Intent(this, CateGrayActivity.class));
    }

    public void baidu(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void tectent(View view) {

    }
}
