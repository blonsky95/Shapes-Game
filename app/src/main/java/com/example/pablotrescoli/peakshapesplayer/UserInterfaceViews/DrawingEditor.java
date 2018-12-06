package com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablotrescoli.peakshapesplayer.R;
import com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects.CustomView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DrawingEditor extends AppCompatActivity {

    private CustomView mCustomView;
    private int SQUARE_TYPE=4;
    private int TRIANGLE_TYPE=3;
    private int CIRCLE_TYPE=1;

    // todo - mockito
    // todo ADD IMPLEMENTATIONS AND MAKE IT A GAME
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_editor);

        mCustomView = findViewById(R.id.customview);

        TextView undoBtn = findViewById(R.id.undoBtn);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.undoAction();
            }
        });

        TextView statsBtn = findViewById(R.id.statsBtn);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shapeDataString = mCustomView.getShapeData();
                Intent intent = new Intent(DrawingEditor.this, Statistics.class);
                intent.putExtra("shapeDataString", shapeDataString);

                String historyArrayListString=mCustomView.getHistoryData();
                intent.putExtra("historyArrayListString",historyArrayListString);

                startActivity(intent);
            }
        });

        ImageView triangleBtn = findViewById(R.id.triangleBtn);
        triangleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.generateRandomShape(mCustomView,TRIANGLE_TYPE);
            }
        });

        ImageView squareBtn = findViewById(R.id.squareBtn);
        squareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.generateRandomShape(mCustomView,SQUARE_TYPE);
            }
        });

        ImageView circleBtn = findViewById(R.id.circleBtn);
        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.generateRandomShape(mCustomView,CIRCLE_TYPE);
            }
        });

        TextView scoreDisplay=findViewById(R.id.scoreDisp);
        mCustomView.updateScore(scoreDisplay);

        retrieveShapeData();

    }

    private void retrieveShapeData() {
        //checks if user has navigated from stats and thus has to load the shape data array and history array
        String shapeDataString = getIntent().getStringExtra("shapeDataString");
        String shapeHistoryString=getIntent().getStringExtra("shapeHistoryString");
        if (shapeDataString!=null) {

            Gson gSshape = new Gson();
            Type typeShape = new TypeToken<ArrayList<Integer[]>>() {
            }.getType();
            ArrayList<Integer[]> shapeDataFromStats = gSshape.fromJson(shapeDataString, typeShape);

            Gson gShistory= new Gson();
            Type typeHistory = new TypeToken<ArrayList<ArrayList<Integer[]>>>(){}.getType();
            ArrayList<ArrayList<Integer[]>> historyArr=gShistory.fromJson(shapeHistoryString,typeHistory);

            mCustomView.loadArrayAndDraw(shapeDataFromStats,historyArr);
        }
    }

}
