package mock.org.astrogrid.registry.messaging.log;

import mockmaker.ReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.registry.messaging.log.XmlBlaster;
import org.xmlBlaster.client.qos.ConnectReturnQos;
import org.xmlBlaster.client.qos.ConnectQos;
import org.xmlBlaster.client.I_Callback;
import org.xmlBlaster.util.XmlBlasterException;
import org.xmlBlaster.client.qos.DisconnectQos;
import org.xmlBlaster.client.qos.PublishReturnQos;
import org.xmlBlaster.util.MsgUnit;
import org.xmlBlaster.client.qos.EraseReturnQos;
public class MockXmlBlaster implements XmlBlaster{
   private ExpectationCounter myConnectCalls = new ExpectationCounter("org.astrogrid.registry.messaging.log.XmlBlaster ConnectCalls");
   private ReturnValues myActualConnectReturnValues = new ReturnValues(false);
   private ExpectationList myConnectParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster org.xmlBlaster.client.qos.ConnectQos");
   private ExpectationList myConnectParameter1Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster org.xmlBlaster.client.I_Callback");
   private ExpectationCounter myDisconnectCalls = new ExpectationCounter("org.astrogrid.registry.messaging.log.XmlBlaster DisconnectCalls");
   private ReturnValues myActualDisconnectReturnValues = new ReturnValues(false);
   private ExpectationList myDisconnectParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster org.xmlBlaster.client.qos.DisconnectQos");
   private ExpectationCounter myPublishStringStringCalls = new ExpectationCounter("org.astrogrid.registry.messaging.log.XmlBlaster PublishStringStringCalls");
   private ReturnValues myActualPublishStringStringReturnValues = new ReturnValues(false);
   private ExpectationList myPublishStringStringParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster java.lang.String");
   private ExpectationCounter myPublishStringStringStringCalls = new ExpectationCounter("org.astrogrid.registry.messaging.log.XmlBlaster PublishStringStringStringCalls");
   private ReturnValues myActualPublishStringStringStringReturnValues = new ReturnValues(false);
   private ExpectationList myPublishStringStringStringParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster java.lang.String");
   private ExpectationList myPublishStringStringStringParameter1Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster java.lang.String");
   private ExpectationCounter myGetMessagesCalls = new ExpectationCounter("org.astrogrid.registry.messaging.log.XmlBlaster GetMessagesCalls");
   private ReturnValues myActualGetMessagesReturnValues = new ReturnValues(false);
   private ExpectationList myGetMessagesParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster java.lang.String");
   private ExpectationCounter myEraseMessagesCalls = new ExpectationCounter("org.astrogrid.registry.messaging.log.XmlBlaster EraseMessagesCalls");
   private ReturnValues myActualEraseMessagesReturnValues = new ReturnValues(false);
   private ExpectationList myEraseMessagesParameter0Values = new ExpectationList("org.astrogrid.registry.messaging.log.XmlBlaster java.lang.String");
   public void setExpectedConnectCalls(int calls){
      myConnectCalls.setExpected(calls);
   }
   public void addExpectedConnectValues(ConnectQos arg0, I_Callback arg1){
      myConnectParameter0Values.addExpected(arg0);
      myConnectParameter1Values.addExpected(arg1);
   }
   public ConnectReturnQos connect(ConnectQos arg0, I_Callback arg1) throws XmlBlasterException{
      myConnectCalls.inc();
      myConnectParameter0Values.addActual(arg0);
      myConnectParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualConnectReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof XmlBlasterException)
          throw (XmlBlasterException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (ConnectReturnQos) nextReturnValue;
   }
   public void setupExceptionConnect(Throwable arg){
      myActualConnectReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupConnect(ConnectReturnQos arg){
      myActualConnectReturnValues.add(arg);
   }
   public void setExpectedDisconnectCalls(int calls){
      myDisconnectCalls.setExpected(calls);
   }
   public void addExpectedDisconnectValues(DisconnectQos arg0){
      myDisconnectParameter0Values.addExpected(arg0);
   }
   public boolean disconnect(DisconnectQos arg0) throws XmlBlasterException{
      myDisconnectCalls.inc();
      myDisconnectParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualDisconnectReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof XmlBlasterException)
          throw (XmlBlasterException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionDisconnect(Throwable arg){
      myActualDisconnectReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupDisconnect(boolean arg){
      myActualDisconnectReturnValues.add(new Boolean(arg));
   }
   public void setExpectedPublishStringStringCalls(int calls){
      myPublishStringStringCalls.setExpected(calls);
   }
   public void addExpectedPublishStringStringValues(String arg0){
      myPublishStringStringParameter0Values.addExpected(arg0);
   }
   public PublishReturnQos publishString(String arg0) throws XmlBlasterException{
      myPublishStringStringCalls.inc();
      myPublishStringStringParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualPublishStringStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof XmlBlasterException)
          throw (XmlBlasterException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (PublishReturnQos) nextReturnValue;
   }
   public void setupExceptionPublishStringString(Throwable arg){
      myActualPublishStringStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupPublishStringString(PublishReturnQos arg){
      myActualPublishStringStringReturnValues.add(arg);
   }
   public void setExpectedPublishStringStringStringCalls(int calls){
      myPublishStringStringStringCalls.setExpected(calls);
   }
   public void addExpectedPublishStringStringStringValues(String arg0, String arg1){
      myPublishStringStringStringParameter0Values.addExpected(arg0);
      myPublishStringStringStringParameter1Values.addExpected(arg1);
   }
   public PublishReturnQos publishString(String arg0, String arg1) throws XmlBlasterException{
      myPublishStringStringStringCalls.inc();
      myPublishStringStringStringParameter0Values.addActual(arg0);
      myPublishStringStringStringParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualPublishStringStringStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof XmlBlasterException)
          throw (XmlBlasterException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (PublishReturnQos) nextReturnValue;
   }
   public void setupExceptionPublishStringStringString(Throwable arg){
      myActualPublishStringStringStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupPublishStringStringString(PublishReturnQos arg){
      myActualPublishStringStringStringReturnValues.add(arg);
   }
   public void setExpectedGetMessagesCalls(int calls){
      myGetMessagesCalls.setExpected(calls);
   }
   public void addExpectedGetMessagesValues(String arg0){
      myGetMessagesParameter0Values.addExpected(arg0);
   }
   public MsgUnit[] getMessages(String arg0) throws XmlBlasterException{
      myGetMessagesCalls.inc();
      myGetMessagesParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetMessagesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof XmlBlasterException)
          throw (XmlBlasterException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (MsgUnit[]) nextReturnValue;
   }
   public void setupExceptionGetMessages(Throwable arg){
      myActualGetMessagesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetMessages(MsgUnit[] arg){
      myActualGetMessagesReturnValues.add(arg);
   }
   public void setExpectedEraseMessagesCalls(int calls){
      myEraseMessagesCalls.setExpected(calls);
   }
   public void addExpectedEraseMessagesValues(String arg0){
      myEraseMessagesParameter0Values.addExpected(arg0);
   }
   public EraseReturnQos[] eraseMessages(String arg0) throws XmlBlasterException{
      myEraseMessagesCalls.inc();
      myEraseMessagesParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualEraseMessagesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof XmlBlasterException)
          throw (XmlBlasterException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (EraseReturnQos[]) nextReturnValue;
   }
   public void setupExceptionEraseMessages(Throwable arg){
      myActualEraseMessagesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupEraseMessages(EraseReturnQos[] arg){
      myActualEraseMessagesReturnValues.add(arg);
   }
   public void verify(){
      myConnectCalls.verify();
      myConnectParameter0Values.verify();
      myConnectParameter1Values.verify();
      myDisconnectCalls.verify();
      myDisconnectParameter0Values.verify();
      myPublishStringStringCalls.verify();
      myPublishStringStringParameter0Values.verify();
      myPublishStringStringStringCalls.verify();
      myPublishStringStringStringParameter0Values.verify();
      myPublishStringStringStringParameter1Values.verify();
      myGetMessagesCalls.verify();
      myGetMessagesParameter0Values.verify();
      myEraseMessagesCalls.verify();
      myEraseMessagesParameter0Values.verify();
   }
}
