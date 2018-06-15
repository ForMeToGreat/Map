package us.zhicheng.administrator.erweima;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.util.HashMap;

import us.zhicheng.administrator.erweima.utils.ZxingUtils;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView tv_test;
    private ImageView pic_test;
    //private Button btn_test;
    //private Button btn_test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit_input);
        tv_test = (TextView) findViewById(R.id.tv_test);
        pic_test = (ImageView) findViewById(R.id.pic_test);
        pic_test.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bitmap obmp = ((BitmapDrawable) (pic_test).getDrawable()).getBitmap();
                showSelectAlert(obmp);
                return true;
            }
        });
        //btn_test = (Button) findViewById(R.id.btn_test);
        //btn_test2 = (Button) findViewById(R.id.btn_test2);
    }

    private void showSelectAlert(final Bitmap bitmap) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择");
        String str[] = {"保存图片", "扫描图中的二维码"};
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterfacem, int i) {
                switch (i) {
                    case 0: {
                        saveImageToGallery(bitmap);
                    }
                    break;
                    case 1: {
                        try {
                            startEncodeImg(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterfacem, int i) {

            }
        });
        builder.show();
    }

    private void startEncodeImg(Bitmap bitmap) throws Exception {

        try{
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));

            QRCodeMultiReader reader = new QRCodeMultiReader();
            //QRCodeReader reader = new QRCodeReader();
            HashMap<DecodeHintType, String> map = new HashMap<>();
            map.put(DecodeHintType.CHARACTER_SET,"UTF-8");

            Result decode = reader.decode(bitmap1,map);

            Log.i("tag", "startEncodeImg: ===22======");
            tv_test.setText(decode.getText());
            Log.i("tag", "startEncodeImg: ========="+decode.getText());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //保存图片
    private void saveImageToGallery(Bitmap obmp) {

        Toast.makeText(this, "图片已保存至手机!", Toast.LENGTH_SHORT).show();
    }

    //生成文字的二维码
    public void encodeBitmap(View view) throws WriterException {
        String str = editText.getText().toString();
        if (str.equals("")) {
            Toast.makeText(this, "您输入的内容为空,请输入有效内容", Toast.LENGTH_SHORT).show();
        } else {
            //Bitmap bb = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            Bitmap bitmap = ZxingUtils.Create2DCode(str,null);
            if (bitmap != null) {
                pic_test.setImageBitmap(bitmap);
            }
        }
    }

    public void encodeBitmap1(View view) throws WriterException {
        String str = editText.getText().toString();
        if (str.equals("")) {
            Toast.makeText(this, "您输入的内容为空,请输入有效内容", Toast.LENGTH_SHORT).show();
        } else {
            Bitmap bb = BitmapFactory.decodeResource(getResources(),R.mipmap.aab);
            Bitmap bitmap = ZxingUtils.Create2DCode(str,bb);
            if (bitmap != null) {
                pic_test.setImageBitmap(bitmap);
            }
        }
    }

    //扫描二维码
    public void scanCode(View view) {
        new IntentIntegrator(this)
                .setOrientationLocked(true)//是否横竖屏切换 false不加锁 true加锁
                .setCaptureActivity(ScanActivity.class) // 设置自定义的activity是ScanActivity
                .initiateScan(); // 初始化扫描
    }

    // 通过 onActivityResult的方法获取扫描回来的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                //String LastResult = recode(ScanResult);
                tv_test.setText(ScanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




}
