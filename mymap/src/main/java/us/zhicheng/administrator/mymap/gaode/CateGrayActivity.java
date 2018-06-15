package us.zhicheng.administrator.mymap.gaode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import us.zhicheng.administrator.mymap.R;

public class CateGrayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_gray);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.map:
                startActivity(new Intent(CateGrayActivity.this,MainActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(CateGrayActivity.this,POISerachActivity.class));
                break;
        }
    }
}
