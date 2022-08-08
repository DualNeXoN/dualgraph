package sk.dualnexon.dualgraph.ui.editorbar;

import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.window.Workspace;

public class EditorBar extends HBox implements Updatable {
	
	private Workspace workspace;
	private double dragStartX, dragStartY;
	
	private HashMap<EditorBarAction, EditorBarToggleButton> actionButtons = new HashMap<>();
	
	public EditorBar(Workspace workspace) {
		super(25);
		this.workspace = workspace;
		setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(-3))));
		
		for(EditorBarAction action : EditorBarAction.values()) {
			EditorBarToggleButton btn = new EditorBarToggleButton(action);
			btn.setOnMouseClicked(e-> {
				setAction(btn.getAction());
				btn.setSelected(true);
			});
			actionButtons.put(action, btn);
			getChildren().add(btn);
		}
		
		setAction(EditorBarAction.NONE);
		
		setOnMouseDragged(e-> {
			if(!e.isPrimaryButtonDown()) return;
			setLayoutX(e.getSceneX() - dragStartX);
			setLayoutY(e.getSceneY() - dragStartY - 60);
			correctPosition();
			setCursor(Cursor.CLOSED_HAND);
		});
		
		setOnMousePressed(e-> {
			dragStartX = e.getX();
			dragStartY = e.getY();
		});
		
		setOnMouseReleased(e-> {
			setCursor(Cursor.DEFAULT);
		});
		
	}
	
	public EditorBarAction getCurrentAction() {
		for(EditorBarAction action : EditorBarAction.values()) {
			if(isActionActivated(action)) return action;
		}
		return EditorBarAction.NONE;
	}
	
	public boolean isActionActivated(EditorBarAction action) {
		return actionButtons.get(action).isSelected();
	}
	
	public void setAction(EditorBarAction action) {
		for(EditorBarAction temp : EditorBarAction.values()) {
			actionButtons.get(temp).toggleOff();
		}
		actionButtons.get(action).toggleOn();
	}
	
	private void correctPosition() {
		if(getScene() == null) return;
		if(getLayoutX() + getLayoutBounds().getWidth() > getScene().getWidth() - 5) setLayoutX(getScene().getWidth() - getLayoutBounds().getWidth() - 5);
		if(getLayoutX() < 5) setLayoutX(5);
		if(getLayoutY() + getLayoutBounds().getHeight() > getScene().getHeight() - getLayoutBounds().getHeight() - 35) setLayoutY(getScene().getHeight() - getLayoutBounds().getHeight() - 60);
		if(getLayoutY() < 5) setLayoutY(5);
	}

	@Override
	public void update() {
		correctPosition();
		toFront();
	}
	
	@Override
	public void destroy() {
		workspace.removeNode(this);
	}
	
}
