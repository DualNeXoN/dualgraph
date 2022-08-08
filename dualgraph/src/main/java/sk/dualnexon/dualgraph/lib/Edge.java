package sk.dualnexon.dualgraph.lib;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import sk.dualnexon.dualgraph.ui.Namespace;
import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.ui.editorbar.EditorBarAction;
import sk.dualnexon.dualgraph.ui.theme.ColorUI;
import sk.dualnexon.dualgraph.ui.theme.ThemeHandler;

public class Edge extends BaseGraphNode {
	
	private static final int DEFAULT_VALUE = 1;
	private static final DirectionType DEFAULT_DIRECTION = DirectionType.BIDIRECTIONAL;
	public static Color defaultColor = ThemeHandler.get().getActiveTheme().getColor(ColorUI.EDGE);
	
	public enum DirectionType {
		UNIDIRECTIONAL, BIDIRECTIONAL;
	}
	
	private Line node;
	private Vertex firstVertex, secondVertex;
	private int value;
	private DirectionType direction;
	private Vertex vertexDirection = null;
	private Color color = defaultColor;
	
	private Namespace namespace;
	
	private ContextMenu contextMenu;
	
	private EdgeArrow arrow;
	
	public Edge(Graph graph, Vertex vertex1, Vertex vertex2, int value, DirectionType direction, Vertex vertexDirection) {
		super(graph);
		this.firstVertex = vertex1;
		this.secondVertex = vertex2;
		this.value = value;
		this.vertexDirection = vertexDirection;
		
		node = new Line();
		graph.getWorkspace().addNode(node);
		
		namespace = new Namespace(this, Integer.toString((int) value));
		namespace.setValueLocked(true);
		graph.getWorkspace().addNode(namespace.getNode());
		
		setDirection(direction);
		
		update();
		
		contextMenuCreation();
		events();
	}
	
	public Edge(Graph graph, Vertex vertex1, Vertex vertex2) {
		this(graph, vertex1, vertex2, DEFAULT_VALUE, DEFAULT_DIRECTION, null);
	}
	
