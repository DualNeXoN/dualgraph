package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.NoVerticesException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.lib.visualization.GraphMask;

public class BridgeDetection extends Algorithm {
	
	private static final String ALGORITHM_NAME = "Bridge Detection";
	
	private AdjacencyList adj;
	private AdjacencyList adjMask;
	private int time = 0;
	private LinkedList<Edge> bridges;
	
	public BridgeDetection(Graph graph) {
		super(graph);
		name = ALGORITHM_NAME;
		adj = graph.getAdjacencyList();
		adjMask = new AdjacencyList();
	}
	
	public void calculate() throws AlgorithmException {
		
		if(graph.getVertices().size() == 0) {
			throw new NoVerticesException(this);
		}
		
		bridges = new LinkedList<>();
		
		HashMap<Vertex, Boolean> visited = new HashMap<>();
		HashMap<Vertex, Integer> disc = new HashMap<Vertex, Integer>();
		HashMap<Vertex, Integer> low = new HashMap<Vertex, Integer>();
		HashMap<Vertex, Vertex> parent = new HashMap<Vertex, Vertex>();
		
		for(Vertex vertex : graph.getVertices()) {
			visited.put(vertex, false);
		}
		
		for(Vertex vertex : graph.getVertices()) {
			if(!visited.get(vertex)) {
				recursive(vertex, visited, disc, low, parent);
			}
		}
		
		GraphMask mask = new GraphMask(graph, (bridges.size() == 0 ? "V grafe neexistujú žiadne mosty" : "V grafe existuje mosty: " + getBridgesAsStringList()));
		visualizer.addMask(mask);
		mask.applyMask(adjMask.clone());
		visualizer.applyLastMask();
		
		finished();
				
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
					Edge bridge = graph.getEdgeByEndPoints(vertex, adjacentVertex);
					adjMask.addEdge(bridge);
					bridges.add(bridge);
				}
			} else if(!adjacentVertex.equals(parent.get(vertex))) { // Update low value of u for parent function calls.
				low.put(vertex, Math.min(low.get(vertex), disc.get(adjacentVertex)));
			}
		}
		
	}
	
	private String getBridgesAsStringList() {
		String output = "";
		
		for(Edge bridge : bridges) {
			output += bridge.toString() + ", ";
		}
		
		return output.substring(0, output.length()-2);
	}

}
