package sk.dualnexon.dualgraph.window;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.ui.Updatable;

public class DebugMonitor implements Updatable {
	
	private Workspace workspace;
	private boolean visible = false;
	private Timeline updateTask;
	
	private VBox container;
	private Text countOfVertices, countOfEdges;
	private Text listOfVertices, listOfEdges;
	
	public DebugMonitor(Workspace workspace) {
		
		this.workspace = workspace;
		
		container = new VBox();
		countOfVertices = new Text();
		listOfVertices = new Text();
		countOfEdges = new Text();
		listOfEdges = new Text();
		container.getChildren().addAll(countOfVertices, listOfVertices, countOfEdges, listOfEdges);
		workspace.addNode(container);
		
		updateTask = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(App.get().getTabPane().getSelectionModel().getSelectedItem() != null) {
					if(App.get().getTabPane().getSelectionModel().getSelectedItem().equals(workspace)) {
						update();
					}
				} else {
					updateTask.stop();
				}
			}
		}));
		updateTask.setCycleCount(Timeline.INDEFINITE);
		
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
		setUpdating(visible);
	}
	
	public void setUpdating(boolean canUpdate) {
		if(canUpdate) {
			updateTask.play();
		} else {
			updateTask.stop();
		}
	}
	
	public void toggleVisibility() {
		setVisible(!isVisible());
	}
	
	@Override
	public void update() {
		
		if(!isVisible()) return;
		
		countOfVertices.setText("Vertices count: " + workspace.getGraph().getVertices().size());
		countOfEdges.setText("Edges count: " + workspace.getGraph().getEdges().size());
		
		String listOfVerticesString = "";
		for(Vertex vertex : workspace.getGraph().getVertices()) {
			listOfVerticesString += vertex.getNamespace().getText() + ", ";
		}
		if(listOfVerticesString.length() > 0) {
			listOfVertices.setText("Vertices: " + listOfVerticesString.substring(0, listOfVerticesString.length() - 2));
		} else {
			listOfVertices.setText("No vertices in list");
		}
		
		String listOfEdgesString = "";
		for(Edge edge : workspace.getGraph().getEdges()) {
			listOfEdgesString += edge.getFirstVertex().getNamespace().getText() + "-" + edge.getSecondVertex().getNamespace().getText() + ", ";
		}
		if(listOfEdgesString.length() > 0) {
			listOfEdges.setText("Edges: " + listOfEdgesString.substring(0, listOfEdgesString.length() - 2));
		} else {
			listOfEdges.setText("No edges in list");
		}
		
	}
	
	@Override
	public void destroy() {
		updateTask.stop();
		workspace.removeNode(container);
	}
	
}
