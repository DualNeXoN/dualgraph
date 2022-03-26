package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.window.Workspace;

public class BFS extends Algorithm {
	
	public BFS(Workspace workspace) {
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
 
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
 
        visited.put(s, true);
        queue.add(s);
 
        while (queue.size() != 0) {
        	
            s = queue.poll();
            System.out.print(s.getNamespace().getText() + " ");
 
            Iterator<Vertex> i = workspace.getGraph().getAdjacencyList().getVertexList(s).keySet().iterator();
            while(i.hasNext()) {
                Vertex n = i.next();
                if(!visited.get(n)) {
                    visited.put(n, true);
                    queue.add(n);
                }
            }
        }
        
        System.out.println();
		
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