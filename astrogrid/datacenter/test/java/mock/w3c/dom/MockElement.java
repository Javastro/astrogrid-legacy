package mock.w3c.dom;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
public class MockElement extends MockNode implements Element{
   private ExpectationCounter myGetTagNameCalls = new ExpectationCounter("org.w3c.dom.Element GetTagNameCalls");
   private ReturnValues myActualGetTagNameReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetAttributeCalls = new ExpectationCounter("org.w3c.dom.Element GetAttributeCalls");
   private ReturnValues myActualGetAttributeReturnValues = new ReturnValues(false);
   private ExpectationList myGetAttributeParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter mySetAttributeCalls = new ExpectationCounter("org.w3c.dom.Element SetAttributeCalls");
   private ReturnValues myActualSetAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetAttributeParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList mySetAttributeParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myRemoveAttributeCalls = new ExpectationCounter("org.w3c.dom.Element RemoveAttributeCalls");
   private ReturnValues myActualRemoveAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList myRemoveAttributeParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myGetAttributeNodeCalls = new ExpectationCounter("org.w3c.dom.Element GetAttributeNodeCalls");
   private ReturnValues myActualGetAttributeNodeReturnValues = new ReturnValues(false);
   private ExpectationList myGetAttributeNodeParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter mySetAttributeNodeCalls = new ExpectationCounter("org.w3c.dom.Element SetAttributeNodeCalls");
   private ReturnValues myActualSetAttributeNodeReturnValues = new ReturnValues(false);
   private ExpectationList mySetAttributeNodeParameter0Values = new ExpectationList("org.w3c.dom.Element org.w3c.dom.Attr");
   private ExpectationCounter myRemoveAttributeNodeCalls = new ExpectationCounter("org.w3c.dom.Element RemoveAttributeNodeCalls");
   private ReturnValues myActualRemoveAttributeNodeReturnValues = new ReturnValues(false);
   private ExpectationList myRemoveAttributeNodeParameter0Values = new ExpectationList("org.w3c.dom.Element org.w3c.dom.Attr");
   private ExpectationCounter myGetElementsByTagNameCalls = new ExpectationCounter("org.w3c.dom.Element GetElementsByTagNameCalls");
   private ReturnValues myActualGetElementsByTagNameReturnValues = new ReturnValues(false);
   private ExpectationList myGetElementsByTagNameParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myGetAttributeNSCalls = new ExpectationCounter("org.w3c.dom.Element GetAttributeNSCalls");
   private ReturnValues myActualGetAttributeNSReturnValues = new ReturnValues(false);
   private ExpectationList myGetAttributeNSParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList myGetAttributeNSParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter mySetAttributeNSCalls = new ExpectationCounter("org.w3c.dom.Element SetAttributeNSCalls");
   private ReturnValues myActualSetAttributeNSReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetAttributeNSParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList mySetAttributeNSParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList mySetAttributeNSParameter2Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myRemoveAttributeNSCalls = new ExpectationCounter("org.w3c.dom.Element RemoveAttributeNSCalls");
   private ReturnValues myActualRemoveAttributeNSReturnValues = new VoidReturnValues(false);
   private ExpectationList myRemoveAttributeNSParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList myRemoveAttributeNSParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myGetAttributeNodeNSCalls = new ExpectationCounter("org.w3c.dom.Element GetAttributeNodeNSCalls");
   private ReturnValues myActualGetAttributeNodeNSReturnValues = new ReturnValues(false);
   private ExpectationList myGetAttributeNodeNSParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList myGetAttributeNodeNSParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter mySetAttributeNodeNSCalls = new ExpectationCounter("org.w3c.dom.Element SetAttributeNodeNSCalls");
   private ReturnValues myActualSetAttributeNodeNSReturnValues = new ReturnValues(false);
   private ExpectationList mySetAttributeNodeNSParameter0Values = new ExpectationList("org.w3c.dom.Element org.w3c.dom.Attr");
   private ExpectationCounter myGetElementsByTagNameNSCalls = new ExpectationCounter("org.w3c.dom.Element GetElementsByTagNameNSCalls");
   private ReturnValues myActualGetElementsByTagNameNSReturnValues = new ReturnValues(false);
   private ExpectationList myGetElementsByTagNameNSParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList myGetElementsByTagNameNSParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myHasAttributeCalls = new ExpectationCounter("org.w3c.dom.Element HasAttributeCalls");
   private ReturnValues myActualHasAttributeReturnValues = new ReturnValues(false);
   private ExpectationList myHasAttributeParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationCounter myHasAttributeNSCalls = new ExpectationCounter("org.w3c.dom.Element HasAttributeNSCalls");
   private ReturnValues myActualHasAttributeNSReturnValues = new ReturnValues(false);
   private ExpectationList myHasAttributeNSParameter0Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   private ExpectationList myHasAttributeNSParameter1Values = new ExpectationList("org.w3c.dom.Element java.lang.String");
   public void setExpectedGetTagNameCalls(int calls){
      myGetTagNameCalls.setExpected(calls);
   }
   public String getTagName(){
      myGetTagNameCalls.inc();
      Object nextReturnValue = myActualGetTagNameReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetTagName(Throwable arg){
      myActualGetTagNameReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetTagName(String arg){
      myActualGetTagNameReturnValues.add(arg);
   }
   public void setExpectedGetAttributeCalls(int calls){
      myGetAttributeCalls.setExpected(calls);
   }
   public void addExpectedGetAttributeValues(String arg0){
      myGetAttributeParameter0Values.addExpected(arg0);
   }
   public String getAttribute(String arg0){
      myGetAttributeCalls.inc();
      myGetAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetAttribute(Throwable arg){
      myActualGetAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttribute(String arg){
      myActualGetAttributeReturnValues.add(arg);
   }
   public void setExpectedSetAttributeCalls(int calls){
      mySetAttributeCalls.setExpected(calls);
   }
   public void addExpectedSetAttributeValues(String arg0, String arg1){
      mySetAttributeParameter0Values.addExpected(arg0);
      mySetAttributeParameter1Values.addExpected(arg1);
   }
   public void setAttribute(String arg0, String arg1) throws DOMException{
      mySetAttributeCalls.inc();
      mySetAttributeParameter0Values.addActual(arg0);
      mySetAttributeParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualSetAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
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
   public void removeAttribute(String arg0) throws DOMException{
      myRemoveAttributeCalls.inc();
      myRemoveAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualRemoveAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionRemoveAttribute(Throwable arg){
      myActualRemoveAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetAttributeNodeCalls(int calls){
      myGetAttributeNodeCalls.setExpected(calls);
   }
   public void addExpectedGetAttributeNodeValues(String arg0){
      myGetAttributeNodeParameter0Values.addExpected(arg0);
   }
   public Attr getAttributeNode(String arg0){
      myGetAttributeNodeCalls.inc();
      myGetAttributeNodeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetAttributeNodeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Attr) nextReturnValue;
   }
   public void setupExceptionGetAttributeNode(Throwable arg){
      myActualGetAttributeNodeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttributeNode(Attr arg){
      myActualGetAttributeNodeReturnValues.add(arg);
   }
   public void setExpectedSetAttributeNodeCalls(int calls){
      mySetAttributeNodeCalls.setExpected(calls);
   }
   public void addExpectedSetAttributeNodeValues(Attr arg0){
      mySetAttributeNodeParameter0Values.addExpected(arg0);
   }
   public Attr setAttributeNode(Attr arg0) throws DOMException{
      mySetAttributeNodeCalls.inc();
      mySetAttributeNodeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualSetAttributeNodeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Attr) nextReturnValue;
   }
   public void setupExceptionSetAttributeNode(Throwable arg){
      myActualSetAttributeNodeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupSetAttributeNode(Attr arg){
      myActualSetAttributeNodeReturnValues.add(arg);
   }
   public void setExpectedRemoveAttributeNodeCalls(int calls){
      myRemoveAttributeNodeCalls.setExpected(calls);
   }
   public void addExpectedRemoveAttributeNodeValues(Attr arg0){
      myRemoveAttributeNodeParameter0Values.addExpected(arg0);
   }
   public Attr removeAttributeNode(Attr arg0) throws DOMException{
      myRemoveAttributeNodeCalls.inc();
      myRemoveAttributeNodeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualRemoveAttributeNodeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Attr) nextReturnValue;
   }
   public void setupExceptionRemoveAttributeNode(Throwable arg){
      myActualRemoveAttributeNodeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupRemoveAttributeNode(Attr arg){
      myActualRemoveAttributeNodeReturnValues.add(arg);
   }
   public void setExpectedGetElementsByTagNameCalls(int calls){
      myGetElementsByTagNameCalls.setExpected(calls);
   }
   public void addExpectedGetElementsByTagNameValues(String arg0){
      myGetElementsByTagNameParameter0Values.addExpected(arg0);
   }
   public NodeList getElementsByTagName(String arg0){
      myGetElementsByTagNameCalls.inc();
      myGetElementsByTagNameParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetElementsByTagNameReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (NodeList) nextReturnValue;
   }
   public void setupExceptionGetElementsByTagName(Throwable arg){
      myActualGetElementsByTagNameReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetElementsByTagName(NodeList arg){
      myActualGetElementsByTagNameReturnValues.add(arg);
   }
   public void setExpectedGetAttributeNSCalls(int calls){
      myGetAttributeNSCalls.setExpected(calls);
   }
   public void addExpectedGetAttributeNSValues(String arg0, String arg1){
      myGetAttributeNSParameter0Values.addExpected(arg0);
      myGetAttributeNSParameter1Values.addExpected(arg1);
   }
   public String getAttributeNS(String arg0, String arg1){
      myGetAttributeNSCalls.inc();
      myGetAttributeNSParameter0Values.addActual(arg0);
      myGetAttributeNSParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualGetAttributeNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetAttributeNS(Throwable arg){
      myActualGetAttributeNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttributeNS(String arg){
      myActualGetAttributeNSReturnValues.add(arg);
   }
   public void setExpectedSetAttributeNSCalls(int calls){
      mySetAttributeNSCalls.setExpected(calls);
   }
   public void addExpectedSetAttributeNSValues(String arg0, String arg1, String arg2){
      mySetAttributeNSParameter0Values.addExpected(arg0);
      mySetAttributeNSParameter1Values.addExpected(arg1);
      mySetAttributeNSParameter2Values.addExpected(arg2);
   }
   public void setAttributeNS(String arg0, String arg1, String arg2) throws DOMException{
      mySetAttributeNSCalls.inc();
      mySetAttributeNSParameter0Values.addActual(arg0);
      mySetAttributeNSParameter1Values.addActual(arg1);
      mySetAttributeNSParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualSetAttributeNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetAttributeNS(Throwable arg){
      myActualSetAttributeNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedRemoveAttributeNSCalls(int calls){
      myRemoveAttributeNSCalls.setExpected(calls);
   }
   public void addExpectedRemoveAttributeNSValues(String arg0, String arg1){
      myRemoveAttributeNSParameter0Values.addExpected(arg0);
      myRemoveAttributeNSParameter1Values.addExpected(arg1);
   }
   public void removeAttributeNS(String arg0, String arg1) throws DOMException{
      myRemoveAttributeNSCalls.inc();
      myRemoveAttributeNSParameter0Values.addActual(arg0);
      myRemoveAttributeNSParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualRemoveAttributeNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionRemoveAttributeNS(Throwable arg){
      myActualRemoveAttributeNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetAttributeNodeNSCalls(int calls){
      myGetAttributeNodeNSCalls.setExpected(calls);
   }
   public void addExpectedGetAttributeNodeNSValues(String arg0, String arg1){
      myGetAttributeNodeNSParameter0Values.addExpected(arg0);
      myGetAttributeNodeNSParameter1Values.addExpected(arg1);
   }
   public Attr getAttributeNodeNS(String arg0, String arg1){
      myGetAttributeNodeNSCalls.inc();
      myGetAttributeNodeNSParameter0Values.addActual(arg0);
      myGetAttributeNodeNSParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualGetAttributeNodeNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Attr) nextReturnValue;
   }
   public void setupExceptionGetAttributeNodeNS(Throwable arg){
      myActualGetAttributeNodeNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttributeNodeNS(Attr arg){
      myActualGetAttributeNodeNSReturnValues.add(arg);
   }
   public void setExpectedSetAttributeNodeNSCalls(int calls){
      mySetAttributeNodeNSCalls.setExpected(calls);
   }
   public void addExpectedSetAttributeNodeNSValues(Attr arg0){
      mySetAttributeNodeNSParameter0Values.addExpected(arg0);
   }
   public Attr setAttributeNodeNS(Attr arg0) throws DOMException{
      mySetAttributeNodeNSCalls.inc();
      mySetAttributeNodeNSParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualSetAttributeNodeNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Attr) nextReturnValue;
   }
   public void setupExceptionSetAttributeNodeNS(Throwable arg){
      myActualSetAttributeNodeNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupSetAttributeNodeNS(Attr arg){
      myActualSetAttributeNodeNSReturnValues.add(arg);
   }
   public void setExpectedGetElementsByTagNameNSCalls(int calls){
      myGetElementsByTagNameNSCalls.setExpected(calls);
   }
   public void addExpectedGetElementsByTagNameNSValues(String arg0, String arg1){
      myGetElementsByTagNameNSParameter0Values.addExpected(arg0);
      myGetElementsByTagNameNSParameter1Values.addExpected(arg1);
   }
   public NodeList getElementsByTagNameNS(String arg0, String arg1){
      myGetElementsByTagNameNSCalls.inc();
      myGetElementsByTagNameNSParameter0Values.addActual(arg0);
      myGetElementsByTagNameNSParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualGetElementsByTagNameNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (NodeList) nextReturnValue;
   }
   public void setupExceptionGetElementsByTagNameNS(Throwable arg){
      myActualGetElementsByTagNameNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetElementsByTagNameNS(NodeList arg){
      myActualGetElementsByTagNameNSReturnValues.add(arg);
   }
   public void setExpectedHasAttributeCalls(int calls){
      myHasAttributeCalls.setExpected(calls);
   }
   public void addExpectedHasAttributeValues(String arg0){
      myHasAttributeParameter0Values.addExpected(arg0);
   }
   public boolean hasAttribute(String arg0){
      myHasAttributeCalls.inc();
      myHasAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualHasAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionHasAttribute(Throwable arg){
      myActualHasAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupHasAttribute(boolean arg){
      myActualHasAttributeReturnValues.add(new Boolean(arg));
   }
   public void setExpectedHasAttributeNSCalls(int calls){
      myHasAttributeNSCalls.setExpected(calls);
   }
   public void addExpectedHasAttributeNSValues(String arg0, String arg1){
      myHasAttributeNSParameter0Values.addExpected(arg0);
      myHasAttributeNSParameter1Values.addExpected(arg1);
   }
   public boolean hasAttributeNS(String arg0, String arg1){
      myHasAttributeNSCalls.inc();
      myHasAttributeNSParameter0Values.addActual(arg0);
      myHasAttributeNSParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualHasAttributeNSReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionHasAttributeNS(Throwable arg){
      myActualHasAttributeNSReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupHasAttributeNS(boolean arg){
      myActualHasAttributeNSReturnValues.add(new Boolean(arg));
   }
   public void verify(){
      myGetTagNameCalls.verify();
      myGetAttributeCalls.verify();
      myGetAttributeParameter0Values.verify();
      mySetAttributeCalls.verify();
      mySetAttributeParameter0Values.verify();
      mySetAttributeParameter1Values.verify();
      myRemoveAttributeCalls.verify();
      myRemoveAttributeParameter0Values.verify();
      myGetAttributeNodeCalls.verify();
      myGetAttributeNodeParameter0Values.verify();
      mySetAttributeNodeCalls.verify();
      mySetAttributeNodeParameter0Values.verify();
      myRemoveAttributeNodeCalls.verify();
      myRemoveAttributeNodeParameter0Values.verify();
      myGetElementsByTagNameCalls.verify();
      myGetElementsByTagNameParameter0Values.verify();
      myGetAttributeNSCalls.verify();
      myGetAttributeNSParameter0Values.verify();
      myGetAttributeNSParameter1Values.verify();
      mySetAttributeNSCalls.verify();
      mySetAttributeNSParameter0Values.verify();
      mySetAttributeNSParameter1Values.verify();
      mySetAttributeNSParameter2Values.verify();
      myRemoveAttributeNSCalls.verify();
      myRemoveAttributeNSParameter0Values.verify();
      myRemoveAttributeNSParameter1Values.verify();
      myGetAttributeNodeNSCalls.verify();
      myGetAttributeNodeNSParameter0Values.verify();
      myGetAttributeNodeNSParameter1Values.verify();
      mySetAttributeNodeNSCalls.verify();
      mySetAttributeNodeNSParameter0Values.verify();
      myGetElementsByTagNameNSCalls.verify();
      myGetElementsByTagNameNSParameter0Values.verify();
      myGetElementsByTagNameNSParameter1Values.verify();
      myHasAttributeCalls.verify();
      myHasAttributeParameter0Values.verify();
      myHasAttributeNSCalls.verify();
      myHasAttributeNSParameter0Values.verify();
      myHasAttributeNSParameter1Values.verify();
   }
}
