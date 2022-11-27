package summativetask1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplicationRunner {

    public static void main(String[] args) {
        ArrayList playersResults = new ArrayList(3);   //Array List for saving Players results

        //Running the game for 3 rounds
        for (int j = 1; j <= 3; j++) {
            ArrayList one_player = new ArrayList(3);   //Array List for saving one player results

            //Running for 3 users
            for (int i = 1; i <= 3; i++) {
                one_player.add(i - 1, startGame("" + i));
            }

            playersResults.add(j - 1, one_player);
            printTable(playersResults);
        }
        winer(playersResults);    //Checking who winns the game
    }

    /*
    Function for printing
    @params, 2 strings, string and int, or one string
    @void, print text
     */
    public static void soutPrint(String str, String param) {
        System.out.printf(str, param);
    }

    public static void soutPrint(String str, int param) {
        System.out.printf(str, param);
    }

    public static void soutPrint(String str) {
        System.out.printf(str);
    }

    public static void soutPrintDivider() {
        System.out.printf("\n------------------------------------------------------");
    }

    /* 
       Start Game for one player 
       @param, player string with player number
       @return, player score int
     */
    public static int startGame(String player) {
        int dicesNumber = 8, currentScore = 0, counter;
        int keptDices = 0, chosedDice, countUsedDices, howManyChosed;
        boolean isDiceUnused, isNumber, isCorrectDiceValue, notTooMany;
        boolean finishPlayerRound = true, continueGame;
        String enumerate;
        int[] usedDices = new int[0];

        Scanner scanner = new Scanner(System.in);

        soutPrintDivider();
        soutPrint("\nPlayer %2s", player);
        soutPrint("\nFirst throw of this turn, starting with %2d dice.", dicesNumber);

        //Start the player round
        do {
            soutPrint("\nEnter 't' to throw > ");
            String operation = scanner.next();

            if (operation.equals("t")) {
                int[] dices = roleDice(dicesNumber);

                if (usedDices.length != 0) {
                    soutPrint("\nYou have already set aside: ");       //Print already seted dices

                    for (int i = 0; i < usedDices.length; i++) {
                        soutPrint("[" + usedDices[i] + "]");
                    }

                    //Checking if player boosted
                    countUsedDices = 0;
                    for (int element : usedDices) {
                        if (Arrays.stream(dices).anyMatch(x -> x == element)) {
                            countUsedDices += 1;
                        }
                    }

                    if (countUsedDices == dices.length) {      //If player boosted, finish the player round
                        soutPrint("\nSorry, you have busted with that throw."
                                + "\nThis ends your turn with no score.");
                        soutPrint("\nFinal score for that turn for Player %2s = 0", player);
                        return 0;
                    }

                    soutPrint("\nYou must now select a different die value."
                            + "\nYou can now select one of the following: ");

                    //Fillter unused dices
                    int[] unicDicesArray = new int[8];
                    for (int i = 0; i < dices.length; i++) {
                        isDiceUnused = false;

                        for (int j = 0; j < usedDices.length; j++) {
                            if (dices[i] == usedDices[j]) {
                                isDiceUnused = true;
                            }
                        }
                        int currentDice = dices[i];
                        if (!isDiceUnused && !(Arrays.stream(unicDicesArray).anyMatch(x -> x == currentDice))) {
                            soutPrint("[" + dices[i] + "]");
                            unicDicesArray[i] = dices[i];
                        }
                    }
                }
                do {
                    try {
                        counter = 0;
                        isNumber = true;

                        do {
                            isCorrectDiceValue = true;

                            soutPrint("\nSelect die value to set aside > ");  //Select die value
                            chosedDice = scanner.nextInt();
                            int finalChosedDice = chosedDice;

                            for (int i = 0; i < dices.length; i++) {     //Count how many dices have that chosed value
                                if (dices[i] == chosedDice && !(Arrays.stream(usedDices).anyMatch(x -> x == finalChosedDice))) {
                                    counter++;
                                }
                            }

                            if (counter == 0) {
                                soutPrint("\nIncorrect dice!!!");
                                isCorrectDiceValue = false;
                            }
                        } while (!isCorrectDiceValue);

                        enumerate = "";                          //Enumerate how many values player can choose
                        for (int i = 0; i < counter; i++) {
                            if (i + 1 != counter) {
                                enumerate += (i + 1) + ", ";
                            } else {
                                enumerate += "or " + (i + 1);
                            }
                        }

                        //If there are more than 1 of chosed value
                        if (counter != 1) {
                            soutPrint("There are %1d dice that have that value", counter);
                            soutPrint("\nYou can choose to keep " + enumerate
                                    + " dice of value " + chosedDice);
                            do {
                                soutPrint("\nHow many do you want to set aside > ");

                                howManyChosed = scanner.nextInt();
                                if (howManyChosed <= counter && howManyChosed > 0) {
                                    notTooMany = false;
                                } else {
                                    notTooMany = true;
                                    soutPrint("\nIncorect amount!!!\n");
                                }
                            } while (notTooMany);

                        } else {
                            howManyChosed = 1;
                            soutPrint("Only one die has that value, setting aside "
                                    + "the one die with value " + chosedDice + "\n");
                        }

                        currentScore += howManyChosed * chosedDice;                     //Add to player score
                        soutPrint("Score so far = %3d", currentScore);         //How many dices player kept so far

                        keptDices += howManyChosed;
                        soutPrint("\nYou have kept " + keptDices + " dice so far.");

                        if (keptDices != 8) {                                            //If player not kept allready all the dices
                            do {
                                soutPrint("\nFinish turn or continue (enter f to "
                                        + "finish turn or c to continue and throw again) > ");
                                operation = scanner.next();

                                if (operation.equals("f")) {                      //If player chosed to finish the round
                                    soutPrint("\nFinal score for that turn for Player "
                                            + player + " = " + currentScore);
                                    finishPlayerRound = false;
                                    continueGame = false;
                                } else if (operation.equals("c")) {               //If Player chosed to continue
                                    dicesNumber -= howManyChosed;
                                    soutPrint("\nTaking " + dicesNumber + " dice forward to next throw.\nNext throw of this turn.");
                                    usedDices = Arrays.copyOf(usedDices, usedDices.length + 1);
                                    usedDices[usedDices.length - 1] = chosedDice;
                                    continueGame = false;
                                } else {
                                    soutPrint("\nIncorrect character!!!");
                                    continueGame = true;
                                }
                            } while (continueGame);

                        } else {
                            soutPrint("Final score for that turn for Player " + player + " = " + currentScore);
                            finishPlayerRound = false;
                        }

                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        isNumber = false;
                        soutPrint("\nNot a Number!!!");
                    }
                } while (!isNumber);
            }

        } while (finishPlayerRound);

        return currentScore;
    }

    /*
        Role dices function
        @param nrOfDices, int number of dices to throw
        @return int[], an array with sorted dices
     */
    public static int[] roleDice(int nrOfDices) {
        int[] dices = new int[nrOfDices];
        soutPrint("Throw: ");

        for (int i = 0; i < nrOfDices; i++) {                  //Generate the dices and print
            int random = (int) (Math.random() * 6 + 1);
            dices[i] = random;
            soutPrint("[" + dices[i] + "] ");
        }

        Arrays.sort(dices);                                 //Sort the dices and print
        soutPrint("\nSorted: ");

        for (int i = nrOfDices - 1; i >= 0; i--) {
            soutPrint("[" + dices[i] + "] ");
        }

        return dices;
    }

    /*
        Print the game table
        @param, userScore, ArrayList with users scores
        @void, print the score table
     */
    public static void printTable(ArrayList usersScore) {
        int total1 = 0, total2 = 0, total3 = 0;

        soutPrintDivider();
        soutPrint("\n|   Round   |   Player1   |   Player2   |   Player3  |");
        soutPrintDivider();

        //Print the rounds lines
        for (int i = 0; i < 3; i++) {
            if (usersScore.size() > i) {
                ArrayList round = (ArrayList) usersScore.get(i);
                total1 += (int) round.get(0);
                total2 += (int) round.get(1);
                total3 += (int) round.get(2);
                printLine(i, round);
            } else {
                soutPrint("\n|     " + (i + 1) + "     |     ---     |     ---     |     ---    |");
            }
            soutPrintDivider();
        }

        //Print the total score
        System.out.printf("\n|   Total   |     %-3s     |     %-3s     |     %-3s    |",
                total1, total2, total3);

        soutPrintDivider();

    }

    /*
        Print one table line
        @params, roundNumber - int, round number and roundScores - ArrayList, round scores
        @void, print a table line
     */
    public static void printLine(int roundNumber, ArrayList roundScores) {
        System.out.printf("\n|     %s     |     %-3s     |     %-3s     |     %-3s    |", roundNumber + 1,
                roundScores.get(0), roundScores.get(1), roundScores.get(2));
    }

    /*
        Find who is winner 
        @param, playersResults - ArrayList with players results
        @void, print who is winner
     */
    public static void winer(ArrayList playersResults) {
        int[] scoresTotal = {0, 0, 0};

        //Calculate the players total score
        for (int i = 0; i < 3; i++) {
            for (int s = 0; s < 3; s++) {
                ArrayList score = (ArrayList) playersResults.get(s);
                scoresTotal[i] += (int) score.get(i);
            }
        }

        //Find who is winner
        if (scoresTotal[0] > scoresTotal[1] && scoresTotal[0] > scoresTotal[2]) {
            soutPrint("\nPlayer 1 wins that game.!");
        } else if (scoresTotal[1] > scoresTotal[0] && scoresTotal[1] > scoresTotal[2]) {
            soutPrint("\nPlayer 2 wins that game.!");
        } else {
            soutPrint("\nPlayer 3 wins that game.!");
        }
    }
}
