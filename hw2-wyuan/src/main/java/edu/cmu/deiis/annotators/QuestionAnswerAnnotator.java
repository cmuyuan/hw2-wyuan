package edu.cmu.deiis.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.*;

/**
 * annotate question and answers
 * must be used in the very first of all the annotators.
 */
public class QuestionAnswerAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String docText = arg0.getDocumentText();
    // System.out.println(docText);
    String[] lines = docText.split("\r\n");
    int begin = 0;
    for (String s : lines) {
      if (s.startsWith("Q")) {
        Question question = new Question(arg0);
        question.setBegin(begin + 2);
        question.setEnd(begin + s.length() - 1);
        question.setCasProcessorId("QuestionAnswerAnnotator");
        question.setConfidence(1.0);
        question.addToIndexes();
        begin += s.length() + 2;
      }
    }
    begin = 0;
    for (String s : lines) {
      if (s.startsWith("A")) {
        Answer anwser = new Answer(arg0);
        anwser.setBegin(begin + 4);
        anwser.setEnd(begin + s.length() - 1);
        anwser.setCasProcessorId("QuestionAnswerAnnotator");
        anwser.setConfidence(1.0);
        if (s.startsWith("A 1"))
          anwser.setIsCorrect(true);
        else if (s.startsWith("A 0"))
          anwser.setIsCorrect(false);
        anwser.addToIndexes();
        begin += s.length() + 2;
      } else
        begin += s.length() + 2;
    }
  }
}
