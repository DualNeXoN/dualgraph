package sk.dualnexon.dualgraph.window;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import sk.dualnexon.dualgraph.ui.Updatable;
import sk.dualnexon.dualgraph.ui.theme.ColorUI;
import sk.dualnexon.dualgraph.ui.theme.ThemeHandler;

public class Grid extends Canvas implements Updatable {
	
	private static final int DEFAULT_SPACING = 50;
	private static final int MIN_SPACING = 10;
	
	private GraphicsContext g2d;
	private int spacing;
	private Workspace workspace;
	
	public Grid(Workspace workspace, int spacing) {
		
		this.workspace = workspace;
		this.spacing = spacing;
		g2d = getGraphicsContext2D();
		
		workspace.addNode(this);
	}
	
	public Grid(Workspace workspace) {
		this(workspace, DEFAULT_SPACING);
	}
	
	public void toggleVisible() {
		setVisible(!isVisible());
	}
	
	@Override
	public void update() {
		
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		setWidth(screenBounds.getWidth());
		setHeight(screenBounds.getHeight());
		
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.setFill(ThemeHandler.get().getActiveTheme().getColor(ColorUI.WORKSPACE_BACKGROUND));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setLineWidth(0.25);
		g2d.setStroke(ThemeHandler.get().getActiveTheme().getColor(ColorUI.GRID_LINE));
		g2d.setFill(ThemeHandler.get().getActiveTheme().getColor(ColorUI.GRID_LINE));
		
		final int CYCLES_WIDTH = (int) getWidth() / spacing;
		for(int cycle = 0; cycle <= CYCLES_WIDTH+1; cycle++) {
			int gridOffsetX = (int)-(workspace.getOffsetX() % spacing);
			int startAt = cycle * spacing + gridOffsetX;
			g2d.strokeLine(startAt, 0, startAt, getHeight());
		}
		
		final int CYCLES_HEIGHT = (int) getHeight() / spacing;
		for(int cycle = 0; cycle <= CYCLES_HEIGHT+1; cycle++) {
			int gridOffsetY = (int)-(workspace.getOffsetY() % spacing);
			int startAt = cycle * spacing + gridOffsetY;
			g2d.strokeLine(0, startAt, getWidth(), startAt);
		}
		
		toBack();
		
	}
	
	public int getSpacing() {
		return spacing;
	}
	
	public void setSpacing(int spacing) {
		this.spacing = spacing;
		if(this.spacing < MIN_SPACING) this.spacing = MIN_SPACING;
		update();
	}

	@Override
	public void destroy() {
		workspace.removeNode(this);
	}
	
}
