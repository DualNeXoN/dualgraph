package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;

import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class CycleDetection extends Algorithm {
	
	public CycleDetection(Graph graph) {
		super(graph);
	}
	
	public boolean calculate() {

		HashMap<Vertex, Boolean> visited = new HashMap<>();
		for(Vertex v : graph.getVerticies()) {
			visited.put(v, false);
		}

		HashMap<Vertex, Boolean> recStack = new HashMap<>();
		for(Vertex v : graph.getVerticies()) {
			recStack.put(v, false);
		}

		for(Vertex v : graph.getVerticies()) {
			if(recursive(v, visited, recStack)) return true;
		}

		return false;
	}
	
	private boolean recursive(Vertex v, HashMap<Vertex, Boolean> visited, HashMap<Vertex, Boolean> recStack) {

		if(recStack.get(v)) return true;
		if(visited.get(v)) return false;
		
		visited.put(v, true);
		recStack.put(v, true);
		
		for(Vertex child : graph.getAdjacencyList().getVertexList(v).keySet()) {
			if(recursive(child, visited, recStack)) return true;
		}

		recStack.put(v, false);

		return false;

	}
	
}
