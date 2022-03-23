package sk.dualnexon.dualgraph;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.util.FileHandler;
import sk.dualnexon.dualgraph.window.Window;
import sk.dualnexon.dualgraph.window.Workspace;

public class App extends Application {
	
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
    	menuBar.getMenus().add(menuFile);
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
    
    public void findAndRemoveFromWorkspace(Node node) {
    	for(Tab tab : tabPane.getTabs()) {
    		((Workspace) tab).removeNode(node);
    	}
    }

    public static void main(String[] args) {
    	launch(args);
    }

}
