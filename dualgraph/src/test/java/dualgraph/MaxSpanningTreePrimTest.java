package dualgraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.algorithm.MaxSpanningTreePrim;
import sk.dualnexon.dualgraph.lib.algorithm.exception.AlgorithmException;
import sk.dualnexon.dualgraph.window.Workspace;

public class MaxSpanningTreePrimTest extends ApplicationTest {
	
	private static final String DEFAULT_WORKSPACE_NAME = "Test";
	
	@Override
    public void start(Stage stage) {}
	
	@Test
	public void graphTest1() {
		final int EXPECTED_RESULT = 7;
		Workspace workspace = new Workspace(DEFAULT_WORKSPACE_NAME);
		Vertex vertexA = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexB = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexC = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexD = new Vertex(workspace.getGraph(), 0, 0);
		Edge edgeAB = new Edge(workspace.getGraph(), vertexA, vertexB);
		edgeAB.setValue(2);
		Edge edgeAC = new Edge(workspace.getGraph(), vertexA, vertexC);
		edgeAC.setValue(4);
		Edge edgeBD = new Edge(workspace.getGraph(), vertexB, vertexD);
		Edge edgeCD = new Edge(workspace.getGraph(), vertexC, vertexD);
		workspace.getGraph().addVertices(vertexA, vertexB, vertexC, vertexD);
		workspace.getGraph().addEdge(edgeAB);
		workspace.getGraph().addEdge(edgeAC);
		workspace.getGraph().addEdge(edgeBD);
		workspace.getGraph().addEdge(edgeCD);
		MaxSpanningTreePrim a = new MaxSpanningTreePrim(workspace.getGraph());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					a.setStartVertex(vertexA);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexB);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexC);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexD);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
				} catch (AlgorithmException e) {
					e.printStackTrace();
					fail();
				}
			}
		});
	}
	
	@Test
	public void graphTest2() {
		final int EXPECTED_RESULT = 71;
		Workspace workspace = new Workspace(DEFAULT_WORKSPACE_NAME);
		Vertex vertexA = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexB = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexC = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexD = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexE = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexF = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexG = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexH = new Vertex(workspace.getGraph(), 0, 0);
		Vertex vertexI = new Vertex(workspace.getGraph(), 0, 0);
		Edge edgeAB = new Edge(workspace.getGraph(), vertexA, vertexB);
		edgeAB.setValue(4);
		Edge edgeAC = new Edge(workspace.getGraph(), vertexA, vertexC);
		edgeAC.setValue(8);
		Edge edgeBD = new Edge(workspace.getGraph(), vertexB, vertexD);
		edgeBD.setValue(8);
		Edge edgeBC = new Edge(workspace.getGraph(), vertexB, vertexC);
		edgeBC.setValue(11);
		Edge edgeCE = new Edge(workspace.getGraph(), vertexC, vertexE);
		edgeCE.setValue(7);
		Edge edgeCF = new Edge(workspace.getGraph(), vertexC, vertexF);
		edgeCF.setValue(1);
		Edge edgeEF = new Edge(workspace.getGraph(), vertexE, vertexF);
		edgeEF.setValue(6);
		Edge edgeDE = new Edge(workspace.getGraph(), vertexD, vertexE);
		edgeDE.setValue(2);
		Edge edgeDG = new Edge(workspace.getGraph(), vertexD, vertexG);
		edgeDG.setValue(7);
		Edge edgeDH = new Edge(workspace.getGraph(), vertexD, vertexH);
		edgeDH.setValue(4);
		Edge edgeFH = new Edge(workspace.getGraph(), vertexF, vertexH);
		edgeFH.setValue(2);
		Edge edgeGH = new Edge(workspace.getGraph(), vertexG, vertexH);
		edgeGH.setValue(14);
		Edge edgeGI = new Edge(workspace.getGraph(), vertexG, vertexI);
		edgeGI.setValue(9);
		Edge edgeHI = new Edge(workspace.getGraph(), vertexH, vertexI);
		edgeHI.setValue(10);
		workspace.getGraph().addVertices(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG, vertexH, vertexI);
		workspace.getGraph().addEdge(edgeAB);
		workspace.getGraph().addEdge(edgeAC);
		workspace.getGraph().addEdge(edgeBD);
		workspace.getGraph().addEdge(edgeBC);
		workspace.getGraph().addEdge(edgeCE);
		workspace.getGraph().addEdge(edgeCF);
		workspace.getGraph().addEdge(edgeEF);
		workspace.getGraph().addEdge(edgeDE);
		workspace.getGraph().addEdge(edgeDG);
		workspace.getGraph().addEdge(edgeDH);
		workspace.getGraph().addEdge(edgeFH);
		workspace.getGraph().addEdge(edgeGH);
		workspace.getGraph().addEdge(edgeGI);
		workspace.getGraph().addEdge(edgeHI);
		MaxSpanningTreePrim a = new MaxSpanningTreePrim(workspace.getGraph());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					a.setStartVertex(vertexA);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexB);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexC);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexD);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexE);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexF);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexG);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexH);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
					a.setStartVertex(vertexI);
					a.calculate();
					assertEquals(EXPECTED_RESULT, a.getValueOfSpanningTree());
				} catch (AlgorithmException e) {
					e.printStackTrace();
					fail();
				}
			}
		});
	}
	
}
