void main() {
    State goalState = new State(new int[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}}, 
            3, 3);
    State initialState = new State(new int[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {10, 0, 11, 12},
            {9, 13, 14, 15}},
            1, 2);
    Graph testGraph = new Graph(goalState);
    System.out.println(BFS(testGraph, new Node(initialState)));
}

ArrayList<Character> BFS(Graph graph, Node beginningNode) {
    Node currentNode;
    if (graph.isGoalState(beginningNode.getPuzzleState())) {
        return graph.returnSolutionPath(beginningNode);
    }
    Deque<Node> openStates = new LinkedList<>();
    HashSet<State> closedStates = new HashSet<>();

    openStates.add(beginningNode);

    while (!openStates.isEmpty()) {
        currentNode = openStates.remove();
        
        for (Node neighbour : graph.generateNeighbours(currentNode)) {
            State neighbourPuzzleState = neighbour.getPuzzleState();
            
            if (graph.isGoalState(neighbourPuzzleState)) {
                return graph.returnSolutionPath(neighbour);
            }
            if (!closedStates.contains(neighbourPuzzleState)) {
                openStates.add(neighbour);
                closedStates.add(neighbourPuzzleState);
            }
        }
    }
    return null;
}