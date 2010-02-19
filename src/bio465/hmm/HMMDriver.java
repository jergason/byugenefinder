package bio465.hmm;
import bio465.hmm.HMM;
import bio465.hmm.HMMIslandIdentifier;

public class HMMDriver {
	public static void main(String[] args) {
		String pathToFastaFile = args[0];
		String pathToParams = args[1];
		HMM markovModel = new HMM(pathToFastaFile, pathToParams);
		String hiddenState = markovModel.calcHiddenState();
		HMMIslandIdentifier islandIdentifier = new HMMIslandIdentifier(200, .65);
		islandIdentifier.identifyCpGIslands(hiddenState);
		System.out.println(hiddenState);
	}
}
