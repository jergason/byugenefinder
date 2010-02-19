package bio465.hmm;

/*
 * Class to conduct supervised training from an annotated DNA sequence.
 * HMMSupervisedTrainer will read in a file containing a DNA sequence 
 * and the CpG island state at each region, and will return the supervised
 * HMM parameters for that state.
 */

public class HMMSupervisedTrainer {
	private String path;
	private String sequence;
	private String hiddenState;
	
	public HMMSupervisedTrainer(String path) {
		this.path = path;
	}
	
	
}
