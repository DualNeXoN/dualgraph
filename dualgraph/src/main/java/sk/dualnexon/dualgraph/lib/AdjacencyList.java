package sk.dualnexon.dualgraph.lib;

import java.util.HashMap;
import java.util.Set;

public class AdjacencyList {
	
	private HashMap<Vertex, HashMap<Vertex, Integer>> list;
	
	public AdjacencyList() {
		list = new HashMap<Vertex, HashMap<Vertex, Integer>>();
	}
	
	public HashMap<Vertex, Integer> addVertex(Vertex vertex) {
		return list.containsKey(vertex) ? list.get(vertex) : list.put(vertex, new HashMap<>());
	}
	
	public void removeVertex(Vertex vertex) {
		list.remove(vertex);
	}
	
	public HashMap<Vertex, Integer> getVertexList(Vertex vertex) {
		return list.containsKey(vertex) ? list.get(vertex) : null;
	}
	
	public void addEdge(Edge edge) {
		if(edge.isDirected()) {
			Vertex startingVertex = edge.getVertexDirectionStarting();
			addVertex(startingVertex);
			addVertex(startingVertex).put(edge.getVertexDirection(), edge.getValue());
		} else {
			addVertex(edge.getFirstVertex());
			addVertex(edge.getSecondVertex());
			addVertex(edge.getFirstVertex()).put(edge.getSecondVertex(), edge.getValue());
			addVertex(edge.getSecondVertex()).put(edge.getFirstVertex(), edge.getValue());
		}
	}
	
	public boolean hasVertex(Vertex vertexToSearch) {
		for(Vertex vertexRoot : list.keySet()) {
			for(Vertex vertexAdj : getVertexList(vertexRoot).keySet()) {
				if(vertexToSearch.equals(vertexAdj)) return true;
			}
		}
		return false;
	}
	
	public boolean hasEdge(Edge edgeToSearch) {
		for(Vertex vertexRoot : list.keySet()) {
			for(Vertex vertexAdj : getVertexList(vertexRoot).keySet()) {
				if((edgeToSearch.getFirstVertex().equals(vertexRoot) && edgeToSearch.getSecondVertex().equals(vertexAdj)) ||
						edgeToSearch.getFirstVertex().equals(vertexAdj) && edgeToSearch.getSecondVertex().equals(vertexRoot)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void removeEdge(Edge edge) {
		HashMap<Vertex, Integer> temp = null;
		temp = getVertexList(edge.getFirstVertex());
		if(temp != null) temp.remove(edge.getSecondVertex());
		temp = getVertexList(edge.getSecondVertex());
		if(temp != null) temp.remove(edge.getFirstVertex());
	}
	
	public Set<Vertex> getRootVerticies() {
		return list.keySet();
	}
	
	public void setList(HashMap<Vertex, HashMap<Vertex, Integer>> list) {
		this.list = list;
	}
	
	public AdjacencyList clone() {
		AdjacencyList obj = new AdjacencyList();
		HashMap<Vertex, HashMap<Vertex, Integer>> objList = new HashMap<>();
		
		for(Vertex vertex : list.keySet()) {
			objList.put(vertex, new HashMap<>());
			for(Vertex vertex2 : list.get(vertex).keySet()) {
				objList.get(vertex).put(vertex2, list.get(vertex).get(vertex2));
			}
		}
		
		obj.setList(objList);
		return obj;
	}
	
	public String toFormattedString() {
		String out = "";
		
		for(Vertex rootVertex : list.keySet()) {
			out += rootVertex.getNamespace().getText();
			HashMap<Vertex, Integer> tempRoot = getVertexList(rootVertex);
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
