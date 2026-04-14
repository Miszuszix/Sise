int generatedStates = 0;
int expandedStates = 0;
int maxDepthReached = 0;

void main(String... args) {

    if (args.length < 1) {
        System.err.println("Please provide a move order.");
        System.exit(1);
    }
    
    String strategy = args[0].toLowerCase();
    String param = args[1].toUpperCase();
    String moveOrder = strategy.equals("astr") ? "RDUL" : param;
    String inputFileName = args[2];
    String outputFileName = args[3];
    String statsFileName = args[4];

    int[][] initialPuzzleState = readPuzzleStateFromFile(inputFileName);
    //dynamic generating goalState
    int goalRows = initialPuzzleState.length;
    int goalColumns = initialPuzzleState[0].length;
    int[][] goalBoard = new int[goalRows][goalColumns];
    
    for(int i = 0; i < goalBoard.length; i++) {
        for(int j = 0; j < goalBoard[0].length; j++) {
            goalBoard[i][j] = i * goalColumns + j + 1;
        }
    }
    goalBoard[goalRows - 1][goalColumns - 1] = 0;
    
    State initialState = new State(initialPuzzleState);
    State goalState = new State(goalBoard);
    
    Graph graph = new Graph(goalState, moveOrder);
    
    ArrayList<Character> solution = switch (strategy) {
        case "bfs" -> BFS(graph, new Node(initialState));
        case "dfs" -> DFS(graph, new Node(initialState), 20);
        case "astr" -> {
            boolean isManhattan = param.equalsIgnoreCase("manh");
            yield ASTAR(graph, new Node(initialState, goalState, isManhattan));
        }
        default -> {
            System.out.println("Unknown strategy: " + strategy);
            yield null;
        }
    };

    writeResultsToFiles(generatedStates, expandedStates, solution, strategy, outputFileName, statsFileName);
}

ArrayList<Character> BFS(Graph graph, Node beginningNode) {
    if (graph.isGoalState(beginningNode.getPuzzleState())) {
        return graph.returnSolutionPath(beginningNode);
    }

    Deque<Node> openStates = new LinkedList<>();
    HashSet<State> closedStates = new HashSet<>();

    openStates.add(beginningNode);
    while (!openStates.isEmpty()) {
        Node currentNode = openStates.remove();
        expandedStates++;
        
        for (Node neighbour : graph.generateNeighbours(currentNode)) {
            State neighbourPuzzleState = neighbour.getPuzzleState();

            if (graph.isGoalState(neighbourPuzzleState)) {
                return graph.returnSolutionPath(neighbour);
            }
            if (!closedStates.contains(neighbourPuzzleState)) {
                openStates.add(neighbour);
                closedStates.add(neighbourPuzzleState);
                generatedStates++;
            }
        }
    }
    return null;
}

ArrayList<Character> DFS(Graph graph, Node beginningNode, int depthLimit) {
    if (graph.isGoalState(beginningNode.getPuzzleState())) {
        return graph.returnSolutionPath(beginningNode);
    }

    Deque<Node> openStates = new ArrayDeque<>();
    HashMap<State, Integer> closedStates = new HashMap<>();
    
    openStates.push(beginningNode);
    generatedStates++;
    closedStates.put(beginningNode.getPuzzleState(), beginningNode.getDepth());

    while (!openStates.isEmpty()) {
        Node currentNode = openStates.pop();
        expandedStates++;
        maxDepthReached = Math.max(maxDepthReached, currentNode.getDepth());

        if (currentNode.getDepth() < depthLimit) {
            ArrayList<Node> neighbours = graph.generateNeighbours(currentNode);
            
            Collections.reverse(neighbours); 

            for (Node neighbour : neighbours) {
                State neighbourPuzzleState = neighbour.getPuzzleState();
                
                if (!closedStates.containsKey(neighbourPuzzleState) || neighbour.getDepth() < closedStates.get(neighbourPuzzleState)) {
                    if (graph.isGoalState(neighbourPuzzleState)) {
                        maxDepthReached = neighbour.getDepth();
                        return graph.returnSolutionPath(neighbour);
                    }
                    closedStates.put(neighbourPuzzleState, neighbour.getDepth());
                    openStates.push(neighbour);
                    generatedStates++;
                }
            }
        }
    }
    return null;
}

ArrayList<Character> ASTAR(Graph graph, Node beginningNode) {
    PriorityQueue<Node> openStates = new PriorityQueue<>();
    HashSet<State> closedStates = new HashSet<>();

    openStates.add(beginningNode);

    while(!openStates.isEmpty()) {
        Node currentNode = openStates.poll();
        if(!closedStates.contains(currentNode.getPuzzleState())) {
            expandedStates++;
            maxDepthReached = Math.max(maxDepthReached, currentNode.getDepth());
            if (graph.isGoalState(currentNode.getPuzzleState())) {
                return graph.returnSolutionPath(currentNode);
            }
            closedStates.add(currentNode.getPuzzleState());
            for (Node neighbour : graph.generateNeighbours(currentNode)) {
                if (!closedStates.contains(neighbour.getPuzzleState())) {
                    openStates.add(neighbour);
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
                statsWriter.print(maxDepthReached);
                break;
            case "astr":
                statsWriter.print(maxDepthReached);
                break;
        }
        statsWriter.close();
    } catch (FileNotFoundException e) {
        System.err.println("File not found.");
        System.exit(1);
    }
}