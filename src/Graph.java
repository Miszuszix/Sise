import java.util.ArrayList;
import java.util.Collections;

public class Graph {
    private final State goalState;
    private final String moveOrder;

    public Graph(State goalState, String moveOrder) {
        this.goalState = goalState;
        this.moveOrder = moveOrder;
    }

    public boolean isGoalState(State state) {
        return goalState.equals(state);
    }

    public ArrayList<Node> generateNeighbours(Node currentNode) {
        ArrayList<Node> neighbours = new ArrayList<>();
        State currentState = currentNode.getPuzzleState();
        ArrayList<Character> possibleMoves = currentState.generatePossibleMoves(moveOrder);
        
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
            boolean isManhattan = moveOrder.equalsIgnoreCase("manh");
            neighbours.add(new Node(nextState, currentNode, move, currentNode.getDepth() + 1, goalState,  isManhattan));
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