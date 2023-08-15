package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * <P>
 * This class represents a general "directed graph", which could be used for any
 * purpose. The graph is viewed as a collection of vertices, which are sometimes
 * connected by weighted, directed edges.
 * </P>
 * 
 * <P>
 * This graph will never store duplicate vertices.
 * </P>
 * 
 * <P>
 * The weights will always be non-negative integers.
 * </P>
 * 
 * <P>
 * The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.
 * </P>
 * 
 * <P>
 * The Weighted Graph will maintain a collection of "GraphAlgorithmObservers",
 * which will be notified during the performance of the graph algorithms to
 * update the observers on how the algorithms are progressing.
 * </P>
 */
public class WeightedGraph<V> {

	/*
	 * STUDENTS: You decide what data structure(s) to use to implement this class.
	 * 
	 * You may use any data structures you like, and any Java collections that we
	 * learned about this semester. Remember that you are implementing a weighted,
	 * directed graph.
	 */

	private Map<V, Map<V, Integer>> weightedGraph;

	/*
	 * Collection of observers. Be sure to initialize this list in the constructor.
	 * The method "addObserver" will be called to populate this collection. Your
	 * graph algorithms (DFS, BFS, and Dijkstra) will notify these observers to let
	 * them know how the algorithms are progressing.
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;

	/**
	 * Initialize the data structures to "empty", including the collection of
	 * GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		// initializes graph and observer list
		weightedGraph = new HashMap<>();
		observerList = new ArrayList<GraphAlgorithmObserver<V>>();
	}

	/**
	 * Add a GraphAlgorithmObserver to the collection maintained by this graph
	 * (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer); // Add observers to list
	}

	/**
	 * Add a vertex to the graph. If the vertex is already in the graph, throw an
	 * IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in the graph
	 */
	public void addVertex(V vertex) throws IllegalArgumentException {
		// Checks if vertex is already contained
		if (containsVertex(vertex)) {
			throw new IllegalArgumentException();
		} else {
			weightedGraph.put(vertex, new HashMap<V, Integer>()); // Places vertex into the graph
		}
	}

	/**
	 * Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		return weightedGraph.containsKey(vertex); // Checks if graph has vertex already
	}

	/**
	 * <P>
	 * Add an edge from one vertex of the graph to another, with the weight
	 * specified.
	 * </P>
	 * 
	 * <P>
	 * The two vertices must already be present in the graph.
	 * </P>
	 * 
	 * <P>
	 * This method throws an IllegalArgumentExeption in three cases:
	 * </P>
	 * <P>
	 * 1. The "from" vertex is not already in the graph.
	 * </P>
	 * <P>
	 * 2. The "to" vertex is not already in the graph.
	 * </P>
	 * <P>
	 * 3. The weight is less than 0.
	 * </P>
	 * 
	 * @param from   the vertex the edge leads from
	 * @param to     the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex is not in the graph, or
	 *                                  the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) throws IllegalArgumentException {
		// Checks if contains from and to vertices and that weight is not less than 0
		if (!containsVertex(from) || !containsVertex(to) || weight < 0) {
			throw new IllegalArgumentException();
		} else {
			// adds edge to the edges set
			weightedGraph.get(from).put(to, weight);
		}
	}

	/**
	 * <P>
	 * Returns weight of the edge connecting one vertex to another. Returns null if
	 * the edge does not exist.
	 * </P>
	 * 
	 * <P>
	 * Throws an IllegalArgumentException if either of the vertices specified are
	 * not in the graph.
	 * </P>
	 * 
	 * @param from vertex where edge begins
	 * @param to   vertex where edge terminates
	 * @return weight of the edge, or null if there is no edge connecting these
	 *         vertices
	 * @throws IllegalArgumentException if either of the vertices specified are not
	 *                                  in the graph.
	 */
	public Integer getWeight(V from, V to) throws IllegalArgumentException {

		// Checks if contains from and to vertices
		if (!containsVertex(from) || !containsVertex(to)) {
			throw new IllegalArgumentException();
		}

		// Returns the weight of vertex
		return weightedGraph.get(from).get(to);

	}

	/**
	 * <P>
	 * This method will perform a Breadth-First-Search on the graph. The search will
	 * begin at the "start" vertex and conclude once the "end" vertex has been
	 * reached.
	 * </P>
	 * 
	 * <P>
	 * Before the search begins, this method will go through the collection of
	 * Observers, calling notifyBFSHasBegun on each one.
	 * </P>
	 * 
	 * <P>
	 * Just after a particular vertex is visited, this method will go through the
	 * collection of observers calling notifyVisit on each one (passing in the
	 * vertex being visited as the argument.)
	 * </P>
	 * 
	 * <P>
	 * After the "end" vertex has been visited, this method will go through the
	 * collection of observers calling notifySearchIsOver on each one, after which
	 * the method should terminate immediately, without processing further vertices.
	 * </P>
	 * 
	 * @param start vertex where search begins
	 * @param end   the algorithm terminates just after this vertex is visited
	 */
	public void DoBFS(V start, V end) {

		// Notify Beginning
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyBFSHasBegun();
		}

		// Initializes the visitedSet and queue, adding start to the queue
		Set<V> visitedSet = new HashSet<V>();
		Queue<V> queue = new LinkedList<>();
		queue.add(start);

