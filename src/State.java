import java.util.ArrayList;
import java.util.Arrays;

public class State {
    int[][] puzzleState;
    int xPosition;
    int yPosition;

    public State(int[][] puzzleState) {
        this.puzzleState = puzzleState;
        for(int i = 0; i < puzzleState.length; i++){
            for(int j = 0; j < puzzleState[0].length; j++){
                if(puzzleState[i][j] == 0){
                    this.xPosition = j;
                    this.yPosition = i;
                    return;
                }
            }
        }
    }
    
    public State(int[][] puzzleState, int xPosition, int yPosition) {
        this.puzzleState = puzzleState;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public State createNextState(char move) {
        int[][] nextState = new int[puzzleState.length][puzzleState[0].length];
        for (int i = 0; i < puzzleState.length; i++) {
            nextState[i] = puzzleState[i].clone();
        }
        switch (move) {
            case 'R':
                nextState[yPosition][xPosition] = nextState[yPosition][xPosition + 1];
                nextState[yPosition][xPosition + 1] = 0;
                return new State(nextState, xPosition + 1, yPosition);
            case 'L':
                nextState[yPosition][xPosition] = nextState[yPosition][xPosition - 1];
                nextState[yPosition][xPosition - 1] = 0;
                return new State(nextState, xPosition - 1, yPosition);
            case 'U':
                nextState[yPosition][xPosition] = nextState[yPosition - 1][xPosition];
                nextState[yPosition - 1][xPosition] = 0;
                return new State(nextState, xPosition, yPosition - 1);
            case 'D':
                nextState[yPosition][xPosition] = nextState[yPosition + 1][xPosition];
                nextState[yPosition + 1][xPosition] = 0;
                return new State(nextState, xPosition, yPosition + 1);
        }
        return null;
    }

    public ArrayList<Character> generatePossibleMoves(String moveOrder) {
        int xBounds = puzzleState[0].length;
        int yBounds = puzzleState.length;
        ArrayList<Character> moves = new ArrayList<>();
        for (char move : moveOrder.toCharArray()) {
            switch (move) {
                case 'R':
                    if (xPosition < xBounds - 1) {
                        moves.add('R');
                    }
                    break;
                case 'L':
                    if (xPosition > 0) {
                        moves.add('L');
                    }
                    break;
                case 'U':
                    if (yPosition > 0) {
                        moves.add('U');
                    }
                    break;
                case 'D':
                    if (yPosition < yBounds - 1) {
                        moves.add('D');
                    }
                    break;
            }
        }
        return moves;
    }

    public int calculateHamming(State goalState){
        int hammingDistance = 0;
        for(int row = 0 ; row < goalState.puzzleState.length; row++){
            for(int column = 0 ; column < goalState.puzzleState[0].length; column++){
                if(this.puzzleState[row][column] !=  0 && goalState.puzzleState[row][column] != puzzleState[row][column]){
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    public int calculateManhattan(State goalState){
        int manhattanDistance = 0;
        int height = goalState.puzzleState.length;
        int width = goalState.puzzleState[0].length;
        for (int row = 0; row < height; row++){
            for(int column = 0; column < width; column++){
                if (this.puzzleState[row][column] == 0) continue;
                int currentTile = this.puzzleState[row][column] - 1;
                int targetRow = currentTile / width;
                int targetColumn = currentTile % width;
                manhattanDistance += Math.abs(targetRow - row) +  Math.abs(targetColumn - column);
            }
        }
        return manhattanDistance;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(puzzleState);
        result = 31 * result + xPosition;
        result = 31 * result + yPosition;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass()) return false;

        State state = (State) object;

        return xPosition == state.xPosition &&
                yPosition == state.yPosition &&
                Arrays.deepEquals(puzzleState, state.puzzleState);
    }
}