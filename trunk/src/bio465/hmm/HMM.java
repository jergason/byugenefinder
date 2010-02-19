//********************************************************************
//  HMM.java     Author: Group 2/Lab3
//
//  Takes a DNA sequence and hidden Markov Model probabilities as parameters
//  and returns both the sequence and the calculated CpG Island or non-island state.
//********************************************************************

package bio465.hmm;

import java.io.IOException;
import bio465.hmm.HMMSequenceReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class HMM {
	final static double comparisonThreshold = .05;
	private HMMSequenceReader data = new HMMSequenceReader();
	private String hiddenState;
	private String sequence;
	private double Istart, ItoI, ItoB, Bstart, BtoB, BtoI = 0;
	private Map<Character, Double>Iemissions;
	private Map<Character, Double>Bemissions;

	public HMM(String path, String parameterPath) {
		sequence = "";
		data.setPath(path);
		hiddenState = "";
		Iemissions = new HashMap<Character, Double>();
		Bemissions = new HashMap<Character, Double>();
		
		try {
			sequence = data.readInFastaSequence();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//read in parameters
		readInParameters(parameterPath);
	}
	
	//read in the parameters, store them as logs in instance vars
	private void readInParameters(String path) {
		FileReader file;
		Scanner scanner;
		try {
			file = new FileReader(path);
			scanner = new Scanner(file);
			Istart = Math.log10(scanner.nextDouble());
			ItoI = Math.log10(scanner.nextDouble());
			ItoB = Math.log10(scanner.nextDouble());
			Iemissions.put(Character.valueOf('a'), Double.valueOf(Math.log10(scanner.nextDouble())));
			Iemissions.put(Character.valueOf('c'), Double.valueOf(Math.log10(scanner.nextDouble())));
			Iemissions.put(Character.valueOf('t'), Double.valueOf(Math.log10(scanner.nextDouble())));
			Iemissions.put(Character.valueOf('g'), Double.valueOf(Math.log10(scanner.nextDouble())));
			
			Bstart = Math.log10(scanner.nextDouble());
			BtoB = Math.log10(scanner.nextDouble());
			BtoI = Math.log10(scanner.nextDouble());
			Bemissions.put(Character.valueOf('a'), Double.valueOf(Math.log10(scanner.nextDouble())));
			Bemissions.put(Character.valueOf('c'), Double.valueOf(Math.log10(scanner.nextDouble())));
			Bemissions.put(Character.valueOf('t'), Double.valueOf(Math.log10(scanner.nextDouble())));
			Bemissions.put(Character.valueOf('g'), Double.valueOf(Math.log10(scanner.nextDouble())));
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
	
	public String getSequence() {
		return sequence;
	}
	
	public String getHiddenState() {
		return hiddenState;
	}
	
	/*
	 * Returns a string containing the most probable states for each location
	 * in the sequence.
	 */
	public String calcHiddenState() {
		//Variables used in loop to store previous states
		String stringI = "I";
		String stringB = "B";
		String previousStringI = "I";
		String previousStringB = "B";
		
		double probI, probB, prevProbI, prevProbB;
		double comeFromI, comeFromB = 0;
		Character currentChar = new Character('x');
		
		//set up start probabilities for each state
		probI = prevProbI= Istart + Iemissions.get(Character.valueOf(sequence.charAt(0)));
		probB = prevProbB = Bstart + Bemissions.get(Character.valueOf(sequence.charAt(0)));
		
		for (int i = 1; i < sequence.length(); i++) {
			//The new probB is the maximum of coming from I and emitting the current char in B
			// and coming from B and emitting the current char in B
			currentChar = Character.valueOf(sequence.charAt(i));
			comeFromI = ItoB + Bemissions.get(currentChar);
			comeFromB = BtoB + Bemissions.get(currentChar);
			probB = Math.max(prevProbI + comeFromI, prevProbB + comeFromB);
			
			//determine where we came from to find out what string to add a B onto
			//@TODO: make this double comparison better
			if (doublesAreEqual((probB - comeFromB), prevProbB)) {
				//we know we came from B, and emitted a B
				stringB = previousStringB + "B";
			}
			else {
				stringB = previousStringI + "B";
			}
			
			//now calculate state for I
			comeFromI = ItoI + Iemissions.get(currentChar);
			comeFromB = BtoI + Iemissions.get(currentChar);
			probI = Math.max(prevProbI + comeFromI, prevProbB + comeFromB);
			
			if (doublesAreEqual((probI - comeFromI), prevProbI)) {
				stringI = previousStringI + "I";
			}
			else {
				stringI = previousStringB + "B";
			}
			
			//set up the previous probabilities and strings
			previousStringB = stringB;
			previousStringI = stringI;
			prevProbI = probI;
			prevProbB = probB;
		}
		return (probI > probB) ? stringI : stringB;
	}
	
	private Boolean doublesAreEqual(double a, double b) {
		double result = b - a;
		return comparisonThreshold > Math.abs(result);
	}
	
}
