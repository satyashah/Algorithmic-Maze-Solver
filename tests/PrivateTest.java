package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import graph.WeightedGraph;

public class PrivateTest {

	@Test
	public void testAddVertexAndContainsVertex() {
		WeightedGraph<String> graph = new WeightedGraph<String>();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		graph.addVertex("F");
		graph.addVertex("G");
		
		graph.addEdge("A", "B", 1);
		graph.addEdge("A", "C", 2);
		
		graph.addEdge("B", "D", 3);
		graph.addEdge("B", "E", 4);
		
		graph.addEdge("C", "F", 5);
		
		graph.addEdge("D", "G", 2);
		graph.addEdge("E", "G", 3);
		graph.addEdge("F", "G", 4);
		
		System.out.println("DIJSKTRA");
		graph.DoDijsktra("A", "G");
		
		System.out.println("DFS");
//		graph.DoDFS("A", "D");
		
		System.out.println("BFS");
//		graph.DoBFS("A", "G");
	}
	
}