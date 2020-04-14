package com.fyh.aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBind;
    private Button btnUnbind;
    private Button btnSend;
    private TextView tvMessage;

    private IServiceInterface iServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnBind = findViewById(R.id.btnBind);
        btnBind.setOnClickListener(this);
        btnUnbind = findViewById(R.id.btnUnbind);
        btnUnbind.setOnClickListener(this);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        tvMessage = findViewById(R.id.tvMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBind:
                bindService(MainActivity.this);
                tvMessage.append("bindService:\n");
                break;
            case R.id.btnUnbind:
                unbindService(MainActivity.this);
                tvMessage.append("unBindService:\n");
                break;
            case R.id.btnSend:
                String result = requestData();
                if (!TextUtils.isEmpty(result)) {
                    tvMessage.append("");
                    tvMessage.append("result:\n");
                    tvMessage.append(result);
                    tvMessage.append("\n");
                }
                break;

                default:
                    break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iServiceInterface = IServiceInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iServiceInterface = null;
        }
    };

    public void bindService(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.fyh.aidl");
        intent.setPackage("com.fyh.aidl.service");
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (null == iServiceInterface) {
            return;
        }
        context.unbindService(connection);
        iServiceInterface = null;
    }

    public String requestData() {
        if (null == iServiceInterface) {
            return null;
        }
        try {
            String data = iServiceInterface.getData();
            return data;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}
