package yunxiaohu_csc483_watson;

/*
 * Yunxiao Hu
 * CSC483
 */
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class Index {

	static String queries = "questions.txt";
	static String indexPath = "./index/haveLemmaNoStemm";
	static String input_dir = "wiki-subset-20140602";
//	static String input_dir = "example";

	static boolean isLemma = true;
	static boolean isStem = false;
//	isLemma and isStem cannot both be true

//	run which part
	static boolean isCreateIndex = false;
	static boolean isRunQueries = true;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (isCreateIndex) {
			CreateIndex create = new CreateIndex(indexPath, isLemma, isStem);
			create.readFiles(input_dir);
		}
		if (isRunQueries) {
			Similarity[] ls = { new ClassicSimilarity(), new BooleanSimilarity() };
			QueryEngine qeDe = new QueryEngine(indexPath, isLemma, isStem);
			QueryEngine qeTFIDF = new QueryEngine(indexPath, isLemma, isStem, new ClassicSimilarity());
			QueryEngine qeBool = new QueryEngine(indexPath, isLemma, isStem, new BooleanSimilarity());
			QueryEngine qeMu = new QueryEngine(indexPath, isLemma, isStem, new MultiSimilarity(ls));

			System.out.println("Running similarity Default(BM25).");
			String de = qeDe.runQueries(queries);
			System.out.println("Using the Default Model:\n" + de);
			System.out.println();

			System.out.println("Running similarity CLASSIC.");
			String ty = qeTFIDF.runQueries(queries);
			System.out.println("Using the CLASSIC Model:\n" + ty);
			System.out.println();

			System.out.println("Running similarity Bool.");
			String bool = qeBool.runQueries(queries);
			System.out.println("Using the Bool Model:\n" + bool);
			System.out.println();

			System.out.println("Running similarity MultiSimilarity.");
			String mu = qeMu.runQueries(queries);
			System.out.println("Using the MultiSimilarity:\n" + mu);
			System.out.println();
		}
	}

}
