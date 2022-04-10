package dualgraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.BFS;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.window.Workspace;

public class BFSTest extends ApplicationTest {
	
	private static final String DEFAULT_WORKSPACE_NAME = "Test";

	@Override
	public void start(Stage stage) throws Exception {}
	
	@Test
	public void graphTest1() {
		Workspace workspace = new Workspace(DEFAULT_WORKSPACE_NAME);
		Vertex vertexA = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexB = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexC = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexD = new Vertex(workspace.getGraph(), 0, 0);
		Edge edgeAB = new Edge(workspace.getGraph(), vertexA, vertexB);
		Edge edgeAC = new Edge(workspace.getGraph(), vertexA, vertexC);
		Edge edgeBD = new Edge(workspace.getGraph(), vertexB, vertexD);
		Edge edgeCD = new Edge(workspace.getGraph(), vertexC, vertexD);
		workspace.getGraph().addVertices(vertexA, vertexB, vertexC, vertexD);
		workspace.getGraph().addEdge(edgeAB);
		workspace.getGraph().addEdge(edgeAC);
		workspace.getGraph().addEdge(edgeBD);
		workspace.getGraph().addEdge(edgeCD);
		BFS a = new BFS(workspace.getGraph());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					a.setStartVertex(vertexA);
					a.calculate();
					assertEquals(4, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexB);
					a.calculate();
					assertEquals(4, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexC);
					a.calculate();
					assertEquals(4, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexD);
					a.calculate();
					assertEquals(4, a.getCountOfDiscoveredVertices());
				} catch (AlgorithmException e) {
					e.printStackTrace();
					fail();
				}
			}
		});
	}
	
	@Test
	public void graphTest2() {
		Workspace workspace = new Workspace(DEFAULT_WORKSPACE_NAME);
		Vertex vertexA = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexB = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexC = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexD = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexE = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexF = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexG = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexH = new Vertex(workspace.getGraph(), 0, 0);
		Edge edgeAB = new Edge(workspace.getGraph(), vertexA, vertexB);
		Edge edgeAC = new Edge(workspace.getGraph(), vertexA, vertexC);
		Edge edgeBD = new Edge(workspace.getGraph(), vertexB, vertexD);
		Edge edgeCF = new Edge(workspace.getGraph(), vertexC, vertexF);
		Edge edgeGH = new Edge(workspace.getGraph(), vertexG, vertexH);
		workspace.getGraph().addVertices(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG, vertexH);
		workspace.getGraph().addEdge(edgeAB);
		workspace.getGraph().addEdge(edgeAC);
		workspace.getGraph().addEdge(edgeBD);
		workspace.getGraph().addEdge(edgeCF);
		workspace.getGraph().addEdge(edgeGH);
		BFS a = new BFS(workspace.getGraph());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					a.setStartVertex(vertexA);
					a.calculate();
					assertEquals(5, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexB);
					a.calculate();
					assertEquals(5, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexC);
					a.calculate();
					assertEquals(5, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexD);
					a.calculate();
					assertEquals(5, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexE);
					a.calculate();
					assertEquals(1, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexF);
					a.calculate();
					assertEquals(5, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexG);
					a.calculate();
					assertEquals(2, a.getCountOfDiscoveredVertices());
					a.setStartVertex(vertexH);
					a.calculate();
					assertEquals(2, a.getCountOfDiscoveredVertices());
				} catch (AlgorithmException e) {
					e.printStackTrace();
					fail();
				}
			}
		});
	}
	
}
