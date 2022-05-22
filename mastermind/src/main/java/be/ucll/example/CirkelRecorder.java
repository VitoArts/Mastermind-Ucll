package be.ucll.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CirkelRecorder {
    ArrayList<Cirkel> cirkelArrayList = new ArrayList<>();

    public void CreateUnit (int xCoordinate, int yCoordinate, java.awt.Color pieceColor)
    {cirkelArrayList.add(new Cirkel(xCoordinate,yCoordinate,pieceColor));}

    public void DeleteList ()
    {
        cirkelArrayList.clear();
    }

    public void CreateRandomRow(int maxRound, int maxCircles)
    {
        Color[] colors = new Color[]
                {
                        Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN,
                        Color.PINK, Color.ORANGE
                };
        for (int i = 0; i < maxCircles; i++) {
            Color pieceColor = colors[randInt(0, colors.length-1)];
            cirkelArrayList.add(new Cirkel(75+((400/maxCircles)*i), 450/maxRound, pieceColor));
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
