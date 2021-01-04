package me.dualnexon.dualgraph.lib;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {
	
	private static Graph instance;
	
	public static Graph get() {
		return instance;
	}
	
	private LinkedList<Vertex> vertices;
	private LinkedList<Edge> edges;
	
	public Graph() {
		
		if(instance == null) instance = this;
		else return;
		
		vertices = new LinkedList<>();
		edges = new LinkedList<>();
		
	}
	
	public void addVertex(Vertex vertex) {
		System.out.println("Added vertex " + vertex.getName());
		vertices.add(vertex);
		render();
	}
	
	public void addEdge(Edge edge) {
		System.out.println("Added edge " + edge.getFirstVertex().getName() + "-" + edge.getSecondVertex().getName());
		edges.add(edge);
		render();
	}
	
	public void render() {
		renderEdges();
		renderVertices();
	}
	
	public void renderVertices() {
		for(Vertex vertex : vertices) {
			vertex.render();
		}
	}
	
	public void renderEdges() {
		for(Edge edge : edges) {
			edge.render();
		}
	}
	
	public void clearSelection() {
		
		for(Vertex vertex : vertices) {
			vertex.select(false);
		}
		
		for(Edge edge : edges) {
			edge.select(false);
		}
		
		render();
		
	}
	
	public void destroyVertex(Vertex vertex) {
		vertex.destroy();
		vertices.remove(vertex);
		List<Edge> edgesOfCurrentVertex = getEdgesOfVertex(vertex);
		for(Edge edge : edgesOfCurrentVertex) {
			destroyEdge(edge);
		}
	}
	
	public void destroyEdge(Edge edge) {
		edge.destroy();
		edges.remove(edge);
	}
	
	public List<Edge> getEdgesOfVertex(Vertex vertex) {
		
		List<Edge> list = new ArrayList<>();
		
		for(Edge edge : edges) {
			if(edge.hasVertex(vertex)) list.add(edge);
		}
		
		return list;
	}
	
	public void deleteAllSelected() {
		
		for(int index = edges.size()-1; index >= 0; index--) {
			if(edges.get(index).isSelected()) destroyEdge(edges.get(index));
		}
		
		for(int index = vertices.size()-1; index >= 0; index--) {
			if(vertices.get(index).isSelected()) destroyVertex(vertices.get(index));
		}
		
	}
	
	public void reset() {
		for(int index = edges.size()-1; index >= 0; index--) destroyEdge(edges.get(index));
		for(int index = vertices.size()-1; index >= 0; index--) destroyVertex(vertices.get(index));
		Vertex.resetAutoName();
		render();
	}
	
	public LinkedList<Vertex> getVertices() {
		return vertices;
	}
	
	public LinkedList<Edge> getEdges() {
		return edges;
	}
	
}
