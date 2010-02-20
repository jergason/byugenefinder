package bio465.hmm;
import bio465.hmm.HMM;
import bio465.hmm.HMMIslandIdentifier;
import java.util.List;

public class HMMDriver {
	public static void main(String[] args) {
		String pathToFastaFile = args[0];
		String pathToParams = args[1];
		//@TODO: convert from a string arg to an int and a double
		int windowSize = 200;
		double threshold = .65;
		HMM markovModel = new HMM(pathToFastaFile, pathToParams);
		String hiddenState = markovModel.calculateHiddenStateUsingTraceback();
		HMMIslandIdentifier islandIdentifier = new HMMIslandIdentifier(windowSize, threshold);
		List<Island> islands = islandIdentifier.identifyCpGIslands(hiddenState);
		System.out.println(hiddenState);
		//System.out.println(islands);
	}
}
