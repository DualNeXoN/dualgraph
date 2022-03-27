package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class BridgeDetection extends Algorithm {
	
	private AdjacencyList adj;
	private int time = 0;
	
	public BridgeDetection(Graph graph) {
		super(graph);
		adj = graph.getAdjacencyList();
	}
	
	public void calculate() {
		
		HashMap<Vertex, Boolean> visited = new HashMap<>();
		HashMap<Vertex, Integer> disc = new HashMap<Vertex, Integer>();
		HashMap<Vertex, Integer> low = new HashMap<Vertex, Integer>();
		HashMap<Vertex, Vertex> parent = new HashMap<Vertex, Vertex>();
		
		for(Vertex vertex : graph.getVerticies()) {
			visited.put(vertex, false);
		}
		
		for(Vertex vertex : graph.getVerticies()) {
			if(!visited.get(vertex)) {
				recursive(vertex, visited, disc, low, parent);
			}
		}
				
	}

	private void recursive(Vertex vertex, HashMap<Vertex, Boolean> visited, HashMap<Vertex, Integer> disc, HashMap<Vertex, Integer> low, HashMap<Vertex, Vertex> parent) {
		visited.put(vertex, true);

		// Initialize discovery time and low value
		low.put(vertex, ++time);
		disc.put(vertex, low.get(vertex));

		// Go through all vertices adjacent to vertex
		Iterator<Vertex> iterator = adj.getVertexList(vertex).keySet().iterator();
		while(iterator.hasNext()) {
			Vertex adjacentVertex = iterator.next();

			// If v is not visited yet, then make it a child of u in DFS tree and recur for it. If v is not visited yet, then recur for it
			if(!visited.get(adjacentVertex)) {
				parent.put(adjacentVertex, vertex);
				recursive(adjacentVertex, visited, disc, low, parent);

				// Check if the subtree rooted with v has a connection to one of the ancestors of u
				low.put(vertex, Math.min(low.get(vertex), low.get(adjacentVertex)));

				// If the lowest vertex reachable from subtree under v is below u in DFS tree, then u-v is a bridge
				if(low.get(adjacentVertex) > disc.get(vertex)) {
					System.out.println(vertex.getNamespace().getText() + " " + adjacentVertex.getNamespace().getText());
				}
			} else if(!adjacentVertex.equals(parent.get(vertex))) { // Update low value of u for parent function calls.
				low.put(vertex, Math.min(low.get(vertex), disc.get(adjacentVertex)));
			}
		}
	}

}
