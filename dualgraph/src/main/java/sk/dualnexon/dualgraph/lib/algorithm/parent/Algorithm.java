package sk.dualnexon.dualgraph.lib.algorithm.parent;

import sk.dualnexon.dualgraph.lib.Graph;

public abstract class Algorithm {
	
	protected Graph graph;
	
	public Algorithm(Graph graph) {
		this.graph = graph;
	}
	
	public Graph getGraph() {
		return graph;
	}
	
}
