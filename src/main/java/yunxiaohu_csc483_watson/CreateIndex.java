package yunxiaohu_csc483_watson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndex {
	private String indexPath;
	private boolean removeTPL;
	private boolean useLemma; // index lemmas
	private boolean useStem; // index stems
	private File indexFile = new File("./index");

	public CreateIndex(String indexPath, boolean useLemma, boolean useStem) {
		this.indexPath = indexPath;
		this.useLemma = useLemma;
		this.useStem = useStem;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void readFiles(String directory) throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = FSDirectory.open(new File(indexPath).toPath());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);

		File dir = new File(directory);
		int i = 0;
		for (String file : dir.list()) {
			System.out.println("Processing document " + i + ": " + file);
			// parse each file
			readFile(directory + "/" + file, w);
			i++;
		}
		w.close();
		index.close();
	}

	public void readFile(String filename, IndexWriter w) {
		String title = ""; // title field
		String categories = ""; // category field
		StringBuilder result = new StringBuilder(); // text field
		File file = new File(filename); // open file

		try {
			Scanner inputScanner = new Scanner(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}