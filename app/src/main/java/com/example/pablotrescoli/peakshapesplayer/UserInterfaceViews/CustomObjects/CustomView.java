package com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import static com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects.MathemathicalHelperMethods.randomiser;
import static com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects.MathemathicalHelperMethods.squareCoordCreator;
import static com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects.MathemathicalHelperMethods.touchDetectRegion;
import static com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects.MathemathicalHelperMethods.triangleCoordCreator;

public class CustomView extends android.support.v7.widget.AppCompatImageView {

    private Rect mRect;
    private Paint mPaint;
    private Path mPath;

    int vWidth;
    int vHeight;
    int xCentre;
    int yCentre;

    private int CIRCLE_RADIUS = 75;
    private int CIRCLE_DIAMETRE = 150;
    private int SQUARE_LENGTH = 150;
    private int TRIANGLE_VERTICAL = 150;
    private int TRIANGLE_TYPE = 3;
    private int SQUARE_TYPE = 4;
    private int CIRCLE_TYPE = 1;

    private long timeTouchStart;
    private long timeTouchFinish;
    private int idShapeTouchedStart;
    private int idShapeTouchedFinish;

    private ShapeDataArray shapeDataArray;
    private ShapeDataArrayHistory shapeDataArrayHistory;
    private Integer[] squareCoordinates;
    private Integer[] triangleCoordinates;

    TextView scoreDisp;

