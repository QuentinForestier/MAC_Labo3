package ch.heigvd.iict.mac.labo1.similarities;

import org.apache.lucene.search.similarities.ClassicSimilarity;

public class MySimilarity extends ClassicSimilarity
{

    // TODO student
    // Implement the functions described in section "Tuning the Lucene Score"


    @Override
    public float tf(float freq)
    {
        return 1f + (float) Math.log(freq);
    }

    @Override
    public float idf(long docFreq, long docCount)
    {
        return (float)Math.log(docCount / (docFreq + 1.0)) + 1f;
    }

    @Override
    public float lengthNorm(int numTerms)
    {
        return 1f;
    }

}


    w