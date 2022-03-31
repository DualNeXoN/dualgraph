package sk.dualnexon.dualgraph.lib.algorithm.exception;

import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class NoVerticiesException extends AlgorithmException {
	
	private static final long serialVersionUID = -1789977177958129172L;
	
	public NoVerticiesException(Algorithm algorithm) {
		super(algorithm);
	}
	
	@Override
	public String getMessage() {
		return "Unable to calculate algorithm without verticies";
	}
	
}
