package me.dualnexon.dualgraph.lib;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import me.dualnexon.dualgraph.app.GraphStage;

public class Edge extends BaseEdge {
	
	private Line line;
	
	public Edge(Vertex v1, Vertex v2) {
		this(v1, v2, 1);
	}
	
	public Edge(Vertex v1, Vertex v2, int edgeValue) {
		super(v1, v2, edgeValue);
		
		line = new Line(vertices[0].getRealX(), vertices[0].getRealY(), vertices[1].getRealX(), vertices[1].getRealY());
		GraphStage.get().addNode(line);
		
		namespace = new Namespace(Integer.toString(this.edgeValue), getCenterX(), getCenterY());
		
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
	
	public void render() {
		
		if(!selected) line.setStroke(Color.BLACK);
		else line.setStroke(Color.RED);
		
		line.setStrokeWidth(4);
		line.setStartX(vertices[0].getRealX());
		line.setStartY(vertices[0].getRealY());
		line.setEndX(vertices[1].getRealX());
		line.setEndY(vertices[1].getRealY());
		line.toFront();
		
		namespace.setX(getCenterX());
		namespace.setY(getCenterY());
		namespace.updatePos();
		
	}
	
	public double[] getCenter() {
		return new double[] {(line.getStartX() + line.getEndX()) / 2, (line.getStartY() + line.getEndY()) / 2};
	}
	
	public double getCenterX() {
		return getCenter()[0];
	}
	
	public double getCenterY() {
		return getCenter()[1];
	}
	
	public boolean hasVertex(Vertex v) {
		return (vertices[0].equals(v)) || (vertices[1].equals(v));
	}

	@Override
	public void destroy() {
		GraphStage.get().removeNode(line);
		namespace.destroy();
	}
	
}
