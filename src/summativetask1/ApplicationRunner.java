package summativetask1;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.IntStream;

public class ApplicationRunner {

    public static void main(String[] args) {
        Vector playersResults = new Vector(3);

        for (int j = 1; j <= 3; j++) {
            Vector one_player = new Vector(3);
            for (int i = 1; i <= 3; i++) {
                one_player.add(i - 1, startGame("" + i));
            }
            playersResults.add(j - 1, one_player);
            printTable(playersResults);
        }

        winer(playersResults);
    }

    public static int startGame(String player) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--------------------------------------------");
        System.out.println("Player " + player);

        int dicesNr = 8;
        int score = 0;
        int[] usedDices = new int[0];
        boolean stop = true;
        int keptDices = 0;

        System.out.println("First throw of this turn, starting with " + dicesNr + " dice.");

        do {
            System.out.print("Enter 't' to throw > ");
            String operation = scanner.nextLine();

            if (operation.equals("t")) {
                int[] dices = roleDice(dicesNr);

                if (usedDices.length != 0) {
                    System.out.print("\nYou have already set aside: ");

                    for (int i = 0; i < usedDices.length; i++) {
                        System.out.print("[" + usedDices[i] + "]");
                    }
                    System.out.print("\n");

                    //Checb if player boosts
                    int inGame = 0;

                    for (int i = 0; i < dices.length; i++) {
                        for (int element : usedDices) {
                            if (element == dices[i]) {
                                inGame += 1;
                            }
                        }
                    }
                    //If player boosted, finish the player round
                    if (inGame == dices.length) {
                        System.out.println("Sorry, you have busted with that throw.");
                        System.out.println("This ends your turn with no score.");
                        System.out.println("Final score for that turn for Player " + player + " = 0");
                        return 0;
                    }

                    System.out.println("You must now select a different die value");
                    System.out.print("You can now select one of the following: ");

                    //Fillter unused dices
                    boolean exist;
                    for (int i = 0; i < dices.length; i++) {
                        exist = false;
                        for (int j = 0; j < usedDices.length; j++) {
                            if (dices[i] == usedDices[j]) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            System.out.print("[" + dices[i] + "]");
                        }

                    }
                }

                //Select die value
                System.out.print("\nSelect die value to set aside > ");
                int chosedDice = scanner.nextInt();

                int counter = 0;

                for (int i = 0; i < dices.length; i++) {
                    if (dices[i] == chosedDice) {
                        counter++;
                    }
                }

                System.out.println("There are " + counter + " dice that have that value");

                String enumerate = "";
                for (int i = 0; i < counter; i++) {
                    if (i + 1 != counter) {
                        enumerate += (i + 1) + ", ";
                    } else {
                        enumerate += "or " + (i + 1);
                    }
                }
                int howMany = 1;
                if (counter != 1) {
                    System.out.println("You can choose to keep " + enumerate + " dice of value " + chosedDice);
                    System.out.print("How many do you want to set aside > ");
                    howMany = scanner.nextInt();
                } else {
                    System.out.println("Only one die has that value, setting aside the one die with value " + chosedDice);
                }

                score += howMany * chosedDice;
                keptDices += counter;
                System.out.println("Score so far = " + score);
                System.out.println("You have kept " + keptDices + " dice so far.");

                if (keptDices != 8) {
                    System.out.print("Finish turn or continue (enter f to finish turn or c to continue and throw again) > ");
                    scanner.nextLine();
                    operation = scanner.nextLine();
                    
                    if (operation.equals("f")) {
                        System.out.println("Final score for that turn for Player " + player + " = " + score);
                        stop = false;
                    } else {
                        dicesNr -= howMany;
                        System.out.println("Taking " + dicesNr + " dice forward to next throw.\nNext throw of this turn.");

                        usedDices = Arrays.copyOf(usedDices, usedDices.length + 1);
                        usedDices[usedDices.length - 1] = chosedDice;

                    }

                } else {
                    System.out.println("Final score for that turn for Player " + player + " = " + score);
                    stop = false;
                }

            }

        } while (stop);
        return score;

    }

    public static int[] roleDice(int nr) {
        int[] dices = new int[nr];
        System.out.print("Throw: ");

        for (int i = 0; i < nr; i++) {
            int random = (int) (Math.random() * 6 + 1);
            dices[i] = random;
            System.out.print("[" + dices[i] + "] ");
        }

        Arrays.sort(dices);
        System.out.print("\nSorted: ");

        for (int i = nr - 1; i >= 0; i--) {
            System.out.print("[" + dices[i] + "] ");
        }

        return dices;
    }

    public static void printTable(Vector usersScore) {
        int total1 = 0, total2 = 0, total3 = 0;

        System.out.println("------------------------------------------------------");
        System.out.println("|   Round   |   Player1   |   Player2   |   Player3  |");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < 3; i++) {
            if (usersScore.size() > i) {
                Vector round = (Vector) usersScore.get(i);
                total1 += (int) round.get(0);
                total2 += (int) round.get(1);
                total3 += (int) round.get(2);
                printLine(i, round);
            } else {
                System.out.println("|     " + (i + 1) + "     |     --      |     --      |     --     |");
            }
            System.out.println("------------------------------------------------------");
        }

        System.out.println("|   Total   |     " + ((total1 > 9) ? total1 : " " + total1) + "      |     " + ((total2 > 9) ? total2 : " " + total2) + "      |     " + ((total3 > 9) ? total3 : " " + total3) + "     |");
        System.out.println("------------------------------------------------------");

    }

    public static void printLine(int nr, Vector round) {
        System.out.println("|     " + (nr + 1) + "     |     "
                + (((int) (round.get(0)) > 9) ? round.get(0) : " " + round.get(0)) + "      |     "
                + (((int) (round.get(1)) > 9) ? round.get(1) : " " + round.get(1)) + "      |     "
                + (((int) (round.get(2)) > 9) ? round.get(2) : " " + round.get(2)) + "     |");
    }

    public static void winer(Vector playersResults) {
        int[] scoresTotal = {0, 0, 0};

        for (int i = 0; i < 3; i++) {
            for (int s = 0; s < 3; s++) {
                Vector score = (Vector) playersResults.get(s);
                scoresTotal[i] += (int) score.get(i);
            }
        }
        if (scoresTotal[0] > scoresTotal[1] && scoresTotal[0] > scoresTotal[2]) {
            System.out.println("Player 1 wins that game.!");
        } else if (scoresTotal[1] > scoresTotal[0] && scoresTotal[1] > scoresTotal[2]) {
            System.out.println("Player 2 wins that game.!");
        } else {
            System.out.println("Player 3 wins that game.!");
        }

    }
}
