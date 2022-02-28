package sk.dualnexon.dualgraph.window;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.ui.Updatable;

public class Workspace implements Updatable {
	
	private static final double MAX_OFFSET = 500;
	
	private String name;
	
	private Group root;
	private Scene scene;
	private Graph graph;
	private Grid grid;
	private DebugMonitor debugMonitor;
	
	private double offsetX = 0, initialDragX = 0, lastOffsetX = offsetX;
	private double offsetY = 0, initialDragY = 0, lastOffsetY = offsetY;
	private boolean stageMove = false;

	public Workspace(String name) {
		
		setName(name);
		
		root = new Group();
		scene = new Scene(root);
		graph = new Graph(this);
		grid = new Grid(this);
		debugMonitor = new DebugMonitor(this);
		
		scene.setOnMouseClicked(e -> {
			if(e.isControlDown()) {
				double x = e.getSceneX();
				double y = e.getSceneY();
				graph.addVertex(new Vertex(graph, x + offsetX, y + offsetY));
			}
		});
		
		scene.setOnMousePressed(e -> {
			if(e.isMiddleButtonDown()) {
				setStageMove(true);
				setInitialDragX(e.getSceneX());
				setInitialDragY(e.getSceneY());
				update();
			}
		});
		
		scene.setOnMouseDragged(e -> {
			if(getStageMove()) {
				setOffsetX(e.getSceneX());
				setOffsetY(e.getSceneY());
				update();
			}
		});
		
		scene.setOnMouseReleased(e -> {
			if(getStageMove()) {
				setStageMove(false);
			}
		});
		
		Timeline delayedUpdateOnCreate = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				update();
			}
		}));
		delayedUpdateOnCreate.play();
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			update();
		};
	    App.get().getBaseWindow().widthProperty().addListener(stageSizeListener);
	    App.get().getBaseWindow().heightProperty().addListener(stageSizeListener);
	    
	    App.get().getBaseWindow().maximizedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
	        	Timeline delayer = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
	        		@Override
	    			public void handle(ActionEvent event) {
	        			update();
	    			}
	        	}));
	        	delayer.play();
	        }
	    });
	    
	    scene.setOnKeyPressed(e -> {
	    	switch(e.getCode()) {
	    	case F3:
	    		debugMonitor.toggleVisibility();
	    		break;
	    	default:
	    		break;
	    	}
	    });
		
	}
	
	public void addNode(Node node) {
		root.getChildren().add(node);
	}
	
	public void removeNode(Node node) {
		root.getChildren().remove(node);
	}
	
	public Group getRoot() {
		return root;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public double getOffsetX() {
		return offsetX;
	}
	
	public double getOffsetY() {
		return offsetY;
	}
	
	public void setOffsetX(double offsetX) {
		double deltaDragOffsetX = initialDragX - offsetX;
		this.offsetX = lastOffsetX + deltaDragOffsetX;
		if(this.offsetX > MAX_OFFSET) this.offsetX = MAX_OFFSET;
		if(this.offsetX < -MAX_OFFSET) this.offsetX = -MAX_OFFSET;
		update();
	}

	public void setOffsetY(double offsetY) {
		double deltaDragOffsetY = initialDragY - offsetY;
		this.offsetY = lastOffsetY + deltaDragOffsetY;
		if(this.offsetY > MAX_OFFSET) this.offsetY = MAX_OFFSET;
		if(this.offsetY < -MAX_OFFSET) this.offsetY = -MAX_OFFSET;
		update();
	}
	
	public double getInitialDragX() {
		return initialDragX;
	}

	public void setInitialDragX(double initialDragX) {
		this.initialDragX = initialDragX;
	}

	public double getInitialDragY() {
		return initialDragY;
	}

	public void setInitialDragY(double initialDragY) {
		this.initialDragY = initialDragY;
	}
	
	public boolean getStageMove() {
		return stageMove;
	}

	public void setStageMove(boolean stageMove) {
		this.stageMove = stageMove;
		if (stageMove) {
			scene.setCursor(Cursor.CLOSED_HAND);
			this.lastOffsetX = this.offsetX;
			this.lastOffsetY = this.offsetY;
		} else {
			scene.setCursor(Cursor.DEFAULT);
		}
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public void setName(String name) {
		this.name = name;
		App.get().getBaseWindow().setTitle("DualGraph - " + name);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void destroy() {
		grid.destroy();
		graph.destroy();
		debugMonitor.destroy();
	}

	@Override
	public void update() {
		grid.update();
		graph.update();
	}
	
}