	private void contextMenuCreation() {
		
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
		MenuItem resetOffsetItem = new MenuItem("Reset namespace offset");
		resetOffsetItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				namespace.resetOffset();
			}
		});
		MenuItem valueItem = new MenuItem("Change value");
		valueItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TextInputDialog dialog = new TextInputDialog(namespace.getText());
				dialog.setHeaderText(null);
				dialog.setGraphic(null);
				dialog.setTitle("DualGraph action");
				dialog.setContentText("Enter new value:");

				Optional<String> result = dialog.showAndWait();
				if(result.isPresent()) {
					String resultString = result.get();
					try {
						setValue(Integer.parseInt(resultString));
					} catch(NumberFormatException ex) {
						
					}
				}
			}
		});
		MenuItem directionItem = new MenuItem("Directed/Undirected");
		directionItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				toggleDirectionMode();
			}
		});
		contextMenu.getItems().addAll(selectItem, directionItem, valueItem, resetOffsetItem, deleteItem);
		
	}
	
	private void events() {
		
		node.setOnContextMenuRequested((e) -> {
			if(graph.isLocked()) return;
			contextMenu.show(node, e.getScreenX(), e.getScreenY());
		});
		
		node.setOnMouseReleased((e) -> {
			if(graph.isLocked()) return;
			if(e.isShiftDown()) {
				toggleSelected();
				graph.update();
			}
		});
		
		node.setOnMouseEntered(e -> {
			node.setCursor(Cursor.HAND);
		});
		
		node.setOnMouseExited(e -> {
			node.setCursor(Cursor.DEFAULT);
		});
		
		node.setOnMouseClicked(e-> {
			if(getWorkspace().getEditorBar().getCurrentAction().equals(EditorBarAction.DELETE)) {
				destroy();
			} else if(getWorkspace().getEditorBar().getCurrentAction().equals(EditorBarAction.RENAME)) {
				namespace.requestToChangeValue();
			}
		});
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
	
	public void setValue(int value) {
		if(graph.isLocked()) return;
		this.value = value;
		String newValueString = "";
		if(value == (long) value) newValueString = String.format("%d",(long) value);
	    else newValueString = String.format("%s",value);
		namespace.setText(newValueString);
		graph.update();
	}
	
	public int getValue() {
		return value;
	}
	
	public DirectionType getDirection() {
		return direction;
	}
	
	public void setDirection(DirectionType direction) {
		if(graph.isLocked()) return;
		this.direction = direction;
		applyDirectionChanges();
	}
	
	public void toggleDirectionMode() {
		if(graph.isLocked()) return;
		setDirection(isDirected() ? DirectionType.BIDIRECTIONAL : DirectionType.UNIDIRECTIONAL);
	}
	
	public boolean isDirected() {
		return direction.equals(DirectionType.UNIDIRECTIONAL);
	}
	
	public void toggleVertexDirection() {
		if(graph.isLocked()) return;
		vertexDirection = (vertexDirection.equals(secondVertex) ? firstVertex : secondVertex);
		graph.update();
	}
	
	public Vertex getVertexDirection() {
		return vertexDirection;
	}
	
	public Vertex getVertexDirectionStarting() {
		if(!isDirected()) return null;
		return vertexDirection.equals(secondVertex) ? firstVertex : secondVertex;
	}
	
	private void applyDirectionChanges() {
		if(graph.isLocked()) return;
		switch(direction) {
		
		case UNIDIRECTIONAL:
			
			if(vertexDirection == null) {
				vertexDirection = secondVertex;
			}
			
			arrow = new EdgeArrow(getRealPositionX(), getRealPositionY(), 14, 40);
	        
			getWorkspace().addNode(arrow);
			break;
			
		case BIDIRECTIONAL:
			
			if(arrow == null) break;
			arrow.destroy();
			arrow = null;
			
			break;
		}
	}
	
	public void setColor(Color color) {
		this.color = (color != null ? color : defaultColor);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Line getNode() {
		return node;
	}
	
	public double getRealPositionX() {
		return (firstVertex.getRealPositionX() + secondVertex.getRealPositionX()) / 2;
	}
	
	public double getRealPositionY() {
		return (firstVertex.getRealPositionY() + secondVertex.getRealPositionY()) / 2;
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	@Override
	public void destroy() {
		graph.getWorkspace().removeNode(node);
		graph.removeEdge(this);
		
		if(arrow != null) {
			arrow.destroy();
		}
		namespace.destroy();
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
		
		node.setStroke((isSelected() ? ThemeHandler.get().getActiveTheme().getColor(ColorUI.NODE_SELECT) : color));
		node.setStrokeWidth(5);
		
		node.toFront();
		
		if(isDirected()) {
			arrow.update();
		}
		
		namespace.update();
	}
	
	@Override
	public String toString() {
		if(direction.equals(DirectionType.BIDIRECTIONAL)) {
			return String.format("%s-%s", firstVertex.getNamespace().getText(), secondVertex.getNamespace().getText());
		} else {
			return String.format("%s->%s", (vertexDirection.equals(secondVertex) ? firstVertex.getNamespace().getText() : secondVertex.getNamespace().getText()), vertexDirection.getNamespace().getText());
		}
	}
	
	class EdgeArrow extends Polygon implements Updatable {
		
		private ContextMenu contextMenu;
		
		public EdgeArrow(double positionX, double positionY, double length, double angle) {
			
			getPoints().addAll(new Double[] {
				0d, 0d,
				(length * Math.tan(angle)), -length,
		        -(length * Math.tan(angle)), -length
			});
			
			contextMenu = new ContextMenu();
			MenuItem swapDirectionItem = new MenuItem("Swap vertex direction");
			swapDirectionItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					toggleVertexDirection();
				}
			});
			contextMenu.getItems().addAll(swapDirectionItem);
			
			setOnContextMenuRequested((e) -> {
				if(graph.isLocked()) return;
				contextMenu.show(node, e.getScreenX(), e.getScreenY());
			});
			
			update();
		    
		    setFill(Color.GOLD);
		    setStroke(Color.BLACK);
		}
		
		private double calculateAngle() {
			
			Vertex vertexFrom = null, vertexTo = null;
			if(vertexDirection.equals(secondVertex)) {
				vertexFrom = firstVertex;
				vertexTo = secondVertex;
			} else {
				vertexFrom = secondVertex;
				vertexTo = firstVertex;
			}
			
		    double angle = Math.toDegrees(Math.atan2(vertexTo.getPositionX() - vertexFrom.getPositionX(), vertexTo.getPositionY() - vertexFrom.getPositionY()));
		    return angle + Math.ceil( -angle / 360 ) * 360;
		}
		
		public double getAngleOfLineBetweenTwoPoints() {
			
			Vertex p1 = null, p2 = null;
			if(vertexDirection.equals(secondVertex)) {
				p1 = firstVertex;
				p2 = secondVertex;
			} else {
				p1 = secondVertex;
				p2 = firstVertex;
			}
			
	        double xDiff = p2.getRealPositionX() - p1.getRealPositionX();
	        double yDiff = p2.getRealPositionY() - p1.getRealPositionY();
	        return Math.toDegrees(Math.atan2(yDiff, xDiff));
	    }

		@Override
		public void destroy() {
			graph.getWorkspace().removeNode(this);
		}

		@Override
		public void update() {
			
			setLayoutX(getRealPositionX());
		    setLayoutY(getRealPositionY());
			
			setRotate(-calculateAngle());
			
			toFront();
		}
		
	}

}
