package mofind.manager;

import java.lang.reflect.Method;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class NetManager extends ContextWrapper {
    private WifiManager mWm = null;

    private NetManager(Context base) {
        super(base);
        if (mWm == null) {
            mWm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        }
    }

    private static NetManager g_Instance = null;

    public static synchronized NetManager getInstance(Context c) {
        if (g_Instance == null) {
            g_Instance = new NetManager(c);
        }
        return g_Instance;
    }

    public int netstate() {
        int i = 0;
        if (isMobileConnected() && !isWifiConnected()) {
            i = 1;
        } else if (isWifiConnected()) {
            i = 2;
        } else if (!isMobileConnected() && !isWifiConnected()) {
            i = 0;
        }
        return i;
    }

    private boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        State wifi = State.DISCONNECTED;
        if (ni != null) {
            wifi = ni.getState();
        }
        return (wifi == State.CONNECTED);
    }

    private boolean isMobileConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        State wifi = State.DISCONNECTED;
        if (ni != null) {
            wifi = ni.getState();
        }
        return (wifi == State.CONNECTED);
    }

    public void setWifi(boolean isEnable) {
        if (isEnable == true) {
            if (!mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(true);
                mWm = null;
            }
        } else {
            if (mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(false);
                mWm = null;
            }
        }
    }

    public void acquireWifiLock() {
    }

    public void releaseWifiLock() {
    }

    public void createWifiLock() {
    }

    public boolean IsCanUseSdCard() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isCanUseSim() {
        try {
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开启系统设置里的移动网络
     */
    public final void setMobileNetEnable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Object[] arg = null;
        try {
            boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled",
                    arg);
            if (!isMobileDataEnable) {
                invokeBooleanArgMethod("setMobileDataEnabled", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean invokeMethod(String methodName, Object[] arg)
            throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
        return isOpen;
    }

    public Object invokeBooleanArgMethod(String methodName, boolean value)
            throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(mConnectivityManager, value);
    }

    public void WifiNeverDormancy(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        int value = Settings.System.getInt(resolver,
                Settings.System.WIFI_SLEEP_POLICY,
                Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
            Settings.System.putInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
                    Settings.System.WIFI_SLEEP_POLICY_NEVER);
        }
        System.out.println("wifi value:" + value);
    }
    /*
     * 上面这个函数，会自动修改我们WIFI设置中的高级选项，将其设置为一直保持连接。不用使用其他控件就可以解决。
     * 
     * 需要注意的是此函数在调用时必须现在AndroidManifest.xml中声明权限
     * 
     * <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
     */
}
