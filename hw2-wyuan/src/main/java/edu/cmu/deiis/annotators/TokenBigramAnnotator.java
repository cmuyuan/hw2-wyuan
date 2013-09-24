package edu.cmu.deiis.annotators;

import java.util.Iterator;
import org.apache.uima.analysis_component.*;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.*;
import org.apache.uima.jcas.cas.FSArray;
import edu.cmu.deiis.types.*;


/**
 * annotate Bigrams(two consecutive words) in answers
 * */
public class TokenBigramAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex questionIndex = arg0.getAnnotationIndex(Question.type);
    FSIndex answerIndex = arg0.getAnnotationIndex(Answer.type);
    Iterator<Question> questionIt = questionIndex.iterator();
    Iterator<Answer> answerIt = answerIndex.iterator();

    /* annotate Bigrams in questions and answers * */
    while (questionIt.hasNext()) {
      Question q = questionIt.next();
      String str = q.getCoveredText();
      int qbegin = q.getBegin();
      String[] tokens = str.split(" ");
      if(tokens.length < 2)
        continue;
      int t1begin = qbegin, t2begin = qbegin + tokens[0].length() + 1;// t2begin is begin position
                                                                      // of 2nd word

      for (int i = 0; i < tokens.length - 1; i++) {
        //System.out.println("t1begin= " + t1begin + "  t2begin=" + t2begin);
        Token token1 = new Token(arg0);
        token1.setCasProcessorId("TokenAnnotator");
        token1.setConfidence(1.0);
        token1.setBegin(t1begin);
        token1.setEnd(t1begin + tokens[i].length());

        Token token2 = new Token(arg0);
        token2.setCasProcessorId("TokenAnnotator");
        token2.setConfidence(1.0);
        token2.setBegin(t2begin);
        token2.setEnd(t2begin + tokens[i + 1].length());
        //System.out.println(" token1= "+token1.getCoveredText()+" token2= "+token2.getCoveredText());
        NGram annotation = new NGram(arg0);
        annotation.setBegin(t1begin);
        annotation.setEnd(t2begin + tokens[i + 1].length());
        annotation.setCasProcessorId("TokenBigramAnnotator");
        annotation.setConfidence(1.0);
        annotation.setElementType("edu.cmu.deiis.types.Token");
        FSArray tokenArray = new FSArray(arg0, 2);
        tokenArray.set(0, token1);
        tokenArray.set(1, token2);
        annotation.setElements(tokenArray);
        annotation.addToIndexes();

        t1begin = t2begin;
        t2begin = t2begin + tokens[i + 1].length() + 1;
      }
    }

    while (answerIt.hasNext()) {
      Answer an = answerIt.next();
      String str = an.getCoveredText();
      int anbegin = an.getBegin();
      String[] tokens = str.split(" ");
      if(tokens.length < 2)
        continue;
      int t1begin = anbegin, t2begin = anbegin + tokens[0].length() + 1;// t2begin is begin position
                                                                        // of 2nd word

      for (int i = 0; i < tokens.length - 1; i++) {
        //System.out.println("t1begin= " + t1begin + "  t2begin=" + t2begin);
        Token token1 = new Token(arg0);
        token1.setCasProcessorId("TokenAnnotator");
        token1.setConfidence(1.0);
        token1.setBegin(t1begin);
        token1.setEnd(t1begin + tokens[i].length());

        Token token2 = new Token(arg0);
        token2.setCasProcessorId("TokenAnnotator");
        token2.setConfidence(1.0);
        token2.setBegin(t2begin);
        token2.setEnd(t2begin + tokens[i + 1].length());
//System.out.println(" token1= "+token1.getCoveredText()+" token2= "+token2.getCoveredText());
        NGram annotation = new NGram(arg0);
        annotation.setBegin(t1begin);
        annotation.setEnd(t2begin + tokens[i + 1].length());
        annotation.setCasProcessorId("TokenBigramAnnotator");
        annotation.setConfidence(1.0);
        annotation.setElementType("edu.cmu.deiis.types.Token");
        FSArray tokenArray = new FSArray(arg0, 2);
        tokenArray.set(0, token1);
        tokenArray.set(1, token2);
        annotation.setElements(tokenArray);
        annotation.addToIndexes();

        t1begin = t2begin;
        t2begin = t2begin + tokens[i + 1].length() + 1;
      }
    }
  }

}
