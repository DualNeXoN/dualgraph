package sk.dualnexon.dualgraph;

import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
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
import sk.dualnexon.dualgraph.lib.algorithm.MinSpanningTreePrim;
import sk.dualnexon.dualgraph.ui.theme.Theme;
import sk.dualnexon.dualgraph.ui.theme.ThemeHandler;
import sk.dualnexon.dualgraph.lib.algorithm.MaxSpanningTreePrim;
import sk.dualnexon.dualgraph.util.FileHandler;
import sk.dualnexon.dualgraph.util.exception.NotValidFormatException;
import sk.dualnexon.dualgraph.window.Window;
import sk.dualnexon.dualgraph.window.Workspace;

public class App extends Application {
	
	private static final String MSG_NO_WORKSPACE = "No workspace has been chosen";
	private static final String DEFAULT_WORKSPACE_NAME = "Workspace";
	
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
    	new ThemeHandler();
    	
    	MenuBar menuBar = new MenuBar();
    	Menu menuFile = new Menu("File");
    	
    	MenuItem menuItemNew = new MenuItem("New...");
    	menuItemNew.setOnAction(e -> {
    		tabPane.getTabs().add(new Workspace(DEFAULT_WORKSPACE_NAME));
    	});
    	MenuItem menuItemOpen = new MenuItem("Open...");
    	menuItemOpen.setOnAction(e -> {
    		try {
    			FileHandler.get().loadWorkspace();
    		} catch(NotValidFormatException ex) {
    			showWarningAlert(ex.toString());
    		}
    	});
    	MenuItem menuItemSave = new MenuItem("Save...");
    	menuItemSave.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			FileHandler.get().saveWorkspace(currentWorkspace);
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	
    	menuFile.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave);
    	
    	Menu menuAlgorithm = new Menu("Algorithm");
    	
    	MenuItem menuAlgorithmBFS = new MenuItem("BFS");
    	menuAlgorithmBFS.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			currentWorkspace.applyAlgorithm(new BFS(currentWorkspace.getGraph()));
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmDFS = new MenuItem("DFS");
    	menuAlgorithmDFS.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			currentWorkspace.applyAlgorithm(new DFS(currentWorkspace.getGraph()));
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmBridgeDetection = new MenuItem("Bridge Detection");
    	menuAlgorithmBridgeDetection.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			currentWorkspace.applyAlgorithm(new BridgeDetection(currentWorkspace.getGraph()));
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmCycleDetection = new MenuItem("Cycle Detection");
    	menuAlgorithmCycleDetection.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			currentWorkspace.applyAlgorithm(new CycleDetection(currentWorkspace.getGraph()));
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmPrimMST = new MenuItem("Minimal Spanning Tree - Prim");
    	menuAlgorithmPrimMST.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			currentWorkspace.applyAlgorithm(new MinSpanningTreePrim(currentWorkspace.getGraph()));
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	MenuItem menuAlgorithmPrimMaxSpanningTree = new MenuItem("Maximal Spanning Tree - Prim");
    	menuAlgorithmPrimMaxSpanningTree.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			currentWorkspace.applyAlgorithm(new MaxSpanningTreePrim(currentWorkspace.getGraph()));
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	
    	menuAlgorithm.getItems().addAll(menuAlgorithmBFS, menuAlgorithmDFS, menuAlgorithmBridgeDetection, menuAlgorithmCycleDetection, menuAlgorithmPrimMST, menuAlgorithmPrimMaxSpanningTree);
    	
    	Menu menuOptions = new Menu("Options");
    	
    	MenuItem menuThemes = new MenuItem("Themes");
    	menuThemes.setOnAction(e -> {
    		ChoiceDialog<Theme> choiceDialog = new ChoiceDialog<Theme>(ThemeHandler.get().getActiveTheme(), ThemeHandler.get().getThemes());
    		choiceDialog.setHeaderText(null);
    		choiceDialog.setContentText("Select theme:");
    		Optional<Theme> opt = choiceDialog.showAndWait();
    		if(opt.isPresent()) {
    			ThemeHandler.get().setActiveTheme(opt.get());
    		}
    	});
    	menuOptions.getItems().addAll(menuThemes);
    	
    	Menu menuHelp = new Menu("Help");
    	
    	MenuItem menuUse = new MenuItem("How to use");
    	menuUse.setOnAction(e-> {
    		showInfoAlert(FileHandler.get().loadTextFileFromRes("use.txt"));
    	});
    	menuHelp.getItems().addAll(menuUse);
    	
    	menuBar.getMenus().addAll(menuFile, menuAlgorithm, menuOptions, menuHelp);
        VBox menuBox = new VBox(menuBar);
    	
    	tabPane = new TabPane();
    	VBox tabBox = new VBox(tabPane);
    	
    	VBox sceneItems = new VBox();
    	sceneItems.getChildren().addAll(menuBox, tabBox);
    	
    	scene = new Scene(sceneItems);
    	baseWindow.setScene(scene);
    	
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
	    		Workspace currentWorkspace = ((Workspace) tabPane.getSelectionModel().getSelectedItem());
	    		if(currentWorkspace == null) break;
	    		currentWorkspace.toggleDebugMonitor();
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
    
    public void showWarningAlert(String titleMessage) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setHeaderText(titleMessage);
    	alert.show();
    }
    
    public void showInfoAlert(String contentText) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setHeaderText(contentText);
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
