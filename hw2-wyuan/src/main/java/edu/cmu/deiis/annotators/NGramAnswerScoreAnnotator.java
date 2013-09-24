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

    String question = questionIt.next().getCoveredText();
    // String[] questionTokens = question.split(" ");

    while (answerIt.hasNext()) {
      Answer answer = answerIt.next();
      int overlapCount = 0, totalCount = 0;
      // String answerStr = answer.getCoveredText();
      while (ngramIt.hasNext()) {
        NGram annotation = ngramIt.next();
        if (annotation.getEnd() <= answer.getEnd()) {// if ngram belongs to the answer
          String ngramstr = annotation.getCoveredText();
          if (question.contains(ngramstr))
            overlapCount++;
          totalCount++;
        } else
          break;
      }
      AnswerScore answerScore = new AnswerScore(arg0);
      answerScore.setBegin(answer.getBegin());
      answerScore.setEnd(answer.getEnd());
      answerScore.setCasProcessorId("AnswerScoreAnnotator");
      answerScore.setConfidence(1.0);
      answerScore.setAnswer(answer);
      answerScore.setScore((double) overlapCount / (double) totalCount);
      answerScore.addToIndexes();
    }
  }
}
