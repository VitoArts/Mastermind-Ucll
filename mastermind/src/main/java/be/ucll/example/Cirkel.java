package be.ucll.example;


import java.awt.*;

public class Cirkel {
    private int xCoordinate;
    private int yCoordinate;
    private java.awt.Color pieceColor;

    public Cirkel (int xCoordinate, int yCoordinate, java.awt.Color pieceColor)
    {
        this.pieceColor = pieceColor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    public Color getPieceColor() {return pieceColor;}
    public int getyCoordinate() {return yCoordinate;}
    public int getxCoordinate() {return xCoordinate;}
}
