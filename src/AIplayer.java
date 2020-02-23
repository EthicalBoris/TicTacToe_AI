import java.util.*;

class AIplayer {
    /*
     * This class Stores the code which creates an AIplayer
     * object which can decide which move to play
     * in order to win or draw the game
     */

    List<Point> availablePoints;
    List<PointsAndScores> rootsChildrenScores; //list of points and their scores. (pointAndScores object)
    Board b = new Board();

    public AIplayer(Board b) { //AIplayer constructor
        this.b = b;
    }

    public Point returnBestMove() {
        /*
         * Selects a point depending on the scores of the points available
         *
         * Attacking heuristics here, also defensive
         */

        int MAX = -100000;
        int best = -1;
        //Boolean to see if a score can be negative, this nerfs the attack implementations to save the game
        boolean haveNegative = false;

        availablePoints = new ArrayList<>();

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
                if (rootsChildrenScores.get(i).score < 0) {
                    haveNegative = true; //if any score is under 0, we have a negative
                }
            }
        }
        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (rootsChildrenScores.get(i).score < 0) {
                haveNegative = true;
            }
        }
        if (rootsChildrenScores.get(best).score == 0 && !haveNegative/*no scores negative*/) {
            for (int i = 0; i < rootsChildrenScores.size(); i++) {
                if (b.canRow(rootsChildrenScores.get(i).point, 1)) {
                    availablePoints.add(rootsChildrenScores.get(i).point);
                }
            }
            for (int i = 0; i < rootsChildrenScores.size(); i++) {
                if (b.canDiag(rootsChildrenScores.get(i).point, 1)) {
                    availablePoints.add(rootsChildrenScores.get(i).point);
                    if (rootsChildrenScores.get(i).point.x == 2 && rootsChildrenScores.get(i).point.y == 2) {
                        return rootsChildrenScores.get(i).point;
                    }
                }
            }
        }

        if (availablePoints.size() > 1 && (best == 0 && !haveNegative)) {
            return availablePoints.get((int) ((availablePoints.size() - 1)));
        } else {
            return rootsChildrenScores.get(best).point;
        }
    }

    public int returnMin(List<Integer> list) {
        /*
         * Returns the smallest item of a list of integers
         */

        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public int returnMax(List<Integer> list) {
        /*
         * Returns the biggest value of a list fo integers
         */

        int max = Integer.MIN_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public void callMinimax(int depth, int turn, Board b) {
        /*
         * Function calls minimax but also empties the arraylist which
         * holds the scores for each point which has become obsolete since the player turn
         */
        rootsChildrenScores = new ArrayList<>(); //empty array
        minimax(depth, turn, b); //call minmax
    }

    public int minimax(int depth, int turn, Board b) {
        /*
         * This method implements the minimax search
         * int depth
         * int turn
         * board b (The board upon which the game is being played)
         */

        if (b.hasXWon()) return 1;
        if (b.hasOWon()) return -1;
        List<Point> pointsAvailable = b.getAvailablePoints();
        if (pointsAvailable.isEmpty() || depth >= 5) return 0; //point lesds to a draw, plus max depth

        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);

            if (turn == 1) {
                b.placeAMove(point, 1);
                int currentScore = minimax(depth + 1, 2, b);
                scores.add(currentScore);
                if (depth == 0)

                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));

            } else if (turn == 2) {

                b.placeAMove(point, 2);
                scores.add(minimax(depth + 1, 1, b));
            }
            b.placeAMove(point, 0);
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }
}
