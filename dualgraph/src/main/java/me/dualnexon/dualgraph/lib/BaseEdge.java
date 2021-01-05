package me.dualnexon.dualgraph.lib;

public abstract class BaseEdge extends BaseNode {
	
	protected Vertex[] vertices = new Vertex[2];
	protected Namespace namespace;
	protected int edgeValue;
	
	public BaseEdge(Vertex v1, Vertex v2, int edgeValue) {
		vertices[0] = v1;
		vertices[1] = v2;
		this.edgeValue = edgeValue;
	}
	
	public BaseEdge(Vertex v1, Vertex v2) {
		this(v1, v2, 1);
	}
	
	public Vertex getFirstVertex() {
		return vertices[0];
	}
	
	public Vertex getSecondVertex() {
		return vertices[1];
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Vertex getVertexByIndex(int index) {
		return index == 0 ? getFirstVertex() : getSecondVertex();
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public int getEdgeValue() {
		return edgeValue;
	}
	
	public void setEdgeValue(int edgeValue) {
		this.edgeValue = edgeValue;
		namespace.setText(Integer.toString(edgeValue));
	}
	
	public boolean hasVertex(Vertex v) {
		return (vertices[0].equals(v)) || (vertices[1].equals(v));
	}
	
	@Override
	public void destroy() {
		namespace.destroy();
	}
	
}
