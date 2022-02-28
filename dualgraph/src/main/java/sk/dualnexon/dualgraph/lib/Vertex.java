package sk.dualnexon.dualgraph.lib;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sk.dualnexon.dualgraph.ui.Updatable;

public class Vertex implements Updatable {
	
	private static final double DEFAULT_SIZE = 25;
	private static final double MIN_SIZE = 10;
	
	private Graph graph;
	private double positionX, positionY;
	private double size;
	private Circle node;
	private boolean selected = false;
	
	public Vertex(Graph graph, double positionX, double positionY, double size) {
		this.graph = graph;
		node = new Circle(positionX, positionY, size);
		this.size = size;
		this.positionX = positionX;
		this.positionY = positionY;
		graph.update();
		
		events();
		
	}
	
	public Vertex(Graph graph, double positionX, double positionY) {
		this(graph, positionX, positionY, DEFAULT_SIZE);
	}
	
	private void events() {
		
		node.setOnContextMenuRequested((e) -> {
			Vertex currentInstance = this;
			ContextMenu contextMenu = new ContextMenu();
			MenuItem item1 = new MenuItem("Delete " + getClass().getSimpleName());
			item1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					destroy();
				}
			});
			MenuItem item2 = new MenuItem((selected ? "Unselect " : "Select ") + getClass().getSimpleName());
			item2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					toggleSelected();
				}
			});
			MenuItem item3 = new MenuItem("Starting " + getClass().getSimpleName() + " of edge");
			item3.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					graph.setStartingVertex(currentInstance);
				}
			});
			MenuItem item4 = new MenuItem("Ending " + getClass().getSimpleName() + " of edge");
			item4.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					graph.setEndingVertex(currentInstance);
					if(graph.getStartingVertex() != null && graph.getEndingVertex() != null) {
						graph.addEdge(new Edge(graph, graph.getStartingVertex(), graph.getEndingVertex()));
					}
				}
			});
			/*MenuItem item5 = new MenuItem("Reset namespace offset");
			item5.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					namespace.resetOffset();
				}
			});*/
			
			contextMenu.getItems().addAll(item2, item3, item4, item1);
			contextMenu.show(node, e.getScreenX(), e.getScreenY());
		});
		
		node.setOnMouseReleased((e) -> {
			if(e.isShiftDown()) {
				toggleSelected();
			}
			node.setCursor(Cursor.HAND);
		});
		
		node.setOnMouseDragged(e -> {
			if(e.isPrimaryButtonDown()) {
				setPositionX(e.getSceneX());
				setPositionY(e.getSceneY());
				node.setCursor(Cursor.CLOSED_HAND);
			}
		});
		
		node.setOnMouseEntered((e) -> {
			node.setCursor(Cursor.HAND);
		});
		
		node.setOnMouseExited((e) -> {
			node.setCursor(Cursor.DEFAULT);
		});
		
	}
	
	public void setPositionX(double positionX) {
		this.positionX = positionX + graph.getWorkspace().getOffsetX();
		graph.update();
	}
	
	public double getPositionX() {
		return positionX;
	}
	
	public double getRealPositionX() {
		return positionX + -graph.getWorkspace().getOffsetX();
	}
	
	public void setPositionY(double positionY) {
		this.positionY = positionY + graph.getWorkspace().getOffsetY();
		graph.update();
	}
	
	public double getPositionY() {
		return positionY;
	}
	
	public double getRealPositionY() {
		return positionY + -graph.getWorkspace().getOffsetY();
	}
	
	public void setSize(double size) {
		this.size = size;
		if(this.size < MIN_SIZE) this.size = MIN_SIZE;
		graph.update();
	}
	
	public double getSize() {
		return size;
	}
	
	public Circle getNode() {
		return node;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void select() {
		selected = true;
		graph.update();
	}
	
	public void toggleSelected() {
		this.selected = !this.selected;
		graph.update();
	}
	
	@Override
	public void destroy() {
		graph.getWorkspace().removeNode(node);
		graph.removeVertex(this);
	}

	@Override
	public void update() {
		node.setCenterX(getRealPositionX());
		node.setCenterY(getRealPositionY());
		node.setRadius(size);
		node.setFill(Color.WHITE);
		node.setStroke((isSelected() ? Color.RED : Color.BLACK));
		node.setStrokeWidth(2);
		node.toFront();
	}
	
}
