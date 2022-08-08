package sk.dualnexon.dualgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sk.dualnexon.dualgraph.lib.Graph;
import sk.dualnexon.dualgraph.lib.algorithm.BFS;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;
import sk.dualnexon.dualgraph.ui.theme.Theme;
import sk.dualnexon.dualgraph.ui.theme.ThemeHandler;
import sk.dualnexon.dualgraph.util.FileHandler;
import sk.dualnexon.dualgraph.util.exception.NotValidFormatException;
import sk.dualnexon.dualgraph.window.Window;
import sk.dualnexon.dualgraph.window.Workspace;

public class App extends Application {
	
	private static final String MSG_NO_WORKSPACE = "No workspace has been chosen";
	private static final String DEFAULT_WORKSPACE_NAME = "Workspace";
	
	private static App instance;
	
	public static App get() {
		return instance;
	}

	private Window baseWindow;
	private Scene scene;
	private TabPane tabPane;
	
	private boolean autofillVerticesNames = false;
	
    @Override
    public void start(Stage stage) {
    	instance = this;
    	baseWindow = new Window();
    	new FileHandler();
    	new ThemeHandler();
    	
    	MenuBar menuBar = new MenuBar();
    	Menu menuFile = new Menu("File");
    	
    	MenuItem menuItemNew = new MenuItem("New...");
    	menuItemNew.setOnAction(e -> {
    		tabPane.getTabs().add(new Workspace(DEFAULT_WORKSPACE_NAME));
    	});
    	MenuItem menuItemOpen = new MenuItem("Open...");
    	menuItemOpen.setOnAction(e -> {
    		try {
    			FileHandler.get().loadWorkspace();
    		} catch(NotValidFormatException ex) {
    			showWarningAlert(ex.toString());
    		}
    	});
    	MenuItem menuItemSave = new MenuItem("Save...");
    	menuItemSave.setOnAction(e -> {
    		Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
    		if(currentWorkspace != null) {
    			FileHandler.get().saveWorkspace(currentWorkspace);
    		} else {
    			showWarningAlert(MSG_NO_WORKSPACE);
    		}
    	});
    	
    	menuFile.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave);
    	
    	Menu menuAlgorithm = new Menu("Algorithm");
    	
    	Set<Class<?>> algorithmSet = loadAlgorithms();
    	for(Class<?> algorithm : algorithmSet) {
    		MenuItem menuItemAlgorithm = new MenuItem(algorithm.getSimpleName());
    		menuItemAlgorithm.setOnAction(e-> {
    			Workspace currentWorkspace = (Workspace) tabPane.getSelectionModel().getSelectedItem();
        		if(currentWorkspace != null) {
        			try {
        				Class<?> clazz = Class.forName(algorithm.getCanonicalName());
        				Constructor<?> constructor = clazz.getConstructor(Graph.class);
        				currentWorkspace.applyAlgorithm((Algorithm) constructor.newInstance(currentWorkspace.getGraph()));
        			} catch(Exception ex) {}
        		} else {
        			showWarningAlert(MSG_NO_WORKSPACE);
        		}
    		});
    		menuAlgorithm.getItems().add(menuItemAlgorithm);
    	}
    	
    	Menu menuOptions = new Menu("Options");
    	
    	MenuItem menuThemes = new MenuItem("Themes");
    	menuThemes.setOnAction(e -> {
    		ChoiceDialog<Theme> choiceDialog = new ChoiceDialog<Theme>(ThemeHandler.get().getActiveTheme(), ThemeHandler.get().getThemes());
    		choiceDialog.setHeaderText(null);
    		choiceDialog.setContentText("Select theme:");
    		Optional<Theme> opt = choiceDialog.showAndWait();
    		if(opt.isPresent()) {
    			ThemeHandler.get().setActiveTheme(opt.get());
    		}
    	});
    	CheckMenuItem menuAutofillVertices = new CheckMenuItem("Autofill vertex names");
    	menuAutofillVertices.setOnAction(e -> {
    		autofillVerticesNames = !autofillVerticesNames;
    		if(autofillVerticesNames) {
    			for(Tab tab : tabPane.getTabs()) {
    				((Workspace) tab).getVertexNameConvention().reset();
    			}
    		}
    	});
    	menuOptions.getItems().addAll(menuThemes, menuAutofillVertices);
    	
