package bio465.hmm;
import bio465.hmm.HMM;
import bio465.hmm.HMMIslandIdentifier;
import java.util.List;

public class HMMDriver {
	public static void main(String[] args) throws Exception{
		if (args.length != 3) {
			System.out.println(help());
		}
		else if (args[0].equals("run")) {
			String pathToFastaFile = args[1];
			String pathToParams = args[2];
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
		else if (args[0].equals("train")) {
			String pathToFastaFile = args[1];
			String pathToParams = args[2];
			HMMSupervisedTrainer trainer = new HMMSupervisedTrainer(pathToFastaFile, pathToParams);
		}
		else {
			throw new Exception("Error: problem with your arguments, sonny!");
		}
	}
	
	private static String help() {
		StringBuilder help = new StringBuilder();
		help.append("HMMGeneFinder usage: HMMGeneFinder [sequence_file] [parameters_file]");
		help.append("\nIt will print out a csv file containing the start and end locations of the island");
		help.append("\n as well as the percentage of island state it is.");
		return help.toString();
	}
}
