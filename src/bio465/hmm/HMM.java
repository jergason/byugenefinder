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
import java.lang.String;


public class HMM {
	private String path;
	private String sequence;
	private String hiddenState;

	public HMM(String path) {
		this.path = path;
		sequence = "";
		hiddenState = "";
		
		//read in the FASTA sequence
		try {
			sequence = readInFastaSequence();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readInFastaSequence() throws IOException {
		String seq = new String();
		String temp = new String();
		BufferedReader scan = new BufferedReader(new FileReader(path));

		while ((temp = scan.readLine()) != null) {
			if (temp.startsWith(">"))
				temp = null;
			else
				seq += temp;
		}
		scan.close();
		return seq;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	public String getHiddenState() {
		return hiddenState;
	}
	
	public void algorithm() {

		List<Double> resultsB = new ArrayList<Double>();
		List<Double> resultsI = new ArrayList<Double>();

		double[] B = new double[2];
		double[] I = new double[2];
		double[] Bactg = new double[4];
		double[] Iactg = new double[4];

		String FF;
		System.out.print("The Sequence is: \n");
		System.out.println(sequence);
		System.out
				.print("Please input probabilities of BtoB, ItoB, BtoI, and ItoI");
		System.out.print("\n");
		for (int k = 0; k < 2; k = k + 1) {
			B[k] = Math.log(TextIO.getlnDouble());
			I[k] = Math.log(TextIO.getlnDouble());
		}
		System.out
				.print("Please Input probabilities of Ba Bc Bt Bg and Ia Ic It Ig");
		System.out.print("\n");
		for (int k = 0; k < 4; k = k + 1) {
			Bactg[k] = Math.log(TextIO.getlnDouble());
		}
		for (int k = 0; k < 4; k = k + 1) {
			Iactg[k] = Math.log(TextIO.getlnDouble());
		}
		System.out.print("Please input initial probabilities for state B or I");
		System.out.print("\n");
		double pB = Math.log(TextIO.getlnDouble());
		double pI = Math.log(TextIO.getlnDouble());

		if (sequence.substring(0, 1).equals("a")) {
			pB += Bactg[0];
			pI += Iactg[0];
		} else if (sequence.substring(0, 1).equals("c")) {
			pB += Bactg[1];
			pI += Iactg[1];
		} else if (sequence.substring(0, 1).equals("t")) {
			pB += Bactg[2];
			pI += Iactg[2];
		} else if (sequence.substring(0, 1).equals("g")) {
			pB += Bactg[3];
			pI += Iactg[3];
		}
		FF = (pB > pI) ? "B" : "I";
		System.out.println(pB);
		resultsB.add(pB);
		System.out.println(pI);
		resultsI.add(pI);
		System.out.println(FF);
		
		for (int i = 1; i < sequence.length(); i = i + 1) {
			pB = ((pB + B[0]) > (pI + I[0])) ? (pB + B[0]) : (pI + I[0]);
			pI = ((pB + B[1]) > (pI + I[1])) ? (pB + B[1]) : (pI + I[1]);

			if (sequence.substring(i, i + 1).equals("a")) {
				pB += Bactg[0];
				pI += Iactg[0];
			} else if (sequence.substring(i, i + 1).equals("c")) {
				pB += Bactg[1];
				pI += Iactg[1];
			} else if (sequence.substring(i, i + 1).equals("t")) {
				pB += Bactg[2];
				pI += Iactg[2];
			} else if (sequence.substring(i, i + 1).equals("g")) {
				pB += Bactg[3];
				pI += Iactg[3];
			}
			FF = (pB > pI) ? "B" : "I";
			System.out.println(pB);
			resultsB.add(pB);
			System.out.println(pI);
			resultsI.add(pI);
			System.out.println(FF);
		}
		/*
		 * System.out.println("This is the size for B:" + resultsI.size());
		 * System.out.println("This is the size for I:" + resultsI.size());
		 * System.out.println("This is the size for seq:" + Seq.length());
		 */

		// /Taking the array and creating the numbers
		int size = resultsI.size() - 1; // ///minus one to start in the last

		// System.out.println("This is the size " + size );
		String[] sequence = new String[size + 1];
		// Here is the for loop to create the backtrace with B or I.
		for (int x = size; x >= 0; x--) {
			double tempB = resultsB.get(x);
			double tempI = resultsI.get(x);
			if (x == size) {
				if (tempB > tempI)
					sequence[x] = "B";
				else
					sequence[x] = "I";
			} 
			else {
				if (sequence[x + 1] == "I") {
					double temporalB = tempB + B[1];
					double temporalI = tempI + I[1];
					if (temporalB > temporalI)
						sequence[x] = "B";
					else
						sequence[x] = "I";
				} 
				else //If the previous was a "B"
				{
					double temporalB = tempB + B[0];
					double temporalI = tempI + I[0];
					if (temporalB > temporalI)
						sequence[x] = "B";
					else
						sequence[x] = "I";
				}
			}
		}
		// This prints the array with the Backtrace sequence
		for (int x = 0; x <= size; x++) {
			System.out.println(x + " " + sequence[x]);
		}
	}
}
