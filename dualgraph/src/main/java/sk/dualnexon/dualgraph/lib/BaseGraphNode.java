package sk.dualnexon.dualgraph.lib;

import java.util.UUID;

import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.util.Logger;
import sk.dualnexon.dualgraph.window.Workspace;

public abstract class BaseGraphNode implements Updatable {
	
	protected boolean selected = false;
	protected Graph graph;
	protected String uuid;
	
	public BaseGraphNode(Graph graph) {
		this.graph = graph;
		uuid = UUID.randomUUID().toString();
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
	
	public String getUUID() {
		return uuid;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public abstract double getRealPositionX();
	public abstract double getRealPositionY();
	
	@Override
	public void destroy() {
		Logger.log("Destroyed " + toString() + " (" + uuid + ") from " + graph.getClass().getSimpleName() + " (" + graph.hashCode() + ")");
	}
	
}
