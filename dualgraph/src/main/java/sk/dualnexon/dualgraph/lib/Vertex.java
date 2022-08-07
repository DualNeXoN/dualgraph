package sk.dualnexon.dualgraph.lib;

import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sk.dualnexon.dualgraph.ui.Namespace;
import sk.dualnexon.dualgraph.ui.theme.ColorUI;
import sk.dualnexon.dualgraph.ui.theme.ThemeHandler;

public class Vertex extends BaseGraphNode {
	
	private static final double DEFAULT_SIZE = 20;
	private static final double MIN_SIZE = 10;
	public static Color defaultColor = ThemeHandler.get().getActiveTheme().getColor(ColorUI.VERTEX_OUTLINE);
	
	private String uuid;
	
	private double positionX, positionY;
	private double size;
	private Circle node;
	private Color color = defaultColor;
	
	private Namespace namespace;
	private double namespaceDefaultOffsetX, namespaceDefaultOffsetY;
	
	private ContextMenu contextMenu;
	
	public Vertex(Graph graph, double positionX, double positionY, double size) {
		super(graph);
		
		uuid = UUID.randomUUID().toString();
		
		node = new Circle(positionX, positionY, size);
		this.size = size;
		this.positionX = positionX;
		this.positionY = positionY;
		
		this.namespaceDefaultOffsetX = -size / 1.5;
		this.namespaceDefaultOffsetY = -size * 1.25;
		
		namespace = new Namespace(this, this.namespaceDefaultOffsetX, this.namespaceDefaultOffsetY, graph.getWorkspace().getVertexNameConvention().getNextName());
		namespace.setOffsetX(-size + namespace.getNode().getBoundsInLocal().getWidth());
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
		MenuItem deleteItem = new MenuItem("Delete " + getClass().getSimpleName());
		deleteItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				destroy();
			}
		});
		MenuItem selectItem = new MenuItem((selected ? "Unselect " : "Select ") + getClass().getSimpleName());
		selectItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				toggleSelected();
			}
		});
		MenuItem startPointItem = new MenuItem("Starting " + getClass().getSimpleName() + " of edge");
		startPointItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				graph.setStartingVertex(thisObjHelper);
			}
		});
		MenuItem endPointItem = new MenuItem("Ending " + getClass().getSimpleName() + " of edge");
		endPointItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				graph.setEndingVertex(thisObjHelper);
				if(graph.getStartingVertex() != null && graph.getEndingVertex() != null) {
					graph.addEdge(new Edge(graph, graph.getStartingVertex(), graph.getEndingVertex()));
				}
			}
		});
		MenuItem resetOffsetItem = new MenuItem("Reset namespace offset");
		resetOffsetItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				namespace.resetOffset(namespaceDefaultOffsetX, namespaceDefaultOffsetY);
			}
		});
		MenuItem renameItem = new MenuItem("Rename " + getClass().getSimpleName());
		renameItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				namespace.requestToChangeValue();
			}
		});
		
		contextMenu.getItems().addAll(selectItem, renameItem, startPointItem, endPointItem, resetOffsetItem, deleteItem);
		
	}
	
	private void events() {
		
		node.setOnContextMenuRequested((e) -> {
			if(graph.isLocked()) return;
			contextMenu.show(node, e.getScreenX(), e.getScreenY());
		});
		
		node.setOnMouseReleased((e) -> {
			if(e.isShiftDown()) {
				if(graph.isLocked()) return;
				toggleSelected();
			}
			node.setCursor(Cursor.HAND);
		});
		
		node.setOnMouseDragged(e -> {
			if(e.isPrimaryButtonDown()) {
				setPositionX(e.getX());
				setPositionY(e.getY());
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
		if(graph.isLocked()) return;
		this.size = (size < MIN_SIZE ? MIN_SIZE : size);
		graph.update();
	}
	
	public double getSize() {
		return size;
	}
	
	public void setColor(Color color) {
		this.color = (color != null ? color : defaultColor);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Circle getNode() {
		return node;
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
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
		node.setFill(ThemeHandler.get().getActiveTheme().getColor(ColorUI.VERTEX_FILL));
		node.setStroke((isSelected() ? ThemeHandler.get().getActiveTheme().getColor(ColorUI.NODE_SELECT) : color));
		node.setStrokeWidth(2);
		node.toFront();
		
		namespace.update();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + namespace.getText();
	}
	
}
