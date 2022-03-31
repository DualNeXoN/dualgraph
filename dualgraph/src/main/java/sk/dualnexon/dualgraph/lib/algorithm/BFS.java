package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.NoVerticiesException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.lib.visualization.GraphMask;

public class BFS extends Algorithm {
	
	private AdjacencyList adjMask;
	
	public BFS(Graph graph) {
		super(graph);
		adjMask = new AdjacencyList();
	}
	
	public void calculate() throws AlgorithmException {
		
		if(graph.getVerticies().size() == 0) {
			throw new NoVerticiesException(this);
		}
		
		Vertex s = getStartingVertex();
		
		if(s == null) {
			return;
		}
		
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        for(Vertex v : graph.getVerticies()) {
        	visited.put(v, false);
        }
 
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
 
        visited.put(s, true);
        queue.add(s);
 
        while(queue.size() != 0) {
        	
            s = queue.poll();
            System.out.print(s.getNamespace().getText() + " ");
            GraphMask mask = new GraphMask(graph);
			visualizer.addMask(mask);
			adjMask.addVertex(s);
			mask.applyMask(adjMask.clone());
			visualizer.applyLastMask();
 
            Iterator<Vertex> i = graph.getAdjacencyList().getVertexList(s).keySet().iterator();
            while(i.hasNext()) {
                Vertex n = i.next();
                if(!visited.get(n)) {
                    visited.put(n, true);
                    queue.add(n);
                }
            }
        }
        
        System.out.println();
		
        finished();
	}
	
}
