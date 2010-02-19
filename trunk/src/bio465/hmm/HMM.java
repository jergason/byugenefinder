//********************************************************************
//  HMM.java     Author: Group 2/Lab3
//
//  Takes a DNA sequence and hidden Markov Model probabilities as parameters
//  and returns both the sequence and the calculated CpG Island or non-island state.
//********************************************************************

package bio465.hmm;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import bio465.hmm.HMMSequenceReader;
import java.util.HashMap;
import java.util.Map;
import java.lang.Double;

public class HMM {
	private HMMSequenceReader data = new HMMSequenceReader();
	private String hiddenState;
	private String sequence;

	public HMM(String path) {
		sequence = "";
		data.setPath(path);
		hiddenState = "";
		
		try {
			sequence = data.readInFastaSequence();
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
	 * We calculate the probability for each location on the genome.
	 */
	
	public String calcHiddenState() {
		//@TODO: make these dynamic
		//I parameters!
		double startI = -.699;
		double ItoB = -.155;
		double ItoI = -.523;
		Map<Character, Double> IEmissions = new HashMap<Character, Double>();
		IEmissions.put(new Character('a'), new Double(-1.0));
		IEmissions.put(new Character('c'), new Double(-.398));
		IEmissions.put(new Character('t'), new Double(-1.0));
		IEmissions.put(new Character('g'), new Double(-.398));
		
		//B parameters!
		double startB = -.097;
		double BtoB = -.097;
		double BtoI = -.669;
		double Ba, Bc, Bg, Bt = -.602;
		Map<Character, Double> BEmissions = new HashMap<Character, Double>();
		BEmissions.put(new Character('a'), new Double(-.602));
		BEmissions.put(new Character('c'), new Double(-.602));
		BEmissions.put(new Character('t'), new Double(-.602));
		BEmissions.put(new Character('g'), new Double(-.602));
		
		//Variables used in loop to store previous states
		String stringI = "I";
		String stringB = "B";
		String previousStringI = "I";
		String previousStringB = "B";
		
		double probI, probB, prevProbI, prevProbB;
		probI = prevProbI= startI + IEmissions.get(sequence.charAt(0));
		probB = prevProbB = startB + BEmissions.get(sequence.charAt(0));
		double comeFromI, comeFromB = 0;
		char currentChar = 'x';
		
		for (int i = 1; i < sequence.length(); i++) {
			//The new probB is the maximum of coming from I and emitting the current char in B
			// and coming from B and emitting the current char in B
			currentChar = sequence.charAt(i);
			comeFromI = ItoB + BEmissions.get(currentChar);
			comeFromB = BtoB + BEmissions.get(currentChar);
			probB = Math.max(prevProbI + comeFromI, prevProbB + comeFromB);
			
			//determine where we came from to find out what string to add a B onto
			//@TODO: make this double comparison better
			if ((probB - comeFromB) == prevProbB) {
				//we know we came from B, and emitted a B
				stringB = previousStringB + "B";
			}
			else {
				stringB = previousStringI + "B";
			}
			
			//now calculate state for I
			comeFromI = ItoI + IEmissions.get(currentChar);
			comeFromB = BtoI + IEmissions.get(currentChar);
			probI = Math.max(prevProbI + comeFromI, prevProbB + comeFromB);
			
			if ((probI - comeFromI) == prevProbI) {
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
	
	
//	public void calculateHiddenState() {
//
//		List<Double> resultsB = new ArrayList<Double>();
//		List<Double> resultsI = new ArrayList<Double>();
//
//		double[] B = new double[2];
//		double[] I = new double[2];
//		double[] Bactg = new double[4];
//		double[] Iactg = new double[4];
//
//		String FF;
//		System.out.println("The Sequence is:");
//		System.out.println(sequence);
//		System.out.println("Please input probabilities of BtoB, ItoB, BtoI, and ItoI");
//		for (int k = 0; k < 2; k = k + 1) {
//			B[k] = Math.log(TextIO.getlnDouble());
//			I[k] = Math.log(TextIO.getlnDouble());
//		}
//		
//		System.out.println("Please Input probabilities of Ba Bc Bt Bg and Ia Ic It Ig");
//		for (int k = 0; k < 4; k = k + 1) {
//			Bactg[k] = Math.log(TextIO.getlnDouble());
//		}
//		for (int k = 0; k < 4; k = k + 1) {
//			Iactg[k] = Math.log(TextIO.getlnDouble());
//		}
//		
//		System.out.println("Please input initial probabilities for state B or I");
//		double pB = Math.log(TextIO.getlnDouble());
//		double pI = Math.log(TextIO.getlnDouble());
//
//		char firstChar = sequence.charAt(0);
//		switch (firstChar) {
//		case 'a':
//			pB += Bactg[0];
//			pI += Iactg[0];
//			break;
//		case 'c':
//			pB += Bactg[1];
//			pI += Iactg[1];
//			break;
//		case 't':
//			pB += Bactg[2];
//			pI += Iactg[2];
//			break;
//		case 'g':
//			pB += Bactg[3];
//			pI += Iactg[3];
//			break;
//		default:
//			System.out.println("Error: problem reading gene sequence.");
//			break;
//	}
//		FF = (pB > pI) ? "B" : "I";
//		System.out.println(pB);
//		resultsB.add(pB);
//		System.out.println(pI);
//		resultsI.add(pI);
//		System.out.println(FF);
//		
//		char currentChar = 'x';
//		for (int i = 1; i < sequence.length(); i++) {
//			pB = ((pB + B[0]) > (pI + I[0])) ? (pB + B[0]) : (pI + I[0]);
//			pI = ((pB + B[1]) > (pI + I[1])) ? (pB + B[1]) : (pI + I[1]);
//			
//			currentChar = sequence.charAt(i);
//			switch (currentChar) {
//				case 'a':
//					pB += Bactg[0];
//					pI += Iactg[0];
//					break;
//				case 'c':
//					pB += Bactg[1];
//					pI += Iactg[1];
//					break;
//				case 't':
//					pB += Bactg[2];
//					pI += Iactg[2];
//					break;
//				case 'g':
//					pB += Bactg[3];
//					pI += Iactg[3];
//					break;
//				default:
//					System.out.println("Error: problem reading gene sequence.");
//					break;
//			}
//			
//			FF = (pB > pI) ? "B" : "I";
//			System.out.println(pB);
//			resultsB.add(pB);
//			System.out.println(pI);
//			resultsI.add(pI);
//			System.out.println(FF);
//		}
//		/*
//		 * System.out.println("This is the size for B:" + resultsI.size());
//		 * System.out.println("This is the size for I:" + resultsI.size());
//		 * System.out.println("This is the size for seq:" + Seq.length());
//		 */
//
//		// /Taking the array and creating the numbers
//		int size = resultsI.size() - 1; // ///minus one to start in the last
//
//		// System.out.println("This is the size " + size );
//		String[] sequence = new String[size + 1];
//		// Here is the for loop to create the backtrace with B or I.
//		for (int x = size; x >= 0; x--) {
//			double tempB = resultsB.get(x);
//			double tempI = resultsI.get(x);
//			if (x == size) {
//				if (tempB > tempI)
//					sequence[x] = "B";
//				else
//					sequence[x] = "I";
//			} 
//			else {
//				if (sequence[x + 1] == "I") {
//					double temporalB = tempB + B[1];
//					double temporalI = tempI + I[1];
//					if (temporalB > temporalI)
//						sequence[x] = "B";
//					else
//						sequence[x] = "I";
//				} 
//				else //If the previous was a "B"
//				{
//					double temporalB = tempB + B[0];
//					double temporalI = tempI + I[0];
//					if (temporalB > temporalI)
//						sequence[x] = "B";
//					else
//						sequence[x] = "I";
//				}
//			}
//		}
//		// This prints the array with the Backtrace sequence
//		for (int x = 0; x <= size; x++) {
//			System.out.println(x + " " + sequence[x]);
//		}
//	}
}
