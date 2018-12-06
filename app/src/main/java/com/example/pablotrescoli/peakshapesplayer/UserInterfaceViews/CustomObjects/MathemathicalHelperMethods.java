package com.example.pablotrescoli.peakshapesplayer.UserInterfaceViews.CustomObjects;

public class MathemathicalHelperMethods {

    //contains functions which help with geometrical and mathematical issues

    public static int randomiser(int max) {
        //returns a random value between 0 and max
        return (int) Math.floor(Math.random() * (max));

    }


    public static double[] touchDetectRegion(Integer[] triangleCoordinates) {
        //the region which triggers if a triangle has been touched has the shape of 2 rectangles
        //these points provide the coordinates of the vertices of the rectangle
        double x31 = triangleCoordinates[2] + (triangleCoordinates[4] - triangleCoordinates[2]) * 0.3;
        double x32 = triangleCoordinates[4] - (triangleCoordinates[4] - triangleCoordinates[2]) * 0.3;
        double y3 = triangleCoordinates[1] + (triangleCoordinates[3] - triangleCoordinates[1]) * 0.5;

        return new double[]{x31, x32, y3};
    }


    public static Integer[] squareCoordCreator(Integer center_x, Integer center_y, Integer squareLength) {
        //returns an array with the coordinates of the top left and bottom right corners of the square,
        //given the square side length and the geometrical centre of the shape.
        Integer[] squareCoords = new Integer[4];
        squareCoords[0] = center_x - (squareLength / 2);
        squareCoords[1] = center_y - (squareLength / 2);
        squareCoords[2] = center_x + (squareLength / 2);
        squareCoords[3] = center_y + (squareLength / 2);

        return squareCoords;
    }

    public static Integer[] triangleCoordCreator(Integer center_x, Integer center_y, Integer triangleVertical) {
        //returns an array with the x and y coordinates of the 3 points which make a triangle,
        //given the triangle vertical length and the geometrical centre of the shape.
        Integer[] triangleCoords = new Integer[6];

        int horizontalDisplacement = (int) Math.floor(triangleVertical / (2 * Math.sin(Math.toRadians(60))));

        triangleCoords[0] = center_x;
        triangleCoords[1] = center_y - ((triangleVertical * 2) / 3);
        triangleCoords[2] = center_x - horizontalDisplacement;
        triangleCoords[3] = center_y + ((triangleVertical) / 3);
        triangleCoords[4] = center_x + horizontalDisplacement;
        triangleCoords[5] = center_y + ((triangleVertical) / 3);

        return triangleCoords;
    }

}
