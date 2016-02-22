package bmobdemo.easynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.leo.gesturelibray.util.StringUtils;

/**
 * Created by Administrator on 2016/2/21.
 */
public class LogninActivity extends Activity {
    private static String PASS_KEY = "PASS_KEY_MAP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        String password = ConfigUtil.getInstance(this).getString(PASS_KEY);
        if (StringUtils.isEmpty(password)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (StringUtils.isNotEmpty(password)) {
            Intent intent = new Intent(this, LockActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
