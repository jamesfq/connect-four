import java.awt.*;
import java.util.Scanner;

/**
 * Driver for the Graphics and the GUI if the user chooses to use their own parameters.
 *
 * @author James Quirk, Dartmouth DALI Developer Challenge! Spring 2023
 */

public class ConnectDriver extends ConnectGUI {
    public static void main(String[] args) {
        // Read from user for parameter choices
        Scanner scanner = new Scanner(System.in);
        String input;
        int row;
        boolean goodRow;
        int column;
        boolean goodColumn;
        Color p1Color;
        Color p2Color;
        Color boardColor;

        // Introduce the user to the interface
        System.out.println("Welcome to Connect Four! Would you like to play with our SPACE settings or make your own?");
        System.out.println("Type \"SPACE\" to play with space settings. Type \"CUSTOM\" to play with your own settings. Type \"QUIT\" to leave.");

        // if the user doesn't type quit, space, or custom, keep them here
        while (!(input = scanner.nextLine()).equals("QUIT") && !(input).equals("SPACE") && !(input.equals("CUSTOM"))) {
            System.out.println("ERROR. Remember, your commands are \"SPACE\" to play with space settings, \"CUSTOM\" to play with your own settings, and \"QUIT\" to leave.");
        }

        // if they choose space, play Space Mode
        if (input.equals("SPACE")) {
            System.out.println("Everything's initialized! Click the 'q' character to quit.");
            new ConnectGUI(); // open the Graphics window
        }

        // if they choose custom, allow them to input their own parameters
        else if (input.equals("CUSTOM")) {
            System.out.println("Input the number of rows you want in your game (between 4 and 13):");

            // will be updated to true when we find a row between 4 and 14
            goodRow = false;

            // sets number of rows to 0
            row = 0;

            // if the user doesn't provide a valid row, keep them here
            while (!goodRow) {
                input = scanner.nextLine();

                // if the user input can be turned into an integer, continue
                try {
                    row = Integer.parseInt(input);

                    // if the integer is outside the specified range, give the user an error
                    if (row < 4 || row >= 14) {
                        System.out.println("We recommend you select a number of rows between 4 and 13.");
                    }

                    // if inside the specified range, allow user to move to next step
                    else {
                        goodRow = true;
                    }
                }

                // otherwise, notify them that their input was invalid
                catch (NumberFormatException e) {
                    System.out.println("ERROR. User input must be an integer.");
                    System.out.println("We recommend you select a number of rows between 4 and 13.");
                }

            }

            System.out.println("Good! You have chosen to have " + row + " rows.");

            // move on to selecting columns and follow the same process
            System.out.println("Input the number of columns you want in your game (between 4 and 14):");

            goodColumn = false;
            column = 0;

            while (!goodColumn) {
                input = scanner.nextLine();
                try {
                    column = Integer.parseInt(input);
                    if (column < 4 || column >= 15) {
                        System.out.println("We recommend you select a number of rows between 4 and 14.");
                    }

                    else {
                        goodColumn = true;
                    }
                }

                catch (NumberFormatException e) {
                    System.out.println("ERROR. User input must be an integer.");
                    System.out.println("We recommend you select a number of rows between 4 and 14.");
                }

            }

            System.out.println("Good! You have chosen to have " + column + " columns.");

            // move onto selecting the color for Player 1
            System.out.println("Choose the color you want for player one (RED or MAGENTA):");
            p1Color = Color.BLACK;

            // follow similar structure to choosing number of rows and number of columns
            while (!(input = scanner.nextLine()).equals("RED") && !(input).equals("MAGENTA")) {
                System.out.println("Choose the color you want for player one (RED or MAGENTA):");

            }

            if (input.equals("RED")) {
                p1Color = Color.RED;
                System.out.println("Player 1's color has been set to RED.");
            }

            else {
                p1Color = Color.MAGENTA;
                System.out.println("Player 1's color has been set to MAGENTA.");
            }

            // repeat the process for Player 2
            System.out.println("Choose the color you want for player two (YELLOW or CYAN):");
            p2Color = Color.BLACK;

            while (!(input = scanner.nextLine()).equals("YELLOW") && !(input).equals("CYAN")) {
                System.out.println("Choose the color you want for player two (YELLOW or CYAN):");

            }

            if (input.equals("YELLOW")) {
                p2Color = Color.YELLOW;
                System.out.println("Player 2's color has been set to YELLOW.");
            }

            else {
                p2Color = Color.CYAN;
                System.out.println("Player 2's color has been set to CYAN.");
            }

            // repeat the process for the board itself
            System.out.println("Choose the color you want for the board (BLUE or BLACK):");
            boardColor = Color.BLACK;

            while (!(input = scanner.nextLine()).equals("BLUE") && !(input).equals("BLACK")) {
                System.out.println("Choose the color you want for the board (BLUE or BLACK):");

            }

            if (input.equals("BLUE")) {
                boardColor = Color.BLUE;
                System.out.println("The board's color has been set to BLUE.");
            }

            else {
                boardColor = Color.BLACK;
                System.out.println("The board's color has been set to BLACK.");
            }

            System.out.println("Everything's initialized! Click the 'q' character to quit.");

            // after identifying every custom variable, start the graphics window
            new ConnectGUI(row, column, p1Color, p2Color, boardColor);

        }
    }
}
