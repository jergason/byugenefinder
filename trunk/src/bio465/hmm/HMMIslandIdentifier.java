package bio465.hmm;

import bio465.hmm.Island;
import java.util.List;
import java.util.ArrayList;

/*
 * Returns a list of island states
 */
public class HMMIslandIdentifier {
	private int windowSize = 0;
	private double threshold = 0;
	private List<Island> islandStates;
	
	public HMMIslandIdentifier(int windowSize, double threshold) {
		this.windowSize = windowSize;
		this.threshold = threshold;
		islandStates = new ArrayList<Island>();
	}
	
	/*
	 * Slide a window of length windowSize along
	 * the sequence. If the ratio of Is to Bs is higher
	 * than threshold mark that region as containing a CpG 
	 * island.
	 * 
	 * @return a list of CpG islands
	 */
	public List<Island> identifyCpGIslands(String state) {
		String window;
		int ICount;
		double CpGRatio;
		for (int i = 0; i < state.length() - windowSize; i++) {
			window = state.substring(i, i + windowSize);
			ICount = this.count(window, 'I');
			CpGRatio = (double)ICount / windowSize;
			if (CpGRatio > threshold) {
				//store the CpG ratio, start and end locations of the island.
				//locations should be stored as 1-offset, so we add 1 to index in string
				islandStates.add(new Island(CpGRatio, i + 1, i + 1 + windowSize));
			}
		}
		mergeOverlappingIslands(state);
		return islandStates;
	}

	/*
	 * Loop through list of islands and merge overlapping islands.
	 * Depends on the islandStates list being sorted in ascending
	 * order by startOfIsland, which they should be by default.
	 */
	private void mergeOverlappingIslands(String state) {
		for (int i = 0; i < islandStates.size() - 1; i++) {
			Island a = islandStates.get(i);
			Island b = islandStates.get(i + 1);
			if (islandsDoOverlap(a, b)) {
				//remove the two overlapping islands, merge them together, and
				// insert the now island in their place.
				islandStates.remove(a);
				islandStates.remove(b);
				a = mergeOverlappingIslands(a, b, state);
				islandStates.add(i, a);
				//Decrement i so next time through the loop we check if the newly
				// inserted island overlaps the next one.
				i--;
			}
		}
	}
	
	//@TODO: Why are some islands ending up with a lower island ratio?
	private Island mergeOverlappingIslands(Island a, Island b, String state) {
		//@TODO: calculate the CG content?
		//String window = sequence.substring(a.getStartOfIsland() - 1, b.getEndOfIsland() - 1);
		int numIslands = state.substring(a.getStartOfIsland() - 1, b.getEndOfIsland() - 1).replaceAll("B", "").length();
		double islandRatio = (double)numIslands / (b.getEndOfIsland() - a.getStartOfIsland());
		return new Island(islandRatio, a.getStartOfIsland(), b.getEndOfIsland());
	}
	
	private int count(String sourceString, char lookFor) {
        if (sourceString == null) {
                return -1;
        }
        
        int count = 0;
        for (int i = 0; i < sourceString.length(); i++) {
                final char c = sourceString.charAt(i);
                if (c == lookFor) {
                        count++;
                }
        }
        return count;
}
	
	/*
	 * Returns true if island a ends inside of island b.
	 */
	private Boolean islandsDoOverlap(Island a, Island b) {
		return a.getEndOfIsland() > b.getStartOfIsland();
	}
}
