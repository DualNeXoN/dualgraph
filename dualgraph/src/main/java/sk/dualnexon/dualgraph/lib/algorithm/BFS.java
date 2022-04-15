package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.NoVerticesException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.lib.visualization.GraphMask;

public class BFS extends Algorithm {
	
	private static final String ALGORITHM_NAME = "Breadth-first Search";
	private static final String MASK_MESSAGE_START = "Starting in vertex %s";
	private static final String MASK_MESSAGE_DISCOVER = "Vertex %s discovered from vertex %s";
	
	private AdjacencyList adjMask;
	private boolean firstWasDiscovered;
	
	public BFS(Graph graph) {
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
			throw new AlgorithmException(this);
		}
		
		firstWasDiscovered = false;
		
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        for(Vertex v : graph.getVertices()) {
        	visited.put(v, false);
        }
 
        LinkedList<Vertex[]> queue = new LinkedList<>();
 
        visited.put(s, true);
        Vertex[] map = {s, s};
        queue.add(map);
 
        while(queue.size() != 0) {
        	
            map = queue.poll();
            Vertex r = map[0];
            s = map[1];
            String outputMessage = String.format((firstWasDiscovered ? MASK_MESSAGE_DISCOVER : MASK_MESSAGE_START), s.getNamespace().getText(), r.getNamespace().getText());
            firstWasDiscovered = true;
            System.out.println(outputMessage);
            GraphMask mask = new GraphMask(graph, outputMessage);
			visualizer.addMask(mask);
			adjMask.addVertex(s);
			if(s != r) adjMask.addEdge(graph.getEdgeByEndPoints(s, r));
			mask.applyMask(adjMask.clone());
			visualizer.applyLastMask();
 
            Iterator<Vertex> i = graph.getAdjacencyList().getVertexList(s).keySet().iterator();
            while(i.hasNext()) {
                Vertex n = i.next();
                if(!visited.get(n)) {
                    visited.put(n, true);
                    queue.add(new Vertex[] {s, n});
                }
            }
        }
        
        System.out.println();
		
        finished();
	}
	
}
