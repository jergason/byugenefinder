package bio465.hmm;

/*
 * Class to conduct supervised training from an annotated DNA sequence.
 * HMMSupervisedTrainer will read in a file containing a DNA sequence 
 * and the CpG island state at each region, and will return the supervised
 * HMM parameters for that state.
 */

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import bio465.hmm.HMMSequenceReader;

public class HMMSupervisedTrainer {
	public float IG, BG;
	public float IC, BC;
	public float IA, BA;
	public float IT, BT;
	public float II, BB;
	public float ItoB, BtoI;
	public List<Integer> Starts;
	public List<Integer> Ends;
	private char[] dnaSequence;

	public HMMSupervisedTrainer(String pathToSequence, String pathToWriteParams) {
		HMMSequenceReader reader = new HMMSequenceReader(pathToSequence);
		try {
			dnaSequence = reader.readInFastaSequence().toCharArray();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Starts = new ArrayList<Integer>();
		Ends = new ArrayList<Integer>();
		initializeParameters();
		setContent();
		setTransitions();
		writeOutParameters(pathToWriteParams);
	}
	
	private void writeOutParameters(String pathToWriteParams) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(pathToWriteParams));
			out.println(".5");
			out.println(II);
			out.println(ItoB);
			out.println(IA);
			out.println(IC);
			out.println(IT);
			out.println(IG);
			out.println(".5");
			out.println(BB);
			out.println(BtoI);
			out.println(BA);
			out.println(BC);
			out.println(BT);
			out.println(BG);
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HMMSupervisedTrainer(String sequence) {
		dnaSequence = sequence.toCharArray();
		Starts = new ArrayList<Integer>();
		Ends = new ArrayList<Integer>();
		initializeParameters();
		setContent();
		setTransitions();
	}

	public HMMSupervisedTrainer(String sequence, List<Integer> s,
			List<Integer> e) {
		dnaSequence = sequence.toCharArray();
		Starts = s;
		Ends = e;
		setContent();
		setTransitions();
	}

	private void setContent() {
		IG = 0;
		BG = 0;
		IC = 0;
		BC = 0;
		IA = 0;
		BA = 0;
		IT = 0;
		BT = 0;
		int previous = 0;
		//for (Integer I : Starts) {
		for (int i = 0; i < Starts.size(); i++) {
			Integer startIndex = Starts.get(i);
			Integer endIndex = Ends.get(i);
			for (int j = previous; j < startIndex.intValue(); j++)
				switch (dnaSequence[j]) {
				case 'g':
					BG++;
				case 'G':
					BG++;
				case 'c':
					BC++;
				case 'C':
					BC++;
				case 'a':
					BA++;
				case 'A':
					BA++;
				case 't':
					BT++;
				case 'T':
					BT++;
				}
			for (int j = startIndex.intValue(); j <= endIndex.intValue(); j++)
				switch (dnaSequence[j]) {
				case 'g':
					IG++;
				case 'G':
					IG++;
				case 'c':
					IC++;
				case 'C':
					IC++;
				case 'a':
					IA++;
				case 'A':
					IA++;
				case 't':
					IT++;
				case 'T':
					IT++;
				}
			previous = endIndex.intValue();
		}
		for (int i = previous; i < dnaSequence.length; i++)
			switch (dnaSequence[i]) {
			case 'g':
				BG++;
			case 'G':
				BG++;
			case 'c':
				BC++;
			case 'C':
				BC++;
			case 'a':
				BA++;
			case 'A':
				BA++;
			case 't':
				BT++;
			case 'T':
				BT++;
			}
		float iTotal = IC + IG + IA + IT;
		float bTotal = BC + BG + BA + BT;
		IC = IC / iTotal;
		IG = IG / iTotal;
		IA = IA / iTotal;
		IT = IT / iTotal;
		BC = BC / bTotal;
		BG = BG / bTotal;
		BA = BA / bTotal;
		BT = BT / bTotal;
	}

	private void setTransitions() {
		int totalIs = 0;
		int totalBs = 0;
		int totalItoB = 0;
		int totalBtoI = 0;
		if (Starts.get(0).intValue() == 0)
			totalBtoI = Starts.size() - 1;
		else
			totalBtoI = Starts.size();

		if (Ends.get(Ends.size() - 1) == Ends.size())
			totalItoB = Ends.size() - 1;
		else
			totalItoB = Ends.size();

		for (int i = 0; i < Starts.size(); i++)
			totalIs += Ends.get(i) - Starts.get(i);
		totalIs -= Ends.size();
		totalBs = dnaSequence.length + 1 - totalIs - Ends.size();
	}

	private void initializeParameters() {
		Starts.add(13192);
		Starts.add(80115);
		Starts.add(228242);
		Starts.add(265307);
		Starts.add(274490);
		Starts.add(571109);
		Starts.add(675031);
		Starts.add(682881);
		Starts.add(923201);
		Ends.add(14692);
		Ends.add(81752);
		Ends.add(228929);
		Ends.add(265762);
		Ends.add(275746);
		Ends.add(571081);
		Ends.add(675249);
		Ends.add(683183);
		Ends.add(924522);
	}
}