		// Repeat until the queue is empty
		while (!queue.isEmpty()) {
			// Get first thing from queue and check if it already is in visited set
			V queueHead = queue.remove();
			if (!visitedSet.contains(queueHead)) {
				// Notify Visit
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(queueHead);
				}

				// Leave loop if the end was found
				if (queueHead.equals(end)) {
					break;
				}

				// Add the vertex to visitedSet
				visitedSet.add(queueHead);

				// Add each adjacency to the queue if it is not in visited set
				for (V adjacency : weightedGraph.get(queueHead).keySet()) {
					if (!visitedSet.contains(adjacency)) {
						queue.add(adjacency);
					}
				}
			}

		}

		// Notify End
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifySearchIsOver();
			;
		}
	}

	/**
	 * <P>
	 * This method will perform a Depth-First-Search on the graph. The search will
	 * begin at the "start" vertex and conclude once the "end" vertex has been
	 * reached.
	 * </P>
	 * 
	 * <P>
	 * Before the search begins, this method will go through the collection of
	 * Observers, calling notifyDFSHasBegun on each one.
	 * </P>
	 * 
	 * <P>
	 * Just after a particular vertex is visited, this method will go through the
	 * collection of observers calling notifyVisit on each one (passing in the
	 * vertex being visited as the argument.)
	 * </P>
	 * 
	 * <P>
	 * After the "end" vertex has been visited, this method will go through the
	 * collection of observers calling notifySearchIsOver on each one, after which
	 * the method should terminate immediately, without visiting further vertices.
	 * </P>
	 * 
	 * @param start vertex where search begins
	 * @param end   the algorithm terminates just after this vertex is visited
	 */
	public void DoDFS(V start, V end) {

		// Notify Beginning
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDFSHasBegun();
		}

		// Creates visitedSet and Stack, pushed the start value to the stack
		Set<V> visitedSet = new HashSet<V>();
		Stack<V> stack = new Stack<V>();
		stack.push(start);

		while (!stack.isEmpty()) {
			// Remove top value from stack
			V stackTop = stack.pop();

			if (!visitedSet.contains(stackTop)) {

				// Notify Visit
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(stackTop);
				}

				// If target found, leave the stack
				if (stackTop.equals(end)) {
					break;
				}

				// Add topValue to stack, loop through all adjacencies and add to stack if not
				// visited
				visitedSet.add(stackTop);
				for (V adjacency : weightedGraph.get(stackTop).keySet()) {
					if (!visitedSet.contains(adjacency)) {
						stack.push(adjacency);
					}
				}
			}
		}

		// Notify End
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifySearchIsOver();
		}
	}

	/**
	 * <P>
	 * Perform Dijkstra's algorithm, beginning at the "start" vertex.
	 * </P>
	 * 
	 * <P>
	 * The algorithm DOES NOT terminate when the "end" vertex is reached. It will
	 * continue until EVERY vertex in the graph has been added to the finished set.
	 * </P>
	 * 
	 * <P>
	 * Before the algorithm begins, this method goes through the collection of
	 * Observers, calling notifyDijkstraHasBegun on each Observer.
	 * </P>
	 * 
	 * <P>
	 * Each time a vertex is added to the "finished set", this method goes through
	 * the collection of Observers, calling notifyDijkstraVertexFinished on each one
	 * (passing the vertex that was just added to the finished set as the first
	 * argument, and the optimal "cost" of the path leading to that vertex as the
	 * second argument.)
	 * </P>
	 * 
	 * <P>
	 * After all of the vertices have been added to the finished set, the algorithm
	 * will calculate the "least cost" path of vertices leading from the starting
	 * vertex to the ending vertex. Next, it will go through the collection of
	 * observers, calling notifyDijkstraIsOver on each one, passing in as the
	 * argument the "lowest cost" sequence of vertices that leads from start to end
	 * (I.e. the first vertex in the list will be the "start" vertex, and the last
	 * vertex in the list will be the "end" vertex.)
	 * </P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end   special vertex used as the end of the path reported to observers
	 *              via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		// Notify Beginning
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraHasBegun();
		}

		// Creates finishedSet and maps for the predecessor and cost of each vertex
		HashSet<V> finishedSet = new HashSet<V>();
		HashMap<V, V> pred = new HashMap<V, V>();
		HashMap<V, Integer> cost = new HashMap<V, Integer>();

		// Sets map values to defaults
		for (V key : weightedGraph.keySet()) {
			pred.put(key, null);
			cost.put(key, Integer.MAX_VALUE);
		}

		// Makes the cost value of the start 0
		cost.put(start, 0);

		while (finishedSet.size() < pred.size()) {

			// Searches for the key with least cost
			V minKey = null;
			int minValue = Integer.MAX_VALUE;
			for (V key : weightedGraph.keySet()) {
				if (!finishedSet.contains(key) && cost.get(key) < minValue) {
					minKey = key;
					minValue = cost.get(minKey);
				}
			}

			// Add minKey to set
			finishedSet.add(minKey);

			// Notify Finished vertex and its cost
			for (GraphAlgorithmObserver<V> observer : observerList) {
				observer.notifyDijkstraVertexFinished(minKey, cost.get(minKey));
			}

			// Update the weights of all adjacencies
			for (V adjacency : weightedGraph.get(minKey).keySet()) {
				if (!finishedSet.contains(adjacency)) {
					// new updated cost is the cost of the path plus the cost of the total path to
					// the predecessor
					int totalCost = cost.get(minKey) + this.getWeight(minKey, adjacency);
					if (totalCost < cost.get(adjacency)) {
						cost.replace(adjacency, totalCost);
						pred.replace(adjacency, minKey);
					}
				}
			}

		}
		// Creates an array for best path
		ArrayList<V> bestPath = new ArrayList<V>();

		// Starts at the end goes through the predecessors until the start, adding them
		// to best path
		V nextVertex = end;
		bestPath.add(0, end);

		while (!nextVertex.equals(start)) {
			nextVertex = pred.get(nextVertex);
			bestPath.add(0, nextVertex);
		}

		// Notify that Dijkstra is over and pass in the best path
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraIsOver(bestPath);
		}
	}

}
