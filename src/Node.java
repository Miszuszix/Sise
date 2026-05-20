public class Node implements Comparable<Node> {

    private final State puzzleState;
    private final Node parentNode;
    private final char operator;
    private final int depth;
    private int functionCost = 0;
    private boolean isManhattan;

    public Node(State puzzleState, Node parentNode, char operator, int depth, State goalState, boolean isManhattan) {
        this.puzzleState = puzzleState;
        this.parentNode = parentNode;
        this.operator = operator;
        this.depth = depth;
        this.isManhattan = isManhattan;
        this.functionCost = (isManhattan ? getPuzzleState().calculateManhattan(goalState) : getPuzzleState().calculateHamming(goalState)) + this.depth;
    }

    public Node(State puzzleState, State goalState, boolean isManhattan) {
        this.puzzleState = puzzleState;
        this.parentNode = null;
        this.operator = ' ';
        this.depth = 0;
        this.isManhattan = isManhattan;
        this.functionCost = isManhattan ? getPuzzleState().calculateManhattan(goalState) : getPuzzleState().calculateHamming(goalState);
    }

    // Konstruktor dla stanu początkowego
    public Node(State puzzleState) {
        this.puzzleState = puzzleState;
        this.parentNode = null;
        this.operator = ' ';
        this.depth = 0;
    }

    @Override
    public int compareTo(Node otherNode) {
        return Integer.compare(this.functionCost, otherNode.functionCost);
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

    public boolean isManhattan() {
        return isManhattan;
    }
}
