package sk.dualnexon.dualgraph.util;

public class VertexNameConvention {
	
	private String currentName = Character.toString(('A'-1));
	
	public void reset() {
		currentName = Character.toString(('A'-1));
	}
	
	public String getNextName() {
		shiftName();
		return getCurrentName();
	}
	
	public String getCurrentName() {
		return currentName;
	}
	
	private void shiftName() {
		
		StringBuilder sb = new StringBuilder(currentName);
		
		for(int index = currentName.length()-1; index >= 0; index--) {
			
			char nextChar = (char)(sb.charAt(index) + 1);
			
			if(nextChar <= 'Z') {
				sb.setCharAt(index, nextChar);
				break;
			} else {
				sb.setCharAt(index, 'A');
				if(index == 0) {
					sb.insert(0, 'A');
					break;
				}
			}
			
		}
		
		currentName = sb.toString();
	}
	
}
