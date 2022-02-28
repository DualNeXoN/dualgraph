package sk.dualnexon.dualgraph.lib;

import java.util.LinkedList;

import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.window.Workspace;

public class Graph implements Updatable {
	
	private static final int ZOOM_FACTOR = 2;
	
	private Workspace workspace;
	private LinkedList<Vertex> verticies;
	private LinkedList<Edge> edges;
	
	private Vertex startingVertex, endingVertex;
	
	public Graph(Workspace workspace) {
		this.workspace = workspace;
		verticies = new LinkedList<>();
		edges = new LinkedList<>();
	}
	
	public void addVertex(Vertex vertex) {
		verticies.add(vertex);
		workspace.addNode(vertex.getNode());
		update();
	}
	
	public void removeVertex(Vertex vertex) {
		verticies.remove(vertex);
		for(int index = edges.size()-1; index >= 0; index--) {
			Edge edge = edges.get(index);
			if(edge.getFirstVertex().equals(vertex) || edge.getSecondVertex().equals(vertex)) {
				edge.destroy();
			}
		}
		update();
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
		update();
	}
	
	public void removeEdge(Edge edge) {
		edges.remove(edge);
		update();
	}
	
	public LinkedList<Vertex> getVerticies() {
		return verticies;
	}
	
	public LinkedList<Edge> getEdges() {
		return edges;
	}
	
	public Vertex getStartingVertex() {
		return startingVertex;
	}
	
	public void setStartingVertex(Vertex startingVertex) {
		this.startingVertex = startingVertex;
	}
	
	public Vertex getEndingVertex() {
		return endingVertex;
	}
	
	public void setEndingVertex(Vertex endingVertex) {
		this.endingVertex = endingVertex;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	public void zoomIn() {
		for(Vertex vertex : verticies) {
			vertex.setSize(vertex.getSize()+ZOOM_FACTOR);
		}
	}
	
	public void zoomOut() {
		for(Vertex vertex : verticies) {
			vertex.setSize(vertex.getSize()-ZOOM_FACTOR);
		}
	}
	
	@Override
	public void destroy() {
		workspace.getGrid().destroy();
		for(Edge edge : edges) edge.destroy();
		for(Vertex vertex : verticies) vertex.destroy();
	}

	@Override
	public void update() {
		workspace.getGrid().update();
		for(Edge edge : edges) edge.update();
		for(Vertex vertex : verticies) vertex.update();
	}
	
}
