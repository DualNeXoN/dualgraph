package sk.dualnexon.dualgraph.window;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class Window extends Stage {
	
	private static final String DEFAULT_WINDOW_TITLE = "DualGraph";
	private static final double DEFAULT_SCALE_DIVIDE_WIDTH = 1.5;
	private static final double DEFAULT_SCALE_DIVIDE_HEIGHT = 1.5;

	public Window() {
		this(Screen.getPrimary().getBounds().getWidth() / DEFAULT_SCALE_DIVIDE_WIDTH, Screen.getPrimary().getBounds().getHeight() / DEFAULT_SCALE_DIVIDE_HEIGHT);
	}
	
	public Window(double width, double height) {
		
		setWidth(width);
		setHeight(height);
		setTitle(DEFAULT_WINDOW_TITLE);
		
		show();
		
	}
	
	public void toggleFullscreen() {
		setFullScreen(!isFullScreen());
	}
	
}