    //SDA abbreviates for Shape data array which is the array containing information on every drawn shape
    //HA is the History array, an arraylist of SDAs, every action updates or makes a new entry to the History Array

    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(null);

    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(null);

    }

    private void init(@Nullable AttributeSet set) {
        mRect = new Rect();
        mPath = new Path();
        mPaint = new Paint();

        squareCoordinates = new Integer[4];
        triangleCoordinates = new Integer[6];

        shapeDataArrayHistory = new ShapeDataArrayHistory();

        shapeDataArray = new ShapeDataArray();
    }


    public void generateRandomShape(View view, Integer shapeType) {

        vWidth = view.getWidth();
        vHeight = view.getHeight();

        int shapeRelevantDimension;

        switch (shapeType) {
            case 1:
                shapeRelevantDimension = CIRCLE_DIAMETRE;
                break;
            case 3:
                shapeRelevantDimension = TRIANGLE_VERTICAL;
                break;
            case 4:
                shapeRelevantDimension = SQUARE_LENGTH;
                break;
            default:
                shapeRelevantDimension = 0;
        }

        xCentre = randomiser(vWidth - shapeRelevantDimension) + shapeRelevantDimension / 2;
        yCentre = randomiser(vHeight - shapeRelevantDimension) + shapeRelevantDimension / 2;

        shapeDataArray.addShape(shapeType, xCentre, yCentre);

        addNewInstanceToHistory();

        postInvalidate();

    }

    public void undoAction() {

        boolean historyEmpty=checkHistoryEmpty();

        if (!historyEmpty) {

            shapeDataArrayHistory.deleteLastEntry();

            ArrayList<ArrayList<Integer[]>> fullHistory = shapeDataArrayHistory.getFullHistory();
            //if history isnt empty, it sets the current SDA to the previous SDA, creates new instance to prevent shared memory adress referencing.
            if (fullHistory.size() > 0) {
                ArrayList<Integer[]> previousSDA = fullHistory.get(fullHistory.size() - 1);
                ArrayList<Integer[]> newInstancePreviousSDA = new ArrayList<>(previousSDA);
                shapeDataArray.setDataToHistory(newInstancePreviousSDA);
            } else {
                shapeDataArray.dataCleared();
            }
        }

        postInvalidate();

    }

    private boolean checkHistoryEmpty() {

        ArrayList<ArrayList<Integer[]>> fullHistory = shapeDataArrayHistory.getFullHistory();
        return fullHistory.size() <= 0;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        float x;
        float y;

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                x = event.getX();
                y = event.getY();
                timeTouchStart = event.getEventTime();
                startTouch(x, y);

                return true;
            case (MotionEvent.ACTION_MOVE):
                return true;
            case (MotionEvent.ACTION_UP):
                x = event.getX();
                y = event.getY();
                timeTouchFinish = event.getEventTime();
                endTouch(x, y);

                return true;
            case (MotionEvent.ACTION_CANCEL):
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void startTouch(float xStart, float yStart) {
        idShapeTouchedStart = detectIdShapeTouch(xStart, yStart);
    }

    private void endTouch(float xFinish, float yFinish) {
        idShapeTouchedFinish = detectIdShapeTouch(xFinish, yFinish);

        if (idShapeTouchedStart == idShapeTouchedFinish && idShapeTouchedStart != -1) {
            long timeHold = timeTouchFinish - timeTouchStart;

            if (timeHold > 1000) {
                deleteShape(idShapeTouchedStart);

            } else {
                transformShape(idShapeTouchedStart);
            }

        }

    }

    private void deleteShape(int idShapeTouchedStart) {

        shapeDataArray.removeSpecificShape(idShapeTouchedStart);

        ArrayList<Integer[]> updatedSDA = shapeDataArray.getShapeDataArray();
        ArrayList<Integer[]> newInstanceUpdatedSDA = new ArrayList<>(updatedSDA);
        shapeDataArrayHistory.addDataToHistory(newInstanceUpdatedSDA);

        postInvalidate();
    }

    private void transformShape(Integer positionID) {

        shapeDataArray.transformType(positionID);
        addNewInstanceToHistory();
        postInvalidate();

    }

    private void addNewInstanceToHistory() {
        //this function makes sure a new instance of a shape data array is created when added to the history array,
        // so that they dont share the same memory adress reference. New instance has a new memory adress.
        ArrayList<Integer[]> newObjectArray = new ArrayList<>();
        ArrayList<Integer[]> SDAcopy = shapeDataArray.getShapeDataArray();
        for (Integer[] currShape : SDAcopy) {
            newObjectArray.add(currShape.clone());
        }
        shapeDataArrayHistory.addDataToHistory(newObjectArray);
    }


    private int detectIdShapeTouch(float touch_x_coordinate, float touch_y_coordinate) {

        ArrayList<Integer[]> shapesToCheck = shapeDataArray.getShapeDataArray();
        int counter = 0;
        for (Integer[] shape : shapesToCheck) {
            //if the touch region is inside the boundaries of a given shape, it returns the position in the array of the shape
            // which can then be used as the identifier to modify the shape.
            // if no shape boundaries match the user touch, returns -1
            if (shape[0] == 4) {
                squareCoordinates = squareCoordCreator(shape[1], shape[2], SQUARE_LENGTH);
                //squareCoordinates represent an array of top left corner x and y coordinates, as well as bottom right x and y.
                if (touch_x_coordinate >= squareCoordinates[0] && touch_x_coordinate <= squareCoordinates[2] && touch_y_coordinate >= squareCoordinates[1] && touch_y_coordinate <= squareCoordinates[3]) {
                    return counter;
                }
            }

            if (shape[0] == 3) {
                triangleCoordinates = triangleCoordCreator(shape[1], shape[2], TRIANGLE_VERTICAL);
                //triangleCoordinates has the coordinates of the 3 x coordinates and the 3 y coordinates of the points which make the triangle.

                double[] touch_detect_region = touchDetectRegion(triangleCoordinates);
                //the area which triggers the action of a triangle being touched, is a shape built with helper points x31, x32, and y3.
                double x31 = touch_detect_region[0];
                double x32 = touch_detect_region[1];
                double y3 = touch_detect_region[2];

                if (touch_x_coordinate >= x31 && touch_x_coordinate <= x32 && touch_y_coordinate <= y3 && touch_y_coordinate >= triangleCoordinates[1]) {
                    return counter;
                }

                if (touch_x_coordinate >= triangleCoordinates[2] && touch_x_coordinate <= triangleCoordinates[4] && touch_y_coordinate <= triangleCoordinates[3] && touch_y_coordinate >= y3) {
                    return counter;
                }
            }

            if (shape[0] == 1) {
                double distToCentre = Math.sqrt((Math.abs(Math.pow(touch_x_coordinate - shape[1], 2)))
                        + Math.abs(Math.pow(touch_y_coordinate - shape[2], 2)));
                if (distToCentre <= CIRCLE_RADIUS) {
                    return counter;
                }

            }
            counter++;

        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int score = 0;
        ArrayList<Integer[]> shapesToDraw = shapeDataArray.getShapeDataArray();

        canvas.drawColor(Color.BLACK);

        if (shapesToDraw.size() > 0) {
            checkHistory("when DRAWING");
            for (Integer[] shape : shapesToDraw) {
                score++;
                if (shape[0] == 4) {

                    squareCoordinates = squareCoordCreator(shape[1], shape[2], SQUARE_LENGTH);
                    mRect.set(squareCoordinates[0], squareCoordinates[1], squareCoordinates[2], squareCoordinates[3]);

                    mPaint.setColor(Color.RED);

                    canvas.drawRect(mRect, mPaint);
                }
                if (shape[0] == 3) {

                    triangleCoordinates = triangleCoordCreator(shape[1], shape[2], TRIANGLE_VERTICAL);

                    mPath.moveTo(triangleCoordinates[0], triangleCoordinates[1]);
                    mPath.lineTo(triangleCoordinates[2], triangleCoordinates[3]);
                    mPath.moveTo(triangleCoordinates[2], triangleCoordinates[3]);
                    mPath.lineTo(triangleCoordinates[4], triangleCoordinates[5]);
                    mPath.moveTo(triangleCoordinates[4], triangleCoordinates[5]);
                    mPath.lineTo(triangleCoordinates[0], triangleCoordinates[1]);

                    mPaint.setColor(Color.BLUE);
                    mPaint.setStrokeWidth(3);
                    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                    canvas.drawPath(mPath, mPaint);
                    mPath.reset();
                }

                if (shape[0] == 1) {

                    mPaint.setColor(Color.GREEN);

                    canvas.drawCircle(shape[1], shape[2], CIRCLE_RADIUS, mPaint);
                }
            }
        } else {
            canvas.drawColor(Color.BLACK);
        }
        scoreDisp.setText(String.valueOf(score));

    }

    public void updateScore(TextView scoreDisplay) {
        scoreDisp = scoreDisplay;
    }


    public void loadArrayAndDraw(ArrayList<Integer[]> shapeDataFromStats, ArrayList<ArrayList<Integer[]>> shapeHistoryFromStats) {
        //this function is called when user navigates back to editor post deleting a type of shape. New instances are created so the
        // history array and the shape data array are loaded/transferred through the intent.
        for (Integer[] shape : shapeDataFromStats) {
            shapeDataArray.addShape(shape[0], shape[1], shape[2]);
        }
        shapeDataArrayHistory.addFullHistory(shapeHistoryFromStats);
        shapeDataArrayHistory.addDataToHistory(shapeDataFromStats);


        postInvalidate();

    }

    public String getShapeData() {
        ArrayList<Integer[]> shapesArray = shapeDataArray.getShapeDataArray();
        Gson gS = new Gson();
        return gS.toJson(shapesArray);
    }

    public String getHistoryData() {
        ArrayList<ArrayList<Integer[]>> historyAr = shapeDataArrayHistory.getFullHistory();
        Gson gS = new Gson();
        return gS.toJson(historyAr);
    }

    public void checkHistory(String stage) {
        ArrayList<ArrayList<Integer[]>> fullHistory = shapeDataArrayHistory.getFullHistory();
        int l = 1;
        for (ArrayList<Integer[]> histArr : fullHistory) {
            for (Integer[] currShape : histArr) {
                Log.d("FULL HIST", stage + " " + l + " type " + currShape[0] + " xc " + currShape[1] + " yc " + currShape[2]);
            }
            l++;
        }
    }

}
