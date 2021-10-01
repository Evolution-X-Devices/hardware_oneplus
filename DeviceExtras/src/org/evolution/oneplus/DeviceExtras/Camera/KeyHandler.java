/*
 * Copyright (C) 2015-2016 The CyanogenMod Project
 * Copyright (C) 2018 The LineageOS Project
 * Copyright (C) 2022 The Evolution X Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.evolution.oneplus.DeviceExtras.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.FileObserver;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.os.DeviceKeyHandler;

import org.evolution.oneplus.DeviceExtras.FileUtils;

import vendor.oneplus.hardware.camera.V1_0.IOnePlusCameraProvider;

public class KeyHandler implements DeviceKeyHandler {
    private static final String TAG = KeyHandler.class.getSimpleName();
    private static final boolean DEBUG = false;

    public static final String CLIENT_PACKAGE_NAME = "com.oneplus.camera";
    public static final String CLIENT_PACKAGE_PATH = "/data/misc/evolution/client_package_name";

    private final Context mContext;
    private ClientPackageNameObserver mClientObserver;
    private IOnePlusCameraProvider mProvider;

    private BroadcastReceiver mSystemStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                onDisplayOn();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                onDisplayOff();
            }
        }
    };

    public KeyHandler(Context context) {
        mContext = context;

        if (PackageUtils.isAvailableApp(CLIENT_PACKAGE_NAME, mContext)) {
            IntentFilter systemStateFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            systemStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
            mContext.registerReceiver(mSystemStateReceiver, systemStateFilter);
            mClientObserver = new ClientPackageNameObserver(CLIENT_PACKAGE_PATH);
            mClientObserver.startWatching();
        }
    }

    public KeyEvent handleKeyEvent(KeyEvent event) {
            return event;
        }

    private void onDisplayOn() {
        if (mClientObserver == null) {
            mClientObserver = new ClientPackageNameObserver(CLIENT_PACKAGE_PATH);
            mClientObserver.startWatching();
        }
    }

    private void onDisplayOff() {
        if (mClientObserver != null) {
            mClientObserver.stopWatching();
            mClientObserver = null;
        }
    }

    private class ClientPackageNameObserver extends FileObserver {

        public ClientPackageNameObserver(String file) {
            super(CLIENT_PACKAGE_PATH, MODIFY);
        }

        @Override
        public void onEvent(int event, String file) {
            String pkgName = FileUtils.getFileValue(CLIENT_PACKAGE_PATH, "0");
            if (event == FileObserver.MODIFY) {
                try {
                    Log.d(TAG, "client_package " + file + " and " + pkgName);
                    mProvider = IOnePlusCameraProvider.getService();
                    mProvider.setPackageName(pkgName);
                } catch (RemoteException e) {
                    Log.e(TAG, "setPackageName error", e);
                }
            }
        }
    }
}
