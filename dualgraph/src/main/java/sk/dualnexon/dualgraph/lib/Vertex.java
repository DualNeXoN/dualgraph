package sk.dualnexon.dualgraph.lib;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sk.dualnexon.dualgraph.ui.Namespace;

public class Vertex extends BaseGraphNode {
	
	private static final double DEFAULT_SIZE = 25;
	private static final double MIN_SIZE = 10;
	
	private double positionX, positionY;
	private double size;
	private Circle node;
	private boolean selected = false;
	
	private Namespace namespace;
	private double namespaceDefaultOffsetX, namespaceDefaultOffsetY;
	
	private ContextMenu contextMenu;
	
	public Vertex(Graph graph, double positionX, double positionY, double size) {
		this.graph = graph;
		
		node = new Circle(positionX, positionY, size);
		this.size = size;
		this.positionX = positionX;
		this.positionY = positionY;
		
		this.namespaceDefaultOffsetX = -size / 1.5;
		this.namespaceDefaultOffsetY = -size * 1.25;
		
		namespace = new Namespace(this, this.namespaceDefaultOffsetX, this.namespaceDefaultOffsetY, "Test");
		graph.getWorkspace().addNode(namespace.getNode());
		
		graph.update();
		
		contextMenuCreation();
		events();
	}
	
	public Vertex(Graph graph, double positionX, double positionY) {
		this(graph, positionX, positionY, DEFAULT_SIZE);
	}
	
	private void contextMenuCreation() {
		
		Vertex thisObjHelper = this;
		
		contextMenu = new ContextMenu();
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
				graph.setStartingVertex(thisObjHelper);
			}
		});
		MenuItem item4 = new MenuItem("Ending " + getClass().getSimpleName() + " of edge");
		item4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				graph.setEndingVertex(thisObjHelper);
				if(graph.getStartingVertex() != null && graph.getEndingVertex() != null) {
					graph.addEdge(new Edge(graph, graph.getStartingVertex(), graph.getEndingVertex()));
				}
			}
		});
		MenuItem item5 = new MenuItem("Reset namespace offset");
		item5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				namespace.resetOffset(namespaceDefaultOffsetX, namespaceDefaultOffsetY);
			}
		});
		
		contextMenu.getItems().addAll(item2, item3, item4, item5, item1);
		
	}
	
	private void events() {
		
		node.setOnContextMenuRequested((e) -> {
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
		this.size = (size < MIN_SIZE ? MIN_SIZE : size);
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
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	@Override
	public void destroy() {
		graph.getWorkspace().removeNode(node);
		graph.removeVertex(this);
		namespace.destroy();
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
		
		namespace.update();
	}
	
}
