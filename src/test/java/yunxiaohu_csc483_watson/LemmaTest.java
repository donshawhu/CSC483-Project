package yunxiaohu_csc483_watson;

/*
 * Yunxiao Hu 
 * CSC483
 */
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.junit.jupiter.api.Test;

/*
 * get the best configuration output
 */
public class LemmaTest {

	static String queries = "questions.txt";
	static String indexPath = "./index/haveLemmaNoStemm";

	static boolean isLemma = true;
	static boolean isStem = false;

	@Test
	public void test() throws ParseException {
		QueryEngine qeDe = new QueryEngine(indexPath, isLemma, isStem);
		QueryEngine qeTFIDF = new QueryEngine(indexPath, isLemma, isStem, new ClassicSimilarity());
		QueryEngine qeBool = new QueryEngine(indexPath, isLemma, isStem, new BooleanSimilarity());
		Similarity[] ls = { new ClassicSimilarity(), new BooleanSimilarity() };
		QueryEngine qeMu = new QueryEngine(indexPath, isLemma, isStem, new MultiSimilarity(ls));

		System.out.println("===============================================================");
		System.out.println("Running similarity Default(BM25).");
		String de = qeDe.runQueries(queries);
		System.out.println("Using the Default(BM25) Model:\n" + de);
		System.out.println();

		System.out.println("===============================================================");
		System.out.println("Running similarity CLASSIC.");
		String ty = qeTFIDF.runQueries(queries);
		System.out.println("Using the CLASSIC Model:\n" + ty);
		System.out.println();

		System.out.println("===============================================================");
		System.out.println("Running similarity Bool.");
		String bool = qeBool.runQueries(queries);
		System.out.println("Using the Bool Model:\n" + bool);
		System.out.println();

		System.out.println("===============================================================");
		System.out.println("Running similarity MultiSimilarity.");
		String mu = qeMu.runQueries(queries);
		System.out.println("Using the MultiSimilarity:\n" + mu);
		System.out.println();
	}
}
