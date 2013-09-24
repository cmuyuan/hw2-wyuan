package edu.cmu.deiis.annotators;

import java.util.Iterator;
import org.apache.uima.analysis_component.*;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.*;
import edu.cmu.deiis.types.*;


/**
 * give answers score using overlap tokens method.
 * NGramAnswerScoreAnnotator is used only after NGramAnnotators.
 * */
public class NGramAnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex questionIndex = arg0.getAnnotationIndex(Question.type);
    FSIndex answerIndex = arg0.getAnnotationIndex(Answer.type);
    FSIndex NGramIndex = arg0.getAnnotationIndex(NGram.type);
    Iterator<Question> questionIt = questionIndex.iterator();
    Iterator<Answer> answerIt = answerIndex.iterator();
    Iterator<NGram> ngramIt = NGramIndex.iterator();

    Question q = questionIt.next();
    String question = q.getCoveredText();
    // String[] questionTokens = question.split(" ");
    boolean out=false;
    String outstr=null;
    
    while (answerIt.hasNext()) {
      Answer answer = answerIt.next();
      int overlapCount = 0, totalCount = 0;
      // String answerStr = answer.getCoveredText();
      //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
      if(out){
        out=false;
        totalCount++;
        if (question.contains(outstr))
          overlapCount++;
      }
        
      while (ngramIt.hasNext()) {
        NGram annotation = ngramIt.next();
        if(annotation.getEnd()<=q.getEnd())
          continue;
        //System.out.println(annotation.getCoveredText());
        //System.out.println("@NGramAnswerScoreAnnotator:   "+annotation.getCasProcessorId());
        //System.out.println("annotation.getEnd()= "+annotation.getEnd()+"answer.getEnd()"+answer.getEnd());
        if (annotation.getEnd() <= answer.getEnd()) {// if ngram belongs to the answer
          String ngramstr = annotation.getCoveredText();
          if (question.contains(ngramstr)){
            //System.out.println("question contain "+ngramstr);
            overlapCount++;
          }
          totalCount++;
        } else{
          out = true;
          outstr = annotation.getCoveredText();
          break;
        }
      }
      //System.out.println("overlapCount="+overlapCount+"  totalcount= "+totalCount);
      AnswerScore answerScore = new AnswerScore(arg0);
      answerScore.setBegin(answer.getBegin());
      answerScore.setEnd(answer.getEnd());
      answerScore.setCasProcessorId("AnswerScoreAnnotator");
      answerScore.setConfidence(1.0);
      answerScore.setAnswer(answer);
      if(totalCount != 0)
        answerScore.setScore((double) overlapCount / (double) totalCount);
      answerScore.addToIndexes();
    }
  }
}
