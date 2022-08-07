package sk.dualnexon.dualgraph.ui.theme;

import javafx.scene.paint.Color;

public enum ColorUI {
	
	WORKSPACE_BACKGROUND(Color.WHITE, Color.BLACK),
	GRID_LINE(Color.BLACK, Color.WHITE),
	VERTEX_OUTLINE(Color.BLACK, Color.WHITE),
	VERTEX_FILL(Color.WHITE, Color.BLACK),
	NAMESPACE(Color.BLACK, Color.WHITE),
	EDGE(Color.BLACK, Color.WHITE),
	NODE_SELECT(Color.RED, Color.RED);
	
	private Color defaultColorDay, defaultColorNight;
	
	ColorUI(Color defaultColorDay, Color defaultColorNight) {
		this.defaultColorDay = defaultColorDay;
		this.defaultColorNight = defaultColorNight;
	}
	
	public Color getDefaultColorDay() {
		return defaultColorDay;
	}
	
	public Color getDefaultColorNight() {
		return defaultColorNight;
	}

}