    	Menu menuHelp = new Menu("Help");
    	
    	MenuItem menuUse = new MenuItem("How to use");
    	menuUse.setOnAction(e-> {
    		showInfoAlert(FileHandler.get().loadTextFileFromRes("use.txt"));
    	});
    	menuHelp.getItems().addAll(menuUse);
    	
    	menuBar.getMenus().addAll(menuFile, menuAlgorithm, menuOptions, menuHelp);
        VBox menuBox = new VBox(menuBar);
    	
    	tabPane = new TabPane();
    	VBox tabBox = new VBox(tabPane);
    	
    	VBox sceneItems = new VBox();
    	sceneItems.getChildren().addAll(menuBox, tabBox);
    	
    	scene = new Scene(sceneItems);
    	baseWindow.setScene(scene);
    	
    	Timeline delayedUpdateOnCreate = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tabPane.requestLayout();
			}
		}));
		delayedUpdateOnCreate.play();
    	
    	scene.setOnKeyPressed(e -> {
	    	switch(e.getCode()) {
	    	case F3:
	    		Workspace currentWorkspace = ((Workspace) tabPane.getSelectionModel().getSelectedItem());
	    		if(currentWorkspace == null) break;
	    		currentWorkspace.toggleDebugMonitor();
	    		break;
	    	default:
	    		break;
	    	}
	    });
    }
    
    public Window getBaseWindow() {
		return baseWindow;
	}
    
    public Scene getScene() {
		return scene;
	}
    
    public TabPane getTabPane() {
		return tabPane;
	}
    
    public boolean getAutofillVerticesNames() {
		return autofillVerticesNames;
	}
    
    public void showWarningAlert(String titleMessage) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setHeaderText(titleMessage);
    	alert.show();
    }
    
    public void showInfoAlert(String contentText) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setHeaderText(contentText);
    	alert.show();
    }
    
    public void findAndRemoveFromWorkspace(Node node) {
    	for(Tab tab : tabPane.getTabs()) {
    		((Workspace) tab).removeNode(node);
    	}
    }
    
    private Set<Class<?>> loadAlgorithms() {
    	if(GlobalSettings.isJar()) {
			return ReflectionScanner.getClassesFromJar(new File(System.getProperty("user.dir") + File.separator + System.getProperty("java.class.path")));
    	} else {
    		return ReflectionScanner.getClassesFromIDE(BFS.class.getPackageName());
    	}
    }
    
	private static class ReflectionScanner {
		
		public static Set<String> getClassNamesFromJar(File file) throws IOException {
		    Set<String> classNames = new HashSet<>();
		    try(JarFile jarFile = new JarFile(file)) {
		        Enumeration<JarEntry> enumerator = jarFile.entries();
		        while(enumerator.hasMoreElements()) {
		            JarEntry jarEntry = enumerator.nextElement();
		            if(jarEntry.getName().endsWith(".class")) {
		                String className = jarEntry.getName().replace("/", ".").replace(".class", "");
		                classNames.add(className);
		            }
		        }
		        return classNames;
		    }
		}
		
		public static Set<Class<?>> getClassesFromJar(File jarFile) {
			try {
				Set<String> classNames = getClassNamesFromJar(jarFile);
			    Set<Class<?>> classes = new HashSet<>(classNames.size());
			    try {
			    	URLClassLoader cl = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + jarFile + "!/") });
			    	for(String name : classNames) {
			        	if(name.startsWith(BFS.class.getPackageName()) && name.split(Pattern.quote(".")).length == 6) {
			        		Class<?> clazz = cl.loadClass(name);
			        		classes.add(clazz);
			        	}
			        }
			    } catch(Exception ex) {}
			    return classes;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		public static Set<Class<?>> getClassesFromIDE(String packageName) {
			InputStream stream = ResourceHandler.getSourceInputStream(packageName.replaceAll("[.]", "/"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName)).collect(Collectors.toSet());
		}

		private static Class<?> getClass(String className, String packageName) {
			try {
				return (Class<?>) Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

    public static void main(String[] args) {
    	launch(args);
    }

}
