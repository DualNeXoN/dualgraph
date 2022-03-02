package sk.dualnexon.dualgraph.lib;

import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.window.Workspace;

public abstract class BaseGraphNode implements Updatable {
	
	protected Graph graph;
	
	public Graph getGraph() {
		return graph;
	}
	
	public Workspace getWorkspace() {
		return graph.getWorkspace();
	}
	
	public abstract double getRealPositionX();
	public abstract double getRealPositionY();
	
}
