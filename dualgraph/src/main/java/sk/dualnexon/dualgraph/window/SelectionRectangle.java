package sk.dualnexon.dualgraph.window;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sk.dualnexon.dualgraph.lib.Edge;
import sk.dualnexon.dualgraph.lib.Vertex;
import sk.dualnexon.dualgraph.ui.Updatable;

public class SelectionRectangle extends Canvas implements Updatable {
	
	private Workspace workspace;
	private GraphicsContext g2d;
	private double startPositionX, startPositionY, endPositionX, endPositionY;
	
	public SelectionRectangle(Workspace workspace, double startPositionX, double startPositionY) {
		this.workspace = workspace;
		g2d = getGraphicsContext2D();
		setStartPositionX(startPositionX);
		setStartPositionY(startPositionY);
		workspace.addNode(this);
	}
	
	public double getStartPositionX() {
		return startPositionX;
	}
	
	public void setStartPositionX(double startPositionX) {
		this.startPositionX = startPositionX;
		endPositionX = startPositionX;
	}
	
	public double getStartPositionY() {
		return startPositionY;
	}
	
	public void setStartPositionY(double startPositionY) {
		this.startPositionY = startPositionY;
		endPositionY = startPositionY;
	}
	
	public double getEndPositionX() {
		return endPositionX;
	}
	
	public void setEndPositionX(double endPositionX) {
		this.endPositionX = endPositionX;
	}
	
	public double getEndPositionY() {
		return endPositionY;
	}
	
	public void setEndPositionY(double endPositionY) {
		this.endPositionY = endPositionY;
	}
	
	private boolean isInRect(double nodeX, double nodeY, double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		return (nodeX > topLeftX && nodeX < bottomRightX && nodeY > topLeftY && nodeY < bottomRightY);
	}
	
	private void calcSelection() {
		
		double offX = workspace.getOffsetX();
		double offY = workspace.getOffsetY();
		
		double topLeftX = startPositionX + offX;
		double topLeftY = startPositionY + offY;
		double bottomRightX = endPositionX + offX;
		double bottomRightY = endPositionY + offY;
		
		if(startPositionX > endPositionX) {
			topLeftX = endPositionX + offX;
			bottomRightX = startPositionX + offX;
		}
		
		if(startPositionY > endPositionY) {
			topLeftY = endPositionY + offY;
			bottomRightY = startPositionY + offY;
		}
		
		for(Vertex vertex : workspace.getGraph().getVertices()) {
			if(isInRect(vertex.getPositionX(), vertex.getPositionY(), topLeftX, topLeftY, bottomRightX, bottomRightY)) vertex.select();
		}
		
		for(Edge edge : workspace.getGraph().getEdges()) {
			double edgeCenterX = (edge.getNode().getStartX() + edge.getNode().getEndX()) / 2;
			double edgeCenterY = (edge.getNode().getStartY() + edge.getNode().getEndY()) / 2;
			if(isInRect(edgeCenterX, edgeCenterY, topLeftX, topLeftY, bottomRightX, bottomRightY)) edge.select();
		}
	}
	
	@Override
	public void update() {
		if(startPositionX <= endPositionX) setLayoutX(startPositionX);
		else setLayoutX(endPositionX);
		
		if(startPositionY <= endPositionY) setLayoutY(startPositionY);
		else setLayoutY(endPositionY);
		setWidth(Math.abs(startPositionX - endPositionX));
		setHeight(Math.abs(startPositionY - endPositionY));
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.setFill(Color.BLUE);
		setOpacity(0.3);
		if(startPositionX != endPositionX || startPositionY != endPositionY) g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public void destroy() {
		calcSelection();
		setWidth(0);
		setHeight(0);
		setLayoutX(-10000);
		setLayoutY(-10000);
		workspace.removeNode(this);
	}
	
}
