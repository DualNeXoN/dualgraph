package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.LinkedList;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmInterruptedException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.NoVerticesException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.UnsupportedGraphException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.lib.visualization.GraphMask;
import sk.dualnexon.dualgraph.util.Logger;

public class MaxSpanningTreePrim extends Algorithm {
	
	private static final String ALGORITHM_NAME = "Prim Max Spanning Tree";
	private static final String MASK_MESSAGE = "Edge with maximal weight of adjacent vertices (%s) added to spanning tree";
	
	private AdjacencyList adj;
	private AdjacencyList adjMask;
	
	public MaxSpanningTreePrim(Graph graph) {
		super(graph);
		name = ALGORITHM_NAME;
		adj = graph.getAdjacencyList();
		adjMask = new AdjacencyList();
	}

	public void calculate() throws AlgorithmException {
		
		if(graph.getVertices().size() == 0) {
			throw new NoVerticesException(this);
		}
		
		Vertex startVertex = getStartingVertex();
		
		if(startVertex == null) {
			throw new AlgorithmInterruptedException(this);
		}
		
		// Map of used vertices
		HashMap<Vertex, Boolean> used = new HashMap<>();
		for(Vertex vertex : graph.getVertices()) {
			used.put(vertex, false);
		}
		used.put(startVertex, true);
		
		LinkedList<Edge> outputListEdges = new LinkedList<>(); // Output MST edges by order
		LinkedList<Edge> queue = new LinkedList<>(); // Queue of edges which are discovered
		
		// Add starting vertex to queue
		for(Vertex vertex : adj.getVertexList(startVertex).keySet()) {
			queue.addLast(graph.getEdgeByEndPoints(startVertex, vertex));
		}
		
		while(!usedAll(used)) {
			
			Edge nextAddedEdge = null;
			
			if(queue.size() == 1) {
				nextAddedEdge = queue.get(0);
				queue.remove(0);
			} else if(queue.size() > 1) {
				nextAddedEdge = queue.get(0);
				for(int index = 1; index < queue.size(); index++) {
					if(queue.get(index).getValue() > nextAddedEdge.getValue()) {
						nextAddedEdge = queue.get(index);
					}
				}
				queue.remove(nextAddedEdge);
			} else {
				throw new UnsupportedGraphException(this);
			}
			
			if(nextAddedEdge != null) {
				used.put(nextAddedEdge.getFirstVertex(), true);
				used.put(nextAddedEdge.getSecondVertex(), true);
				outputListEdges.addLast(nextAddedEdge);
				GraphMask mask = new GraphMask(graph, String.format(MASK_MESSAGE, nextAddedEdge.toString()));
				visualizer.addMask(mask);
				adjMask.addEdge(nextAddedEdge);
				mask.applyMask(adjMask.clone());
				visualizer.applyLastMask();
				nextAddedEdge = null;
			}
			
			queue.clear();
			for(Vertex vertex : used.keySet()) {
				if(used.get(vertex)) {
					for(Vertex vertex2 : adj.getVertexList(vertex).keySet()) {
						if(!used.get(vertex2)) {
							queue.addLast(graph.getEdgeByEndPoints(vertex, vertex2));
						}
					}
				}
			}
			
		}
		
		int sum = 0;
		for(Edge edge : outputListEdges) {
			sum += edge.getValue();
		}
		Logger.log(toString() + " Tree weight: " + sum);
		
		finished();
	}
	
	private boolean usedAll(HashMap<Vertex, Boolean> used) {
		for(Vertex vertex : used.keySet()) {
			if(!used.get(vertex)) return false;
		}
		return true;
	}

}
