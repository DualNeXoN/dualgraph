package sk.dualnexon.dualgraph;

import javafx.application.Application;
import javafx.stage.Stage;
import sk.dualnexon.dualgraph.window.Window;
import sk.dualnexon.dualgraph.window.Workspace;

public class App extends Application {
	
	private static App instance;
	
	public static App get() {
		return instance;
	}

	private Window baseWindow;
	private Workspace workspace;
	
    @Override
    public void start(Stage stage) {
    	instance = this;
    	baseWindow = new Window();
    	workspace = new Workspace("Workspace1");
    	baseWindow.setScene(workspace.getScene());
    }
    
    public Workspace getWorkspace() {
		return workspace;
	}
    
    public Window getBaseWindow() {
		return baseWindow;
	}

    public static void main(String[] args) {
    	launch(args);
    }

}
