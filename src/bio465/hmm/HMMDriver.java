package bio465.hmm;
import bio465.hmm.HMM;

public class HMMDriver {
	public static void main(String[] args) {
		String pathToFastaFile = args[0];
		String pathToParams = args[1];
		HMM markovModel = new HMM(pathToFastaFile, pathToParams);
		String hiddenState = markovModel.calcHiddenState();
		System.out.println(hiddenState);
	}
}
