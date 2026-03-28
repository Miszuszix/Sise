void main() {
    State goalState = new State(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}},
            2, 2);
    State initialState = new State(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {0, 7, 8}},
            0, 2);
    Graph testGraph = new Graph(goalState);
    System.out.println(BFS(testGraph, new Node(initialState)));
}

ArrayList<Character> BFS(Graph graph, Node beginningNode) {
    Node currentNode;
    if (graph.isGoalState(beginningNode.getPuzzleState())) {
        return returnSolutionPath(beginningNode);
    }
    Deque<Node> openStates = new LinkedList<>();
    HashSet<State> closedStates = new HashSet<>();

    openStates.add(beginningNode);

    while (!openStates.isEmpty()) {
        currentNode = openStates.remove();
        
        for (Node neighbour : graph.generateNeighbours(currentNode)) {
            State neighbourPuzzleState = neighbour.getPuzzleState();
            
            if (graph.isGoalState(neighbourPuzzleState)) {
                return returnSolutionPath(neighbour);
            }
            if (!closedStates.contains(neighbourPuzzleState)) {
                openStates.add(neighbour);
                closedStates.add(neighbourPuzzleState);
            }
        }
    }
    return null;
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