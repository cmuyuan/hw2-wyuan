package edu.cmu.deiis.annotators;

import java.util.*;
import org.apache.uima.analysis_component.*;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.*;
import edu.cmu.deiis.types.*;

/**
 * calculate precision of every answer using different annotators
 * used only after AnswerScoreAnnotator or NGramAnswerScoreAnnotator
 * precision depends on a threshold which can be changed.
 * */
public class Evaluator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex scoreIndex=arg0.getAnnotationIndex(AnswerScore.type);
    Iterator scoreIt=scoreIndex.iterator();
    
    /*count number of correct prediction*/
    int correct=0;
    while(scoreIt.hasNext()){
      AnswerScore as = (AnswerScore) scoreIt.next();
      double score = as.getScore();
      /*1/3 is a threshold*/
      if(score >= 0.333 && as.getAnswer().getIsCorrect())//must use fraction number !!
        correct++;
      else if(score < 0.333 && !as.getAnswer().getIsCorrect())
        correct++;
      //System.out.println("score="+score+"   IsCorrect="+as.getAnswer().getIsCorrect()+"  correct="+correct);
    }
    //System.out.println("correct="+correct+"  scoreIndex.size()="+scoreIndex.size());;
    System.out.println("accuracy is "+(double)correct/(double)scoreIndex.size());
  }
}