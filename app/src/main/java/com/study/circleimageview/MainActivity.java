package com.study.circleimageview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private InputStream in;
    private CircleView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.circle);
        try {
            in = getAssets().open("jianli.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //view.setPicture(in);
    }
}