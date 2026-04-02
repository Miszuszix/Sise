int generatedStates = 0;
int expandedStates = 0;
int maxDepthReached = 0;

void main(String... args) {

    if (args.length < 1) {
        System.err.println("Please provide a move order.");
        System.exit(1);
    }
    
    String strategy = args[0].toLowerCase();
    String moveOrder = args[1].toUpperCase();
    String inputFileName = args[2];
    String outputFileName = args[3];
    String statsFileName = args[4];

    final State goalState = new State(new int[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}}
    );

    int[][] initialPuzzleState = readPuzzleStateFromFile(inputFileName);
    State initialState = new State(initialPuzzleState);
    
    Graph graph = new Graph(goalState, moveOrder);
    
    ArrayList<Character> solution;

    switch (strategy) {
    case "bfs":
        solution = BFS(graph, new Node(initialState));
        break;
    case "dfs":
        solution = DFS(graph, new Node(initialState), 20);
        break;
    case "astr":
        //TODO: add astr strategy
        solution = null;
        break;
    default:
        System.out.println("Unknown strategy: " + strategy);
        solution = null;
}
    
    writeResultsToFiles(generatedStates, expandedStates, solution, strategy, outputFileName, statsFileName);
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
        expandedStates++;
        
        for (Node neighbour : graph.generateNeighbours(currentNode)) {
            generatedStates++;
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

ArrayList<Character> DFS(Graph graph, Node beginningNode, int depthLimit) {
    // Sprawdzenie, czy stan początkowy jest stanem docelowym.
    if (graph.isGoalState(beginningNode.getPuzzleState())) {
        return graph.returnSolutionPath(beginningNode);
    }

    Deque<Node> openStates = new ArrayDeque<>();
    HashSet<State> closedStates = new HashSet<>();
    
    openStates.push(beginningNode);
    generatedStates++;

    while (!openStates.isEmpty()) {
        Node currentNode = openStates.pop();
        expandedStates++;
        closedStates.add(currentNode.getPuzzleState());
        maxDepthReached = Math.max(maxDepthReached, currentNode.getDepth());

        // Sprawdzenie, czy osiągnięto limit głębokości.
        if (currentNode.getDepth() < depthLimit) {
            ArrayList<Node> neighbours = graph.generateNeighbours(currentNode);
            
            Collections.reverse(neighbours); 

            for (Node neighbour : neighbours) {
                State neighbourPuzzleState = neighbour.getPuzzleState();
                
                if (!closedStates.contains(neighbourPuzzleState) && !openStates.contains(neighbour)) {
                    if (graph.isGoalState(neighbourPuzzleState)) {
                        maxDepthReached = neighbour.getDepth();
                        return graph.returnSolutionPath(neighbour);
                    }
                    openStates.push(neighbour);
                    generatedStates++;
                }
            }
        }
    }
    return null;
}

public int[][] readPuzzleStateFromFile(String inputFileName) {
    File puzzleFile = new File(inputFileName);

    try (Scanner scanner = new Scanner(puzzleFile)){
        
        if(scanner.hasNextInt()) {
            int columns = scanner.nextInt();
            int rows = scanner.nextInt();
            int[][] initialPuzzleState = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    initialPuzzleState[i][j] = scanner.nextInt();
                }
            }
            return initialPuzzleState;
        }

    } catch (FileNotFoundException e) {
        System.err.println("File not found: " + inputFileName);
        System.exit(1);
    }
    return null;
}

public void writeResultsToFiles(int generatedStates, int expandedStates, ArrayList<Character> solutionPath, String... args) {
    String strategy = args[0];
    String outputFileName = args[1];
    String statsFileName = args[2];
    int solutionLength;
    try {
        File solutionFile = new File(outputFileName);
        File statsFile = new File(statsFileName);
        
        PrintWriter solutionWriter = new PrintWriter(solutionFile);
        PrintWriter statsWriter = new PrintWriter(statsFile);
        
        if (solutionPath == null) {
            solutionLength = -1;
            solutionWriter.println(solutionLength);
            statsWriter.println(solutionLength);
        }else {
            solutionLength = solutionPath.size();
            solutionWriter.println(solutionLength);
            statsWriter.println(solutionLength);
            for (Character move : solutionPath) {
                solutionWriter.print(move);
            }
        }
        solutionWriter.close();
        statsWriter.println(generatedStates);
        statsWriter.println(expandedStates);
        switch(strategy){
            case "bfs":
                statsWriter.print(solutionLength);
                break;
            case "dfs":
                // Największa odwiedzona głębokość
                statsWriter.print(maxDepthReached);
                break;
            case "astr":
                //TODO: add max recursive depth for ASTR
                break;
        }
        statsWriter.close();
    } catch (FileNotFoundException e) {
        System.err.println("File not found.");
        System.exit(1);
    }
}