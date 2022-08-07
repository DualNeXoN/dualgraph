package sk.dualnexon.dualgraph.ui.theme;

import java.util.HashMap;

import javafx.scene.paint.Color;

public class Theme {
	
	public static final String DEFAULT_NAME = "Default Theme";
	
	private String themeName;
	private HashMap<ColorUI, Color> colorMap = new HashMap<>();
	
	public Theme(String themeName) {
		this.themeName = themeName;
	}
	
	public Theme() {
		this(DEFAULT_NAME);
	}
	
	public Color getColor(ColorUI ui) {
		return (colorMap.containsKey(ui) ? colorMap.get(ui) : ui.getDefaultColorDay());
	}
	
	public void setColor(ColorUI ui, Color color) {
		colorMap.put(ui, color);
	}
	
	public void setDefaultColor(ColorUI ui) {
		colorMap.put(ui, ui.getDefaultColorDay());
	}
	
	public String getThemeName() {
		return themeName;
	}
	
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	
	@Override
	public String toString() {
		return themeName;
	}
	
}
