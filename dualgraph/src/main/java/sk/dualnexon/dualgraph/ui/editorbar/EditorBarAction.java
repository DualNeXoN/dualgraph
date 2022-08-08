package sk.dualnexon.dualgraph.ui.editorbar;

import javafx.scene.image.Image;
import sk.dualnexon.dualgraph.ResourceHandler;

public enum EditorBarAction {
	
	NONE("editorBarButtonNone.png", "No special action"),
	CREATE_VERTEX("editorBarButtonVertex.png", "Create vertex"),
	CREATE_EDGE("editorBarButtonEdge.png", "Create edge"),
	RENAME("editorBarButtonRename.png", "Rename graph node"),
	DELETE("editorBarButtonDelete.png", "Delete graph node");
	
	private Image icon;
	private String description;
	
	EditorBarAction(String icon, String description) {
		this.icon = new Image(ResourceHandler.getResourceInputStream("icons/" + icon));
		this.description = description;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	public String getDescription() {
		return description;
	}
	
}
