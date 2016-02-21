package bmobdemo.easynotes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.gesturelibray.CustomLockView;
import com.leo.gesturelibray.util.*;
import com.leo.gesturelibray.util.StringUtils;

/**
 * Created by Administrator on 2016/2/21.
 */
public class LockActivity extends AppCompatActivity {
    private TextView lock_text;
    private CustomLockView lv_lock;
    private static String PASS_KEY = "PASS_KEY_MAP";
    private boolean isUnLock = false;
    private String mPassword = null;
    private Toolbar sl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_layout);
        initView();
        initData();
        lv_lock.setShow(true);//不显示绘制方向
        lv_lock.setErrorTimes(3);//允许最大输入次数
        lv_lock.setPasswordMinLength(4);//密码最少位数
        setLockMode();
        lv_lock.setOnCompleteListener(onCompleteListener);
    }

    private void initData() {
        sl_toolbar.setTitle("密码锁");
        sl_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(sl_toolbar);
        String password = ConfigUtil.getInstance(this).getString(PASS_KEY);
        if (StringUtils.isEmpty(password)){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLockMode() {
        String password = ConfigUtil.getInstance(this).getString(PASS_KEY);
        if (StringUtils.isNotEmpty(password)) {
            lv_lock.setStatus(1);//解锁
            isUnLock = true;
            lv_lock.setOldPassword(password);
            lock_text.setText("请输入密码");
        } else {
            isUnLock = false;
            lv_lock.setStatus(0);//设置密码
            lock_text.setText("请绘制密码手势");
        }
    }

    private void initView() {
        lock_text= (TextView) findViewById(R.id.lock_text);
        lv_lock= (CustomLockView) findViewById(R.id.lv_lock);
        sl_toolbar = (Toolbar) findViewById(R.id.tl_custom);
    }
    CustomLockView.OnCompleteListener onCompleteListener = new CustomLockView.OnCompleteListener() {
        @Override
        public void onComplete(String password, int[] indexs) {
            if (isUnLock) {
                lock_text.setText("密码正确");
                Intent intent =new Intent(LockActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(LockActivity.this, "欢迎来到极简便签！", Toast.LENGTH_SHORT).show();
            } else {
                if (com.leo.gesturelibray.util.StringUtils.isEmpty(mPassword)) {
                    mPassword = password;
                    lock_text.setText("请再次绘制密码手势");
                    Toast.makeText(LockActivity.this, "请再次绘制密码手势", Toast.LENGTH_SHORT).show();
                }else if (com.leo.gesturelibray.util.StringUtils.isEquals(mPassword, password)){
                    lock_text.setText("密码手势绘制成功");
                    Toast.makeText(LockActivity.this, "密码手势绘制成功", Toast.LENGTH_SHORT).show();
                    ConfigUtil.getInstance(LockActivity.this).putString(PASS_KEY, password);
                    finish();
                }else if(!com.leo.gesturelibray.util.StringUtils.isEquals(mPassword, password)){
                    lock_text.setText("两次绘制的密码手势不一致");
                    Toast.makeText(LockActivity.this, "两次绘制的密码手势不一致", Toast.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        public void onError(int errorTimes) {
            if (isUnLock){
            Toast.makeText(LockActivity.this, "密码手势错误，还可以绘制" + errorTimes + "次", Toast.LENGTH_SHORT).show();
        }else {
                lock_text.setText("两次绘制的密码手势不一致");
                Toast.makeText(LockActivity.this, "两次绘制的密码手势不一致", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPasswordIsShort(int passwordMinLength) {
            Toast.makeText(LockActivity.this, "密码手势不能少于" + passwordMinLength + "个点", Toast.LENGTH_SHORT).show();
        }};

}
