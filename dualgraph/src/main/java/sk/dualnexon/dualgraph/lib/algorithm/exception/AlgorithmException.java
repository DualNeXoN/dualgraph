package sk.dualnexon.dualgraph.lib.algorithm.exception;

import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class AlgorithmException extends Exception {
	
	private static final long serialVersionUID = -2579491136856030552L;
	
	protected Algorithm algorithm;
	
	public AlgorithmException(Algorithm algorithm) {
		this.algorithm = algorithm;
	}
	
	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
}
