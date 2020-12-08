package yunxiaohu_csc483_watson;

import org.tartarus.snowball.ext.PorterStemmer;

import edu.stanford.nlp.simple.Sentence;

public class LemmaOrStemm {
	public LemmaOrStemm() {

	}

	/*
	 * Convert the input string line to lemma, then add it to string builder
	 */
	public void convertLemma(StringBuilder container, String line) {
		/*
		 * Properties props = new Properties(); // set the list of annotators to run
		 * props.setProperty("annotators", "tokenize,ssplit,pos,lemma"); // build
		 * pipeline StanfordCoreNLP pipeline = new StanfordCoreNLP(props); // create a
		 * document object CoreDocument document = pipeline.processToCoreDocument(line);
		 * // display tokens for (CoreLabel tok : document.tokens()) {
		 * container.append(tok.lemma().toLowerCase() + " "); //
		 * System.out.println(String.format("Token: %s\tLemma: %s", tok.word(),
		 * tok.lemma())); }
		 */

		for (String lemma : new Sentence(line.toLowerCase()).lemmas()) {
			container.append(lemma + " ");
		}
	}

	/*
	 * Convert the input string line to stemm, then add it to string builder
	 */
	public void convertStem(StringBuilder container, String line) {
		for (String ste : new Sentence(line.toLowerCase()).words()) {
			container.append(getStem(ste) + " ");
		}
	}

	/*
	 * stemming a term
	 */
	private String getStem(String term) {
		PorterStemmer stemmer = new PorterStemmer();
		stemmer.setCurrent(term);
		stemmer.stem();
		return stemmer.getCurrent();
	}
}
