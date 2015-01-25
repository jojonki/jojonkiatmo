package net.jonki.app.jojonkiatmo;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.netatmo.weatherstation.api.NetatmoHttpClient;
import com.netatmo.weatherstation.api.NetatmoUtils;


/**
 * Created by jonki on 15/01/21.
 */
public class JojonkiNetAtmoHttpClient  extends NetatmoHttpClient {
    final String CLIENT_ID = "54bb4e244a5a8862f70bcdcc";
    final String CLIENT_SECRET = "w8asWm7PHtvR7FSfI05kBBjsxU9uoZHWR6g";

    SharedPreferences mSharedPrefs;

    public JojonkiNetAtmoHttpClient(Context context) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected String getClientId() {
        return CLIENT_ID;
    }

    @Override
    protected String getClientSecret() {
        return CLIENT_SECRET;
    }

    @Override
    public void storeTokens(String refreshToken, String accessToken, long expiresAt) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(NetatmoUtils.KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(NetatmoUtils.KEY_ACCESS_TOKEN, accessToken);
        editor.putLong(NetatmoUtils.KEY_EXPIRES_AT, expiresAt);
        editor.apply();
    }

    @Override
    public void clearTokens() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public String getAccessToken() {
        return mSharedPrefs.getString(NetatmoUtils.KEY_ACCESS_TOKEN, null);
    }

    @Override
    public String getRefreshToken() {
        return mSharedPrefs.getString(NetatmoUtils.KEY_REFRESH_TOKEN, null);
    }

    @Override
    public long getExpiresAt() {
        return mSharedPrefs.getLong(NetatmoUtils.KEY_EXPIRES_AT, 0);
    }
}
