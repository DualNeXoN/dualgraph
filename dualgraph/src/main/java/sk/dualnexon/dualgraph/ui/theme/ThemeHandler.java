package sk.dualnexon.dualgraph.ui.theme;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.Tab;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.util.FileHandler;
import sk.dualnexon.dualgraph.window.Workspace;

public class ThemeHandler {
	
	private static ThemeHandler instance;
	
	public static ThemeHandler get() {
		return instance;
	}
	
	private ArrayList<Theme> themes = new ArrayList<>();
	private Theme activeTheme;
	
	public ThemeHandler() {
		if(instance != null) return;
		instance = this;
		themes.add(activeTheme = new Theme());
		loadThemes();
	}
	
	private void loadThemes() {
		
		JSONObject jsonThemesObject = FileHandler.get().loadThemes();
		JSONArray jsonThemesArray = jsonThemesObject.getJSONArray("themes");
		
		for(int index = 0; index < jsonThemesArray.length(); index++) {
			themes.add(ThemeParserJSON.jsonToTheme(jsonThemesArray.getJSONObject(index)));
		}
		
	}
	
	public Theme getActiveTheme() {
		return activeTheme;
	}
	
	public void setActiveTheme(Theme activeTheme) {
		this.activeTheme = activeTheme;
		Vertex.defaultColor = activeTheme.getColor(ColorUI.VERTEX_FILL);
		Edge.defaultColor = activeTheme.getColor(ColorUI.EDGE);
		for(Tab tab : App.get().getTabPane().getTabs()) {
			Workspace workspace = (Workspace) tab;
			for(Vertex obj : workspace.getGraph().getVertices()) {
				obj.setColor(activeTheme.getColor(ColorUI.VERTEX_OUTLINE));
				obj.getNamespace().getNode().setFill(activeTheme.getColor(ColorUI.NAMESPACE));
			}
			for(Edge obj : workspace.getGraph().getEdges()) {
				obj.setColor(activeTheme.getColor(ColorUI.EDGE));
				obj.getNamespace().getNode().setFill(activeTheme.getColor(ColorUI.NAMESPACE));
			}
		}
		try {
			for(Tab tab : App.get().getTabPane().getTabs()) {
				Workspace workspace = (Workspace) tab;
				workspace.update();
			}
		} catch(NullPointerException ex) {}
	}
	
	public ArrayList<Theme> getThemes() {
		return themes;
	}
	
	public void addTheme(Theme theme) {
		themes.add(theme);
	}
	
	public void removeTheme(Theme theme) {
		themes.remove(theme);
	}
	
}
