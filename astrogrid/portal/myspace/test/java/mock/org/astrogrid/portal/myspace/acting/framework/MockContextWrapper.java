package mock.org.astrogrid.portal.myspace.acting.framework;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapper;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import java.io.InputStream;
public class MockContextWrapper implements ContextWrapper{
   private ExpectationCounter myGetParameterStringCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetParameterStringCalls");
   private ReturnValues myActualGetParameterStringReturnValues = new ReturnValues(false);
   private ExpectationList myGetParameterStringParameter0Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.String");
   private ExpectationCounter myGetParameterStringStringCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetParameterStringStringCalls");
   private ReturnValues myActualGetParameterStringStringReturnValues = new ReturnValues(false);
   private ExpectationList myGetParameterStringStringParameter0Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.String");
   private ExpectationList myGetParameterStringStringParameter1Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.String");
   private ExpectationCounter myGetUserCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetUserCalls");
   private ReturnValues myActualGetUserReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetEndPointCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetEndPointCalls");
   private ReturnValues myActualGetEndPointReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetAgslCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetAgslCalls");
   private ReturnValues myActualGetAgslReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetStoreClientCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetStoreClientCalls");
   private ReturnValues myActualGetStoreClientReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetGlobalAttributeCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper SetGlobalAttributeCalls");
   private ReturnValues myActualSetGlobalAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetGlobalAttributeParameter0Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.String");
   private ExpectationList mySetGlobalAttributeParameter1Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.Object");
   private ExpectationCounter mySetLocalAttributeCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper SetLocalAttributeCalls");
   private ReturnValues myActualSetLocalAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetLocalAttributeParameter0Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.String");
   private ExpectationList mySetLocalAttributeParameter1Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.Object");
   private ExpectationCounter myGetFileInputStreamCalls = new ExpectationCounter("org.astrogrid.portal.myspace.acting.framework.ContextWrapper GetFileInputStreamCalls");
   private ReturnValues myActualGetFileInputStreamReturnValues = new ReturnValues(false);
   private ExpectationList myGetFileInputStreamParameter0Values = new ExpectationList("org.astrogrid.portal.myspace.acting.framework.ContextWrapper java.lang.String");
   public void setExpectedGetParameterStringCalls(int calls){
      myGetParameterStringCalls.setExpected(calls);
   }
   public void addExpectedGetParameterStringValues(String arg0){
      myGetParameterStringParameter0Values.addExpected(arg0);
   }
   public String getParameter(String arg0){
      myGetParameterStringCalls.inc();
      myGetParameterStringParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetParameterStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetParameterString(Throwable arg){
      myActualGetParameterStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetParameterString(String arg){
      myActualGetParameterStringReturnValues.add(arg);
   }
   public void setExpectedGetParameterStringStringCalls(int calls){
      myGetParameterStringStringCalls.setExpected(calls);
   }
   public void addExpectedGetParameterStringStringValues(String arg0, String arg1){
      myGetParameterStringStringParameter0Values.addExpected(arg0);
      myGetParameterStringStringParameter1Values.addExpected(arg1);
   }
   public String getParameter(String arg0, String arg1){
      myGetParameterStringStringCalls.inc();
      myGetParameterStringStringParameter0Values.addActual(arg0);
      myGetParameterStringStringParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualGetParameterStringStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetParameterStringString(Throwable arg){
      myActualGetParameterStringStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetParameterStringString(String arg){
      myActualGetParameterStringStringReturnValues.add(arg);
   }
   public void setExpectedGetUserCalls(int calls){
      myGetUserCalls.setExpected(calls);
   }
   public User getUser(){
      myGetUserCalls.inc();
      Object nextReturnValue = myActualGetUserReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (User) nextReturnValue;
   }
   public void setupExceptionGetUser(Throwable arg){
      myActualGetUserReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetUser(User arg){
      myActualGetUserReturnValues.add(arg);
   }
   public void setExpectedGetEndPointCalls(int calls){
      myGetEndPointCalls.setExpected(calls);
   }
   public String getEndPoint(){
      myGetEndPointCalls.inc();
      Object nextReturnValue = myActualGetEndPointReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetEndPoint(Throwable arg){
      myActualGetEndPointReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetEndPoint(String arg){
      myActualGetEndPointReturnValues.add(arg);
   }
   public void setExpectedGetAgslCalls(int calls){
      myGetAgslCalls.setExpected(calls);
   }
   public Agsl getAgsl(){
      myGetAgslCalls.inc();
      Object nextReturnValue = myActualGetAgslReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Agsl) nextReturnValue;
   }
   public void setupExceptionGetAgsl(Throwable arg){
      myActualGetAgslReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAgsl(Agsl arg){
      myActualGetAgslReturnValues.add(arg);
   }
   public void setExpectedGetStoreClientCalls(int calls){
      myGetStoreClientCalls.setExpected(calls);
   }
   public StoreClient getStoreClient(){
      myGetStoreClientCalls.inc();
      Object nextReturnValue = myActualGetStoreClientReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (StoreClient) nextReturnValue;
   }
   public void setupExceptionGetStoreClient(Throwable arg){
      myActualGetStoreClientReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetStoreClient(StoreClient arg){
      myActualGetStoreClientReturnValues.add(arg);
   }
   public void setExpectedSetGlobalAttributeCalls(int calls){
      mySetGlobalAttributeCalls.setExpected(calls);
   }
   public void addExpectedSetGlobalAttributeValues(String arg0, Object arg1){
      mySetGlobalAttributeParameter0Values.addExpected(arg0);
      mySetGlobalAttributeParameter1Values.addExpected(arg1);
   }
   public void setGlobalAttribute(String arg0, Object arg1){
      mySetGlobalAttributeCalls.inc();
      mySetGlobalAttributeParameter0Values.addActual(arg0);
      mySetGlobalAttributeParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualSetGlobalAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetGlobalAttribute(Throwable arg){
      myActualSetGlobalAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedSetLocalAttributeCalls(int calls){
      mySetLocalAttributeCalls.setExpected(calls);
   }
   public void addExpectedSetLocalAttributeValues(String arg0, Object arg1){
      mySetLocalAttributeParameter0Values.addExpected(arg0);
      mySetLocalAttributeParameter1Values.addExpected(arg1);
   }
   public void setLocalAttribute(String arg0, Object arg1){
      mySetLocalAttributeCalls.inc();
      mySetLocalAttributeParameter0Values.addActual(arg0);
      mySetLocalAttributeParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualSetLocalAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetLocalAttribute(Throwable arg){
      myActualSetLocalAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetFileInputStreamCalls(int calls){
      myGetFileInputStreamCalls.setExpected(calls);
   }
   public void addExpectedGetFileInputStreamValues(String arg0){
      myGetFileInputStreamParameter0Values.addExpected(arg0);
   }
   public InputStream getFileInputStream(String arg0) throws Exception{
      myGetFileInputStreamCalls.inc();
      myGetFileInputStreamParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetFileInputStreamReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (InputStream) nextReturnValue;
   }
   public void setupExceptionGetFileInputStream(Throwable arg){
      myActualGetFileInputStreamReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetFileInputStream(InputStream arg){
      myActualGetFileInputStreamReturnValues.add(arg);
   }
   public void verify(){
      myGetParameterStringCalls.verify();
      myGetParameterStringParameter0Values.verify();
      myGetParameterStringStringCalls.verify();
      myGetParameterStringStringParameter0Values.verify();
      myGetParameterStringStringParameter1Values.verify();
      myGetUserCalls.verify();
      myGetEndPointCalls.verify();
      myGetAgslCalls.verify();
      myGetStoreClientCalls.verify();
      mySetGlobalAttributeCalls.verify();
      mySetGlobalAttributeParameter0Values.verify();
      mySetGlobalAttributeParameter1Values.verify();
      mySetLocalAttributeCalls.verify();
      mySetLocalAttributeParameter0Values.verify();
      mySetLocalAttributeParameter1Values.verify();
      myGetFileInputStreamCalls.verify();
      myGetFileInputStreamParameter0Values.verify();
   }
}
