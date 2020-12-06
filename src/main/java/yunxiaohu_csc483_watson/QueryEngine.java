package yunxiaohu_csc483_watson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class QueryEngine {
	private String indexPath;
	private boolean isLemma;
	private boolean isStem;
	private boolean isWays;
	private Similarity ways;

	public QueryEngine(String indexPath, boolean isLemma, boolean isStem, Similarity ways) {
		this.indexPath = indexPath;
		this.isLemma = isLemma;
		this.isStem = isStem;
		this.ways = ways;
		this.isWays = true;

	}

	public QueryEngine(String indexPath, boolean isLemma, boolean isStem) {
		this.indexPath = indexPath;
		this.isLemma = isLemma;
		this.isStem = isStem;
		this.isWays = false;
	}

	public String runQueries(String filename) throws ParseException {
		String result = "";
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			String category = "";
			String clue = "";
			String answer = "";
			int total = 0, correct = 0, i = 0;
			double mmr = 0;
			while (scanner.hasNextLine()) {
				if (i % 4 == 0)
					category = scanner.nextLine();
				else if (i % 4 == 1)
					clue = scanner.nextLine();
				else if (i % 4 == 2)
					answer = scanner.nextLine();
				else {
					scanner.nextLine();
					List<ResultClass> ans = runQuery(category + " " + clue);
					if (ans.get(0).DocName.get("title").equals(answer)) {
						correct++;
						mmr += 1.0;
//						System.out.println("find: " + ans.get(0).DocName.get("title") + "\tanswer: " + answer);
					} else {
						for (int j = 0; j < ans.size(); j++) {
							if (ans.get(j).DocName.get("title").equals(answer)) {
								mmr += (double) 1 / (j + 1);
//								System.out
//										.println("find: " + ans.get(0).DocName.get("title") + "    answer: " + answer);
								break;
							}
						}
					}
					total++;
				}
				i++;
			}
			System.out.println(correct + " " + total);
			result = "\tP@1: " + correct + "/" + total + " = " + (double) correct / total + "\n\tMMR: "
					+ (double) mmr / total;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<ResultClass> runQuery(String query) throws ParseException {
		List<ResultClass> ans = new ArrayList<ResultClass>();

		StandardAnalyzer analyzer = new StandardAnalyzer();
		LemmaOrStemm convert = new LemmaOrStemm();
		StringBuilder container = new StringBuilder();
		String clue = "";
		if (isLemma) {
			convert.convertLemma(container, query);
			clue = removeStopWords(container.toString());
		} else if (isStem) {
			convert.convertStem(container, query);
			clue = removeStopWords(container.toString());
		}
		clue = removeStopWords(query);
//		System.out.println(1 + clue);

		try {
			Directory index = FSDirectory.open(new File(indexPath).toPath());
//			System.out.println(clue);
			Query q = new QueryParser("text", analyzer).parse(clue);

			int hitsPerPage = 10;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			if (isWays) {

				searcher.setSimilarity(this.ways);
			}

			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			if (hits != null) {
				for (ScoreDoc scoredoc : hits) {
					int docId = scoredoc.doc;
					double score = scoredoc.score;
					Document doc = new Document();
					doc = searcher.doc(docId);
					ResultClass objResultClass = new ResultClass();
					objResultClass.DocName = doc;
					objResultClass.docScore = score;
					ans.add(objResultClass);
				}
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public String removeStopWords(String text) {
		CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
		Analyzer analyzer = new StandardAnalyzer(stopWords);
		try {
			String resultString = "";
			TokenStream tokenStream = analyzer.tokenStream("", new StringReader(text));
			CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
			tokenStream.reset();

			while (tokenStream.incrementToken()) {
//				System.out.print("[" + term.toString() + "] ");
				resultString = resultString + term.toString() + " ";

			}
//			System.out.println();
			tokenStream.close();
			analyzer.close();
			return resultString.trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}