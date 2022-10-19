
package summativetask1;

import java.util.Arrays;
import java.util.Scanner;



public class ApplicationRunner {

    public static void main(String[] args) {
        startGame("1");

    }
    
    
    
    
    
    public static void startGame(String player) {
         Scanner scanner = new Scanner(System.in);
         System.out.println("Player " + player);
         
         int dicesNr = 8;
         int score = 0;
         int[] usedDices = new int[6];
         boolean stop = true;
         int keptDices  = 0;
         
         System.out.println("First throw of this turn, starting with "+ dicesNr + " dice.");
         
         do {
            System.out.println("Enter 't' to throw > ");
            String operation = scanner.nextLine();

            if(operation.equals("t") ) {
               int[] dices = roleDice(dicesNr);

               System.out.println("Select die value to set aside > ");
               int chosedDice = scanner.nextInt();
               int counter = 0;

               for(int i = 0; i < dices.length; i++) {
                  if(dices[i]==chosedDice) {
                      counter++;
                  }
               }
               
               System.out.println("There are " + counter + " dice that have that value");

               String enumerate = "";
               for(int i = 0; i < counter; i++) {
                   if(i+1 != counter) enumerate += (i+1) + ", ";
                   else enumerate += "or " + (i+1);
               }

               System.out.println("You can choose to keep " + enumerate + " dice of value "+ chosedDice);
               System.out.println("How many do you want to set aside > ");
               int howMany = scanner.nextInt();

               score += howMany * chosedDice;
               keptDices += counter;
               System.out.println("Score so far = "+ score);
               System.out.println("You have kept " + keptDices +" dice so far.");
               
               System.out.println("Finish turn or continue (enter f to finish turn or c to continue and throw again) > ");
               scanner.nextLine();
               operation = scanner.nextLine();
            

               if(operation.equals("f")) {
                   System.out.println("Final score for that turn for Player 1 = " + score);
                    stop = false;
               } else {
                   System.out.println("Taking 5 dice forward to next throw.\nNext throw of this turn.");
                    dicesNr -= howMany;
                   
               }
            
            } 

         }while(stop);
       
         
        
    }
    
    
    
    
    
    public static int[] roleDice(int nr) {
        int[] dices = new int[nr];
        System.out.print("Throw: ");
        
        for(int i = 0; i < nr; i++) {
            int random = (int) (Math.random() * 6 + 1);
            dices[i] = random;
            System.out.print("["+ dices[i] + "] ");
        }
        System.out.println("\n");

        Arrays.sort(dices);
        System.out.print("Sorted: ");
        
        for(int i = nr-1; i >= 0; i--) {
            System.out.print("["+ dices[i] + "] ");
        }
        System.out.println("\n");
        
        return dices;
    }
    
    
    
    
    
    
}
