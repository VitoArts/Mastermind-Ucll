package be.ucll.example;

import be.ucll.tekenbord.OngeldigeCoordinaatException;
import be.ucll.tekenbord.TekenBord;
import jdk.internal.dynalink.linker.ConversionComparator;
import sun.management.counter.ByteArrayCounter;

import java.awt.*;
import java.util.Random;

public class Mastermind {

    //Circle counter = counter1
    static int circleCounter = 0;
    //Round counter = counter2
    static int roundCounter = 0;
    //Peg counter = counter3
    static int pegCounter = 0;

    //Max round = Number of rounds that the game plays for. This can be altered and the design of the game changes with it as a result.
    //This should not be changed, but can be changed with some possibly weird results.
    static int maxRound = 10;
        static int yBase = 900/maxRound;
    //Max circles = Number of circles that the hidden row has and that the player must guess correctly.

    //Not fully included in the program (Up to 4 pegs can be placed at the moment), but ideally a step towards expanding the program.
    //To fully implement we would need to make the location of the pegs that are placed connected to the number of circles that could be drawn
    //which (like the maxCircles option) I would need to think up an aesthetically pleasing solution, which isn't part of the required work and I shall therefor not spend time on.
    static int maxCircles = 4;
    //xBase and xGap should ideally get a value that works to place the circles aesthetically pleasing, but since this isn't part of the required work I will not work this out.
        static int xBase = (75);
        static int xGap = ((400/maxCircles));

    private CirkelRecorder hiddenRandom = new CirkelRecorder();
    private CirkelRecorder testRow = new CirkelRecorder();
    private CirkelRecorder colorEliminator = new CirkelRecorder();

    public static void main(String[] args) {
        Mastermind main = new Mastermind();
        main.start();
    }

