package net.jonki.app.jojonkiatmo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import com.netatmo.weatherstation.api.NetatmoResponseHandler;
import com.netatmo.weatherstation.api.model.Station;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jonki on 15/01/20.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static String TAG = "JONKITAG: ";

    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

    final int REQUEST_CODE = 0;

    JojonkiNetAtmoHttpClient mHttpClient;
    List<Station> mDevices;
    Handler handler = new Handler();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "AppWidgetPRovier onUpdate dayo");

        mHttpClient = new JojonkiNetAtmoHttpClient(context);
        if (mHttpClient.getAccessToken() != null) {
            Log.v(TAG, "access token is valid.");

            // NetatmoResponseHandler returns a parsed response (by overriding onGetDevicesListResponse).
            // You can also use JsonHttpResponseHandler and process the response as you wish.
            mHttpClient.getDevicesList(new NetatmoResponseHandler(mHttpClient,
                    NetatmoResponseHandler.REQUEST_GET_DEVICES_LIST, null) {
                @Override
                public void onStart() {
                    super.onStart();
//                    setProgressBarIndeterminateVisibility(Boolean.TRUE);
                }

                @Override
                public void onGetDevicesListResponse(final List<Station> devices) {
                    mDevices = devices;

                    handler.post(new Runnable() {

                        @Override	public void run() {


                            List<String> stationsNames = new ArrayList<String>();
                            for (Station station : devices) {
                                stationsNames.add(station.getName());
                                Log.d(TAG, station.getName());
                            }

//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
//                                    android.R.layout.simple_spinner_dropdown_item, stationsNames);

//                            ActionBar actionBar = getActionBar();
//                            actionBar.setDisplayShowTitleEnabled(false);
//                            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//                            actionBar.setListNavigationCallbacks(adapter, activity);

                        }});
                }

                @Override
                public void onFinish() {
                    super.onFinish();
//                    handler.post(new Runnable() {
//                        @Override	public void run() {
//                            setProgressBarIndeterminateVisibility(Boolean.FALSE);
//                        }});
                }
            });
        } else {
            Log.v(TAG, "---- getttAccessToken is failed, need login?");

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.atmo_layout);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.jonki_image, pendingIntent);
        }

        for(int appWidgetId: appWidgetIds) {
            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.atmo_layout);
            watchWidget = new ComponentName(context, WidgetProvider.class);
            Intent intent = new Intent(context, getClass());
            intent.setAction(SYNC_CLICKED);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.jonki_btn, pendingIntent);
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    public static PendingIntent clickButton(Context context) {
        // クリック回数を増加
        WidgetIntentReceiver.clickCount ++;

        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction("UPDATE_WIDGET");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // http://stackoverflow.com/questions/14798073/button-click-event-for-android-widget
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.v(TAG, "onReceive: " + action);
        if (SYNC_CLICKED.equals(intent.getAction())) {
            Log.v(TAG, "jonki Button1 Clicked");
        }
    }

    // アップデート
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}