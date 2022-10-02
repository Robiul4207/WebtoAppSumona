package com.robiultec.webtoappsumon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class ScanQrCode2 extends AppCompatActivity  {
    Button scannerButton;
    public static TextView scannerTextView;
    public static ImageView imageView;
    ClipboardManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code2);
       // scannerButton=findViewById(R.id.scannerId);
        scannerTextView=findViewById(R.id.scannerText);
       // scannerButton.setOnClickListener(this);
        startActivity(new Intent(getApplicationContext(),ScannerViewActivity.class));


    }

//        startActivity(new Intent(getApplicationContext(),ScannerViewActivity.class));
//
//
    public void copy(View view) {
        String text = scannerTextView.getText().toString();
        if (!text.isEmpty()) {
            manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("key", text);
            manager.setPrimaryClip(clipData);
            Toast.makeText(this, "copied text", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "no data here", Toast.LENGTH_SHORT).show();

        }
    }
}