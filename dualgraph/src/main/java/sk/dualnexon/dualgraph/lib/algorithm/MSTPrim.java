package sk.dualnexon.dualgraph.lib.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;
import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.window.Workspace;

public class MSTPrim extends Algorithm {
	
	private AdjacencyList adj;
	
	public MSTPrim(Workspace workspace) {
		super(workspace);
		adj = workspace.getGraph().getAdjacencyList();
	}

	public void calculate() {
		
		Vertex startVertex = getStartingVertex();
		
		if(startVertex == null) {
			return;
		}
		
		// Map of used verticies
		HashMap<Vertex, Boolean> used = new HashMap<>();
		for(Vertex vertex : workspace.getGraph().getVerticies()) {
			used.put(vertex, false);
		}
		used.put(startVertex, true);
		
		LinkedList<Edge> outputListEdges = new LinkedList<>(); // Output MST edges by order
		LinkedList<Edge> queue = new LinkedList<>(); // Queue of edges which are discovered
		
		// Add starting vertex to queue
		for(Vertex vertex : adj.getVertexList(startVertex).keySet()) {
			queue.addLast(workspace.getGraph().getEdgeByEndPoints(startVertex, vertex));
		}
		
		while(!usedAll(used)) {
			
			Edge nextAddedEdge = null;
			
			if(queue.size() == 1) {
				nextAddedEdge = queue.get(0);
				queue.remove(0);
			} else {
				nextAddedEdge = queue.get(0);
				for(int index = 1; index < queue.size(); index++) {
					if(queue.get(index).getValue() < nextAddedEdge.getValue()) {
						nextAddedEdge = queue.get(index);
					}
				}
				queue.remove(nextAddedEdge);
			}
			
			if(nextAddedEdge != null) {
				used.put(nextAddedEdge.getFirstVertex(), true);
				used.put(nextAddedEdge.getSecondVertex(), true);
				outputListEdges.addLast(nextAddedEdge);
				nextAddedEdge = null;
			}
			
			queue.clear();
			for(Vertex vertex : used.keySet()) {
				if(used.get(vertex)) {
					for(Vertex vertex2 : adj.getVertexList(vertex).keySet()) {
						if(!used.get(vertex2)) {
							queue.addLast(workspace.getGraph().getEdgeByEndPoints(vertex, vertex2));
						}
					}
				}
			}
			
		}
		
		int sum = 0;
		for(Edge edge : outputListEdges) {
			System.out.println(edge.getFirstVertex().getNamespace().getText() + "-" + edge.getSecondVertex().getNamespace().getText());
			sum += edge.getValue();
		}
		System.out.println(sum);
		
		System.out.println();
		
	}
	
	private boolean usedAll(HashMap<Vertex, Boolean> used) {
		for(Vertex vertex : used.keySet()) {
			if(!used.get(vertex)) return false;
		}
		return true;
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
