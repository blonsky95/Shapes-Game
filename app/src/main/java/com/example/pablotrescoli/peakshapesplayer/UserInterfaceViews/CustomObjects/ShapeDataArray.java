package com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects;


import java.util.ArrayList;

public class ShapeDataArray {

    int numberOfSides;
    private int TRIANGLE_TYPE = 3;
    private int SQUARE_TYPE = 4;
    private int CIRCLE_TYPE = 1;

    private ArrayList<Integer[]> shapeDataArray;

    public ShapeDataArray() {
        shapeDataArray = new ArrayList<>();
    }

    public void setDataToHistory(ArrayList<Integer[]> previousData) {
        shapeDataArray = previousData;
    }

    public void dataCleared() {
        shapeDataArray = new ArrayList<>();
    }

    public void addShape(int nSides, int cx, int cy) {
        numberOfSides = nSides;
        Integer[] newShape = new Integer[3];
        newShape[0] = numberOfSides;
        newShape[1] = cx;
        newShape[2] = cy;
        shapeDataArray.add(newShape);

    }

    public ArrayList<Integer[]> getShapeDataArray() {
        return shapeDataArray;
    }

    public void removeSpecificShape(int positionN) {

        shapeDataArray.remove(positionN);

    }

    public void deleteType(ArrayList<Integer[]> shapeData, int type) {
        shapeDataArray = shapeData;
        for (int i = 0; i < shapeDataArray.size(); i++) {
            Integer[] shape = shapeDataArray.get(i);
            if (shape[0] == type) {
                shapeDataArray.remove(i);
                i--; //accounts for the changing size of the currentSDA, if we
                // remove one row, all the rows shift downwards one, so no need to increment i.

            }

        }

    }

    public void transformType(Integer positionID) {
        int positionIDint = positionID;
        Integer[] toBeTransformedShape = shapeDataArray.get(positionID);
        boolean alreadyChanged = false;

        if (toBeTransformedShape[0] == SQUARE_TYPE) {
            toBeTransformedShape[0] = CIRCLE_TYPE;
            alreadyChanged = true;
        }

        if (toBeTransformedShape[0] == CIRCLE_TYPE && !alreadyChanged) {
            toBeTransformedShape[0] = TRIANGLE_TYPE;
            alreadyChanged = true;
        }

        if (toBeTransformedShape[0] == TRIANGLE_TYPE && !alreadyChanged) {
            toBeTransformedShape[0] = SQUARE_TYPE;
        }

        shapeDataArray.remove(positionIDint);
        shapeDataArray.add(positionID, toBeTransformedShape);

    }

}
