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
	
	private AdjacencyList adjMask;
	private Vertex startVertex = null;
	private int countOfDiscoveredVertices = -1;
	
	public BFS(Graph graph) {
		super(graph);
		adjMask = new AdjacencyList();
	}
	
	public void calculate() throws AlgorithmException {
		
		if(graph.getVertices().size() == 0) {
			throw new NoVerticesException(this);
		}
		
		if(startVertex == null) {
			startVertex = getStartingVertex();
			if(startVertex == null) {
				return;
			}
		}
		
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        for(Vertex v : graph.getVertices()) {
        	visited.put(v, false);
        }
 
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
 
        visited.put(startVertex, true);
        queue.add(startVertex);
        countOfDiscoveredVertices = 1;
 
        while(queue.size() != 0) {
        	
            startVertex = queue.poll();
            System.out.print(startVertex.getNamespace().getText() + " ");
            GraphMask mask = new GraphMask(graph);
			visualizer.addMask(mask);
			adjMask.addVertex(startVertex);
			mask.applyMask(adjMask.clone());
			visualizer.applyLastMask();
 
            Iterator<Vertex> i = graph.getAdjacencyList().getVertexList(startVertex).keySet().iterator();
            while(i.hasNext()) {
                Vertex n = i.next();
                if(!visited.get(n)) {
                    visited.put(n, true);
                    queue.add(n);
                    countOfDiscoveredVertices++;
                }
            }
        }
        
        System.out.println();
		
        finished();
	}
	
	public void setStartVertex(Vertex startVertex) {
		this.startVertex = startVertex;
	}
	
	public int getCountOfDiscoveredVertices() {
		return countOfDiscoveredVertices;
	}
	
}
