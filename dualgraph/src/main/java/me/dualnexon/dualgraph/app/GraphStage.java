package me.dualnexon.dualgraph.app;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.dualnexon.dualgraph.AppConfiguration;
import me.dualnexon.dualgraph.AppInfo;

public class GraphStage extends Stage {
	
	private static GraphStage instance;
	
	public static GraphStage get() {
		return instance;
	}
	
	private Scene scene;
	private Group root;
	
	public GraphStage(int width, int height) {
		
		if(instance == null) instance = this;
		else return;
		
		setTitle(AppInfo.appName());
		
		setWidth(width);
		setHeight(height);
		
		root = new Group();
		scene =  new Scene(root, width, height, AppConfiguration.backgroundColor());
		
		setScene(scene);
		
		SceneBuilder.mainScene();
		
		show();
		
	}
	
	public void addNode(Node node) {
		root.getChildren().add(node);
	}
	
	public void removeNode(Node node) {
		root.getChildren().remove(node);
	}
	
	public void removeAllNodes() {
		root.getChildren().removeAll();
	}
	
}
