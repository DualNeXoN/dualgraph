package sk.dualnexon.dualgraph.lib;

import javafx.scene.shape.Line;
import sk.dualnexon.dualgraph.ui.Updatable;

public class Edge implements Updatable {
	
	private static final double DEFAULT_VALUE = 1;
	
	private Graph graph;
	private Line node;
	private Vertex firstVertex, secondVertex;
	private double value;
	
	public Edge(Graph graph, Vertex vertex1, Vertex vertex2, double value) {
		this.graph = graph;
		this.firstVertex = vertex1;
		this.secondVertex = vertex2;
		this.value = value;
		
		node = new Line();
		graph.getWorkspace().addNode(node);
		
		update();
	}
	
	public Edge(Graph graph, Vertex vertex1, Vertex vertex2) {
		this(graph, vertex1, vertex2, DEFAULT_VALUE);
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public Vertex getFirstVertex() {
		return firstVertex;
	}
	
	public Vertex getSecondVertex() {
		return secondVertex;
	}
	
	public double getValue() {
		return value;
	}
	
	public Line getNode() {
		return node;
	}
	
	@Override
	public void destroy() {
		graph.getWorkspace().removeNode(node);
	}

	@Override
	public void update() {
		double v1X = firstVertex.getPositionX();
		double v1Y = firstVertex.getPositionY();
		double v2X = secondVertex.getPositionX();
		double v2Y = secondVertex.getPositionY();
		
		node.setStartX(v1X + -graph.getWorkspace().getOffsetX());
		node.setStartY(v1Y + -graph.getWorkspace().getOffsetY());
		node.setEndX(v2X + -graph.getWorkspace().getOffsetX());
		node.setEndY(v2Y + -graph.getWorkspace().getOffsetY());
		
		node.setStrokeWidth(3);
		
		node.toFront();
	}

}
