package sk.dualnexon.dualgraph.lib.algorithm.parent;

import java.util.Optional;

import javafx.scene.control.ChoiceDialog;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.lib.visualization.GraphVisualizer;
import sk.dualnexon.dualgraph.ui.Destroyable;
import sk.dualnexon.dualgraph.ui.VisualControl;

public abstract class Algorithm implements Destroyable {
	
	protected Graph graph;
	protected GraphVisualizer visualizer;
	protected VisualControl controls;
	
	public Algorithm(Graph graph) {
		this.graph = graph;
		graph.unselectAll();
		visualizer = new GraphVisualizer();
		controls = new VisualControl(this);
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public GraphVisualizer getVisualizer() {
		return visualizer;
	}
	
	public VisualControl getControls() {
		return controls;
	}
	
	public abstract void calculate() throws AlgorithmException;
	
	protected void finished() {
		controls.setVisible(true);
		controls.update();
	}
	
	protected Vertex getStartingVertex() {
		ChoiceDialog<Vertex> choiceDialog = new ChoiceDialog<Vertex>(graph.getVertices().getFirst(), graph.getVertices());
		choiceDialog.setHeaderText(null);
		choiceDialog.setContentText("Select starting vertex:");
		Optional<Vertex> opt = choiceDialog.showAndWait();
		if(opt.isPresent()) {
			controls.update();
			return opt.get();
		} else {
			return null;
		}
	}
	
	@Override
	public void destroy() {
		visualizer.clearMasks();
		controls.destroy();
	}
	
}
