public class Node {
    
    private final State puzzleState;
    private final Node parentNode;
    private final char operator;

    public Node(State puzzleState, Node parentNode, char operator) {
        this.puzzleState = puzzleState;
        this.parentNode = parentNode;
        this.operator = operator;
    }

    public Node(State puzzleState) {
        this.puzzleState = puzzleState;
        this.parentNode = null;
        this.operator = ' ';
    }

    public State getPuzzleState() {
        return puzzleState;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public char getOperator() {
        return operator;
    }
}
