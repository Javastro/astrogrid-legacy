package mock.org.astrogrid.registry.messaging.queue;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.registry.messaging.queue.OpenJmsReceiverInterface;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import java.util.List;
import javax.jms.MessageListener;
public class MockOpenJmsReceiverInterface extends MockOpenJms implements OpenJmsReceiverInterface{
   private ExpectationCounter myGetTextMessageCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJmsReceiverInterface GetTextMessageCalls");
   private ReturnValues myActualGetTextMessageReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetAllTextMessagesCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJmsReceiverInterface GetAllTextMessagesCalls");
   private ReturnValues myActualGetAllTextMessagesReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetMessageListenerCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJmsReceiverInterface SetMessageListenerCalls");
   private ReturnValues myActualSetMessageListenerReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetMessageListenerParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.queue.OpenJmsReceiverInterface javax.jms.MessageListener");
   public void setExpectedGetTextMessageCalls(int calls){
      myGetTextMessageCalls.setExpected(calls);
   }
   public TextMessage getTextMessage() throws JMSException{
      myGetTextMessageCalls.inc();
      Object nextReturnValue = myActualGetTextMessageReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (TextMessage) nextReturnValue;
   }
   public void setupExceptionGetTextMessage(Throwable arg){
      myActualGetTextMessageReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetTextMessage(TextMessage arg){
      myActualGetTextMessageReturnValues.add(arg);
   }
   public void setExpectedGetAllTextMessagesCalls(int calls){
      myGetAllTextMessagesCalls.setExpected(calls);
   }
   public List getAllTextMessages(){
      myGetAllTextMessagesCalls.inc();
      Object nextReturnValue = myActualGetAllTextMessagesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (List) nextReturnValue;
   }
   public void setupExceptionGetAllTextMessages(Throwable arg){
      myActualGetAllTextMessagesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAllTextMessages(List arg){
      myActualGetAllTextMessagesReturnValues.add(arg);
   }
   public void setExpectedSetMessageListenerCalls(int calls){
      mySetMessageListenerCalls.setExpected(calls);
   }
   public void addExpectedSetMessageListenerValues(MessageListener arg0){
      mySetMessageListenerParameter0Values.addExpected(arg0);
   }
   public void setMessageListener(MessageListener arg0) throws JMSException{
      mySetMessageListenerCalls.inc();
      mySetMessageListenerParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualSetMessageListenerReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetMessageListener(Throwable arg){
      myActualSetMessageListenerReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void verify(){
      myGetTextMessageCalls.verify();
      myGetAllTextMessagesCalls.verify();
      mySetMessageListenerCalls.verify();
      mySetMessageListenerParameter0Values.verify();
   }
}
