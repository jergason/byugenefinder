package bio465.hmm;
import bio465.hmm.HMM;
import bio465.hmm.HMMIslandIdentifier;
import java.util.List;

public class HMMDriver {
	public static void main(String[] args) throws Exception{
		if (args.length != 3 || args[0].equals("help")) {
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
			System.out.print(islandIdentifier.identifyCpGIslands(hiddenState));
			//System.out.println(hiddenState);
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
		help.append("**HMMGeneFinder usage**");
		help.append("\nrun HMMGeneFinder to show this help.");
		help.append("\nHMMGeneFinder run [sequence_file] [parameters_file]");
		help.append("\n\tRun the main hidden markov model algorithm using the sequence from");
		help.append("\n\t [sequence_file] and the parameters in [parameters_file].");
		help.append("\n\tThe islands will be written out to standard output as CSVs.");
		help.append("\n\tTry redirecting them to a text file like this:\n\t\tHMMGeneFinder run seq.fasta prams.txt > out.csv");
		help.append("\nHMMGeneFinder train [sequence_file] [parameters_file]");
		help.append("\n\tUse supervised training on [sequence_file] and write the resulting");
		help.append("\n\t parameters to [parameters_file].");
		return help.toString();
	}
}
