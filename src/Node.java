public class Node {
    
    private final State puzzleState;
    private final Node parentNode;
    private final char operator;
    private final int depth; // dla DFSa

    public Node(State puzzleState, Node parentNode, char operator, int depth) {
        this.puzzleState = puzzleState;
        this.parentNode = parentNode;
        this.operator = operator;
        this.depth = depth;
    }

    // Konstruktor dla stanu początkowego
    public Node(State puzzleState) {
        this.puzzleState = puzzleState;
        this.parentNode = null;
        this.operator = ' ';
        this.depth = 0;
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

    public int getDepth() {
        return depth;
    }
}
