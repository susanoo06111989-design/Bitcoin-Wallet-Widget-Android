package com.bitcoin.walletwidget;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
public class MainActivity extends Activity {
 @Override public void onCreate(Bundle state) {
  super.onCreate(state);
  LinearLayout root=new LinearLayout(this);
  root.setOrientation(LinearLayout.VERTICAL);
  root.setPadding(36,48,36,36);
  root.setBackgroundColor(Color.rgb(20,20,20));
  TextView title=new TextView(this);
  title.setText("$  Bitcoin Wallet Widget");
  title.setTextColor(Color.rgb(51,214,122));title.setTextSize(26);
  root.addView(title);
  TextView info=new TextView(this);
  info.setText("\nYour Bitcoin wallet is already configured.\n\nAddress: "+WalletWidget.WALLET_ADDRESS+"\n\nAdd the widget to your Android home screen. It updates automatically when Android permits. No private keys are used.\n");
  info.setTextColor(Color.WHITE);info.setTextSize(15);
  root.addView(info);
  Button refresh=new Button(this);refresh.setText("Update wallet now");
  root.addView(refresh);
  refresh.setOnClickListener(v->WalletWidget.refresh(this));
  setContentView(root);
 }
}
