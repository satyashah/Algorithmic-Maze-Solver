package graph;

import java.util.HashSet;
import java.util.Set;

import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

/**
 * <P>
 * The MazeGraph is an extension of WeightedGraph. The constructor converts a
 * Maze into a graph.
 * </P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/*
	 * STUDENTS: SEE THE PROJECT DESCRIPTION FOR A MUCH MORE DETAILED EXPLANATION
	 * ABOUT HOW TO WRITE THIS CONSTRUCTOR
	 */

	/**
	 * <P>
	 * Construct the MazeGraph using the "maze" contained in the parameter to
	 * specify the vertices (Junctures) and weighted edges.
	 * </P>
	 * 
	 * <P>
	 * The Maze is a rectangular grid of "junctures", each defined by its X and Y
	 * coordinates, using the usual convention of (0, 0) being the upper left
	 * corner.
	 * </P>
	 * 
	 * <P>
	 * Each juncture in the maze should be added as a vertex to this graph.
	 * </P>
	 * 
	 * <P>
	 * For every pair of adjacent junctures (A and B) which are not blocked by a
	 * wall, two edges should be added: One from A to B, and another from B to A.
	 * The weight to be used for these edges is provided by the Maze. (The Maze
	 * methods getMazeWidth and getMazeHeight can be used to determine the number of
	 * Junctures in the maze. The Maze methods called "isWallAbove",
	 * "isWallToRight", etc. can be used to detect whether or not there is a wall
	 * between any two adjacent junctures. The Maze methods called "getWeightAbove",
	 * "getWeightToRight", etc. should be used to obtain the weights.)
	 * </P>
	 * 
	 * @param maze to be used as the source of information for adding vertices and
	 *             edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		super(); // Call to the constructor of the WeightedGraph class

		int mazeHeight = maze.getMazeHeight();
		int mazeWidth = maze.getMazeWidth();

		// Parses through all the X and Y values of the maze
		for (int X = 0; X < mazeWidth; X++) {
			for (int Y = 0; Y < mazeHeight; Y++) {

				// Check if the activeJuncture is already in graph, if not it will be added
				Juncture mainJuncture = new Juncture(X, Y);
				if (!super.containsVertex(mainJuncture)) {
					super.addVertex(mainJuncture);
				}

				// Checks if can add a juncture above
				if (!maze.isWallAbove(mainJuncture) && Y > 0) {
					Juncture addJuncture = new Juncture(X, Y - 1);
					if (!super.containsVertex(addJuncture)) {
						super.addVertex(addJuncture);
					}
					super.addEdge(mainJuncture, addJuncture, maze.getWeightAbove(mainJuncture));
				}

				// Checks if can add a juncture below
				if (!maze.isWallBelow(mainJuncture) && Y < mazeHeight) {
					Juncture addJuncture = new Juncture(X, Y + 1);
					if (!super.containsVertex(addJuncture)) {
						super.addVertex(addJuncture);
					}
					super.addEdge(mainJuncture, addJuncture, maze.getWeightBelow(mainJuncture));
				}

				// Checks if can add a juncture to right
				if (!maze.isWallToRight(mainJuncture) && X < mazeWidth) {
					Juncture addJuncture = new Juncture(X + 1, Y);
					if (!super.containsVertex(addJuncture)) {
						super.addVertex(addJuncture);
					}
					super.addEdge(mainJuncture, addJuncture, maze.getWeightToRight(mainJuncture));
				}

				// Checks if can add a juncture to left
				if (!maze.isWallToLeft(mainJuncture) && X > 0) {
					Juncture addJuncture = new Juncture(X - 1, Y);
					if (!super.containsVertex(addJuncture)) {
						super.addVertex(addJuncture);
					}
					super.addEdge(mainJuncture, addJuncture, maze.getWeightToLeft(mainJuncture));
				}

				// Dijkstra's, Breadth First Traversal, Depths First Traversal

			}
		}

	}

}
