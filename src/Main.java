import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void print(char[][] matrix) {
        //print a 2d matrix in an intelligible way
        for (char[] r : matrix) {
            for (char c : r) {
                System.out.print(c);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void right(char[][] matrix, boolean[][] taken, String word) {
        //based on the matrix size and the word size, find a range of valid spots in the matrix the word could be placed
        int maxRow = matrix.length;
        int maxColumn = matrix[0].length - word.length() + 1;

        int randomRow = (int) Math.floor(Math.random()*maxRow);
        int randomColumn = (int) Math.floor(Math.random()*maxColumn);

        //the row and column where the word begins
        Tuple origin = new Tuple(randomRow,randomColumn);

        //loop through the word once to check if the spots it occupies are taken by other words,
        // if it is, just run function again
        for (int i = 0; i < word.length(); i++) {
            if (taken[origin.getRow()][origin.getColumn() + i]) {
                right(matrix, taken, word);
                return;
            }
        }

        //change appropriate values in matrix, moving to the right
        for (int i = 0; i < word.length(); i++) {
            matrix[origin.getRow()][origin.getColumn() + i] = word.charAt(i);
            taken[origin.getRow()][origin.getColumn() + i] = true;
        }
    }

    public static void down(char[][] matrix, boolean[][] taken, String word) {
        //based on the matrix size and the word size, find a range of valid spots in the matrix the word could be placed
        int maxRow = matrix.length - word.length() + 1;
        int maxColumn = matrix[0].length;

        int randomRow = (int) Math.floor(Math.random()*maxRow);
        int randomColumn = (int) Math.floor(Math.random()*maxColumn);

        //the row and column where the word begins
        Tuple origin = new Tuple(randomRow,randomColumn);

        //loop through the word once to check if the spots it occupies are taken by other words,
        // if it is, just run function again
        for (int i = 0; i < word.length(); i++) {
            if (taken[origin.getRow() + i][origin.getColumn()]) {
                System.out.println("taken");
                down(matrix, taken, word);
                return;
            }
        }

        //change appropriate values in matrix, moving down
        for (int i = 0; i < word.length(); i++) {
            matrix[origin.getRow() + i][origin.getColumn()] = word.charAt(i);
            taken[origin.getRow() + i][origin.getColumn()] = true;
        }
    }

    public static void diagonal(char[][] matrix, boolean[][] taken, String word) {
        //based on the matrix size and the word size, find a range of valid spots in the matrix the word could be placed
        int maxRow = matrix.length - word.length() + 1;
        int maxColumn = matrix[0].length - word.length() + 1;

        int randomRow = (int) Math.floor(Math.random()*maxRow);
        int randomColumn = (int) Math.floor(Math.random()*maxColumn);

        //the row and column where the word begins
        Tuple origin = new Tuple(randomRow,randomColumn);

        //loop through the word once to check if the spots it occupies are taken by other words,
        // if it is, just run function again
        for (int i = 0; i < word.length(); i++) {
            if (taken[origin.getRow() + i][origin.getColumn() + i]) {
                System.out.println("taken");
                diagonal(matrix, taken, word);
                return;
            }
        }

        //change appropriate values in matrix, moving diagonal
        for (int i = 0; i < word.length(); i++) {
            matrix[origin.getRow() + i][origin.getColumn() + i] = word.charAt(i);
            taken[origin.getRow() + i][origin.getColumn() + i] = true;
        }
    }

    public static void fillRandom(char[][] matrix, boolean[][] taken, char[] letters) {
        //fill the matrix with random characters where there are no user inputted words
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                if (!taken[r][c]) matrix[r][c] = letters[(int) Math.floor(Math.random()*letters.length)];
            }
        }
    }

    public static String guessWord(char[][] matrix) {
        Scanner scanner = new Scanner(System.in);
        Tuple start, end;

        //if any errors with user input, return "/invalid".
        try {
            System.out.print("Start(r,c): ");
            String[] input1 = scanner.nextLine().replaceAll(" ","").split(",");

            System.out.print("End(r,c): ");
            String[] input2 = scanner.nextLine().replaceAll(" ","").split(",");

            start = new Tuple(Integer.parseInt(input1[0]), Integer.parseInt(input1[1]));
            end = new Tuple(Integer.parseInt(input2[0]), Integer.parseInt(input2[1]));

        } catch (Exception ignored) {
            return "/invalid";
        }

        //exceptions (ex. if start position is to the right of end)
        if (start.getRow() == end.getRow() && start.getColumn() == end.getColumn()) {
            return "/invalid";
        }
        if (end.getRow() < start.getRow()) {
            return "/invalid";
        }
        if (end.getColumn() < start.getColumn()) {
            return "/invalid";
        }

        StringBuilder guess = new StringBuilder();

        //based on rows and columns of user inputted start and end, append the characters into a guess
        try {
            //down
            if (start.getColumn() == end.getColumn()) {
                for (int i = start.getRow(); i <= end.getRow(); i++) {
                    guess.append(matrix[i][start.getColumn()]);
                }
            }
            //right
            else if (start.getRow() == end.getRow()) {
                for (int i = start.getColumn(); i <= end.getColumn(); i++) {
                    guess.append(matrix[start.getRow()][i]);
                }
            }
            //diagonal
            else {
                int r = start.getRow();
                int c = start.getColumn();
                while (true) {
                    guess.append(matrix[r][c]);

                    if (r == matrix.length - 1 || c == matrix[0].length - 1) break;
                    if (r == end.getRow() && c == end.getColumn()) break;

                    r++; c++;
                }
            }
        } catch (Exception ignored) {return "/invalid";}

        //return a guess
        return guess.toString();
    }


    public static void main(String[] args) {
        //1. randomly place user inputted words into an empty 2d array "matrix" making sure not to overlap words by checking "taken."
        //2. place random characters in all the spaces that aren't occupied by those user inputted words.
        //3. guess by typing in the row and column of where the word starts, and the row and column of where the word ends.
        // making sure start -> end go from either left to right or up to down.


        char[] letters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
                'o','p','q','r','s','t','u','v','w','x','y','z'};

        //dimensions of word search board
        int width = 10;
        int height = 10;

        //true items in array have words in them, false do not
        boolean[][] taken = new boolean[height][width];
        //matrix of random characters and user inputted characters, what is displayed
        char[][] matrix = new char[height][width];

        Scanner scanner = new Scanner(System.in);
        //user inputted words
        List<String> words;
        //for loop
        boolean valid;

        //loop asking for user inputted words
        do {
            valid = true;

            System.out.print("Type words to be guessed, separated by commas: ");

            words = Arrays.asList(scanner.nextLine().replaceAll(" ", "").split(","));

            //check exceptions, of user input
            for (String s : words) {
                if (s.length() == 0 || s.length() > width || s.length() > height) {
                    valid = false;
                    break;
                }
            }
        } while (!valid);

        //remove duplicates in user inputted words
        words = words.stream().distinct().collect(Collectors.toList());

        //words user has found in word search
        List<String> foundWords = new ArrayList<>();

        //randomly pick if each of the user inputted words is placed in the right, down or diagonal direction
        // and run corresponding functions
        for (String s : words) {
            switch ((int) Math.floor(Math.random() * 3)) {
                case 0 -> right(matrix, taken, s);
                case 1 -> down(matrix, taken, s);
                case 2 -> diagonal(matrix, taken, s);
                default -> {
                }
            }
        }

        //after user inputted words are added, fill rest of matrix with random characters
        fillRandom(matrix, taken, letters);

        //gameplay loop
        do {
            //print word search board, words looking to be found and words found
            print(matrix);
            System.out.println("WORD(S): ");
            for (String s : words) System.out.println(s);

            System.out.println();
            System.out.println("FOUND: ");
            for (String s : foundWords) System.out.println(s);
            System.out.println();

            //guess function returns "/invalid" if the user inputted rows and columns are not a valid guess
            // function returns a valid guess if the guess is valid (naturally something that isn't "/invalid")
            String guess = "/invalid";
            //guess until not invalid guess is guessed
            while (guess.equals("/invalid")) {
                guess = guessWord(matrix);
            }

            //beat game?? (if the guess is not one of user inputted words, wrong guess)
            if (!words.contains(guess)) System.out.println("Wrong, try again");
            if (words.contains(guess)) foundWords.add(guess);
            //while haven't found all words, play game
        } while (words.size() != foundWords.size());

        System.out.println("you win yay !!!!!");
        System.out.println("word(s) found: ");
        for (String s : foundWords) System.out.println(s);
    }
}
