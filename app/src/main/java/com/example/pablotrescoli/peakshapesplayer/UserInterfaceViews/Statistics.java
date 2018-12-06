package com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pablotrescoli.peakshapesplayer.R;
import com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects.ShapeDataArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Statistics extends AppCompatActivity {


    TextView squares;
    TextView triangles;
    TextView circles;

    int nSquares = 0;
    int nTriangles = 0;
    int nCircles = 0;

    private int SQUARE = 4;
    private int TRIANGLE = 3;
    private int CIRCLE = 1;

    ArrayList<Integer[]> currentSDA;  //we send back data structure, not class
    ArrayList<ArrayList<Integer[]>> historyArray; //we need the data structure, not the class to send back, historyArray isnt being modified here
    ShapeDataArray shapeDataArray = new ShapeDataArray();

    int[] numberOfShapes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        squares = findViewById(R.id.squaresDisp);
        triangles = findViewById(R.id.trianglesDisp);
        circles = findViewById(R.id.circlesDisp);

        retrieveShapeData();

        numberOfShapes = getNumberOfShapes(currentSDA);

        updateLayout(numberOfShapes);


        Button deleteSquaresBtn = findViewById(R.id.deleteSqBtn);
        deleteSquaresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeDataArray.deleteType(currentSDA, SQUARE);
                goBackToEditor();
            }
        });

        Button deleteCirclesBtn = findViewById(R.id.deleteCirBtn);
        deleteCirclesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeDataArray.deleteType(currentSDA, CIRCLE);
                goBackToEditor();
            }
        });

        Button deleteTrianglesBtn = findViewById(R.id.deleteTrBtn);
        deleteTrianglesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeDataArray.deleteType(currentSDA, TRIANGLE);
                goBackToEditor();
            }
        });

    }

    private void goBackToEditor() {
        String shapeDataString = getShapeData();
        String historyDataString=getHistoryData();
        Intent intent = new Intent(Statistics.this, DrawingEditor.class);
        intent.putExtra("shapeDataString", shapeDataString);
        intent.putExtra("shapeHistoryString", historyDataString);
        startActivity(intent);
    }

    public String getShapeData() {
        currentSDA = shapeDataArray.getShapeDataArray();
        Gson gS = new Gson();
        return gS.toJson(currentSDA);
    }

    public String getHistoryData() {
        Gson gS = new Gson();
        return gS.toJson(historyArray);
    }

    private void retrieveShapeData() {
        String shapeDataString = getIntent().getStringExtra("shapeDataString");
        Gson gSshape = new Gson();
        Type typeShape = new TypeToken<ArrayList<Integer[]>>() {}.getType();
        currentSDA = gSshape.fromJson(shapeDataString, typeShape);

        String shapeHistoryString=getIntent().getStringExtra("historyArrayListString");
        Gson gShistory=new Gson();
        Type typeHistory=new TypeToken<ArrayList<ArrayList<Integer[]>>>(){}.getType();
        historyArray=gShistory.fromJson(shapeHistoryString,typeHistory);

    }

    private void updateLayout(int[] numberOfShapes) {
        nSquares = numberOfShapes[0];
        nTriangles = numberOfShapes[1];
        nCircles = numberOfShapes[2];

        String sqStr = "SQUARES: " + nSquares;
        String trStr = "TRIANGLES: " + nTriangles;
        String ciStr = "CIRCLES: " + nCircles;

        squares.setText(sqStr);
        triangles.setText(trStr);
        circles.setText(ciStr);

    }

    private int[] getNumberOfShapes(ArrayList<Integer[]> shapeDataArray) {

        numberOfShapes = new int[3];
        for (Integer[] shape : shapeDataArray) {
            if (shape[0] == 4) {
                numberOfShapes[0]++;
            }
            if (shape[0] == 3) {
                numberOfShapes[1]++;
            }
            if (shape[0] == 1) {
                numberOfShapes[2]++;
            }
        }

        return numberOfShapes;
    }

}
