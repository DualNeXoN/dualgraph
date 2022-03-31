package sk.dualnexon.dualgraph.lib;

import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.window.Workspace;

public abstract class BaseGraphNode implements Updatable {
	
	protected boolean selected = false;
	protected Graph graph;
	
	public BaseGraphNode(Graph graph) {
		this.graph = graph;
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public Workspace getWorkspace() {
		return graph.getWorkspace();
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void select() {
		if(graph.isLocked()) return;
		selected = true;
		graph.update();
	}
	
	public void toggleSelected() {
		if(graph.isLocked()) return;
		this.selected = !this.selected;
		graph.update();
	}
	
	public abstract double getRealPositionX();
	public abstract double getRealPositionY();
	
}
