package bio465.hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HMMSequenceReader {
	private String path;
	
	public HMMSequenceReader() {
		path = "";
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String readInFastaSequence() throws IOException {
		StringBuilder buffer = new StringBuilder();
		String temp = "";
		BufferedReader scan = new BufferedReader(new FileReader(path));

		while ((temp = scan.readLine()) != null) {
			if (temp.startsWith(">"))
				temp = null;
			else
				buffer.append(temp);
		}
		scan.close();
		return buffer.toString().toLowerCase();
	}
}