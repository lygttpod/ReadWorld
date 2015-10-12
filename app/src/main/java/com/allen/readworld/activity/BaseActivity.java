package com.allen.readworld.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.allen.readworld.utils.NetWorkUtils;

/**
 * Created by Allen on 15/9/27.
 */
public class BaseActivity extends FragmentActivity {
    NetWorkUtils netWorkUtils;
    private Context context;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netWorkUtils = new NetWorkUtils();
        context = this;
        isConnected();
    }

    private void isConnected(){
        if (!netWorkUtils.isConnnected(BaseActivity.this)){
            Toast.makeText(context,"检查网络是否可用...",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {

            if (flag) {

                finish();
            } else {
                Toast.makeText(this, "连按两次退出程序", Toast.LENGTH_SHORT).show();
                flag = true;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        flag = false;
                        super.run();
                    }
                }.start();
            }

        }
        return true;    }
}
