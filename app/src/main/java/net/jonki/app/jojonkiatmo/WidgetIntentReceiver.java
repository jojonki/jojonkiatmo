package net.jonki.app.jojonkiatmo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by jonki on 15/01/20.
 */
public class WidgetIntentReceiver extends BroadcastReceiver {
    public static int clickCount = 0;
    final int[] ICON_IMAGES = {
            R.drawable.jojonki_mark,
            R.drawable.jojonki_neko
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("UPDATE_WIDGET")) {
//            Toast.makeText(context, "jonki", Toast.LENGTH_SHORT).show();
//            Log.v("HOGE", "FUGA");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.atmo_layout);


            remoteViews.setImageViewResource(R.id.jonki_image, ICON_IMAGES[clickCount % ICON_IMAGES.length]);

            // テキストをクリック回数を元に更新
            remoteViews.setTextViewText(R.id.title, "クリック回数: " + WidgetIntentReceiver.clickCount);

            // もう一回クリックイベントを登録(毎回登録しないと上手く動かず)
//            remoteViews.setOnClickPendingIntent(R.id.button, WidgetProvider.clickButton(context));

            remoteViews.setOnClickPendingIntent(R.id.jonki_image, WidgetProvider.clickButton(context));

            WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }
}