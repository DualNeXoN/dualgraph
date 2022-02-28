package sk.dualnexon.dualgraph.window;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.ui.Updatable;

public class DebugMonitor implements Updatable {
	
	private Workspace workspace;
	private boolean visible = false;
	private Timeline updateTask;
	
	private VBox container;
	private Text countOfVerticies, countOfEdges;
	
	public DebugMonitor(Workspace workspace) {
		
		this.workspace = workspace;
		
		container = new VBox();
		countOfVerticies = new Text();
		countOfEdges = new Text();
		container.getChildren().addAll(countOfVerticies, countOfEdges);
		workspace.addNode(container);
		
		updateTask = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				update();
			}
		}));
		updateTask.setCycleCount(Timeline.INDEFINITE);
		updateTask.play();
		
	}
	
	public VBox getContainer() {
		return container;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		container.setVisible(visible);
	}
	
	public void toggleVisibility() {
		setVisible(!isVisible());
	}
	
	@Override
	public void update() {
		
		if(!isVisible()) return;
		
		countOfVerticies.setText("Verticies count: " + workspace.getGraph().getVerticies().size());
		countOfEdges.setText("Edges count: " + workspace.getGraph().getEdges().size());
		
	}
	
	@Override
	public void destroy() {
		updateTask.stop();
		workspace.removeNode(container);
	}
	
}
