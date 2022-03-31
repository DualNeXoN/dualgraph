package sk.dualnexon.dualgraph.lib.visualization;

import java.util.LinkedList;

public class GraphVisualizer {
	
	private LinkedList<GraphMask> masks;
	private int currentMask = 0;
	
	public GraphVisualizer() {
		masks = new LinkedList<>();
	}
	
	public LinkedList<GraphMask> getMasks() {
		return masks;
	}
	
	public GraphVisualizer addMask(GraphMask mask) {
		masks.addLast(mask);
		return this;
	}
	
	public void removeMask(GraphMask mask) {
		masks.remove(mask);
	}
	
	public void clearMasks() {
		for(int index = masks.size()-1; index >= 0; index--) {
			masks.get(index).destroy();
		}
		masks.clear();
	}
	
	public void applyMask(int index) {
		if(index >= masks.size()) return;
		currentMask = index;
		masks.get(currentMask).resetMask();
		masks.get(currentMask).applyMask();
	}
	
	public void applyLastMask() {
		if(masks.size() == 0) return;
		currentMask = masks.size()-1;
		applyMask(currentMask);
	}
	
	public void nextStep() {
		if((currentMask+1) >= masks.size()) return;
		applyMask(currentMask+1);
	}
	
	public void previousStep() {
		if((currentMask-1) < 0) return;
		applyMask(currentMask-1);
	}
	
	public int getCurrentMask() {
		return currentMask;
	}
	
	public int getMaskCount() {
		return masks.size();
	}
	
}
