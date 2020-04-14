package com.fyh.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AIDLService extends Service {
    public AIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    IServiceInterface.Stub stub = new IServiceInterface.Stub() {
        @Override
        public String getData() throws RemoteException {
            String s = "{'data':'AIDLServiceTest'}";
            return s;
        }
    };
}
