import java.util.ArrayList;
import java.util.Collections;

public class Graph {
    private final State goalState;
    
    public Graph(State goalState) {
        this.goalState = goalState;
    }
    
    public boolean isGoalState(State state) {
        return goalState.equals(state);
    }

    public ArrayList<Node> generateNeighbours(Node currentNode) {
        ArrayList<Node> neighbours = new ArrayList<>();
        State currentState = currentNode.getPuzzleState();
        ArrayList<Character> possibleMoves = currentState.generatePossibleMoves();
        
        char parentOperator = currentNode.getOperator();
        
        for (char move : possibleMoves) {
            switch (move) {
                case 'R':
                    if (parentOperator == 'L') {
                        continue;
                    }
                    break;
                case 'L':
                    if (parentOperator == 'R') {
                        continue;
                    }
                    break;
                case 'U':
                    if (parentOperator == 'D') {
                        continue;
                    }
                    break;
                case 'D':
                    if (parentOperator == 'U') {
                        continue;
                    }
            }
            State nextState = currentState.createNextState(move);
            neighbours.add(new Node(nextState, currentNode, move));
        }
        return neighbours;
    }

    ArrayList<Character> returnSolutionPath(Node goalNode){
        ArrayList<Character> solutionPath = new ArrayList<>();
        while(goalNode.getParentNode() != null){
            solutionPath.add(goalNode.getOperator());
            goalNode = goalNode.getParentNode();
        }
        Collections.reverse(solutionPath);
        return solutionPath;
    }
}