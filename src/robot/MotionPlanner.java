/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class MotionPlanner
{
    final int INF = 1000;
    final int REGULAR_SQUARE_COUNT = 64;
    final int REGULAR_ROW_SIZE = 8;
    final int REGULAR_COLUMN_SIZE = 8;
    
    int boardRows;
    int boardColumns;
    boolean occupancyGrid[]; // indicates which squares are occupied
    ArrayList<Move> movesNeeded;
    
    public MotionPlanner(int currentLocations[], int desiredLocations[],
                         int boardRows, int boardColumns)
    {
        this.boardRows = boardRows;
        this.boardColumns = boardColumns;
        occupancyGrid = fillOccupancyGrid(currentLocations);
        movesNeeded = generateMoves(currentLocations, desiredLocations);
    }

    private boolean[] fillOccupancyGrid(int currentLocations[])
    {
        boolean occupancyGrid[] = new boolean[REGULAR_SQUARE_COUNT];
        
        for (int i = 0; i < occupancyGrid.length; i++)
            occupancyGrid[i] = false;

        for (int i = 0; i < currentLocations.length; i++)
            occupancyGrid[currentLocations[i]] = true;

        return occupancyGrid;
    }

    private ArrayList<Move> generateMoves(int currentLocations[], int desiredLocations[])
    {
        ArrayList<Move> moves = new ArrayList<>();
        
        for (int i = 0; i < currentLocations.length; i++)
            if (currentLocations[i] != desiredLocations[i])
                moves.add(new Move(i, currentLocations[i], desiredLocations[i]));

        return moves;
    }

    private ArrayList<Edge> computeEdges(int vertex)
    {
        final int LATERAL_WEIGHT = 2;
        final int DIAGONAL_WEIGHT = 3;
        
        ArrayList<Edge> edges = new ArrayList<>();

        // check north neighbor
        if (vertex > REGULAR_ROW_SIZE)
            edges.add(new Edge(vertex, vertex - REGULAR_ROW_SIZE, LATERAL_WEIGHT));

        // check northeast neighbor
        if (vertex > REGULAR_ROW_SIZE &&
            vertex % REGULAR_ROW_SIZE < boardColumns - 1)
            edges.add(new Edge(vertex, vertex - REGULAR_ROW_SIZE + 1, DIAGONAL_WEIGHT));

        // check east neighbor
        if (vertex % REGULAR_ROW_SIZE < boardColumns - 1)
            edges.add(new Edge(vertex, vertex + 1, LATERAL_WEIGHT));

        // check southeast neighbor
        if (vertex < REGULAR_COLUMN_SIZE * (boardRows - 1) &&
            vertex % REGULAR_ROW_SIZE < boardColumns - 1)
            edges.add(new Edge(vertex, vertex + REGULAR_ROW_SIZE + 1, DIAGONAL_WEIGHT));

        // check south neighbor
        if (vertex < REGULAR_COLUMN_SIZE * (boardRows - 1))
            edges.add(new Edge(vertex, vertex + REGULAR_ROW_SIZE, LATERAL_WEIGHT));

        // check southwest neighbor
        if (vertex < REGULAR_COLUMN_SIZE * (boardRows - 1) &&
            vertex % REGULAR_ROW_SIZE != 0)
            edges.add(new Edge(vertex, vertex + REGULAR_ROW_SIZE - 1, DIAGONAL_WEIGHT));

        // check west neighbor
        if (vertex % REGULAR_ROW_SIZE != 0)
            edges.add(new Edge(vertex, vertex - 1, LATERAL_WEIGHT));

        // check northwest neighbor
        if (vertex > REGULAR_ROW_SIZE &&
            vertex % REGULAR_ROW_SIZE != 0)
            edges.add(new Edge(vertex, vertex - REGULAR_ROW_SIZE - 1, DIAGONAL_WEIGHT));

        // we can't move to a square that is occupied by another piece
        // remove edges whose destinations are occupied
        for (int i = 0; i < edges.size(); i++) {
            Edge testEdge = edges.get(i);
            
            if (occupancyGrid[testEdge.destination])
                edges.remove(i--);
        }
        
        return edges; 
    }

    public ArrayList<Integer> plan()
    {
        ArrayList<Integer> plan = new ArrayList<>();

        // for now, let's not handle any more than single move plans
        if (movesNeeded.size() > 1)
            return new ArrayList<Integer>();


        for (int i = 0; i < movesNeeded.size(); i++) {
            Move thisMove = movesNeeded.get(i);
            
            ArrayList<Integer> squareSequence = dijkstra(thisMove.origin,
                                                         thisMove.destination);

            if (squareSequence.size() == 0)
                return new ArrayList<Integer>();
            
            plan.add(thisMove.origin);

            for (int j = 0; j < squareSequence.size(); j++)
                plan.add(squareSequence.get(j));
        }
        
        return plan;
    }

    private ArrayList<Integer> dijkstra(int origin, int destination)
    {
        Vertex vertices[] = new Vertex[REGULAR_SQUARE_COUNT];
        PriorityQueue<Vertex> queue = new PriorityQueue<>(boardRows * boardColumns,
                                                          new VertexComparator());

        enqueueVertices(vertices, queue, origin);
        updateDistances(vertices, queue);

        ArrayList<Integer> path = generatePath(vertices, destination);
        
        return path;
    }

    private void enqueueVertices(Vertex vertices[], PriorityQueue<Vertex> queue,
                                 int origin)
    {
        for (int i = 0; i < vertices.length; i++) {
            if (i == origin)
                vertices[i] = new Vertex(i, 0);
            else
                vertices[i] = new Vertex(i, INF);

            queue.add(vertices[i]);
        }
    }

    private void updateDistances(Vertex vertices[], PriorityQueue<Vertex> queue)
    {
        while (queue.size() > 0) {
            Vertex u = queue.poll();

            ArrayList<Edge> edges = computeEdges(u.id);

            for (int i = 0; i < edges.size(); i++) {
                Edge e = edges.get(i);
                Vertex v = vertices[e.destination];

                if (v.distance > u.distance + e.weight) {
                    queue.remove(v);
                    v.distance = u.distance + e.weight;
                    v.predecessor = u;
                    queue.add(v);
                }
            }
        }
    }

    private ArrayList<Integer> generatePath(Vertex vertices[], int destination)
    {
        Stack<Vertex> stack = new Stack<>();
        Vertex u = vertices[destination];

        while (u.predecessor != null) {
            stack.push(u);
            u = u.predecessor;
        }

        ArrayList<Integer> path = new ArrayList<>();

        while (!stack.empty()) {
            u = stack.pop();
            path.add(u.id);
        }

        return path;
    }
}

class Vertex
{
    int distance;
    int id;
    Vertex predecessor;

    Vertex(int id, int distance)
    {
        this.distance = distance;
        this.id = id;
        predecessor = null;
    }
}

class Edge
{
    int origin;
    int destination;
    int weight;

    Edge(int origin, int destination, int weight)
    {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
    }
}

class VertexComparator implements Comparator<Vertex>
{
    @Override
    public int compare(Vertex x, Vertex y)
    {
        if (x.distance < y.distance)
            return -1;

        if (x.distance > y.distance)
            return 1;

        return 0;
    }
}

class Move
{
    int pieceID;
    int origin;
    int destination;
    
    Move(int pieceID, int origin, int destination)
    {
        this.pieceID = pieceID;
        this.origin = origin;
        this.destination = destination;
    }
}