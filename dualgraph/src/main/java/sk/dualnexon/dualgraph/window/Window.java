package sk.dualnexon.dualgraph.window;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class Window extends Stage {

	public Window() {
		this(Screen.getPrimary().getBounds().getWidth() / 1.5, Screen.getPrimary().getBounds().getHeight() / 1.5);
	}
	
	public Window(double width, double height) {
		
		setWidth(width);
		setHeight(height);
		
		show();
		
	}
	
}
