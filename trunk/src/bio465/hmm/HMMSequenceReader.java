package bio465.hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HMMSequenceReader {
	private String path;
	private String sequence;
	
	public HMMSequenceReader() {
		path = "";
		sequence = "";
	}
	
	public HMMSequenceReader(String path) {
		this.path = path;
	}
	
	public String readInFastaSequence() throws IOException {
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
		return seq.toLowerCase();
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}