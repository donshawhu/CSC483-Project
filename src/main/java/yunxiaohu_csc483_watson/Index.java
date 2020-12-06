package yunxiaohu_csc483_watson;

import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;

public class Index {

	static String queries = "questions.txt";
	static String indexPath = "./index/noLeemHaveStemm";
	static String input_dir = "wiki-subset-20140602";
//	static String input_dir = "example";

	static boolean isLemma = false;
	static boolean isStem = true;

	static boolean isCreateIndex = false;
	static boolean isRunQueries = true;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (isCreateIndex) {
			CreateIndex ic = new CreateIndex(indexPath, isLemma, isStem);
			ic.readFiles(input_dir);
		}
		if (isRunQueries) {
			QueryEngine qeDe = new QueryEngine(indexPath, isLemma, isStem);
			QueryEngine qeTFIDF = new QueryEngine(indexPath, isLemma, isStem, new ClassicSimilarity());
			QueryEngine qeBool = new QueryEngine(indexPath, isLemma, isStem, new BooleanSimilarity());

			System.out.println("Running similarity Default(BM25).");
			String de = qeDe.runQueries(queries);
			System.out.println("Using the Default Model:\n" + de);

			System.out.println("Running similarity CLASSIC.");
			String ty = qeTFIDF.runQueries(queries);
			System.out.println("Using the CLASSIC Model:\n" + ty);

			System.out.println("Running similarity Bool.");
			String bool = qeBool.runQueries(queries);
			System.out.println("Using the Bool Model:\n" + bool);
		}
	}

}
