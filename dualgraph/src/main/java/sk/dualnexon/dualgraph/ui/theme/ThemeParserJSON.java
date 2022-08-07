package sk.dualnexon.dualgraph.ui.theme;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.paint.Color;

public class ThemeParserJSON {
	
	public static Theme jsonToTheme(JSONObject obj) {
		
		Theme theme = new Theme();
		theme.setThemeName(obj.getString("name"));
		
		JSONArray uiArray = obj.getJSONArray("settings");
		for(Object temp : uiArray) {
			JSONObject uiObject = (JSONObject) temp;
			ColorUI colorUI = ColorUI.valueOf(uiObject.getString("ui"));
			Color color = Color.color(uiObject.getDouble("red"), uiObject.getDouble("green"), uiObject.getDouble("blue"));
			theme.setColor(colorUI, color);
		}
		
		return theme;
	}
	
}
