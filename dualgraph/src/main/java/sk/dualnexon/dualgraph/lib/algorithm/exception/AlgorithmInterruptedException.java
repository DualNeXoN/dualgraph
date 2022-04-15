package sk.dualnexon.dualgraph.lib.algorithm.exception;

import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class AlgorithmInterruptedException extends AlgorithmException {
	
	private static final long serialVersionUID = -1789977177958129172L;
	
	public AlgorithmInterruptedException(Algorithm algorithm) {
		super(algorithm);
	}
	
}
