package sk.dualnexon.dualgraph.lib;

import java.util.HashMap;

public class AdjacencyList {
	
	private HashMap<Vertex, HashMap<Vertex, Double>> list;
	
	public AdjacencyList() {
		list = new HashMap<Vertex, HashMap<Vertex, Double>>();
	}
	
	public HashMap<Vertex, Double> addVertex(Vertex vertex) {
		return list.containsKey(vertex) ? list.get(vertex) : list.put(vertex, new HashMap<Vertex, Double>());
	}
	
	public void removeVertex(Vertex vertex) {
		list.remove(vertex);
	}
	
	public HashMap<Vertex, Double> getVertexList(Vertex vertex) {
		return list.containsKey(vertex) ? list.get(vertex) : null;
	}
	
	public void addEdge(Edge edge) {
		if(edge.isDirected()) {
			Vertex startingVertex = edge.getVertexDirectionStarting();
			addVertex(startingVertex).put(edge.getVertexDirection(), edge.getValue());
		} else {
			addVertex(edge.getFirstVertex()).put(edge.getSecondVertex(), edge.getValue());
			addVertex(edge.getSecondVertex()).put(edge.getFirstVertex(), edge.getValue());
		}
	}
	
	public void removeEdge(Edge edge) {
		HashMap<Vertex, Double> temp = null;
		temp = getVertexList(edge.getFirstVertex());
		if(temp != null) temp.remove(edge.getSecondVertex());
		temp = getVertexList(edge.getSecondVertex());
		if(temp != null) temp.remove(edge.getFirstVertex());
	}
	
	public String toFormattedString() {
		String out = "";
		
		for(Vertex rootVertex : list.keySet()) {
			out += rootVertex.getNamespace().getText();
			HashMap<Vertex, Double> tempRoot = getVertexList(rootVertex);
			if(tempRoot == null || tempRoot.size() == 0) {
				out += " {}";
			} else {
				for(Vertex innerVertex : tempRoot.keySet()) {
					out += " -> " + innerVertex.getNamespace().getText() + "(" + tempRoot.get(innerVertex) + ")";
				}
			}
			out += "\n";
		}
		
		return out;
	}
	
}
