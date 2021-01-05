package me.dualnexon.dualgraph.lib;

public class EdgeSelf extends BaseEdge {

	public EdgeSelf(Vertex v1, Vertex v2) {
		super(v1, v2);
	}
	
	public EdgeSelf(Vertex v1, Vertex v2, int value) {
		super(v1, v2, value);
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
}
