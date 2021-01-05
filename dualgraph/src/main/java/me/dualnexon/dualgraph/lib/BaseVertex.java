package me.dualnexon.dualgraph.lib;

import me.dualnexon.dualgraph.app.GraphStage;

public abstract class BaseVertex extends BaseNode {
	
	protected double x;
	protected double y;
	
	public final double getX() {
		return x;
	}
	
	public final double getY() {
		return y;
	}
	
	public final double getRealX() {
		return x - GraphStage.get().getOffsetX();		
	}
	
	public final double getRealY() {
		return y - GraphStage.get().getOffsetY();
	}
	
}
