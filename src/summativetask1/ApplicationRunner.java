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
                one_player.add(i - 1, playGame("" + i));
            }

            playersResults.add(j - 1, one_player);
            printTable(playersResults);
        }
        whoIsWiner(playersResults);    //Checking who winns the game
    }

    
    /* 
       Start Game for one player 
       @param, player string with player number
       @return, player score int
     */
    public static int playGame(String player) {
        int dicesNumber = 8, currentScore = 0, counter,
                keptDices = 0, chosedDice, howManyChosed;
        boolean isNumber, isCorrectDiceValue,
                finishPlayerRound = true, continueGame;
        int[] usedDices = new int[0];

        Scanner scanner = new Scanner(System.in);

        soutPrintDivider();
        soutPrint("\nPlayer " + player);
        soutPrint("\nFirst throw of this turn, starting with " + dicesNumber + " dices.");

        //Start the player round
        do {
            soutPrint("\nEnter 't' to throw > ");
            String operation = scanner.next();

            if (operation.equals("t")) {
                int[] dices = roleDice(dicesNumber);

                if (usedDices.length != 0) {
                    soutPrint("\nYou have already set aside: ");

                    //Print already seted dices
                    for (int i = 0; i < usedDices.length; i++) {
                        soutPrint("[" + usedDices[i] + "]");
                    }

                    //Checking if player boosted
                    if (isBoostead(usedDices, dices, player)) {
                        return 0;
                    }

                    soutPrint("\nYou must now select a different die value."
                            + "\nYou can now select one of the following: ");

                    //Print unused dices
                    printUnusedDices(dices, usedDices);
                }

                do {
                    try {
                        counter = 0;
                        isNumber = true;

                        do {
                            isCorrectDiceValue = true;

                            soutPrint("\nSelect die value to set aside > ");  //Select die value
                            chosedDice = scanner.nextInt();

                            //Count how many dices have that chosed value
                            counter = countDices(chosedDice, dices, usedDices);

                            if (counter == 0) {
                                soutPrintIncorrect();
                                isCorrectDiceValue = false;
                            }
                        } while (!isCorrectDiceValue);

                        //Print options and scann how many dices to take aside
                        howManyChosed = selectDicesAmount(counter, chosedDice);

                        currentScore += howManyChosed * chosedDice;                     //Add to player score
                        soutPrint("Score so far = " + currentScore);                    //How many dices player kept so far

                        keptDices += howManyChosed;
                        soutPrint("\nYou have kept " + keptDices + " dice so far.");

                        if (keptDices != 8) {
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
                                    soutPrintIncorrect();
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
                        soutPrintIncorrect();
                    }
                } while (!isNumber);
            } else {
                soutPrintIncorrect();
            }

        } while (finishPlayerRound);

        return currentScore;
    }

    /*
        Functions for printing
        @param, one string, or nothing
        @void, print text
     */
    public static void soutPrint(String str) {
        System.out.printf(str);
    }

    public static void soutPrintIncorrect() {
        System.out.printf("\nIncorect character/number!!!\n");
    }

    public static void soutPrintDivider() {
        System.out.printf("\n------------------------------------------------------");
    }

    /*
        Enumerate how many values player can choose
        @param, counter, how many to enumerate
        @return String with enumerated list
     */
    public static String enumerateToString(int counter) {

        String enumerate = "";
        for (int i = 0; i < counter; i++) {
            if (i + 1 != counter) {
                enumerate += (i + 1) + ", ";
            } else {
                enumerate += "or " + (i + 1);
            }
        }
        return enumerate;
    }

    /*
        Checking if player boosted
        @params, useedDices - used dices, dices in game, player number
        @return and print boolean, if player boostead or not
     */
    public static boolean isBoostead(int[] usedDices, int[] dices, String player) {

        int countUsedDices = 0;
        for (int element : usedDices) {
            if (Arrays.stream(dices).anyMatch(x -> x == element)) {
                countUsedDices += 1;
            }
        }

        if (countUsedDices == dices.length) {      //If player boosted, finish the player round
            soutPrint("\nSorry, you have busted with that throw."
                    + "\nThis ends your turn with no score.");
            soutPrint("\nFinal score for that turn for Player " + player + " = 0");
            return true;
        }
        return false;
    }

    /*
        Print unused dices
        @params, int[] dices- dices in game, and int[] usedDices - already used dices
        @void, print the list of unused dices
     */
    public static void printUnusedDices(int[] dices, int[] usedDices) {
        boolean isDiceUnused;
        int[] unicDicesArr = new int[8];

        for (int i = 0; i < dices.length; i++) {
            isDiceUnused = false;

            for (int j = 0; j < usedDices.length; j++) {
                if (dices[i] == usedDices[j]) {
                    isDiceUnused = true;
                }
            }
            int currentDice = dices[i];
            if (!isDiceUnused && !(Arrays.stream(unicDicesArr).anyMatch(x -> x == currentDice))) {
                soutPrint("[" + dices[i] + "]");
                unicDicesArr[i] = dices[i];
            }
        }

    }

    /*
        Count how many dices of chosed value are in game
        @params,  int chosedDice - chosed dice value, int[] dices- dices in game, and int[] usedDices - already used dices
        @return int counter
     */
    public static int countDices(int chosedDice, int[] dices, int[] usedDices) {
        int counter = 0;
        int finalChosedDice = chosedDice;

        for (int i = 0; i < dices.length; i++) {     //Count how many dices have that chosed value
            if (dices[i] == chosedDice && !(Arrays.stream(usedDices).anyMatch(x -> x == finalChosedDice))) {
                counter++;
            }
        }

        return counter;
    }

    /*
        Select how many dices of chosed value to set aside
        @params, int counter - how many dices of chosed value are in game, int chosedDice - chosed dice value
        @return int, how many dices to set aside
     */
    public static int selectDicesAmount(int counter, int chosedDice) {
        int howManyChosed;
        boolean notTooMany;

        Scanner scanner = new Scanner(System.in);

        //Enumerate how many values player can choose
        String enumerate = enumerateToString(counter);

        if (counter != 1) {
            soutPrint("There are " + counter + " dice that have that value");
            soutPrint("\nYou can choose to keep " + enumerate
                    + " dice of value " + chosedDice);
            do {
                soutPrint("\nHow many do you want to set aside > ");

                howManyChosed = scanner.nextInt();
                if (howManyChosed <= counter && howManyChosed > 0) {
                    notTooMany = false;
                } else {
                    notTooMany = true;
                    soutPrintIncorrect();
                }
            } while (notTooMany);

        } else {
            howManyChosed = 1;
            soutPrint("Only one die has that value, setting aside "
                    + "the one die with value " + chosedDice + "\n");
        }
        return howManyChosed;
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
    public static void whoIsWiner(ArrayList playersResults) {
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
            soutPrint("\nPlayer 1 wins that game.!\n");
        } else if (scoresTotal[1] > scoresTotal[0] && scoresTotal[1] > scoresTotal[2]) {
            soutPrint("\nPlayer 2 wins that game.!\n");
        } else {
            soutPrint("\nPlayer 3 wins that game.!\n");
        }
    }
}
