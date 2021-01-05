package me.dualnexon.dualgraph.lib;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.dualnexon.dualgraph.app.GraphStage;

public class Namespace implements INode {
	
	private Text text;
	private double x;
	private double y;
	private double offsetX = 0;
	private double offsetY = 0;
	
	public Namespace(String name, double x, double y) {
		
		this.x = x;
		this.y = y;
		
		this.text = new Text(name);
		text.setFont(new Font(20));
		GraphStage.get().addNode(text);
		
		updatePos();
		
		events();
		
	}
	
	private void events() {
		
		text.setOnMouseDragged((e) -> {
			offsetX = e.getSceneX() - x - getWidth()/2;
			offsetY = e.getSceneY() - y + getHeight()/2;
			updatePos();
		});
		
	}
	
	public void updatePos() {
		text.setLayoutX(x + offsetX);
		text.setLayoutY(y + offsetY);
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public String getName() {
		return text.getText();
	}
	
	public double getWidth() {
		return text.getBoundsInLocal().getWidth();
	}
	
	public double getHeight() {
		return text.getBoundsInLocal().getHeight();
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void toFront() {
		text.toFront();
	}

	@Override
	public void destroy() {
		GraphStage.get().removeNode(text);
	}
	
}
