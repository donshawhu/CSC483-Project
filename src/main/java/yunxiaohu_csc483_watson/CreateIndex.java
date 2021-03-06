package yunxiaohu_csc483_watson;

/*
 * Yunxiao Hu 
 * CSC483
 * Create the Index
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndex {
	private String indexPath;
	private boolean isLemma;
	private boolean isStem;

	public CreateIndex(String indexPath, boolean isLemma, boolean isStem) {
		this.indexPath = indexPath;
		this.isLemma = isLemma;
		this.isStem = isStem;

	}

	/*
	 * read files in directory, and index all files by using readFile() function
	 */
	public void readFiles(String directory) throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = FSDirectory.open(new File(indexPath).toPath());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);

		File dir = new File(directory);
		int i = 1;
		for (String file : dir.list()) {
			System.out.println("Reading document " + i + ": " + file);
			readFile(directory + "/" + file, w);
			i++;
		}
		w.close();
		index.close();
	}

	/*
	 * Read a single file and index it
	 */
	public void readFile(String filename, IndexWriter w) {
		String title = "";
		String result = "";
		File file = new File(filename);
		try {
			Scanner inputScanner = new Scanner(file);
			while (inputScanner.hasNextLine()) {
//				System.out.println("Reading file " + filename);
				String line = inputScanner.nextLine();

				String lineString = "";

				if (line.length() != 0) {
					lineString = cleanTextContent(line);
				} else {
					lineString = line;
				}
				int length = lineString.length();

				if (lineString.length() > 4 && lineString.substring(0, 2).equals("[[")
						&& lineString.substring(length - 2, length).equals("]]")) {
//					find the title and add the previous page to document
					if (!title.equals("") && !title.contains("File:") && !title.contains("Image:")) {

						addDoc(w, title, result.toString().trim());
					}
					title = lineString.substring(2, length - 2);
					result = "";
				}

				else if (lineString.length() > 2 && lineString.charAt(0) == '='
						&& lineString.charAt(length - 1) == '=') {
					// remove all equal signs in content
					while (lineString.length() > 2 && lineString.charAt(0) == '='
							&& lineString.charAt(lineString.length() - 1) == '=') {
						lineString = lineString.substring(1, lineString.length() - 1);
					}
					if (!lineString.equals("See also") && !lineString.equals("References")
							&& !lineString.equals("Further reading") && !lineString.equals("External links")) {
						result = result + lineString + " ";
					}
				}

				else {

					result = result + lineString + " ";
				}

			}
			addDoc(w, title, result.toString().trim());
			inputScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// remove all non-English text characters
	private String cleanTextContent(String text) {
		text = text.replaceAll("[^\\x00-\\x7F]", "");

		return text.trim();
	}

	/*
	 * add one wiki page and index it, if needs lemma or stemming, it also can deal
	 * with it
	 */
	private void addDoc(IndexWriter w, String title, String text) {
		// TODO Auto-generated method stub
		Document doc = new Document();
		StringBuilder txt = new StringBuilder("");
		LemmaOrStemm convert = new LemmaOrStemm();
		if (isLemma && text.length() != 0) {
			convert.convertLemma(txt, text);
		} else if (isStem && text.length() != 0) {
			convert.convertStem(txt, text);
		} else {

			txt.append(text.toLowerCase());
		}
		doc.add(new StringField("title", title, Field.Store.YES));

		doc.add(new TextField("text", title + " " + txt.toString().trim(), Field.Store.YES));

		try {
			w.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}