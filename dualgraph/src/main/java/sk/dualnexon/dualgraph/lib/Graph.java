package sk.dualnexon.dualgraph.lib;

import java.util.LinkedList;

import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.window.Workspace;

public class Graph implements Updatable {
	
	private static final int ZOOM_FACTOR = 2;
	
	private Workspace workspace;
	private LinkedList<Vertex> vertices;
	private LinkedList<Edge> edges;
	
	private Vertex startingVertex, endingVertex;
	
	private boolean locked = false;
	
	public Graph(Workspace workspace) {
		this.workspace = workspace;
		vertices = new LinkedList<>();
		edges = new LinkedList<>();
	}
	
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
		workspace.addNode(vertex.getNode());
		update();
	}
	
	public void removeVertex(Vertex vertex) {
		if(startingVertex != null && startingVertex.equals(vertex)) startingVertex = null;
		else if(endingVertex != null && endingVertex.equals(vertex)) endingVertex = null;
		vertices.remove(vertex);
		
		for(int index = edges.size()-1; index >= 0; index--) {
			if(edges.size() == 0) break;
			Edge edge = edges.get(index);
			if(edge.getFirstVertex().equals(vertex) || edge.getSecondVertex().equals(vertex)) {
				edge.destroy();
			}
		}
		
		if(vertex.isSelected()) {
			removeAllSelected();
		}
		
		update();
	}
	
	public void addEdge(Edge edge) {
		for(Edge edgeInstance : edges) {
			if(edge.getFirstVertex().equals(edgeInstance.getFirstVertex()) && edge.getSecondVertex().equals(edgeInstance.getSecondVertex()) || edge.getFirstVertex().equals(edgeInstance.getSecondVertex()) && edge.getSecondVertex().equals(edgeInstance.getFirstVertex())) {
				edge.destroy();
				return;
			}
		}
		edges.add(edge);
		update();
	}
	
	public void removeEdge(Edge edge) {
		edges.remove(edge);
		
		if(edge.isSelected()) {
			removeAllSelected();
		}
		
		update();
	}
	
	public Edge getEdgeByEndPoints(Vertex vertex1, Vertex vertex2) {
		
		for(Edge edge : edges) {
			if((edge.getFirstVertex().equals(vertex1) && edge.getSecondVertex().equals(vertex2)) || (edge.getFirstVertex().equals(vertex2) && edge.getSecondVertex().equals(vertex1))) {
				return edge;
			}
		}
		
		return null;
	}
	
	private void removeAllSelected() {
		LinkedList<Vertex> selectedVertices = getSelectedVertices();
		if(selectedVertices.size() > 0) selectedVertices.get(0).destroy();
		LinkedList<Edge> selectedEdges = getSelectedEdges();
		if(selectedEdges.size() > 0) selectedEdges.get(0).destroy();
	}
	
	public LinkedList<Vertex> getVertices() {
		return vertices;
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
	
	public LinkedList<Vertex> getSelectedVertices() {
		LinkedList<Vertex> list = new LinkedList<>();
		
		for(Vertex vertex : vertices) {
			if(vertex.isSelected()) list.add(vertex);
		}
		
		return list;
	}
	
	public LinkedList<Edge> getSelectedEdges() {
		LinkedList<Edge> list = new LinkedList<>();
		
		for(Edge edge : edges) {
			if(edge.isSelected()) list.add(edge);
		}
		
		return list;
	}
	
	public void zoomIn() {
		for(Vertex vertex : vertices) {
			vertex.setSize(vertex.getSize()+ZOOM_FACTOR);
		}
	}
	
	public void zoomOut() {
		for(Vertex vertex : vertices) {
			vertex.setSize(vertex.getSize()-ZOOM_FACTOR);
		}
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void unselectAll() {
		for(BaseGraphNode graphNode : vertices) {
			if(graphNode.isSelected()) graphNode.toggleSelected();
		}
		for(BaseGraphNode graphNode : edges) {
			if(graphNode.isSelected()) graphNode.toggleSelected();
		}
	}
	
	public AdjacencyList getAdjacencyList() {
		AdjacencyList adj = new AdjacencyList();
		
		for(Vertex v : vertices) {
			adj.addVertex(v);
		}
		
		for(Edge e : edges) {
			adj.addEdge(e);
		}
		
		return adj;
	}
	
	@Override
	public void destroy() {
		for(int index = edges.size()-1; index >= 0; index--) {
			edges.get(index).destroy();
		}
		for(int index = vertices.size()-1; index >= 0; index--) {
			vertices.get(index).destroy();
		}
	}

	@Override
	public void update() {
		workspace.getGrid().update();
		for(Edge edge : edges) edge.update();
		for(Vertex vertex : vertices) vertex.update();
	}
	
}
