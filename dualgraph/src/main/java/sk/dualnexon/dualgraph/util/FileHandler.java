package sk.dualnexon.dualgraph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.stage.FileChooser;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.lib.Edge.DirectionType;
import sk.dualnexon.dualgraph.window.Workspace;

public class FileHandler {
	
	private static FileHandler instance;
	
	public static FileHandler get() {
		return instance;
	}
	
	public FileHandler() {
		if(instance != null) return;
		instance = this;
	}
	
	public void save(Workspace workspace) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("DG Workspaces", "*.dgw"));
		fileChooser.setInitialFileName(workspace.getName() + ".dgw");
		fileChooser.setTitle("DualGraph Save");
		File file = fileChooser.showSaveDialog(null);
		if(file == null) return;
		
		JSONObject joWorkspace = new JSONObject();
		joWorkspace.put("name", workspace.getName());
		joWorkspace.put("offsetX", workspace.getOffsetX() * -1);
		joWorkspace.put("offsetY", workspace.getOffsetY() * -1);
		
		JSONArray verticies = new JSONArray();
		for(Vertex vertex : workspace.getGraph().getVerticies()) {
			JSONObject obj = new JSONObject();
			obj.put("uuid", vertex.getUUID());
			obj.put("positionX", vertex.getPositionX());
			obj.put("positionY", vertex.getPositionY());
			obj.put("namespaceText", vertex.getNamespace().getText());
			obj.put("namespaceOffsetX", vertex.getNamespace().getOffsetX());
			obj.put("namespaceOffsetY", vertex.getNamespace().getOffsetY());
			verticies.put(obj);
		}
		joWorkspace.put("verticies", verticies);
		
		JSONArray edges = new JSONArray();
		for(Edge edge : workspace.getGraph().getEdges()) {
			JSONObject obj = new JSONObject();
			obj.put("firstVertex", edge.getFirstVertex().getUUID());
			obj.put("secondVertex", edge.getSecondVertex().getUUID());
			obj.put("value", edge.getValue());
			obj.put("directionType", edge.getDirection());
			if(edge.getVertexDirection() != null) {
				obj.put("vertexDirection", edge.getVertexDirection().getUUID());
			} else {
				obj.put("vertexDirection", "");
			}
			obj.put("namespaceOffsetX", edge.getNamespace().getOffsetX());
			obj.put("namespaceOffsetY", edge.getNamespace().getOffsetY());
			edges.put(obj);
		}
		joWorkspace.put("edges", edges);
		
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(joWorkspace.toString(4));
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void load() {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("DG Workspaces", "*.dgw"));
		fileChooser.setTitle("DualGraph Open");
		File file = fileChooser.showOpenDialog(null);
		if(file == null) return;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			JSONTokener tokener = new JSONTokener(br);
	        JSONObject objWorkspace = new JSONObject(tokener);
	        
	        Workspace w = new Workspace(objWorkspace.getString("name"));
			w.setOffsetX(objWorkspace.getDouble("offsetX"));
			w.setOffsetY(objWorkspace.getDouble("offsetY"));
			
			JSONArray verticies = objWorkspace.getJSONArray("verticies");
	        for (int i = 0; i < verticies.length(); i++) {
	        	JSONObject obj = verticies.getJSONObject(i);
	        	String uuid = obj.getString("uuid");
	        	double positionX = obj.getDouble("positionX");
	        	double positionY = obj.getDouble("positionY");
	        	String namespaceText = obj.getString("namespaceText");
	        	double namespaceOffsetX = obj.getDouble("namespaceOffsetX");
	        	double namespaceOffsetY = obj.getDouble("namespaceOffsetY");
	        	Vertex v = new Vertex(w.getGraph(), positionX, positionY);
	        	v.setUUID(uuid);
	        	v.getNamespace().setText(namespaceText);
	        	v.getNamespace().setOffsetX(namespaceOffsetX);
	        	v.getNamespace().setOffsetY(namespaceOffsetY);
	        	w.getGraph().addVertex(v);
	        }
	        
	        JSONArray edges = objWorkspace.getJSONArray("edges");
	        for (int i = 0; i < edges.length(); i++) {
	        	JSONObject obj = edges.getJSONObject(i);
	        	Vertex firstVertex = getVertexByUUID(w, obj.getString("firstVertex"));
	        	Vertex secondVertex = getVertexByUUID(w, obj.getString("secondVertex"));
	        	double value = obj.getDouble("value");
	        	DirectionType directionType = DirectionType.valueOf(obj.getString("directionType"));
	        	Vertex vertexDirection = getVertexByUUID(w, obj.getString("vertexDirection"));
	        	Edge e = new Edge(w.getGraph(), firstVertex, secondVertex, value, directionType, vertexDirection);
	        	e.getNamespace().setOffsetX(obj.getDouble("namespaceOffsetX"));
	        	e.getNamespace().setOffsetY(obj.getDouble("namespaceOffsetY"));
	        	w.getGraph().addEdge(e);
	        }
			
			App.get().getTabPane().getTabs().add(w);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
	}
	
	private Vertex getVertexByUUID(Workspace workspace, String uuid) {
		for(Vertex vertex : workspace.getGraph().getVerticies()) {
			if(vertex.getUUID().equals(uuid)) return vertex;
		}
		return null;
	}
	
}