package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.window.Workspace;

public class DFS extends Algorithm {
	
	public DFS(Workspace workspace) {
		super(workspace);
	}
	
	public void calculate() {
		
		Vertex s = getStartingVertex();
		
		if(s == null) {
			return;
		}
		
		HashMap<Vertex, Boolean> visited = new HashMap<>();
        for(Vertex v : workspace.getGraph().getVerticies()) {
        	visited.put(v, false);
        }
        
        recursive(s, visited);
        System.out.println();
		
	}
	
	private void recursive(Vertex v, HashMap<Vertex, Boolean> visited) {
		
		visited.put(v, true);
        System.out.print(v.getNamespace().getText() + " ");
 
        Iterator<Vertex> i = workspace.getGraph().getAdjacencyList().getVertexList(v).keySet().iterator();
        while(i.hasNext()) {
            Vertex n = i.next();
            if(!visited.get(n)) {
            	recursive(n, visited);
            }
        }
		
	}
	
	private Vertex getStartingVertex() {
		ChoiceDialog<Vertex> choiceDialog = new ChoiceDialog<Vertex>(workspace.getGraph().getVerticies().getFirst(), workspace.getGraph().getVerticies());
		choiceDialog.setHeaderText(null);
		choiceDialog.setContentText("Select starting vertex:");
		Optional<Vertex> opt = choiceDialog.showAndWait();
		if(opt.isPresent()) {
			return opt.get();
		} else {
			return null;
		}
	}
	
}
