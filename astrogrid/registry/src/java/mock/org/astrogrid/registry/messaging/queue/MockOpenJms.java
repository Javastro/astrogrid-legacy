package mock.org.astrogrid.registry.messaging.queue;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.registry.messaging.queue.OpenJms;
import javax.naming.NamingException;
import javax.jms.JMSException;
public class MockOpenJms implements OpenJms{
   private ExpectationCounter myConnectCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJms ConnectCalls");
   private ReturnValues myActualConnectReturnValues = new VoidReturnValues(false);
   private ExpectationList myConnectParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.queue.OpenJms java.lang.String");
   private ExpectationList myConnectParameter1Values = new ExpectationList("org.astrogrid.registry.messaging.queue.OpenJms boolean");
   private ExpectationList myConnectParameter2Values = new ExpectationList("org.astrogrid.registry.messaging.queue.OpenJms int");
   private ExpectationCounter myDisconnectCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJms DisconnectCalls");
   private ReturnValues myActualDisconnectReturnValues = new VoidReturnValues(false);
   private ExpectationCounter myStartCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJms StartCalls");
   private ReturnValues myActualStartReturnValues = new VoidReturnValues(false);
   private ExpectationCounter myStopCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJms StopCalls");
   private ReturnValues myActualStopReturnValues = new VoidReturnValues(false);
   private ExpectationCounter myCommitCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJms CommitCalls");
   private ReturnValues myActualCommitReturnValues = new VoidReturnValues(false);
   private ExpectationCounter myRollbackCalls = new ExpectationCounter("org.astrogrid.registry.messaging.queue.OpenJms RollbackCalls");
   private ReturnValues myActualRollbackReturnValues = new VoidReturnValues(false);
   public void setExpectedConnectCalls(int calls){
      myConnectCalls.setExpected(calls);
   }
   public void addExpectedConnectValues(String arg0, boolean arg1, int arg2){
      myConnectParameter0Values.addExpected(arg0);
      myConnectParameter1Values.addExpected(new Boolean(arg1));
      myConnectParameter2Values.addExpected(new Integer(arg2));
   }
   public void connect(String arg0, boolean arg1, int arg2) throws NamingException, JMSException{
      myConnectCalls.inc();
      myConnectParameter0Values.addActual(arg0);
      myConnectParameter1Values.addActual(new Boolean(arg1));
      myConnectParameter2Values.addActual(new Integer(arg2));
      Object nextReturnValue = myActualConnectReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof NamingException)
          throw (NamingException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionConnect(Throwable arg){
      myActualConnectReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedDisconnectCalls(int calls){
      myDisconnectCalls.setExpected(calls);
   }
   public void disconnect() throws JMSException{
      myDisconnectCalls.inc();
      Object nextReturnValue = myActualDisconnectReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionDisconnect(Throwable arg){
      myActualDisconnectReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedStartCalls(int calls){
      myStartCalls.setExpected(calls);
   }
   public void start() throws JMSException{
      myStartCalls.inc();
      Object nextReturnValue = myActualStartReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionStart(Throwable arg){
      myActualStartReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedStopCalls(int calls){
      myStopCalls.setExpected(calls);
   }
   public void stop() throws JMSException{
      myStopCalls.inc();
      Object nextReturnValue = myActualStopReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionStop(Throwable arg){
      myActualStopReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedCommitCalls(int calls){
      myCommitCalls.setExpected(calls);
   }
   public void commit() throws JMSException{
      myCommitCalls.inc();
      Object nextReturnValue = myActualCommitReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionCommit(Throwable arg){
      myActualCommitReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedRollbackCalls(int calls){
      myRollbackCalls.setExpected(calls);
   }
   public void rollback() throws JMSException{
      myRollbackCalls.inc();
      Object nextReturnValue = myActualRollbackReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof JMSException)
          throw (JMSException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionRollback(Throwable arg){
      myActualRollbackReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void verify(){
      myConnectCalls.verify();
      myConnectParameter0Values.verify();
      myConnectParameter1Values.verify();
      myConnectParameter2Values.verify();
      myDisconnectCalls.verify();
      myStartCalls.verify();
      myStopCalls.verify();
      myCommitCalls.verify();
      myRollbackCalls.verify();
   }
}
