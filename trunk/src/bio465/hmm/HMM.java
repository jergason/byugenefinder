//********************************************************************
//  HMM.java     Author: Group 2/Lab3
//
//  Takes a DNA sequence and hidden Markov Model probabilities as parameters
//  and returns both the sequence and the calculated CpG Island or non-island state.
//********************************************************************

package bio465.hmm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;
import bio465.hmm.HMMSequenceReader;

public class HMM {
	final static double comparisonThreshold = .05;
	private HMMSequenceReader sequenceReader;
	private String sequence;
	private double iStart, iToI, iToB, bStart, bToB, bToI = 0;
	private Map<Character, Double>iEmissions;
	private Map<Character, Double>bEmissions;

	public HMM(String pathToSequenceFile, String pathToParameterFile) {
		sequence = "";
		sequenceReader = new HMMSequenceReader(pathToSequenceFile);
		iEmissions = new HashMap<Character, Double>();
		bEmissions = new HashMap<Character, Double>();
		
		try {
			sequence = sequenceReader.readInFastaSequence();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		readInParametersAndStoreInIVars(pathToParameterFile);
	}
	
	private void readInParametersAndStoreInIVars(String path) {
		FileReader file;
		Scanner scanner;
		try {
			file = new FileReader(path);
			scanner = new Scanner(file);
			iStart = Math.log(scanner.nextDouble());
			iToI = Math.log(scanner.nextDouble());
			iToB = Math.log(scanner.nextDouble());
			iEmissions.put(Character.valueOf('a'), Double.valueOf(Math.log(scanner.nextDouble())));
			iEmissions.put(Character.valueOf('c'), Double.valueOf(Math.log(scanner.nextDouble())));
			iEmissions.put(Character.valueOf('t'), Double.valueOf(Math.log(scanner.nextDouble())));
			iEmissions.put(Character.valueOf('g'), Double.valueOf(Math.log(scanner.nextDouble())));
			
			bStart = Math.log(scanner.nextDouble());
			bToB = Math.log(scanner.nextDouble());
			bToI = Math.log(scanner.nextDouble());
			bEmissions.put(Character.valueOf('a'), Double.valueOf(Math.log(scanner.nextDouble())));
			bEmissions.put(Character.valueOf('c'), Double.valueOf(Math.log(scanner.nextDouble())));
			bEmissions.put(Character.valueOf('t'), Double.valueOf(Math.log(scanner.nextDouble())));
			bEmissions.put(Character.valueOf('g'), Double.valueOf(Math.log(scanner.nextDouble())));
			file.close();
			scanner.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * Returns a string containing the most probable states for each location
	 * in the sequence.
	 */
	public String calculateHiddenStateUsingTraceback() {
		//Variables used in loop to store previous states
		double probI, probB, prevProbI, prevProbB;
		double comeFromI, comeFromB = 0;
		Character currentChar = new Character('x');
		
		double[] iProbabilities = new double[sequence.length()];
		double[] bProbabilities = new double[sequence.length()];
		//set up start probabilities for each state
		probI = prevProbI = iProbabilities[0] = iStart + iEmissions.get(Character.valueOf(sequence.charAt(0)));
		probB = prevProbB = bProbabilities[0] = bStart + bEmissions.get(Character.valueOf(sequence.charAt(0)));
		
		//build array of all probabilities
		for (int i = 1; i < sequence.length(); i++) {
			//The new probB is the maximum of coming from I and emitting the current char in B
			// and coming from B and emitting the current char in B
			currentChar = Character.valueOf(sequence.charAt(i));
			comeFromI = iToB + bEmissions.get(currentChar);
			comeFromB = bToB + bEmissions.get(currentChar);
			probB = Math.max(prevProbI + comeFromI, prevProbB + comeFromB);
			bProbabilities[i] = probB;
			
			//now calculate state for I
			comeFromI = iToI + iEmissions.get(currentChar);
			comeFromB = bToI + iEmissions.get(currentChar);
			probI = Math.max(prevProbI + comeFromI, prevProbB + comeFromB);
			iProbabilities[i] = probI;
			
			//set up the previous probabilities
			prevProbI = probI;
			prevProbB = probB;
		}
		
		//char[] backwardsString = calculateTraceback(iProbabilities, bProbabilities, sequence);
		//return (probI > probB) ? stringI : stringB;
		//return (probI > probB) ? stringIBuilder.toString() : stringBBuilder.toString();
		return calculateTraceback(iProbabilities, bProbabilities, sequence);
	}
	
	private String calculateTraceback(double[] iProbs, double[]bProbs, String sequence) {
		//start at end of sequence and trace backwards.
		char currentChar;
		char[] backwardsTraceback = new char[sequence.length()];
		double comeFromI, comeFromB, currentProbability;
		Boolean tracingFromI = (iProbs[sequence.length() - 1] > bProbs[sequence.length() - 1]);
		backwardsTraceback[sequence.length() - 1] = (tracingFromI) ? 'I' : 'B';
		for (int i = sequence.length() - 1; i > 0; i--) {
			currentChar = Character.valueOf(sequence.charAt(i));
			if (tracingFromI) {
				currentProbability = iProbs[i];
				comeFromI = iToI + iEmissions.get(currentChar);
				comeFromB = bToI + iEmissions.get(currentChar);
			}
			else {
				currentProbability = bProbs[i];
				comeFromB = bToB + bEmissions.get(currentChar);
				comeFromI = iToB + bEmissions.get(currentChar);
			}
			if (didComeFromI(comeFromI, currentProbability, iProbs[i - 1])) {
				backwardsTraceback[i - 1] = 'I';
				tracingFromI = true;
			}
			else {
				backwardsTraceback[i - 1] = 'B';
				tracingFromI = false;
			}
		}
		return getStateFromTraceback(backwardsTraceback);
	}
	
	/*
	 * Take an array of chars in reverse order and build a string in forward order.
	 */
	private String getStateFromTraceback(char[] traceback) {
		StringBuilder state = new StringBuilder(traceback.length);
		for (int i = 0; i < traceback.length; i++) {
			state.append(traceback[i]);
		}
		return state.toString();
	}

	private boolean didComeFromI(double comeFromIProb, double currentProb, double prevIProb) {
		return doublesAreEqual(currentProb - comeFromIProb, prevIProb);
	}

	public String getSequence() {
		return sequence;
	}
	
	private Boolean doublesAreEqual(double a, double b) {
		double result = b - a;
		return comparisonThreshold > Math.abs(result);
	}
	
}
