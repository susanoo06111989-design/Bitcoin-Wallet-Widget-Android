package com.bitcoin.walletwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
 @Override public void onCreate(Bundle state) {
  super.onCreate(state);
  LinearLayout root = new LinearLayout(this);
  root.setOrientation(LinearLayout.VERTICAL);
  root.setPadding(36,48,36,36);
  root.setBackgroundColor(Color.rgb(20,20,20));
  TextView title = new TextView(this);
  title.setText("₿ Bitcoin Wallet Widget");
  title.setTextColor(Color.rgb(247,147,26)); title.setTextSize(26);
  root.addView(title);
  TextView info = new TextView(this);
  info.setText("\nEnter a PUBLIC Bitcoin mainnet address (bc1, 1 or 3). Never enter a seed phrase or private key.\n\nThe address is queried through mempool.space. Add the widget from your home screen after saving.\n");
  info.setTextColor(Color.WHITE); info.setTextSize(15);
  root.addView(info);
  EditText address = new EditText(this);
  address.setSingleLine(true);
  address.setTextColor(Color.WHITE);
  address.setHintTextColor(Color.LTGRAY);
  address.setHint("Bitcoin public address");
  address.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
  address.setText(getSharedPreferences("wallet",0).getString("address",""));
  root.addView(address);
  TextView result = new TextView(this); result.setTextColor(Color.WHITE); result.setPadding(0,18,0,18);
  Button save = new Button(this); save.setText("Save address & refresh");
  root.addView(save); root.addView(result);
  save.setOnClickListener(v -> {
   String a = address.getText().toString().trim();
   if (!a.matches("(?i)^(bc1[ac-hj-np-z02-9]{11,87}|[13][a-km-zA-HJ-NP-Z1-9]{25,34})$")) {
    result.setText("Invalid Bitcoin address format."); return;
   }
   getSharedPreferences("wallet",0).edit().putString("address",a).apply();
   result.setText("Saved locally. Updating widget...");
   WalletWidget.refresh(this);
  });
  setContentView(root);
 }
}
