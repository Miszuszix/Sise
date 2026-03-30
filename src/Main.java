void main(String... args) {

    if (args.length < 1) {
        System.err.println("Please provide a move order.");
        System.exit(1);
    }
    
    String strategy = args[0];
    String moveOrder = args[1].toUpperCase();
    String inputFileName = args[2];

    final State goalState = new State(new int[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}}
    );

    int[][] initialPuzzleState = readPuzzleStateFromFile(inputFileName);
    State initialState = new State(initialPuzzleState);
    
    Graph testGraph = new Graph(goalState, moveOrder);
    
    writeSolutionPathToFile(BFS(testGraph, new Node(initialState)), strategy, moveOrder, inputFileName);
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

public int[][] readPuzzleStateFromFile(String inputFileName) {
    try {
        File puzzleFile = new File(inputFileName);
        Scanner scanner = new Scanner(puzzleFile);
        
        if(scanner.hasNextInt()) {
            int rows = scanner.nextInt();
            int columns = scanner.nextInt();
            int[][] initialPuzzleState = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    initialPuzzleState[i][j] = scanner.nextInt();
                }
            }
            return initialPuzzleState;
        }

    } catch (FileNotFoundException e) {
        System.err.println("File not found.");
        System.exit(1);
    }
    return null;
}

public void writeSolutionPathToFile(ArrayList<Character> solutionPath, String... args) {
    try {
        String fileName = args[2];
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        
        String outputFileName = String.format("%s_%s_%s_sol.txt", baseName, args[0], args[1].toLowerCase());
        
        File solutionFile = new File(outputFileName);
        PrintWriter writer = new PrintWriter(solutionFile);
        writer.println(solutionPath.size());
        for (Character move : solutionPath) {
            writer.print(move);
        }
        writer.close();
    } catch (FileNotFoundException e) {
        System.err.println("File not found.");
        System.exit(1);
    }
}