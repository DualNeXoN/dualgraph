package sk.dualnexon.dualgraph.lib.algorithm.exception;

import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class UnsupportedGraphException extends AlgorithmException {

	private static final long serialVersionUID = -3428845541447888502L;
	
	public UnsupportedGraphException(Algorithm algorithm) {
		super(algorithm);
	}
	
	@Override
	public String getMessage() {
		return "Graph is not supported";
	}
	
}
