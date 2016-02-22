package bmobdemo.easynotes;

import android.app.Application;

/**
 * Created by Administrator on 2016/2/16.
 */
public class AppContext extends Application {
    private int themeColor = R.color.green;
    private static AppContext appContext;

    public static AppContext getInstance() {
        if (appContext == null) {
            appContext = new AppContext();
        }
        return appContext;
    }


    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
