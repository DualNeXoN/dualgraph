package me.dualnexon.dualgraph.lib;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import me.dualnexon.dualgraph.app.GraphStage;

public class Edge extends BaseNode implements INode {
	
	private Vertex[] vertices = new Vertex[2];
	private Line line;
	
	public Edge(Vertex v1, Vertex v2) {
		vertices[0] = v1;
		vertices[1] = v2;
		line = new Line(vertices[0].getX(), vertices[0].getY(), vertices[1].getX(), vertices[1].getY());
		GraphStage.get().addNode(line);
		
		events();
		
	}
	
	private void events() {
		
		line.setOnContextMenuRequested((e) -> {
			Edge currentInstance = this;
			ContextMenu contextMenu = new ContextMenu();
			MenuItem item = new MenuItem("Delete " + getClass().getSimpleName());
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Graph.get().destroyEdge(currentInstance);
				}
			});
			contextMenu.getItems().add(item);
			contextMenu.show(line, e.getScreenX(), e.getScreenY());
		});
		
		line.setOnMouseReleased((e) -> {
			if(e.isShiftDown()) {
				selected = !selected;
				Graph.get().render();
			}
		});
		
	}
	
	public Vertex getFirstVertex() {
		return vertices[0];
	}
	
	public Vertex getSecondVertex() {
		return vertices[1];
	}
	
	public Vertex getVertexByIndex(int index) {
		return index == 0 ? getFirstVertex() : getSecondVertex();
	}
	
	public void render() {
		
		if(!selected) line.setStroke(Color.BLACK);
		else line.setStroke(Color.RED);
		
		line.setStrokeWidth(4);
		line.setStartX(vertices[0].getX());
		line.setStartY(vertices[0].getY());
		line.setEndX(vertices[1].getX());
		line.setEndY(vertices[1].getY());
		line.toFront();
		
	}
	
	public boolean hasVertex(Vertex v) {
		return (vertices[0].equals(v)) || (vertices[1].equals(v));
	}

	@Override
	public void destroy() {
		GraphStage.get().removeNode(line);
	}
	
}
