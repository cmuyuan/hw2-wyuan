package edu.cmu.deiis.annotators;

import java.util.Iterator;
import org.apache.uima.analysis_component.*;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.*;
import edu.cmu.deiis.types.*;


/**
 * give answers using overlap tokens methods.
 * used only after TokenAnnotator
 * */
public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex questionIndex = arg0.getAnnotationIndex(Question.type);
    FSIndex answerIndex = arg0.getAnnotationIndex(Answer.type);
    Iterator<Question> questionIt = questionIndex.iterator();
    Iterator<Answer> answerIt = answerIndex.iterator();

    String question = questionIt.next().getCoveredText();
    String[] questionTokens = question.split(" ");    

    while(answerIt.hasNext()){
          int overlapCount=0;
          Answer answer = answerIt.next();
          String answerStr = answer.getCoveredText();
          String[] answerTokens = answerStr.split(" ");
          for(String an : answerTokens){//for every word in answer, if appears in question, then count++
            for(String q : questionTokens){
              if(q.equalsIgnoreCase(an)){
                overlapCount++;
                break;//one word in answer counts only once
              }              
            }
          }
          AnswerScore answerScore = new AnswerScore(arg0);
          answerScore.setBegin(answer.getBegin());
          answerScore.setEnd(answer.getEnd());
          answerScore.setCasProcessorId("AnswerScoreAnnotator");
          answerScore.setConfidence(1.0);
          answerScore.setAnswer(answer);        
          answerScore.setScore((double)overlapCount/(double)answerTokens.length);          
          answerScore.addToIndexes();
    }    
  }
}
