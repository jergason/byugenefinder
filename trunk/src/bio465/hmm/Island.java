package bio465.hmm;

public class Island {
	public double islandToNonIslandRatio = 0;
	public int startOfIsland = 0;
	public int endOfIsland = 0;
	
	public Island(double iToNon, int start, int end) {
		islandToNonIslandRatio = iToNon;
		startOfIsland = start;
		endOfIsland = end;
	}

	public double getIslandToNonIslandRatio() {
		return islandToNonIslandRatio;
	}

	public int getStartOfIsland() {
		return startOfIsland;
	}

	public int getEndOfIsland() {
		return endOfIsland;
	}
	
	public String toString() {
		String toReturn = "";
		toReturn += "<Island from " + startOfIsland + " to " + endOfIsland + " | " + islandToNonIslandRatio + "% Island state>";
		return toReturn;
	}
	
	public String toCSV() {
		return "" + startOfIsland + "," + endOfIsland + "," + islandToNonIslandRatio;
	}
}
