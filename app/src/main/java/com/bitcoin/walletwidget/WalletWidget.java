package com.bitcoin.walletwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WalletWidget extends AppWidgetProvider {
 private static final String ACTION_REFRESH="com.bitcoin.walletwidget.REFRESH";
 private static final ExecutorService EXEC=Executors.newSingleThreadExecutor();
 @Override public void onUpdate(Context context,AppWidgetManager manager,int[] ids){refresh(context);}
 @Override public void onReceive(Context context,Intent intent){
  super.onReceive(context,intent);
  if(ACTION_REFRESH.equals(intent.getAction()))refresh(context);
 }
 static void refresh(Context context){
  Context app=context.getApplicationContext();
  EXEC.execute(()->{
   String address=app.getSharedPreferences("wallet",0).getString("address","");
   if(address.isEmpty()){render(app,"Set wallet address","Open the app","No address configured");return;}
   try {
    JSONObject stats=getJson("https://mempool.space/api/address/"+address);
    JSONObject chain=stats.getJSONObject("chain_stats");
    JSONObject mem=stats.getJSONObject("mempool_stats");
    long confirmed=chain.getLong("funded_txo_sum")-chain.getLong("spent_txo_sum");
    long pending=mem.getLong("funded_txo_sum")-mem.getLong("spent_txo_sum");
    double btc=(confirmed+pending)/100000000.0;
    String fiat="Price unavailable";
    try {
     JSONObject prices=getJson("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd,brl").getJSONObject("bitcoin");
     fiat=String.format(Locale.US,"≈ $%,.2f USD  |  R$ %,.2f",btc*prices.getDouble("usd"),btc*prices.getDouble("brl"));
    }catch(Exception ignored){}
    String status="Confirmed: "+String.format(Locale.US,"%.8f",confirmed/100000000.0)+" BTC";
    if(pending!=0)status+="  |  Pending: "+String.format(Locale.US,"%+.8f",pending/100000000.0);
    status+="  •  "+DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
    render(app,String.format(Locale.US,"%.8f BTC",btc),fiat,status);
   }catch(Exception e){render(app,"Update unavailable","Tap refresh to retry","Network/API error • previous data not retained");}
  });
 }
 private static JSONObject getJson(String url) throws Exception{
  HttpURLConnection c=(HttpURLConnection)new URL(url).openConnection();
  c.setConnectTimeout(12000);c.setReadTimeout(12000);
  c.setRequestProperty("Accept","application/json");
  c.setRequestProperty("User-Agent","BitcoinWalletWidget/1.0");
  try(InputStream in=c.getInputStream()){
   byte[] bytes=in.readAllBytes();
   return new JSONObject(new String(bytes,StandardCharsets.UTF_8));
  }finally{c.disconnect();}
 }
 private static void render(Context context,String balance,String fiat,String status){
  AppWidgetManager mgr=AppWidgetManager.getInstance(context);
  ComponentName component=new ComponentName(context,WalletWidget.class);
  int[] ids=mgr.getAppWidgetIds(component);
  for(int id:ids){
   RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget_wallet);
   views.setTextViewText(R.id.balance,balance);
   views.setTextViewText(R.id.fiat,fiat);
   views.setTextViewText(R.id.status,status);
   Intent refresh=new Intent(context,WalletWidget.class).setAction(ACTION_REFRESH);
   PendingIntent pi=PendingIntent.getBroadcast(context,0,refresh,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
   views.setOnClickPendingIntent(R.id.refresh,pi);
   Intent open=new Intent(context,MainActivity.class);
   PendingIntent app=PendingIntent.getActivity(context,0,open,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
   views.setOnClickPendingIntent(R.id.balance,app);
   mgr.updateAppWidget(id,views);
  }
 }
}
