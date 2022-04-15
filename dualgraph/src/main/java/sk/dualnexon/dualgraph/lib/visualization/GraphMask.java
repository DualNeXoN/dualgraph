package sk.dualnexon.dualgraph.lib.visualization;

import javafx.scene.paint.Color;
import sk.dualnexon.dualgraph.lib.AdjacencyList;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.ui.Destroyable;

public class GraphMask implements Destroyable {
	
	private static final Color DEFAULT_COLOR = Color.DARKVIOLET;
	
	private Graph graph;
	private AdjacencyList mask;
	private String message;
	
	public GraphMask(Graph graph, String message) {
		this.graph = graph;
		mask = new AdjacencyList();
		this.message = message;
	}
	
	public AdjacencyList getMask() {
		return mask;
	}
	
	public void applyMask() {
		applyMask(mask);
	}
	
	public void applyMask(AdjacencyList mask) {
		this.mask = mask;
		
		for(Vertex vertexRoot : mask.getRootVertices()) {
			vertexRoot.setColor(DEFAULT_COLOR);
			for(Vertex vertexAdj : mask.getVertexList(vertexRoot).keySet()) {
				vertexAdj.setColor(DEFAULT_COLOR);
				graph.getEdgeByEndPoints(vertexRoot, vertexAdj).setColor(DEFAULT_COLOR);
			}
		}
		
		graph.update();
	}
	
	public void resetMask() {
		for(Vertex vertex : graph.getVertices()) {
			vertex.setColor(null);
		}
		for(Edge edge : graph.getEdges()) {
			edge.setColor(null);
		}
		
		graph.update();
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public void destroy() {
		resetMask();
	}
	
}
