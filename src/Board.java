import java.util.*;

class Point {
    /*
     * This class describes a point which is made of two integers:
     * int x
     * int y
     *
     * these integers represent a point on the 5 by 5 grid that is the board.
     */

    int x, y;

    //Point constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override //Function to show which area of the grid a specific point points to.
    public String toString() {
        return "[" + (x + 1) + ", " + (y + 1) + "]";
    }
}

class PointsAndScores {
    /*
     * This class stores the score associated with a certain point for
     * the AI to be able to evaluate which point is more beneficial to play
     *
     * score = 0 if it will not lead to win or loss
     * score = 1 if it leads to a win
     * score = -1 if it could lead to a loss
     */

    int score;
    Point point;

    PointsAndScores(int score, Point point) { //PointsAndScores Constructor
        this.score = score;
        this.point = point;
    }
}

class Board {
    List<Point> availablePoints; //List contains all the points which have not yet been played
    Scanner scan = new Scanner(System.in); //Scanner for system input
    int[][] board = new int[5][5]; //5 dimensional array holding the board, initially all values are 0

    public Board() { //Board constructor, creates a board object
    }

    public boolean isGameOver() {
        /*
         * This method checks wether or not the game has ended.
         * returns true if either player has won, or if no points can be played anymore
         */

        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    public boolean hasXWon() {
        /*
         * This method checks whether or not player X has won
         * AKA: the AI
         *
         */

        //Diagonal checks
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3] && board[0][0] == board[4][4] && board[0][0] == 1)
                || (board[0][4] == board[1][3] && board[0][4] == board[2][2] && board[0][4] == board[3][1] && board[0][4] == board[4][0] && board[0][4] == 1)) {
            return true;
        }
        for (int i = 0; i < 5; ++i) { //checking for rows
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == board[i][3] && board[i][0] == board[i][4] && board[i][0] == 1)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == board[4][i] && board[0][i] == 1))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOWon() {
        /*
         * This method checks whether or not player O has won
         * AKA: the human player
         *
         */

        //Diagonal checks
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3] && board[0][0] == board[4][4] && board[0][0] == 2)
                || (board[0][4] == board[1][3] && board[0][4] == board[2][2] && board[0][4] == board[3][1] && board[0][4] == board[4][0] && board[0][4] == 2)) {
            return true;
        }
        for (int i = 0; i < 5; ++i) { //checking for rows
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == board[i][3] && board[i][0] == board[i][4] && board[i][0] == 2)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == board[4][i] && board[0][i] == 2))) {
                return true;
            }
        }
        return false;
    }

    public boolean canDiag(Point p, int player) {
        /*
         * method checks if a player can potentially score diagonally using the point
         */
        boolean test = false;

        if ((p.x == p.y) && ((board[0][0] == 0 || board[0][0] == player) && (board[1][1] == 0 || board[1][1] == player) && (board[2][2] == 0
                || board[2][2] == player) && (board[3][3] == 0 || board[3][3] == player) && (board[4][4] == 0 || board[4][4] == player))) {
            test = true;
        }
        if ((p.x + p.y == 4) && ((board[0][4] == 0 || board[0][4] == player) && (board[1][3] == 0 || board[1][3] == player)
                && (board[2][2] == 0 || board[2][2] == player) && (board[3][1] == 0 || board[3][1] == player) && (board[4][0] == 0 || board[4][0] == player))) {
            test = true;
        }

        return test;
    }

    public boolean canRow(Point p, int player) {
        /*
         * method checks if a player can potentially score by column or row using the point
         */
        boolean test = false;
        //checking for row according to X axis
        if ((board[p.x][0] == 0 || board[p.x][0] == player) && (board[p.x][1] == 0 || board[p.x][1] == player) && (board[p.x][2] == 0
                || board[p.x][2] == player) && (board[p.x][3] == 0 || board[p.x][3] == player) && (board[p.x][4] == 0 || board[p.x][4] == player)) {
            test = true;
        }
        //check for column with y axis
        if ((board[0][p.y] == 0 || board[0][p.y] == player) && (board[1][p.y] == 0 || board[1][p.y] == player)
                && (board[2][p.y] == 0 || board[2][p.y] == player) && (board[3][p.y] == 0 || board[3][p.y] == player) && (board[4][p.y] == 0 || board[4][p.y] == player)) {
            test = true;
        }

        return test;
    }

    public List<Point> getAvailablePoints() {
        /*
         * This function clears the availablePoints list and
         * refreshes it so that it displays the correct available points
         * after being called
         */
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (board[i][j] == 0) { //AKA if point at place [i][j] has not been touched yet
                    availablePoints.add(new Point(i, j)); //add non populated point to the available points list
                }
            }
        }
        return availablePoints;
    }

    public int getState(Point point) {
        /*
         * Shows wether a point has been played by the computer, player,
         * or if it has not been played.
         * outputs:
         *  0 if point is not populated
         *  1 if played by computer
         *  2 if played by player
         */
        return board[point.x][point.y];
    }

    public void placeAMove(Point point, int player) {
        /*
        This function places the current point on the board
        */
        board[point.x][point.y] = player;
    }

    public void displayBoard() {
        /*
         * method which displays the board in System.out
         * uses the point values to see which player played which
         * points and which points are unplayed
         */
        System.out.println();

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (board[i][j] == 1)
                    System.out.print("X ");
                else if (board[i][j] == 2)
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }
}
