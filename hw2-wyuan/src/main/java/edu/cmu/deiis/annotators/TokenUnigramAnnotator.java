package edu.cmu.deiis.annotators;

import java.util.Iterator;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import edu.cmu.deiis.types.*;

/**
 * annotate every word in question and answers as token Annotator does
 * TokenUnigramAnnotator must be used after TokenAnnotator because it uses the output of TokenAnnotator
 * */
public class TokenUnigramAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex Index=arg0.getAnnotationIndex(edu.cmu.deiis.types.Token.type);
    Iterator<Token> it=Index.iterator();
    while(it.hasNext()){
      Token token = it.next();
      NGram annotation=new NGram(arg0);
      
      annotation.setBegin(token.getBegin());
      annotation.setEnd(token.getEnd());
      annotation.setCasProcessorId("TokenUnigramAnnotator");
      annotation.setConfidence(1.0);      
      annotation.setElementType("edu.cmu.deiis.types.Token");
      
      FSArray tokens=new FSArray(arg0, 1);
      tokens.set(0, token);
      annotation.setElements(tokens);
      
      annotation.addToIndexes();
    }
  }
}
