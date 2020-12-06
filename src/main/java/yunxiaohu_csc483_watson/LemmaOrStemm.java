package yunxiaohu_csc483_watson;

import org.tartarus.snowball.ext.PorterStemmer;

import edu.stanford.nlp.simple.Sentence;

public class LemmaOrStemm {
	public LemmaOrStemm() {

	}

	public void convertLemma(StringBuilder container, String line) {
		for (String lemma : new Sentence(line.toLowerCase()).lemmas()) {
			container.append(lemma + " ");
		}
	}

	public void convertStem(StringBuilder container, String line) {
		for (String word : new Sentence(line.toLowerCase()).words()) {
			container.append(getStem(word) + " ");
		}
	}

	private String getStem(String term) {
		PorterStemmer stemmer = new PorterStemmer();
		stemmer.setCurrent(term);
		stemmer.stem();
		return stemmer.getCurrent();
	}
}
