package ch.heigvd.iict.mac.labo1;

import ch.heigvd.iict.mac.labo1.indexer.CACMIndexer;
import ch.heigvd.iict.mac.labo1.parsers.CACMParser;
import ch.heigvd.iict.mac.labo1.queries.QueriesPerformer;
import ch.heigvd.iict.mac.labo1.similarities.MySimilarity;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.analysis.Analyzer;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		// 1.1. create an analyzer
		Analyzer analyser = getAnalyzer();

		// TODO student "Tuning the Lucene Score"
		//Similarity similarity = new ClassicSimilarity();
		Similarity similarity = new MySimilarity();

		CACMIndexer indexer = new CACMIndexer(analyser, similarity);
		indexer.openIndex();
		CACMParser parser = new CACMParser("documents/cacm.txt", indexer);
		Long startTime = System.currentTimeMillis();
		parser.startParsing();
		indexer.finalizeIndex();
		Long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime) + " ms to create the index.");

		QueriesPerformer queriesPerformer = new QueriesPerformer(analyser, similarity);

		// Section "Reading Index"
		readingIndex(queriesPerformer);

		// Section "Searching"
		searching(queriesPerformer);

		queriesPerformer.close();
		
	}

	private static void readingIndex(QueriesPerformer queriesPerformer) {
		queriesPerformer.printTopRankingTerms("authors", 10);
		queriesPerformer.printTopRankingTerms("title", 10);
	}

	private static void searching(QueriesPerformer queriesPerformer) {
		// Example
		 queriesPerformer.query("compiler program");

		// TODO student
//		queriesPerformer.query("\"Information Retrieval\"");
//		queriesPerformer.query("Information && Retrieval");
//		queriesPerformer.query("+Retrieval Information NOT Database");
//		queriesPerformer.query("Info*");
//		queriesPerformer.query("\"Information Retrieval\"~10");
	}

	private static Analyzer getAnalyzer() {
	    // TODO student... For the part "Indexing and Searching CACM collection
		// - Indexing" use, as indicated in the instructions,
		// the StandardAnalyzer class.
		//
		// For the next part "Using different Analyzers" modify this method
		// and return the appropriate Analyzers asked.

		// TODO student
		/* -------------- Standard --------------- */
//		return new StandardAnalyzer();

		/* -------------- Whitespace --------------- */
//		return new WhitespaceAnalyzer();

		/* -------------- English --------------- */
		return new EnglishAnalyzer();

		/* -------------- Shingle 1 and 2--------------- */
//		return new ShingleAnalyzerWrapper(2, 2);

		/* -------------- Shingle 1 and 3 --------------- */
//		return new ShingleAnalyzerWrapper(3, 3);

		/* -------------- Stop --------------- */
//		try {
//			return new StopAnalyzer(new File("./common_words.txt").getAbsoluteFile().toPath());
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
	}

}