    private void start() {
        //We draw the board here.
        final TekenBord tekenBord = new TekenBord();
        try {
            tekenBord.tekenLijn(450, 0, 450, 900);
            for (int i = 1; i < maxRound; i++) {
                tekenBord.tekenLijn(550, yBase * i, 0, yBase * i);
            }
            tekenBord.setStatusTekst("Choose the colors for your row on the right.");
        } catch (OngeldigeCoordinaatException e) {
            e.printStackTrace();
        }
        /////////////////////////
        //We create our randomized hidden row.
        hiddenRandom.CreateRandomRow(maxRound, maxCircles);
        /////////////////////////

        //We draw the circles in the colors that were selected by the user.
        tekenBord.registreerKleurenHandler(
                k -> {
                    //Restart game option (Not necessary, but useful for game testing purposes)
                    if (roundCounter >= maxRound) {
                        hiddenRandom.DeleteList();
                        tekenBord.wisAlles();
                        roundCounter = 0;
                        hiddenRandom.CreateRandomRow(maxRound, maxCircles);

                        try {
                            tekenBord.tekenLijn(450, 0, 450, 900);
                            for (int i = 1; i < maxRound; i++) {
                                tekenBord.tekenLijn(550, yBase * i, 0, yBase * i);
                            }
                            tekenBord.setStatusTekst("Choose the colors for your row on the right.");
                        } catch (OngeldigeCoordinaatException e) {
                            e.printStackTrace();
                        }
                    }

                    //tekenBord.wisAlles();
                    try {
                        tekenBord.tekenCirkel(xBase + circleCounter * xGap, 900 - (yBase / 2) - roundCounter * (yBase), (250 / maxRound), k);
                        testRow.CreateUnit(xBase + circleCounter * xGap, 900 - (yBase / 2) - roundCounter * (yBase), k);
                        //Record color and coordinates
                        circleCounter += 1;
                        ///////////////////////////////////

                        //When the circle counter hits 4 (4 circles drawn in the testing row) then the row is evaluated.
                        //First we evaluate using the white pegs since we can then easily overwrite them with black ones if needed.

                        if (circleCounter == maxCircles) {
                            for (circleCounter = 0; circleCounter < maxCircles; circleCounter++) {
                                //colorEliminate checks if the color has already been evaluated in the test row
                                if (colorEliminate(testRow.cirkelArrayList.get(circleCounter).getPieceColor())) {
                                    //Here we attempt to find the lowest match count in the two rows. Meaning we get the value of the lowest number of similar colored circles.
                                    //When the value of colored circles in the test row is less than (or equal to) the value in the hidden row, we take the value of the test row as the number of pegs.
                                    //Alternatively we use the value of the colored circles in the hidden row when this value is less than the test row.
                                    if (colorTestCounter(testRow.cirkelArrayList.get(circleCounter).getPieceColor()) <= colorHiddenCounter(testRow.cirkelArrayList.get(circleCounter).getPieceColor())) {
                                        pegCounter += colorTestCounter(testRow.cirkelArrayList.get(circleCounter).getPieceColor());
                                    } else if (colorTestCounter(testRow.cirkelArrayList.get(circleCounter).getPieceColor()) > colorHiddenCounter(testRow.cirkelArrayList.get(circleCounter).getPieceColor())) {
                                        pegCounter += colorHiddenCounter(testRow.cirkelArrayList.get(circleCounter).getPieceColor());
                                    }
                                    colorEliminator.CreateUnit(75, 45, testRow.cirkelArrayList.get(circleCounter).getPieceColor());
                                }
                            }

                            //Based on the amount of pegs (Colors that correspond with the Test row) we now place the pegs on the board.
                            while (pegCounter != 0) {
                                int xValue = 475;
                                int yValue = 900 - (yBase * 2 / 3) - roundCounter * (yBase);
                                if (pegCounter % 2 == 0) {
                                    xValue = 525;
                                }
                                if (pegCounter > maxCircles / 2) {
                                    yValue = 900 - (yBase / 3) - roundCounter * (yBase);
                                }
                                tekenBord.tekenCirkel(xValue, yValue, 100 / maxRound, Color.WHITE);
                                pegCounter -= 1;
                            }
                            pegCounter = 0;


                            //Now we display the black pegs.
                            // We restarted the peg counter so now black pegs will overlap the white ones when they also have their x-coordinate in common.
                            for (int i = 0; i < maxCircles; i++) {
                                if (testRow.cirkelArrayList.get(i).getPieceColor() == hiddenRandom.cirkelArrayList.get(i).getPieceColor()) {
                                    int xValue = 525;
                                    int yValue = 900 - (yBase * 2 / 3) - roundCounter * (yBase);
                                    if (pegCounter % 2 == 0) {
                                        xValue = 475;
                                    }
                                    if (pegCounter+1 > maxCircles / 2) {
                                        yValue = 900 - (yBase / 3) - roundCounter * (yBase);
                                    }
                                    tekenBord.tekenCirkel(xValue, yValue, 100 / maxRound, Color.BLACK);

                                    if (pegCounter + 1 == maxCircles){
                                        roundCounter = maxRound;
                                    }
                                    pegCounter += 1;
                                }
                            }


                            //We restart all counters for the next round and count +1 for the round timer so the circles for next round align with the drawn design.
                            circleCounter = 0;
                            pegCounter = 0;
                            roundCounter += 1;

                            //All lists are emptied so new information can be recorded in the next round.
                            testRow.DeleteList();
                            colorEliminator.DeleteList();

                            //When 10 round have passed: the game enters an end-game state where it needs to display the correct result.
                            if (roundCounter >= maxRound) {
                                tekenBord.setStatusTekst("You lose. Click a circle to try again.");
                            if (roundCounter == maxRound + 1) {
                                tekenBord.setStatusTekst("You win! Click a circle to try again.");
                            }
                                //endgame display:
                                for (int i = 0; i < maxCircles; i++) {
                                    try {
                                        tekenBord.tekenCirkel(hiddenRandom.cirkelArrayList.get(i).getxCoordinate(), hiddenRandom.cirkelArrayList.get(i).getyCoordinate(), 250 / maxRound, hiddenRandom.cirkelArrayList.get(i).getPieceColor());
                                    } catch (OngeldigeCoordinaatException e) {
                                        // Zal niet gebeuren, we kennen zelf de coordinaat en deze is geldig.
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (OngeldigeCoordinaatException e) {
                        e.printStackTrace();
                    }
                });
    }

    //colorTestCounter counts the # of units given in the test row.
    private int colorTestCounter(Color choiceColor) {
        int Counter = 0;
        for (int i = 0; i < maxCircles; i++) {
            if (testRow.cirkelArrayList.get(i).getPieceColor() == choiceColor) {
                Counter += 1;
            }
        }
        return Counter;
    }
    //colorHiddenCounter counts the # of units given in the hidden row.
    private int colorHiddenCounter (Color choiceColor) {
        int Counter = 0;
        for (int i = 0; i < maxCircles; i++) {
            if (hiddenRandom.cirkelArrayList.get(i).getPieceColor() == choiceColor) {
                Counter += 1;
            }
        }
        return Counter;
    }
    //colorEliminate checks if the color has been evaluated before (Within the row evaluation).
    private boolean colorEliminate (Color choiceColor){
        if (colorEliminator.cirkelArrayList.isEmpty())
        {
            return true;
        }
        for (int i = 0; i < colorEliminator.cirkelArrayList.size(); i++){
            if (colorEliminator.cirkelArrayList.get(i).getPieceColor() == choiceColor){
                return false;
            }
        }
        return true;
    }
}