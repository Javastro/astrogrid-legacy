package mock.org.astrogrid.registry.messaging.queue;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.registry.messaging.queue.OpenJmsSenderInterface;
import javax.jms.JMSException;
public class MockOpenJmsSenderInterface extends MockOpenJms implements OpenJmsSenderInterface{
   private ExpectationCounter mySendTextMessageCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJmsSenderInterface SendTextMessageCalls");
   private ReturnValues myActualSendTextMessageReturnValues = new VoidReturnValues(false);
   private ExpectationList mySendTextMessageParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.queue.OpenJmsSenderInterface java.lang.String");
   private ExpectationList mySendTextMessageParameter1Values = new ExpectationList("org.astrogrid.registry.messaging.queue.OpenJmsSenderInterface int");
   public void setExpectedSendTextMessageCalls(int calls){
      mySendTextMessageCalls.setExpected(calls);
   }
   public void addExpectedSendTextMessageValues(String arg0, int arg1){
      mySendTextMessageParameter0Values.addExpected(arg0);
      mySendTextMessageParameter1Values.addExpected(new Integer(arg1));
   }
   public void sendTextMessage(String arg0, int arg1) throws JMSException{
      mySendTextMessageCalls.inc();
      mySendTextMessageParameter0Values.addActual(arg0);
      mySendTextMessageParameter1Values.addActual(new Integer(arg1));
      Object nextReturnValue = myActualSendTextMessageReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSendTextMessage(Throwable arg){
      myActualSendTextMessageReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void verify(){
      mySendTextMessageCalls.verify();
      mySendTextMessageParameter0Values.verify();
      mySendTextMessageParameter1Values.verify();
   }
}
