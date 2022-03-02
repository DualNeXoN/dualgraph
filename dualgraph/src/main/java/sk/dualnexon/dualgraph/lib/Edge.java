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
import sk.dualnexon.dualgraph.ui.Namespace;

public class Edge extends BaseGraphNode {
	
	private static final double DEFAULT_VALUE = 1;
	
	private Line node;
	private Vertex firstVertex, secondVertex;
	private double value;
	private boolean selected = false;
	
	private Namespace namespace;
	
	private ContextMenu contextMenu;
	
	public Edge(Graph graph, Vertex vertex1, Vertex vertex2, double value) {
		this.graph = graph;
		this.firstVertex = vertex1;
		this.secondVertex = vertex2;
		this.value = value;
		
		node = new Line();
		graph.getWorkspace().addNode(node);
		
		namespace = new Namespace(this, Integer.toString((int) value));
		namespace.setValueLocked(true);
		graph.getWorkspace().addNode(namespace.getNode());
		
		update();
		
		contextMenuCreation();
		events();
	}
	
	public Edge(Graph graph, Vertex vertex1, Vertex vertex2) {
		this(graph, vertex1, vertex2, DEFAULT_VALUE);
	}
	
	private void contextMenuCreation() {
		
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
		MenuItem item3 = new MenuItem("Reset namespace offset");
		item3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				namespace.resetOffset();
			}
		});
		MenuItem item4 = new MenuItem("Change value");
		item4.setOnAction(new EventHandler<ActionEvent>() {
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
						setValue(Double.parseDouble(resultString));
					} catch(NumberFormatException ex) {
						
					}
				}
			}
		});
		contextMenu.getItems().addAll(item2, item4, item3, item1);
		
	}
	
	private void events() {
		
		node.setOnContextMenuRequested((e) -> {
			contextMenu.show(node, e.getScreenX(), e.getScreenY());
		});
		
		node.setOnMouseReleased((e) -> {
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
	
	public void setValue(double value) {
		this.value = value;
		String newValueString = "";
		if(value == (long) value) newValueString = String.format("%d",(long) value);
	    else newValueString = String.format("%s",value);
		namespace.setText(newValueString);
		graph.update();
	}
	
	public double getValue() {
		return value;
	}
	
	public Line getNode() {
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
		
		node.setStroke((isSelected() ? Color.RED : Color.BLACK));
		node.setStrokeWidth(5);
		
		node.toFront();
		
		namespace.update();
	}

}
