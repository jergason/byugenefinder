package bio465.hmm;

public class HMMIslandIdentifier {
	private int windowSize = 0;
	private double threshold = 0;
	
	public HMMIslandIdentifier(int windowSize, double threshold) {
		this.windowSize = windowSize;
		this.threshold = threshold;
	}
	
	/*
	 * Slide a window of length windowSize along
	 * the sequence. If the ratio of Is to Bs is higher
	 * than threshold mark that region as containing a CpG 
	 * island.
	 */
	public void identifyCpGIslands(String state) {
		String window;
		int ICount;
		for (int i = 0; i < state.length(); i++) {
			window = state.substring(i, i + windowSize);
			ICount = window.replaceAll("B", "").length();
			if ((double)(ICount/windowSize) > threshold) {
				//@TODO: mark this region as a state
			}
		}
	}
}
