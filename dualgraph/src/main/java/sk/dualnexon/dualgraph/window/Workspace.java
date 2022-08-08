package sk.dualnexon.dualgraph.window;

import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmInterruptedException;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.ui.editorbar.EditorBar;
import sk.dualnexon.dualgraph.ui.editorbar.EditorBarAction;
import sk.dualnexon.dualgraph.util.FileHandler;
import sk.dualnexon.dualgraph.util.VertexNameConvention;

public class Workspace extends Tab implements Updatable {
	
	private static final double MAX_OFFSET = 1500;
	
	private String name;
	
	private Graph graph;
	private Grid grid;
	private VertexNameConvention vertexNameConvention;
	private DebugMonitor debugMonitor;
	private Group group;
	
	private Algorithm algorithm = null;
	
	private SelectionRectangle selectionRectangle;
	
	private EditorBar editorBar;
	
	private double offsetX = 0, initialDragX = 0, lastOffsetX = offsetX;
	private double offsetY = 0, initialDragY = 0, lastOffsetY = offsetY;
	private boolean stageMove = false;

	public Workspace(String name) {
		super(name);
		setContent(group = new Group());
		
		setName(name);
		
		graph = new Graph(this);
		grid = new Grid(this);
		vertexNameConvention = new VertexNameConvention();
		debugMonitor = new DebugMonitor(this);
		addNode(editorBar = new EditorBar(this));
		
		getContent().setOnMouseClicked(e -> {
			if((e.isControlDown() || editorBar.getCurrentAction().equals(EditorBarAction.CREATE_VERTEX)) && e.getButton().equals(MouseButton.PRIMARY)) {
				if(graph.isLocked()) return;
				double x = e.getX();
				double y = e.getY();
				graph.addVertex(new Vertex(graph, x + offsetX, y + offsetY));
				if(App.get().getAutofillVerticesNames()) vertexNameConvention.reset();
			}
		});
		
		getContent().setOnMousePressed(e -> {
			if(e.isMiddleButtonDown()) {
				setStageMove(true);
				setInitialDragX(e.getSceneX());
				setInitialDragY(e.getSceneY());
				update();
			} else if(e.isPrimaryButtonDown() && e.isShiftDown()) {
				selectionRectangle = new SelectionRectangle(this, e.getX(), e.getY());
			}
		});
		
		getContent().setOnMouseDragged(e -> {
			if(getStageMove()) {
				setOffsetX(e.getSceneX());
				setOffsetY(e.getSceneY());
				update();
			} else if(selectionRectangle != null && e.isShiftDown()) {
				selectionRectangle.setEndPositionX(e.getX());
				selectionRectangle.setEndPositionY(e.getY());
				selectionRectangle.update();
			}
		});
		
		getContent().setOnMouseReleased(e -> {
			if(getStageMove()) {
				setStageMove(false);
			}
			
			if(selectionRectangle != null) {
				graph.unselectAll();
				selectionRectangle.destroy();
				selectionRectangle = null;
			}
		});
		
		ContextMenu contextMenu = new ContextMenu();
		MenuItem renameItem = new MenuItem("Rename");
		renameItem.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog(getName());
			dialog.setHeaderText(null);
			dialog.setGraphic(null);
			dialog.setTitle("DualGraph action");
			dialog.setContentText("Enter new name:");

			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				setName(result.get());
			}
		});
		MenuItem saveItem = new MenuItem("Save");
		saveItem.setOnAction(e -> {
			FileHandler.get().saveWorkspace(this);
		});
		contextMenu.getItems().addAll(renameItem, saveItem);
		setContextMenu(contextMenu);
		
		Timeline delayedUpdateOnCreate = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				App.get().getTabPane().requestLayout();
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
	        	Timeline delayer = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
	        		@Override
	    			public void handle(ActionEvent event) {
	        			update();
	    			}
	        	}));
	        	delayer.play();
	        }
	    });
		
	}
	
	public void addNode(Node node) {
		group.getChildren().add(node);
	}
	
	public void removeNode(Node node) {
		group.getChildren().remove(node);
	}
	
	public void applyAlgorithm(Algorithm algorithm) {
		destroyCurrentAlgorithm();
		try {
			algorithm.calculate();
			this.algorithm = algorithm;
			graph.setLocked(true);
			editorBar.setVisible(false);
			editorBar.setAction(EditorBarAction.NONE);
		} catch (AlgorithmException ex) {
			algorithm.destroy();
			if(!(ex instanceof AlgorithmInterruptedException)) App.get().showWarningAlert(ex.getMessage());
		}
	}
	
	public void destroyCurrentAlgorithm() {
		if(algorithm == null) return;
		algorithm.destroy();
		algorithm = null;
		graph.setLocked(false);
		editorBar.setVisible(true);
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
			getContent().setCursor(Cursor.CLOSED_HAND);
			this.lastOffsetX = this.offsetX;
			this.lastOffsetY = this.offsetY;
		} else {
			getContent().setCursor(Cursor.DEFAULT);
		}
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public void setName(String name) {
		this.name = name;
		setText(name);
	}
	
	public String getName() {
		return name;
	}
	
	public VertexNameConvention getVertexNameConvention() {
		return vertexNameConvention;
	}
	
	public EditorBar getEditorBar() {
		return editorBar;
	}
	
	public void toggleDebugMonitor() {
		debugMonitor.toggleVisibility();
	}

	@Override
	public void destroy() {
		grid.destroy();
		graph.destroy();
		debugMonitor.destroy();
		editorBar.destroy();
	}

	@Override
	public void update() {
		grid.update();
		graph.update();
		if(algorithm != null) algorithm.getControls().update();
		editorBar.update();
		
	}
	
}
