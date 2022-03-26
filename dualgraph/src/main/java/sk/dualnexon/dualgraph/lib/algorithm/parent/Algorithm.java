package sk.dualnexon.dualgraph.lib.algorithm.parent;

import sk.dualnexon.dualgraph.window.Workspace;

public abstract class Algorithm {
	
	protected Workspace workspace;
	
	public Algorithm(Workspace workspace) {
		this.workspace = workspace;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
}
