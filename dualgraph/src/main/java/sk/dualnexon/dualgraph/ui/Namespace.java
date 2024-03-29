package sk.dualnexon.dualgraph.ui;

import java.util.Optional;

import javafx.scene.Cursor;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.lib.BaseGraphNode;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.ui.theme.ColorUI;
import sk.dualnexon.dualgraph.ui.theme.ThemeHandler;

public class Namespace implements Updatable {

	private BaseGraphNode graphNode;
	private Text node;
	private double offsetX = 0, offsetY = 0;
	private boolean valueLocked = false;
	
	public Namespace(BaseGraphNode graphNode, double offsetX, double offsetY, String text) {
		this.graphNode = graphNode;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		node = new Text(graphNode.getRealPositionX() + offsetX, graphNode.getRealPositionY() + offsetY, text);
		node.setFont(new Font(24));
		node.setFill(ThemeHandler.get().getActiveTheme().getColor(ColorUI.NAMESPACE));
		
		node.setOnMouseDragged(e -> {
			if(e.isPrimaryButtonDown()) {
				setOffsetX(e.getX() - graphNode.getRealPositionX() - node.getBoundsInLocal().getWidth() / 2);
				setOffsetY(e.getY() - graphNode.getRealPositionY() + node.getBoundsInLocal().getHeight() / 2);
				node.setCursor(Cursor.CLOSED_HAND);
			}
		});
		
		node.setOnMouseReleased((e) -> {
			node.setCursor(Cursor.HAND);
		});
		
		node.setOnMouseEntered((e) -> {
			node.setCursor(Cursor.HAND);
		});
		
		node.setOnMouseExited((e) -> {
			node.setCursor(Cursor.DEFAULT);
		});
		
	}
	
	public Namespace(BaseGraphNode graphNode, String text) {
		this(graphNode, 0, 0, text);
	}
	
	public double getRealPositionX() {
		return graphNode.getRealPositionX() + offsetX;
	}
	
	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
		update();
	}
	
	public double getOffsetX() {
		return offsetX;
	}
	
	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
		update();
	}
	
	public double getOffsetY() {
		return offsetY;
	}
	
	public double getRealPositionY() {
		return graphNode.getRealPositionY() + offsetY;
	}
	
	public void resetOffset(double x, double y) {
		setOffsetX(x);
		setOffsetY(y);
	}
	
	public void resetOffset() {
		resetOffset(0, 0);
	}
	
	public void requestToChangeValue() {
		TextInputDialog dialog = new TextInputDialog(node.getText());
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setTitle("DualGraph action");
		dialog.setContentText("Enter new value:");

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			String newName = result.get();
			for(Vertex vertex : graphNode.getGraph().getVertices()) {
				if(vertex.getNamespace().getText().equals(newName)) {
					App.get().showAlert(AlertType.WARNING, "Vertex with name " + newName + " already exists!");
					return;
				}
			}
		    setText(newName);
		}
	}
	
	public void setText(String text) {
		node.setText(text);
	}
	
	public String getText() {
		return node.getText();
	}
	
	public void setValueLocked(boolean valueLocked) {
		this.valueLocked = valueLocked;
	}
	
	public boolean isValueLocked() {
		return valueLocked;
	}
	
	public void toggleVisible() {
		node.setVisible(!node.isVisible());
	}
	
	public boolean isVisible() {
		return node.isVisible();
	}
	
	public Text getNode() {
		return node;
	}
	
	public BaseGraphNode getGraphNode() {
		return graphNode;
	}

	@Override
	public void update() {
		node.setX(getRealPositionX());
		node.setY(getRealPositionY());
		node.toFront();
	}

	@Override
	public void destroy() {
		App.get().findAndRemoveFromWorkspace(node);
	}
	
}
