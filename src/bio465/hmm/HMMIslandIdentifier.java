package bio465.hmm;
import java.util.List;
import java.util.LinkedList;

public class HMMIslandIdentifier {
	private int windowSize = 0;
	private double threshold = 0;
	private List<Island> islandStates;
	
	public HMMIslandIdentifier(int windowSize, double threshold) {
		this.windowSize = windowSize;
		this.threshold = threshold;
		islandStates = new LinkedList<Island>();
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
		double CpGRatio;
		for (int i = 0; i < state.length(); i++) {
			window = state.substring(i, i + windowSize);
			ICount = window.replaceAll("B", "").length();
			CpGRatio = (double)ICount / windowSize;
			if (CpGRatio > threshold) {
				//store a state object in a list containing the number of islands, and the percentage
				//@TODO: mark this region as a state
				islandStates.add(new Island(CpGRatio, i, i + windowSize));
			}
		}
	}
	
	private class Island {
		public double CpGRatio = 0;
		public int startOfIsland = 0;
		public int endOfIsland = 0;
		
		public Island(double CpG, int start, int end) {
			CpGRatio = CpG;
			startOfIsland = start;
			endOfIsland = end;
		}

		public double getCpGRatio() {
			return CpGRatio;
		}

		public void setCpGRatio(double cpGRatio) {
			CpGRatio = cpGRatio;
		}

		public int getStartOfIsland() {
			return startOfIsland;
		}

		public void setStartOfIsland(int startOfIsland) {
			this.startOfIsland = startOfIsland;
		}

		public int getEndOfIsland() {
			return endOfIsland;
		}

		public void setEndOfIsland(int endOfIsland) {
			this.endOfIsland = endOfIsland;
		}
	}
}
