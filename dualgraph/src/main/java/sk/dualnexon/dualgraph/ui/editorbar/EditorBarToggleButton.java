package sk.dualnexon.dualgraph.ui.editorbar;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class EditorBarToggleButton extends ToggleButton {
	
	private static final String STYLE_FOCUS = "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-width: 2px; -fx-border-stroke: solid; -fx-padding: 0;";
	private static final String STYLE_DISABLED = "-fx-border-color: black;";
	private static final String STYLE_ENABLED = "-fx-border-color: red;";
	
	private EditorBarAction action;
	
	public EditorBarToggleButton(EditorBarAction action) {
		this.action = action;
		setStyle(STYLE_FOCUS + STYLE_DISABLED);
		
		ImageView view = new ImageView(action.getIcon());
		view.setFitHeight(32);
		view.setPreserveRatio(true);
		setPrefSize(32, 32);
		setGraphic(view);
		
		setTooltip(new Tooltip(action.getDescription()));
		
	}
	
	public void toggleOn() {
		setSelected(true);
		setStyle(STYLE_FOCUS + STYLE_ENABLED);
	}
	
	public void toggleOff() {
		setSelected(false);
		setStyle(STYLE_FOCUS + STYLE_DISABLED);
	}
	
	public EditorBarAction getAction() {
		return action;
	}
	
}
