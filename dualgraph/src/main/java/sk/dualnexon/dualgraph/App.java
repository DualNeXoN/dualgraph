package sk.dualnexon.dualgraph;

import javafx.application.Application;
import javafx.stage.Stage;
import sk.dualnexon.dualgraph.window.Window;

public class App extends Application {

    @Override
    public void start(Stage stage) {
    	new Window();
    }

    public static void main(String[] args) {
    	launch(args);
    }

}
