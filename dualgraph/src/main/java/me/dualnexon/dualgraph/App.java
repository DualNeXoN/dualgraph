package me.dualnexon.dualgraph;

import javafx.application.Application;
import javafx.stage.Stage;
import me.dualnexon.dualgraph.app.GraphStage;

public class App extends Application {

    @Override
    public void start(Stage stage) { new GraphStage(1280, 720); }

    public static void main(String[] args) { launch(args); }

}