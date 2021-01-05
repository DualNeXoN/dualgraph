package me.dualnexon.dualgraph.lib;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import me.dualnexon.dualgraph.AppConfiguration;
import me.dualnexon.dualgraph.app.GraphStage;

public class Vertex extends BaseVertex implements INode {
	
	private static char autoName = 'A';
	
	public static void resetAutoName() {
		autoName = 'A';
	}
	
	private final int SIZE = 40;
	
	private Canvas canvas;
	private GraphicsContext g2d;
	private Namespace namespace;
	
	public Vertex(String name, double x, double y) {
		
		this.x = x;
		this.y = y;
		
		namespace = new Namespace(name, getRealX(), getRealY());
		
		canvas = new Canvas(SIZE, SIZE);
		GraphStage.get().addNode(canvas);
		
		g2d = canvas.getGraphicsContext2D();
		
		events();
		
	}
	
	public Vertex(double x, double y) {
		this(Character.toString(autoName++), x, y);
	}
	
	public void render() {
		
		canvas.setLayoutX(getRealX()-SIZE/2);
		canvas.setLayoutY(getRealY()-SIZE/2);
		
		g2d.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		if(!selected) g2d.setStroke(Color.BLACK);
		else g2d.setStroke(AppConfiguration.selectedColor());
		g2d.setLineWidth(3);
		g2d.strokeOval(3, 3, SIZE-6, SIZE-6);
		
		g2d.setFill(Color.WHITE);
		g2d.fillOval(3, 3, SIZE-6, SIZE-6);
		
		namespace.setX(getRealX() - namespace.getWidth()/2);
		namespace.setY(getRealY() - namespace.getHeight());
		namespace.updatePos();
		
		canvas.toFront();
		namespace.toFront();
		
	}
	
	private void events() {
		
		canvas.setOnContextMenuRequested((e) -> {
			Vertex currentInstance = this;
			ContextMenu contextMenu = new ContextMenu();
			MenuItem item1 = new MenuItem("Delete " + getClass().getSimpleName());
			item1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Graph.get().destroyVertex(currentInstance);
				}
			});
			MenuItem item2 = new MenuItem((selected ? "Unselect " : "Select ") + getClass().getSimpleName());
			item2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					select(!selected);
				}
			});
			MenuItem item3 = new MenuItem("Starting " + getClass().getSimpleName() + " of edge");
			item3.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Graph.get().setStarting(currentInstance);
					System.out.println("S");
				}
			});
			MenuItem item4 = new MenuItem("Ending " + getClass().getSimpleName() + " of edge");
			item4.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Graph.get().setEnding(currentInstance);
					System.out.println("E");
					if(Graph.get().getStarting() != null && Graph.get().getEnding() != null) {
						Graph.get().addEdge(new Edge(Graph.get().getStarting(), Graph.get().getEnding()));
					}
				}
			});
			contextMenu.getItems().add(item1);
			contextMenu.getItems().add(item2);
			contextMenu.getItems().add(item3);
			contextMenu.getItems().add(item4);
			contextMenu.show(canvas, e.getScreenX(), e.getScreenY());
		});
		
		canvas.setOnMouseReleased((e) -> {
			if(e.isShiftDown()) {
				select(!selected);
			}
		});
		
		canvas.setOnMouseDragged((e) -> {
			this.x = e.getSceneX();
			this.y = e.getSceneY();
			Graph.get().render();
		});
		
	}
	
	public String getName() {
		return namespace.getName();
	}

	@Override
	public void destroy() {
		namespace.destroy();
		GraphStage.get().removeNode(canvas);
	}
	
}
