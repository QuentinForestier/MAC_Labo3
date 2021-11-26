package ch.heigvd.iict.mac.labo1.queries;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Comparator;

public class QueriesPerformer {
	
	private Analyzer		analyzer		= null;
	private IndexReader 	indexReader 	= null;
	private IndexSearcher 	indexSearcher 	= null;

	public QueriesPerformer(Analyzer analyzer, Similarity similarity) {
		this.analyzer = analyzer;
		Path path = FileSystems.getDefault().getPath("index");
		Directory dir;
		try {
			dir = FSDirectory.open(path);
			this.indexReader = DirectoryReader.open(dir);
			this.indexSearcher = new IndexSearcher(indexReader);
			if(similarity != null)
				this.indexSearcher.setSimilarity(similarity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printTopRankingTerms(String field, int numTerms) {
		// TODO student
		// This methods print the top ranking term for a field.
		// See "Reading Index".
		TermStats[] stats = null;
		try {
			/* ------------ Empty terms like others --------------*/
//			stats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field, Comparator.comparingLong(a -> a.totalTermFreq));

			/* ------------ Empty terms at the end --------------*/
			stats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field,
					(a,b) -> {
						if (a.termtext.utf8ToString().equals("") ^ b.termtext.utf8ToString().equals("")) {
							if (a.termtext.utf8ToString().equals("")) return -1;
							if (b.termtext.utf8ToString().equals("")) return 1;
						}

						return Long.compare(a.totalTermFreq, b.totalTermFreq);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Top 10 ranking terms for field ["  + field +"] are: ");
		if( stats != null) {
			for(TermStats stat : stats) {
				System.out.println( " * " + stat.totalTermFreq + " time(s) in " + stat.docFreq + " docs: " + stat.termtext.utf8ToString());
			}
		}
	}
	
	public void query(String q) {
		// TODO student
		// See "Searching" section

    	// Text used on query
		System.out.println("\nSearching for [" + q +"]");

		QueryParser parser = new QueryParser("summary", analyzer);

		try {
      		// Query prepare
			Query query = parser.parse(q);

      		// Query exec for top 10 best results
			TopDocs results = indexSearcher.search(query, 10);
			System.out.println("Total result: " + results.totalHits);

      		// Printing top 10
			System.out.println("\nTop 10: ");
			ScoreDoc[] hits = results.scoreDocs;
			for(ScoreDoc hit : hits) {
				Document doc = indexSearcher.doc(hit.doc);
				System.out.println(doc.get("id")+ ": " + doc.get("title") + " (" + hit.score + ")");
			}

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
	 
	public void close() {
		if(this.indexReader != null)
			try { this.indexReader.close(); } catch(IOException e) { /* BEST EFFORT */ }
	}
	
}
