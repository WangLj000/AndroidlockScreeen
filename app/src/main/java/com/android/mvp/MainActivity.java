package com.android.mvp;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    DeviceMangerBroadCast deviceMangerBroadCast;
    private DevicePolicyManager devicePolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        registerReceiver();
        devicePolicyManager =(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        //若未注册设备管理器，跳转到配置界面
        //若已注册设备管理器，显示取消设备管理器页面
        if(devicePolicyManager.isAdminActive(new ComponentName(this, DeviceMangerBroadCast.class))){
            devicePolicyManager.lockNow();
            finish();
        }else{
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, DeviceMangerBroadCast.class));
            startActivityForResult(intent, 1);
        }
    }


    public void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.app.action.DEVICE_ADMIN_ENABLED");
        intentFilter.addAction("android.app.action.DEVICE_ADMIN_DISABLED");
        registerReceiver(deviceMangerBroadCast,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(deviceMangerBroadCast);
    }
}
