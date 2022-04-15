package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmInterruptedException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.NoVerticesException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.lib.visualization.GraphMask;

public class DFS extends Algorithm {
	
	private static final String ALGORITHM_NAME = "Depth-first Search";
	private static final String MASK_MESSAGE_START = "Starting in vertex %s";
	private static final String MASK_MESSAGE_DISCOVER = "Vertex %s discovered from vertex %s";
	
	private AdjacencyList adjMask;
	private boolean firstWasDiscovered;
	
	public DFS(Graph graph) {
		super(graph);
		name = ALGORITHM_NAME;
		adjMask = new AdjacencyList();
	}
	
	public void calculate() throws AlgorithmException {
		
		if(graph.getVertices().size() == 0) {
			throw new NoVerticesException(this);
		}
		
		Vertex s = getStartingVertex();
		
		if(s == null) {
			throw new AlgorithmInterruptedException(this);
		}
		
		firstWasDiscovered = false;
		
		HashMap<Vertex, Boolean> visited = new HashMap<>();
        for(Vertex v : graph.getVertices()) {
        	visited.put(v, false);
        }
        
        recursive(s, s, visited);
        System.out.println();
        finished();
		
	}
	
	private void recursive(Vertex r, Vertex v, HashMap<Vertex, Boolean> visited) {
		
		visited.put(v, true);
        String outputMessage = String.format((firstWasDiscovered ? MASK_MESSAGE_DISCOVER : MASK_MESSAGE_START), v.getNamespace().getText(), r.getNamespace().getText());
        firstWasDiscovered = true;
        System.out.println(outputMessage);
        GraphMask mask = new GraphMask(graph, outputMessage);
		visualizer.addMask(mask);
		adjMask.addVertex(v);
		if(r != v) adjMask.addEdge(graph.getEdgeByEndPoints(r, v));
		mask.applyMask(adjMask.clone());
		visualizer.applyLastMask();
 
        Iterator<Vertex> i = graph.getAdjacencyList().getVertexList(v).keySet().iterator();
        while(i.hasNext()) {
            Vertex n = i.next();
            if(!visited.get(n)) {
            	recursive(v, n, visited);
            }
        }
		
	}
	
}
