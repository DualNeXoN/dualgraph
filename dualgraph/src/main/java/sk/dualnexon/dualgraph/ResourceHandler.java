package sk.dualnexon.dualgraph;

import java.io.InputStream;

public class ResourceHandler {
	
	public static InputStream getResourceInputStream(String path) {
		return ClassLoader.getSystemResourceAsStream((GlobalSettings.isJar() ? "resources/" : "") + path);
	}
	
	public static InputStream getSourceInputStream(String path) {
		return ClassLoader.getSystemResourceAsStream((GlobalSettings.isJar() ? "src/main/" : "") + path);
	}
	
}
