package me.dualnexon.dualgraph.lib;

public abstract class BaseNode {
	
	protected boolean selected = false;
	
	public void select(boolean value) {
		selected = value;
		Graph.get().render();
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public abstract void destroy();
	
}
