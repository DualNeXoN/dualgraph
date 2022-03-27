package sk.dualnexon.dualgraph;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.lib.algorithm.BFS;
import sk.dualnexon.dualgraph.lib.algorithm.BridgeDetection;
import sk.dualnexon.dualgraph.lib.algorithm.CycleDetection;
import sk.dualnexon.dualgraph.lib.algorithm.DFS;
import sk.dualnexon.dualgraph.lib.algorithm.MSTPrim;
import sk.dualnexon.dualgraph.util.FileHandler;
import sk.dualnexon.dualgraph.window.Window;
import sk.dualnexon.dualgraph.window.Workspace;

public class App extends Application {
	
	private static final String MSG_NO_WORKSPACE = "No workspace has been chosen";
	
	private static App instance;
	
	public static App get() {
		return instance;
	}

	private Window baseWindow;
	private Scene scene;
	private TabPane tabPane;
	
    @Override
    public void start(Stage stage) {
    	instance = this;
    	baseWindow = new Window();
    	new FileHandler();
    	
    	MenuBar menuBar = new MenuBar();
    	Menu menuFile = new Menu("File");
    	
    	MenuItem menuItemNew = new MenuItem("New...");
    	menuItemNew.setOnAction(e -> {
    		tabPane.getTabs().add(new Workspace("Workspace"));
    	});
    	MenuItem menuItemOpen = new MenuItem("Open...");
    	menuItemOpen.setOnAction(e -> {
    		FileHandler.get().load();
    	});
    	
    	menuFile.getItems().addAll(menuItemNew, menuItemOpen);
    	
    	Menu menuAlgorithm = new Menu("Algorithm");
    	
    	MenuItem menuAlgorithmBFS = new MenuItem("BFS");
    	menuAlgorithmBFS.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			new BFS(currentWorkspace.getGraph()).calculate();
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmDFS = new MenuItem("DFS");
    	menuAlgorithmDFS.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			new DFS(currentWorkspace.getGraph()).calculate();
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmBridgeDetection = new MenuItem("Bridge Detection");
    	menuAlgorithmBridgeDetection.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			new BridgeDetection(currentWorkspace.getGraph()).calculate();
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmCycleDetection = new MenuItem("Cycle Detection");
    	menuAlgorithmCycleDetection.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			System.out.println(new CycleDetection(currentWorkspace.getGraph()).calculate());
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmPrimMST = new MenuItem("Minimal Spanning Tree - Prim");
    	menuAlgorithmPrimMST.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			new MSTPrim(currentWorkspace.getGraph()).calculate();
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	
    	menuAlgorithm.getItems().addAll(menuAlgorithmBFS, menuAlgorithmDFS, menuAlgorithmBridgeDetection, menuAlgorithmCycleDetection, menuAlgorithmPrimMST);
    	menuBar.getMenus().addAll(menuFile, menuAlgorithm);
        VBox menuBox = new VBox(menuBar);
    	
    	tabPane = new TabPane();
    	VBox tabBox = new VBox(tabPane);
    	
    	VBox sceneItems = new VBox();
    	sceneItems.getChildren().addAll(menuBox, tabBox);
    	
    	scene = new Scene(sceneItems);
    	baseWindow.setScene(scene);
    	
    	tabPane.getTabs().addAll(new Workspace("Workspace1"));
    	
    	Timeline delayedUpdateOnCreate = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tabPane.requestLayout();
			}
		}));
		delayedUpdateOnCreate.play();
    	
    	scene.setOnKeyPressed(e -> {
	    	switch(e.getCode()) {
	    	case F3:
	    		((Workspace) tabPane.getSelectionModel().getSelectedItem()).toggleDebugMonitor();
	    		break;
	    	default:
	    		break;
	    	}
	    });
    }
    
    public Window getBaseWindow() {
		return baseWindow;
	}
    
    public Scene getScene() {
		return scene;
	}
    
    public TabPane getTabPane() {
		return tabPane;
	}
    
    private void showWarningAlert(String titleMessage) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setHeaderText(titleMessage);
    	alert.show();
    }
    
    public void findAndRemoveFromWorkspace(Node node) {
    	for(Tab tab : tabPane.getTabs()) {
    		((Workspace) tab).removeNode(node);
    	}
    }

    public static void main(String[] args) {
    	launch(args);
    }

}
