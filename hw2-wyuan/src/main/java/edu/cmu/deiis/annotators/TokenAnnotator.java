package edu.cmu.deiis.annotators;

import java.util.Iterator;
import java.util.regex.*;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.Token;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Answer;


public class TokenAnnotator extends JCasAnnotator_ImplBase {
  public void process(JCas aJCas){
    Pattern tokenPattern = Pattern.compile("\\w+");//Compiles the given regular expression into a pattern. 

    //obtain an iterator from an index that allows you to step through all annotations of a particular type.
    FSIndex questionIndex = aJCas.getAnnotationIndex(edu.cmu.deiis.types.Question.type);
    FSIndex answerIndex = aJCas.getAnnotationIndex(edu.cmu.deiis.types.Answer.type);
    
    //get iterator of annotation list
    Iterator<Question> questionIter = questionIndex.iterator();
    Iterator<Answer> answerIter = answerIndex.iterator();
    
    while (questionIter.hasNext()) {//iterator over Questions
      Question question = questionIter.next();
      String questionString = question.getCoveredText();//question content  
      int begin = question.getBegin();//begin position of quesion (=2)      
      Matcher matcher = tokenPattern.matcher(questionString);
      while (matcher.find()) {
        // found one - create annotation
        Token annotation = new Token(aJCas);
        annotation.setCasProcessorId("edu.cmu.deiis.types.TokenAnnotator");
        annotation.setConfidence(1.0);
        annotation.setBegin(begin+matcher.start());
        annotation.setEnd(begin+matcher.end());
        annotation.addToIndexes();
      }  
    }

    while (answerIter.hasNext()) {
      Answer amswer = answerIter.next();
      String answerString = amswer.getCoveredText();
      int begin = amswer.getBegin();
      Matcher matcher = tokenPattern.matcher(answerString);
      while (matcher.find()) {
        // found one - create annotation
        Token annotation = new Token(aJCas);
        annotation.setCasProcessorId("edu.cmu.deiis.types.TokenAnnotator");
        annotation.setConfidence(1.0);
        annotation.setBegin(begin+matcher.start());
        annotation.setEnd(begin+matcher.end());
        annotation.addToIndexes();
      }
    }
  }
}
