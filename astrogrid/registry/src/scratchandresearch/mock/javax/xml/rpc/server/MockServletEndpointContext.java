package mock.javax.xml.rpc.server;

import java.security.Principal;

import mockmaker.ReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
public class MockServletEndpointContext implements ServletEndpointContext{
   private ExpectationCounter myGetMessageContextCalls = new ExpectationCounter("javax.xml.rpc.server.ServletEndpointContext GetMessageContextCalls");
   private ReturnValues myActualGetMessageContextReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetUserPrincipalCalls = new ExpectationCounter("javax.xml.rpc.server.ServletEndpointContext GetUserPrincipalCalls");
   private ReturnValues myActualGetUserPrincipalReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetHttpSessionCalls = new ExpectationCounter("javax.xml.rpc.server.ServletEndpointContext GetHttpSessionCalls");
   private ReturnValues myActualGetHttpSessionReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetServletContextCalls = new ExpectationCounter("javax.xml.rpc.server.ServletEndpointContext GetServletContextCalls");
   private ReturnValues myActualGetServletContextReturnValues = new ReturnValues(false);
   public void setExpectedGetMessageContextCalls(int calls){
      myGetMessageContextCalls.setExpected(calls);
   }
   public MessageContext getMessageContext(){
      myGetMessageContextCalls.inc();
      Object nextReturnValue = myActualGetMessageContextReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (MessageContext) nextReturnValue;
   }
   public void setupExceptionGetMessageContext(Throwable arg){
      myActualGetMessageContextReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetMessageContext(MessageContext arg){
      myActualGetMessageContextReturnValues.add(arg);
   }
   public void setExpectedGetUserPrincipalCalls(int calls){
      myGetUserPrincipalCalls.setExpected(calls);
   }
   public Principal getUserPrincipal(){
      myGetUserPrincipalCalls.inc();
      Object nextReturnValue = myActualGetUserPrincipalReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Principal) nextReturnValue;
   }
   public void setupExceptionGetUserPrincipal(Throwable arg){
      myActualGetUserPrincipalReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetUserPrincipal(Principal arg){
      myActualGetUserPrincipalReturnValues.add(arg);
   }
   public void setExpectedGetHttpSessionCalls(int calls){
      myGetHttpSessionCalls.setExpected(calls);
   }
   public HttpSession getHttpSession(){
      myGetHttpSessionCalls.inc();
      Object nextReturnValue = myActualGetHttpSessionReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (HttpSession) nextReturnValue;
   }
   public void setupExceptionGetHttpSession(Throwable arg){
      myActualGetHttpSessionReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetHttpSession(HttpSession arg){
      myActualGetHttpSessionReturnValues.add(arg);
   }
   public void setExpectedGetServletContextCalls(int calls){
      myGetServletContextCalls.setExpected(calls);
   }
   public ServletContext getServletContext(){
      myGetServletContextCalls.inc();
      Object nextReturnValue = myActualGetServletContextReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (ServletContext) nextReturnValue;
   }
   public void setupExceptionGetServletContext(Throwable arg){
      myActualGetServletContextReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetServletContext(ServletContext arg){
      myActualGetServletContextReturnValues.add(arg);
   }
   public void verify(){
      myGetMessageContextCalls.verify();
      myGetUserPrincipalCalls.verify();
      myGetHttpSessionCalls.verify();
      myGetServletContextCalls.verify();
   }
}
