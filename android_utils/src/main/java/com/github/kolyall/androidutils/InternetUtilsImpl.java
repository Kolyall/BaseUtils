/*
* Copyright (C) 2014 The Android Open Source Project
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

package com.github.kolyall.androidutils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Generic reusable network methods.
 */
public class InternetUtilsImpl implements InternetUtils {
    Context context;

    public InternetUtilsImpl(Context context) {
        this.context = context;
    }

    public static boolean isNeedNeverSleepPolicy(Context context) throws Exception {
        Context applicationContext = context.getApplicationContext();
        WifiManager wm = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        if (wm != null && !wm.isWifiEnabled()) {
            return false;
        }
        ContentResolver cr = applicationContext.getContentResolver();
        Settings.System.putInt(cr, Settings.Global.WIFI_SLEEP_POLICY, Settings.Global.WIFI_SLEEP_POLICY_NEVER);

        if (Settings.System.getInt(cr, Settings.Global.WIFI_SLEEP_POLICY) != Settings.Global.WIFI_SLEEP_POLICY_NEVER) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isInternetConnected() {
        return InternetUtilsImpl.isInternetConnected(context);
    }

    /**
     * @param context to use to check for network connectivity.
     * @return true if connected, false otherwise.
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean onWiFi(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == TYPE_WIFI;
    }

}
