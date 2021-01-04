package me.dualnexon.dualgraph.app;

import me.dualnexon.dualgraph.lib.Graph;
import me.dualnexon.dualgraph.lib.Vertex;

public class SceneBuilder {
	
	public static void mainScene() {
		
		GraphStage gs = GraphStage.get();
		gs.removeAllNodes();
		
		new Graph();
		
		gs.getScene().setOnMousePressed((e) -> {
			if(e.isControlDown()) Graph.get().addVertex(new Vertex(e.getX(), e.getY()));
		});
		
		gs.getScene().setOnKeyPressed((e) -> {
			switch(e.getCode()) {
			case ESCAPE:
				System.exit(0);
				break;
			case DELETE:
				Graph.get().deleteAllSelected();
				break;
			case F1:
				Graph.get().reset();
				break;
			default:
			}
		});
		
	}
	
}
