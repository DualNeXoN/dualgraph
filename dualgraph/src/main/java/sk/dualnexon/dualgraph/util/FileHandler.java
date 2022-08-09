package sk.dualnexon.dualgraph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.ResourceHandler;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.util.exception.NotValidFormatException;
import sk.dualnexon.dualgraph.ui.theme.ColorUI;
import sk.dualnexon.dualgraph.ui.theme.Theme;
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
	
	public void saveWorkspace(Workspace workspace) {
		
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
		
		JSONArray vertices = new JSONArray();
		for(Vertex vertex : workspace.getGraph().getVertices()) {
			JSONObject obj = new JSONObject();
			obj.put("uuid", vertex.getUUID());
			obj.put("positionX", vertex.getPositionX());
			obj.put("positionY", vertex.getPositionY());
			obj.put("namespaceText", vertex.getNamespace().getText());
			obj.put("namespaceOffsetX", vertex.getNamespace().getOffsetX());
			obj.put("namespaceOffsetY", vertex.getNamespace().getOffsetY());
			vertices.put(obj);
		}
		joWorkspace.put("vertices", vertices);
		
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
	
	public void loadWorkspace() throws NotValidFormatException {
		
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
			
			JSONArray vertices = objWorkspace.getJSONArray("vertices");
	        for (int i = 0; i < vertices.length(); i++) {
	        	JSONObject obj = vertices.getJSONObject(i);
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
	        	int value = obj.getInt("value");
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
			App.get().showAlert(AlertType.WARNING, "File not found");
		} catch(JSONException ex) {
			ex.printStackTrace();
			throw new NotValidFormatException();
		}
		
	}
	
	public String loadTextFileFromRes(String path) {
		String output = "";
		
		try {
			InputStream in = ResourceHandler.getResourceInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String read = null;
			while((read = reader.readLine()) != null) {
				output += read + "\n";
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return output;
	}
	
	private Vertex getVertexByUUID(Workspace workspace, String uuid) {
		for(Vertex vertex : workspace.getGraph().getVertices()) {
			if(vertex.getUUID().equals(uuid)) return vertex;
		}
		return null;
	}
	
	public JSONObject loadThemes() {
		InputStream in = ResourceHandler.getResourceInputStream("themes.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		JSONTokener tokener = new JSONTokener(br);
		
		JSONObject obj = null;
		try {
			obj = new JSONObject(tokener);
		} catch(Exception ex) {
			generateThemeFile();
		} finally {
			try {
				br.close();
				in.close();
			} catch(IOException ex) {}
		}
		
		return obj;
	}
	
	public void saveTheme(Theme theme) {
		
		if(theme.getThemeName().equals(Theme.DEFAULT_NAME)) return;
		
		File file = Paths.get("src/main/resources/themes.json").toFile();
		JSONObject rootObject = loadThemes();
		
		JSONArray themeArray = rootObject.getJSONArray("themes");
		JSONObject themeObject = new JSONObject();
		
		for(int index = 0; index < themeArray.length(); index++) {
			if(themeArray.getJSONObject(index).getString("name").equals(theme.getThemeName())) {
				themeArray.remove(index);
				break;
			}
		}
		
		themeObject.put("name", theme.getThemeName());
		JSONArray themeArrayUI = new JSONArray();
		for(ColorUI ui : ColorUI.values()) {
			JSONObject uiObject = new JSONObject();
			Color color = theme.getColor(ui);
			uiObject.put("ui", ui.name());
			uiObject.put("red", color.getRed());
			uiObject.put("green", color.getGreen());
			uiObject.put("blue", color.getBlue());
			themeArrayUI.put(uiObject);
		}
		
		themeObject.put("settings", themeArrayUI);
		themeArray.put(themeObject);
		rootObject.put("themes", themeArray);
		
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(rootObject.toString(4));
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void generateThemeFile() {
		File file = Paths.get("src/main/resources/themes.json").toFile();
		JSONObject rootObject = new JSONObject();
		
		rootObject.put("themes", new JSONArray());
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(rootObject.toString(4));
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}