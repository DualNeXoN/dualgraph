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
	
	private double offsetX = 0, initialDragX = 0;
	private double offsetY = 0, initialDragY = 0;
	
	private boolean stageMove = false;
	
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
	
	public double getOffsetX() {
		return offsetX;
	}
	
	public void setOffsetX(double offsetX) {
		this.offsetX = initialDragX - offsetX;
	}
		
	public double getOffsetY() {
		return offsetY;
	}
	
	public void setOffsetY(double offsetY) {
		this.offsetY = initialDragY - offsetY;
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
	}
	
}
