package yunxiaohu_csc483_watson;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;

public class Index {

	static String queries = "questions.txt";
	static String indexPath = "./index";
	static String input_dir = "wiki-subset-20140602";

	static boolean isLemma = false;
	static boolean isStem = false;

	static boolean isCreateIndex = true;
	static boolean isRunQueries = true;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * CreateIndex cIndex = new CreateIndex("./Index", false, true); try {
		 * cIndex.readFiles("example"); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * 
		 * QueryEngine qEngine = new QueryEngine("./Index", false, false); String
		 * txtString = "Lucene is simple yet powerful java based search library.";
		 * txtString = qEngine.removeStopWords(txtString); System.out.println(txtString
		 * + 1);
		 */
		if (isCreateIndex) {
			CreateIndex ic = new CreateIndex(indexPath, isLemma, isStem);
			ic.readFiles(input_dir);
		}
		if (isRunQueries) {
			QueryEngine qeDe = new QueryEngine(indexPath, isLemma, isStem);
			QueryEngine qeBM25 = new QueryEngine(indexPath, isLemma, isStem, new BM25Similarity());
//			QueryEngine qeTFIDF = new QueryEngine(indexPath, isLemma, isStem, new TFIDFSimilarity());
			QueryEngine qeBool = new QueryEngine(indexPath, isLemma, isStem, new BooleanSimilarity());

			System.out.println("Running similarity Default.");
			String de = qeDe.runQueries(queries);
			System.out.println("Using the Default Model:\n" + de);

			System.out.println("Running similarity BM25.");
			String bm25 = qeBM25.runQueries(queries);
			System.out.println("Using the BM25 Model:\n" + bm25);

			System.out.println("Running similarity Bool.");
			String bool = qeBool.runQueries(queries);
			System.out.println("Using the Bool Model:\n" + bool);
//			System.out.println("Using the TFIDF Model:\n" + tf);
		}
	}

}
