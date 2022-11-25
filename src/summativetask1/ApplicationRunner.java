package summativetask1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplicationRunner {

    /* 
    The Main function of the game
    Where I am runnuing the game for 3 users and 3 rounds
     */
    public static void main(String[] args) {
        ArrayList playersResults = new ArrayList(3);   //Array List for saving Players results

        //Running the game for 3 users
        for (int j = 1; j <= 3; j++) {
            ArrayList one_player = new ArrayList(3);   //Array List for saving one player results

            //Running 3 rounds
            for (int i = 1; i <= 3; i++) {
                one_player.add(i - 1, startGame("" + i));
            }

            playersResults.add(j - 1, one_player);
            printTable(playersResults);
        }
        winer(playersResults);    //Checking who winns the game
    }

    /* 
       Start Game for one player 
       It takes a parameter, string with player number
       Returns an int, player score
     */
    public static int startGame(String player) {
        int dicesNr = 8, score = 0, counter;
        int keptDices = 0, chosedDice, countSelectedDices = 0, howMany;
        boolean isDiceUnused, isNumber, isCorrectDiceValue, notTooMany;
        boolean finishPlayerRound = true, continueGame;
        String enumerate;
        int[] usedDices = new int[0];
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n------------------------------------------------------");
        System.out.println("Player " + player);
        System.out.println("First throw of this turn, starting with " + dicesNr + " dice.");

        //Start the player round
        do {
            System.out.print("Enter 't' to throw > ");
            String operation = scanner.next();

            if (operation.equals("t")) {
                int[] dices = roleDice(dicesNr);

                if (usedDices.length != 0) {
                    //Print already seted dices
                    System.out.print("\nYou have already set aside: ");

                    for (int i = 0; i < usedDices.length; i++) {
                        System.out.print("[" + usedDices[i] + "]");
                    }
                    System.out.print("\n");

                    //Checking if player boosted
                    for (int i = 0; i < dices.length; i++) {
                        for (int element : usedDices) {
                            if (element == dices[i]) {
                                countSelectedDices += 1;
                            }
                        }
                    }

                    //If player boosted, finish the player round
                    if (countSelectedDices == dices.length) {
                        System.out.println("Sorry, you have busted with that throw.");
                        System.out.println("This ends your turn with no score.");
                        System.out.println("Final score for that turn for Player " + player + " = 0");
                        return 0;
                    }

                    System.out.println("You must now select a different die value");
                    System.out.print("You can now select one of the following: ");

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
                            System.out.print("[" + dices[i] + "]");
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

                            //Select die value
                            System.out.print("\nSelect die value to set aside > ");
                            chosedDice = scanner.nextInt();

                            //Count how many dices have that chosed value
                            for (int i = 0; i < dices.length; i++) {
                                if (dices[i] == chosedDice) {
                                    counter++;
                                }
                            }

                            if (counter == 0) {
                                System.out.println("Incorrect dice!!!");
                                isCorrectDiceValue = false;
                            }
                        } while (!isCorrectDiceValue);

                        System.out.println("There are " + counter + " dice that have that value");

                        //Enumerate how many values player can choose
                        enumerate = "";
                        for (int i = 0; i < counter; i++) {
                            if (i + 1 != counter) {
                                enumerate += (i + 1) + ", ";
                            } else {
                                enumerate += "or " + (i + 1);
                            }
                        }

                        //If it there are more than 1 of chosed value
                        howMany = 1;

                        if (counter != 1) {
                            System.out.println("You can choose to keep " + enumerate + " dice of value " + chosedDice);

                            do {
                                System.out.print("How many do you want to set aside > ");
                                howMany = scanner.nextInt();
                                if (howMany <= counter && howMany > 0) {
                                    notTooMany = false;
                                } else {
                                    notTooMany = true;
                                    System.out.println("Incorect amount!!!");
                                }
                            } while (notTooMany);

                        } else {
                            System.out.println("Only one die has that value, setting aside the one die with value " + chosedDice);
                        }

                        //Add to player score
                        score += howMany * chosedDice;
                        System.out.println("Score so far = " + score);
                        //How many dices player kept so far
                        keptDices += counter;
                        System.out.println("You have kept " + keptDices + " dice so far.");

                        //If player not kept allready all the dices
                        if (keptDices != 8) {
                            do {
                                System.out.print("Finish turn or continue (enter f to finish turn or c to continue and throw again) > ");
                                operation = scanner.next();

                                //If player chosed to finish the round
                                if (operation.equals("f")) {
                                    System.out.println("Final score for that turn for Player " + player + " = " + score);
                                    finishPlayerRound = false;
                                    continueGame = false;
                                } else if (operation.equals("c")) {
                                    //If Player chosed to continue
                                    dicesNr -= howMany;
                                    System.out.println("Taking " + dicesNr + " dice forward to next throw.\nNext throw of this turn.");
                                    usedDices = Arrays.copyOf(usedDices, usedDices.length + 1);
                                    usedDices[usedDices.length - 1] = chosedDice;
                                    continueGame = false;
                                } else {
                                    System.out.println("Incorrect character!!!");
                                    continueGame = true;

                                }
                            } while (continueGame);

                        } else {
                            System.out.println("Final score for that turn for Player " + player + " = " + score);
                            finishPlayerRound = false;
                        }

                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        isNumber = false;
                        System.out.println("Not a Number!!!");
                    }
                } while (!isNumber);
            }

        } while (finishPlayerRound);

        return score;
    }

    /*
        Role dices function
        It takes one parameter, int number of dices to throw
        return an array with sorted dices
     */
    public static int[] roleDice(int nrOfDices) {
        int[] dices = new int[nrOfDices];
        System.out.print("Throw: ");

        //Generate the dices and print
        for (int i = 0; i < nrOfDices; i++) {
            int random = (int) (Math.random() * 6 + 1);
            dices[i] = random;
            System.out.print("[" + dices[i] + "] ");
        }
        //Sort the dices and print
        Arrays.sort(dices);
        System.out.print("\nSorted: ");

        for (int i = nrOfDices - 1; i >= 0; i--) {
            System.out.print("[" + dices[i] + "] ");
        }

        return dices;
    }

    /*
        Print the game table
        It takes one parameter, ArrayList with users scores
     */
    public static void printTable(ArrayList usersScore) {
        int total1 = 0, total2 = 0, total3 = 0;

        System.out.println("------------------------------------------------------");
        System.out.println("|   Round   |   Player1   |   Player2   |   Player3  |");
        System.out.println("------------------------------------------------------");

        //Print the rounds lines
        for (int i = 0; i < 3; i++) {
            if (usersScore.size() > i) {
                ArrayList round = (ArrayList) usersScore.get(i);
                total1 += (int) round.get(0);
                total2 += (int) round.get(1);
                total3 += (int) round.get(2);
                printLine(i, round);
            } else {
                System.out.println("|     " + (i + 1) + "     |     ---     |     ---     |     ---    |");
            }
            System.out.println("------------------------------------------------------");
        }

        //Print the total score
        System.out.printf("|   Total   |     %-3s     |     %-3s     |     %-3s    |\n",
                total1, total2, total3);

        System.out.println("------------------------------------------------------");

    }

    /*
        Print one table line
        It takes two parameters, round number and round scores
     */
    public static void printLine(int nr, ArrayList roundScores) {
        System.out.printf("|     %s     |     %-3s     |     %-3s     |     %-3s    |\n", nr + 1,
                roundScores.get(0), roundScores.get(1), roundScores.get(2));
    }

    /*
        Find who is winner 
        It takes a parameter, ArrayList with players results
        And print who is winner
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
            System.out.println("Player 1 wins that game.!");
        } else if (scoresTotal[1] > scoresTotal[0] && scoresTotal[1] > scoresTotal[2]) {
            System.out.println("Player 2 wins that game.!");
        } else {
            System.out.println("Player 3 wins that game.!");
        }
    }
}
