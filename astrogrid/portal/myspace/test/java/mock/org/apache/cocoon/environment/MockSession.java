package mock.org.apache.cocoon.environment;

import java.util.Enumeration;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.apache.cocoon.environment.Session;
public class MockSession implements Session{
   private ExpectationCounter myGetCreationTimeCalls = new ExpectationCounter("org.apache.cocoon.environment.Session GetCreationTimeCalls");
   private ReturnValues myActualGetCreationTimeReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetIdCalls = new ExpectationCounter("org.apache.cocoon.environment.Session GetIdCalls");
   private ReturnValues myActualGetIdReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetLastAccessedTimeCalls = new ExpectationCounter("org.apache.cocoon.environment.Session GetLastAccessedTimeCalls");
   private ReturnValues myActualGetLastAccessedTimeReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetMaxInactiveIntervalCalls = new ExpectationCounter("org.apache.cocoon.environment.Session SetMaxInactiveIntervalCalls");
   private ReturnValues myActualSetMaxInactiveIntervalReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetMaxInactiveIntervalParameter0Values = new ExpectationList("org.apache.cocoon.environment.Session int");
   private ExpectationCounter myGetMaxInactiveIntervalCalls = new ExpectationCounter("org.apache.cocoon.environment.Session GetMaxInactiveIntervalCalls");
   private ReturnValues myActualGetMaxInactiveIntervalReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetAttributeCalls = new ExpectationCounter("org.apache.cocoon.environment.Session GetAttributeCalls");
   private ReturnValues myActualGetAttributeReturnValues = new ReturnValues(false);
   private ExpectationList myGetAttributeParameter0Values = new ExpectationList("org.apache.cocoon.environment.Session java.lang.String");
   private ExpectationCounter myGetAttributeNamesCalls = new ExpectationCounter("org.apache.cocoon.environment.Session GetAttributeNamesCalls");
   private ReturnValues myActualGetAttributeNamesReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetAttributeCalls = new ExpectationCounter("org.apache.cocoon.environment.Session SetAttributeCalls");
   private ReturnValues myActualSetAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetAttributeParameter0Values = new ExpectationList("org.apache.cocoon.environment.Session java.lang.String");
   private ExpectationList mySetAttributeParameter1Values = new ExpectationList("org.apache.cocoon.environment.Session java.lang.Object");
   private ExpectationCounter myRemoveAttributeCalls = new ExpectationCounter("org.apache.cocoon.environment.Session RemoveAttributeCalls");
   private ReturnValues myActualRemoveAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList myRemoveAttributeParameter0Values = new ExpectationList("org.apache.cocoon.environment.Session java.lang.String");
   private ExpectationCounter myInvalidateCalls = new ExpectationCounter("org.apache.cocoon.environment.Session InvalidateCalls");
   private ReturnValues myActualInvalidateReturnValues = new VoidReturnValues(false);
   private ExpectationCounter myIsNewCalls = new ExpectationCounter("org.apache.cocoon.environment.Session IsNewCalls");
   private ReturnValues myActualIsNewReturnValues = new ReturnValues(false);
   public void setExpectedGetCreationTimeCalls(int calls){
      myGetCreationTimeCalls.setExpected(calls);
   }
   public long getCreationTime(){
      myGetCreationTimeCalls.inc();
      Object nextReturnValue = myActualGetCreationTimeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Long) nextReturnValue).longValue();
   }
   public void setupExceptionGetCreationTime(Throwable arg){
      myActualGetCreationTimeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetCreationTime(long arg){
      myActualGetCreationTimeReturnValues.add(new Long(arg));
   }
   public void setExpectedGetIdCalls(int calls){
      myGetIdCalls.setExpected(calls);
   }
   public String getId(){
      myGetIdCalls.inc();
      Object nextReturnValue = myActualGetIdReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetId(Throwable arg){
      myActualGetIdReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetId(String arg){
      myActualGetIdReturnValues.add(arg);
   }
   public void setExpectedGetLastAccessedTimeCalls(int calls){
      myGetLastAccessedTimeCalls.setExpected(calls);
   }
   public long getLastAccessedTime(){
      myGetLastAccessedTimeCalls.inc();
      Object nextReturnValue = myActualGetLastAccessedTimeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Long) nextReturnValue).longValue();
   }
   public void setupExceptionGetLastAccessedTime(Throwable arg){
      myActualGetLastAccessedTimeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetLastAccessedTime(long arg){
      myActualGetLastAccessedTimeReturnValues.add(new Long(arg));
   }
   public void setExpectedSetMaxInactiveIntervalCalls(int calls){
      mySetMaxInactiveIntervalCalls.setExpected(calls);
   }
   public void addExpectedSetMaxInactiveIntervalValues(int arg0){
      mySetMaxInactiveIntervalParameter0Values.addExpected(new Integer(arg0));
   }
   public void setMaxInactiveInterval(int arg0){
      mySetMaxInactiveIntervalCalls.inc();
      mySetMaxInactiveIntervalParameter0Values.addActual(new Integer(arg0));
      Object nextReturnValue = myActualSetMaxInactiveIntervalReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetMaxInactiveInterval(Throwable arg){
      myActualSetMaxInactiveIntervalReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetMaxInactiveIntervalCalls(int calls){
      myGetMaxInactiveIntervalCalls.setExpected(calls);
   }
   public int getMaxInactiveInterval(){
      myGetMaxInactiveIntervalCalls.inc();
      Object nextReturnValue = myActualGetMaxInactiveIntervalReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Integer) nextReturnValue).intValue();
   }
   public void setupExceptionGetMaxInactiveInterval(Throwable arg){
      myActualGetMaxInactiveIntervalReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetMaxInactiveInterval(int arg){
      myActualGetMaxInactiveIntervalReturnValues.add(new Integer(arg));
   }
   public void setExpectedGetAttributeCalls(int calls){
      myGetAttributeCalls.setExpected(calls);
   }
   public void addExpectedGetAttributeValues(String arg0){
      myGetAttributeParameter0Values.addExpected(arg0);
   }
   public Object getAttribute(String arg0){
      myGetAttributeCalls.inc();
      myGetAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Object) nextReturnValue;
   }
   public void setupExceptionGetAttribute(Throwable arg){
      myActualGetAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttribute(Object arg){
      myActualGetAttributeReturnValues.add(arg);
   }
   public void setExpectedGetAttributeNamesCalls(int calls){
      myGetAttributeNamesCalls.setExpected(calls);
   }
   public Enumeration getAttributeNames(){
      myGetAttributeNamesCalls.inc();
      Object nextReturnValue = myActualGetAttributeNamesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Enumeration) nextReturnValue;
   }
   public void setupExceptionGetAttributeNames(Throwable arg){
      myActualGetAttributeNamesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttributeNames(Enumeration arg){
      myActualGetAttributeNamesReturnValues.add(arg);
   }
   public void setExpectedSetAttributeCalls(int calls){
      mySetAttributeCalls.setExpected(calls);
   }
   public void addExpectedSetAttributeValues(String arg0, Object arg1){
      mySetAttributeParameter0Values.addExpected(arg0);
      mySetAttributeParameter1Values.addExpected(arg1);
   }
   public void setAttribute(String arg0, Object arg1){
      mySetAttributeCalls.inc();
      mySetAttributeParameter0Values.addActual(arg0);
      mySetAttributeParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualSetAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetAttribute(Throwable arg){
      myActualSetAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedRemoveAttributeCalls(int calls){
      myRemoveAttributeCalls.setExpected(calls);
   }
   public void addExpectedRemoveAttributeValues(String arg0){
      myRemoveAttributeParameter0Values.addExpected(arg0);
   }
   public void removeAttribute(String arg0){
      myRemoveAttributeCalls.inc();
      myRemoveAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualRemoveAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionRemoveAttribute(Throwable arg){
      myActualRemoveAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedInvalidateCalls(int calls){
      myInvalidateCalls.setExpected(calls);
   }
   public void invalidate(){
      myInvalidateCalls.inc();
      Object nextReturnValue = myActualInvalidateReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionInvalidate(Throwable arg){
      myActualInvalidateReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedIsNewCalls(int calls){
      myIsNewCalls.setExpected(calls);
   }
   public boolean isNew(){
      myIsNewCalls.inc();
      Object nextReturnValue = myActualIsNewReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsNew(Throwable arg){
      myActualIsNewReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsNew(boolean arg){
      myActualIsNewReturnValues.add(new Boolean(arg));
   }
   public void verify(){
      myGetCreationTimeCalls.verify();
      myGetIdCalls.verify();
      myGetLastAccessedTimeCalls.verify();
      mySetMaxInactiveIntervalCalls.verify();
      mySetMaxInactiveIntervalParameter0Values.verify();
      myGetMaxInactiveIntervalCalls.verify();
      myGetAttributeCalls.verify();
      myGetAttributeParameter0Values.verify();
      myGetAttributeNamesCalls.verify();
      mySetAttributeCalls.verify();
      mySetAttributeParameter0Values.verify();
      mySetAttributeParameter1Values.verify();
      myRemoveAttributeCalls.verify();
      myRemoveAttributeParameter0Values.verify();
      myInvalidateCalls.verify();
      myIsNewCalls.verify();
   }
}
