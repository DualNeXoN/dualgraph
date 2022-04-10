package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;

import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.NoVerticesException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.lib.visualization.GraphMask;

public class DFS extends Algorithm {
	
	private AdjacencyList adjMask;
	private Vertex startVertex = null;
	private int countOfDiscoveredVertices = -1;
	
	public DFS(Graph graph) {
		super(graph);
		adjMask = new AdjacencyList();
	}
	
	public void calculate() throws AlgorithmException {
		
		countOfDiscoveredVertices = 0;
		
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
        
        recursive(startVertex, visited);
        System.out.println();
        finished();
		
	}
	
	private void recursive(Vertex v, HashMap<Vertex, Boolean> visited) {
		countOfDiscoveredVertices++;
		visited.put(v, true);
        System.out.print(v.getNamespace().getText() + " ");
        GraphMask mask = new GraphMask(graph);
		visualizer.addMask(mask);
		adjMask.addVertex(v);
		mask.applyMask(adjMask.clone());
		visualizer.applyLastMask();
 
        Iterator<Vertex> i = graph.getAdjacencyList().getVertexList(v).keySet().iterator();
        while(i.hasNext()) {
            Vertex n = i.next();
            if(!visited.get(n)) {
            	recursive(n, visited);
            }
        }
		
	}
	
	public void setStartVertex(Vertex startVertex) {
		this.startVertex = startVertex;
	}
	
	public int getCountOfDiscoveredVertices() {
		return countOfDiscoveredVertices;
	}
	
}
