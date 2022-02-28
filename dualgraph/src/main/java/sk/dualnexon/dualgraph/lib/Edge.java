package sk.dualnexon.dualgraph.lib;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
		
		node.setOnContextMenuRequested((e) -> {
			ContextMenu contextMenu = new ContextMenu();
			MenuItem item1 = new MenuItem("Delete " + getClass().getSimpleName());
			item1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					destroy();
				}
			});
			/*MenuItem item2 = new MenuItem("Reset namespace offset");
			item2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					namespace.resetOffset();
				}
			});*/
			contextMenu.getItems().add(item1);
			//contextMenu.getItems().add(item2);
			contextMenu.show(node, e.getScreenX(), e.getScreenY());
		});
		
		/*
		line.setOnMouseReleased((e) -> {
			if(e.isShiftDown()) {
				selected = !selected;
				Graph.get().render();
			}
		});*/
		
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
		graph.removeEdge(this);
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
