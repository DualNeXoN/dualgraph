package sk.dualnexon.dualgraph;

import java.net.URL;

import javafx.scene.image.Image;

public class GlobalSettings {
	
	private static Boolean runningFromJar;
	
	public static boolean isJar() {
		if(runningFromJar == null) {
			URL url = App.class.getResource("/logo.png");
			if(url.getProtocol().startsWith("file")) runningFromJar = false;
			else runningFromJar = true;
		}
		return runningFromJar;
    }
	
	private static Image applicationIcon;
	
	public static Image getApplicationIcon() {
		if(applicationIcon == null) {
			applicationIcon = new Image(ResourceHandler.getResourceInputStream("logo.png"));
		}
		return applicationIcon;
	}
	
	public static boolean logConsole() {
		return true;
	}
	
}
