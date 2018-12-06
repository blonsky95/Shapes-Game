package com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects;

import java.util.ArrayList;

public class ShapeDataArrayHistory {

    private ArrayList<ArrayList<Integer[]>> actionHistory;

    public ShapeDataArrayHistory() {
        this.actionHistory = new ArrayList<>();
    }

    //example of static method - can be called anywhere without creating an instance of ShapeDataArrayHistory
    //todo - get rid of this once another static method has been applied
    public static int returnSize(String lol) {
        return lol.length();
    }

    public void addDataToHistory(ArrayList<Integer[]> currentShapeData) {
        //used to add an shape data array entry
        ArrayList<Integer[]> newDataArray;
        newDataArray=currentShapeData;
        actionHistory.add(newDataArray);

    }

    public void addFullHistory(ArrayList<ArrayList<Integer[]>> fullHistory) {
        //used when history has been cleared, or instanstiated to new, so has to be reloaded.
        actionHistory=fullHistory;
    }


    public ArrayList<ArrayList<Integer[]>> getFullHistory () {
        //mostly used to recopile information for debugging
            return actionHistory;
    }

    public void deleteLastEntry() {
        //used when undo action called
        actionHistory.remove(actionHistory.size()-1);
    }

}
