package mock.w3c.dom;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class MockNode implements Node{
   private ExpectationCounter myGetNodeNameCalls = new ExpectationCounter("org.w3c.dom.Node GetNodeNameCalls");
   private ReturnValues myActualGetNodeNameReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetNodeValueCalls = new ExpectationCounter("org.w3c.dom.Node GetNodeValueCalls");
   private ReturnValues myActualGetNodeValueReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetNodeValueCalls = new ExpectationCounter("org.w3c.dom.Node SetNodeValueCalls");
   private ReturnValues myActualSetNodeValueReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetNodeValueParameter0Values = new ExpectationList("org.w3c.dom.Node java.lang.String");
   private ExpectationCounter myGetNodeTypeCalls = new ExpectationCounter("org.w3c.dom.Node GetNodeTypeCalls");
   private ReturnValues myActualGetNodeTypeReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetParentNodeCalls = new ExpectationCounter("org.w3c.dom.Node GetParentNodeCalls");
   private ReturnValues myActualGetParentNodeReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetChildNodesCalls = new ExpectationCounter("org.w3c.dom.Node GetChildNodesCalls");
   private ReturnValues myActualGetChildNodesReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetFirstChildCalls = new ExpectationCounter("org.w3c.dom.Node GetFirstChildCalls");
   private ReturnValues myActualGetFirstChildReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetLastChildCalls = new ExpectationCounter("org.w3c.dom.Node GetLastChildCalls");
   private ReturnValues myActualGetLastChildReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetPreviousSiblingCalls = new ExpectationCounter("org.w3c.dom.Node GetPreviousSiblingCalls");
   private ReturnValues myActualGetPreviousSiblingReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetNextSiblingCalls = new ExpectationCounter("org.w3c.dom.Node GetNextSiblingCalls");
   private ReturnValues myActualGetNextSiblingReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetAttributesCalls = new ExpectationCounter("org.w3c.dom.Node GetAttributesCalls");
   private ReturnValues myActualGetAttributesReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetOwnerDocumentCalls = new ExpectationCounter("org.w3c.dom.Node GetOwnerDocumentCalls");
   private ReturnValues myActualGetOwnerDocumentReturnValues = new ReturnValues(false);
   private ExpectationCounter myInsertBeforeCalls = new ExpectationCounter("org.w3c.dom.Node InsertBeforeCalls");
   private ReturnValues myActualInsertBeforeReturnValues = new ReturnValues(false);
   private ExpectationList myInsertBeforeParameter0Values = new ExpectationList("org.w3c.dom.Node org.w3c.dom.Node");
   private ExpectationList myInsertBeforeParameter1Values = new ExpectationList("org.w3c.dom.Node org.w3c.dom.Node");
   private ExpectationCounter myReplaceChildCalls = new ExpectationCounter("org.w3c.dom.Node ReplaceChildCalls");
   private ReturnValues myActualReplaceChildReturnValues = new ReturnValues(false);
   private ExpectationList myReplaceChildParameter0Values = new ExpectationList("org.w3c.dom.Node org.w3c.dom.Node");
   private ExpectationList myReplaceChildParameter1Values = new ExpectationList("org.w3c.dom.Node org.w3c.dom.Node");
   private ExpectationCounter myRemoveChildCalls = new ExpectationCounter("org.w3c.dom.Node RemoveChildCalls");
   private ReturnValues myActualRemoveChildReturnValues = new ReturnValues(false);
   private ExpectationList myRemoveChildParameter0Values = new ExpectationList("org.w3c.dom.Node org.w3c.dom.Node");
   private ExpectationCounter myAppendChildCalls = new ExpectationCounter("org.w3c.dom.Node AppendChildCalls");
   private ReturnValues myActualAppendChildReturnValues = new ReturnValues(false);
   private ExpectationList myAppendChildParameter0Values = new ExpectationList("org.w3c.dom.Node org.w3c.dom.Node");
   private ExpectationCounter myHasChildNodesCalls = new ExpectationCounter("org.w3c.dom.Node HasChildNodesCalls");
   private ReturnValues myActualHasChildNodesReturnValues = new ReturnValues(false);
   private ExpectationCounter myCloneNodeCalls = new ExpectationCounter("org.w3c.dom.Node CloneNodeCalls");
   private ReturnValues myActualCloneNodeReturnValues = new ReturnValues(false);
   private ExpectationList myCloneNodeParameter0Values = new ExpectationList("org.w3c.dom.Node boolean");
   private ExpectationCounter myNormalizeCalls = new ExpectationCounter("org.w3c.dom.Node NormalizeCalls");
   private ReturnValues myActualNormalizeReturnValues = new VoidReturnValues(false);
   private ExpectationCounter myIsSupportedCalls = new ExpectationCounter("org.w3c.dom.Node IsSupportedCalls");
   private ReturnValues myActualIsSupportedReturnValues = new ReturnValues(false);
   private ExpectationList myIsSupportedParameter0Values = new ExpectationList("org.w3c.dom.Node java.lang.String");
   private ExpectationList myIsSupportedParameter1Values = new ExpectationList("org.w3c.dom.Node java.lang.String");
   private ExpectationCounter myGetNamespaceURICalls = new ExpectationCounter("org.w3c.dom.Node GetNamespaceURICalls");
   private ReturnValues myActualGetNamespaceURIReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetPrefixCalls = new ExpectationCounter("org.w3c.dom.Node GetPrefixCalls");
   private ReturnValues myActualGetPrefixReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetPrefixCalls = new ExpectationCounter("org.w3c.dom.Node SetPrefixCalls");
   private ReturnValues myActualSetPrefixReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetPrefixParameter0Values = new ExpectationList("org.w3c.dom.Node java.lang.String");
   private ExpectationCounter myGetLocalNameCalls = new ExpectationCounter("org.w3c.dom.Node GetLocalNameCalls");
   private ReturnValues myActualGetLocalNameReturnValues = new ReturnValues(false);
   private ExpectationCounter myHasAttributesCalls = new ExpectationCounter("org.w3c.dom.Node HasAttributesCalls");
   private ReturnValues myActualHasAttributesReturnValues = new ReturnValues(false);
   public void setExpectedGetNodeNameCalls(int calls){
      myGetNodeNameCalls.setExpected(calls);
   }
   public String getNodeName(){
      myGetNodeNameCalls.inc();
      Object nextReturnValue = myActualGetNodeNameReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetNodeName(Throwable arg){
      myActualGetNodeNameReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNodeName(String arg){
      myActualGetNodeNameReturnValues.add(arg);
   }
   public void setExpectedGetNodeValueCalls(int calls){
      myGetNodeValueCalls.setExpected(calls);
   }
   public String getNodeValue() throws DOMException{
      myGetNodeValueCalls.inc();
      Object nextReturnValue = myActualGetNodeValueReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetNodeValue(Throwable arg){
      myActualGetNodeValueReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNodeValue(String arg){
      myActualGetNodeValueReturnValues.add(arg);
   }
   public void setExpectedSetNodeValueCalls(int calls){
      mySetNodeValueCalls.setExpected(calls);
   }
   public void addExpectedSetNodeValueValues(String arg0){
      mySetNodeValueParameter0Values.addExpected(arg0);
   }
   public void setNodeValue(String arg0) throws DOMException{
      mySetNodeValueCalls.inc();
      mySetNodeValueParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualSetNodeValueReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetNodeValue(Throwable arg){
      myActualSetNodeValueReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetNodeTypeCalls(int calls){
      myGetNodeTypeCalls.setExpected(calls);
   }
   public short getNodeType(){
      myGetNodeTypeCalls.inc();
      Object nextReturnValue = myActualGetNodeTypeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Short) nextReturnValue).shortValue();
   }
   public void setupExceptionGetNodeType(Throwable arg){
      myActualGetNodeTypeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNodeType(short arg){
      myActualGetNodeTypeReturnValues.add(new Short(arg));
   }
   public void setExpectedGetParentNodeCalls(int calls){
      myGetParentNodeCalls.setExpected(calls);
   }
   public Node getParentNode(){
      myGetParentNodeCalls.inc();
      Object nextReturnValue = myActualGetParentNodeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionGetParentNode(Throwable arg){
      myActualGetParentNodeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetParentNode(Node arg){
      myActualGetParentNodeReturnValues.add(arg);
   }
   public void setExpectedGetChildNodesCalls(int calls){
      myGetChildNodesCalls.setExpected(calls);
   }
   public NodeList getChildNodes(){
      myGetChildNodesCalls.inc();
      Object nextReturnValue = myActualGetChildNodesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (NodeList) nextReturnValue;
   }
   public void setupExceptionGetChildNodes(Throwable arg){
      myActualGetChildNodesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetChildNodes(NodeList arg){
      myActualGetChildNodesReturnValues.add(arg);
   }
   public void setExpectedGetFirstChildCalls(int calls){
      myGetFirstChildCalls.setExpected(calls);
   }
   public Node getFirstChild(){
      myGetFirstChildCalls.inc();
      Object nextReturnValue = myActualGetFirstChildReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionGetFirstChild(Throwable arg){
      myActualGetFirstChildReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetFirstChild(Node arg){
      myActualGetFirstChildReturnValues.add(arg);
   }
   public void setExpectedGetLastChildCalls(int calls){
      myGetLastChildCalls.setExpected(calls);
   }
   public Node getLastChild(){
      myGetLastChildCalls.inc();
      Object nextReturnValue = myActualGetLastChildReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionGetLastChild(Throwable arg){
      myActualGetLastChildReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetLastChild(Node arg){
      myActualGetLastChildReturnValues.add(arg);
   }
   public void setExpectedGetPreviousSiblingCalls(int calls){
      myGetPreviousSiblingCalls.setExpected(calls);
   }
   public Node getPreviousSibling(){
      myGetPreviousSiblingCalls.inc();
      Object nextReturnValue = myActualGetPreviousSiblingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionGetPreviousSibling(Throwable arg){
      myActualGetPreviousSiblingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetPreviousSibling(Node arg){
      myActualGetPreviousSiblingReturnValues.add(arg);
   }
   public void setExpectedGetNextSiblingCalls(int calls){
      myGetNextSiblingCalls.setExpected(calls);
   }
   public Node getNextSibling(){
      myGetNextSiblingCalls.inc();
      Object nextReturnValue = myActualGetNextSiblingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionGetNextSibling(Throwable arg){
      myActualGetNextSiblingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNextSibling(Node arg){
      myActualGetNextSiblingReturnValues.add(arg);
   }
   public void setExpectedGetAttributesCalls(int calls){
      myGetAttributesCalls.setExpected(calls);
   }
   public NamedNodeMap getAttributes(){
      myGetAttributesCalls.inc();
      Object nextReturnValue = myActualGetAttributesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (NamedNodeMap) nextReturnValue;
   }
   public void setupExceptionGetAttributes(Throwable arg){
      myActualGetAttributesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttributes(NamedNodeMap arg){
      myActualGetAttributesReturnValues.add(arg);
   }
   public void setExpectedGetOwnerDocumentCalls(int calls){
      myGetOwnerDocumentCalls.setExpected(calls);
   }
   public Document getOwnerDocument(){
      myGetOwnerDocumentCalls.inc();
      Object nextReturnValue = myActualGetOwnerDocumentReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Document) nextReturnValue;
   }
   public void setupExceptionGetOwnerDocument(Throwable arg){
      myActualGetOwnerDocumentReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetOwnerDocument(Document arg){
      myActualGetOwnerDocumentReturnValues.add(arg);
   }
   public void setExpectedInsertBeforeCalls(int calls){
      myInsertBeforeCalls.setExpected(calls);
   }
   public void addExpectedInsertBeforeValues(Node arg0, Node arg1){
      myInsertBeforeParameter0Values.addExpected(arg0);
      myInsertBeforeParameter1Values.addExpected(arg1);
   }
   public Node insertBefore(Node arg0, Node arg1) throws DOMException{
      myInsertBeforeCalls.inc();
      myInsertBeforeParameter0Values.addActual(arg0);
      myInsertBeforeParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualInsertBeforeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionInsertBefore(Throwable arg){
      myActualInsertBeforeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupInsertBefore(Node arg){
      myActualInsertBeforeReturnValues.add(arg);
   }
   public void setExpectedReplaceChildCalls(int calls){
      myReplaceChildCalls.setExpected(calls);
   }
   public void addExpectedReplaceChildValues(Node arg0, Node arg1){
      myReplaceChildParameter0Values.addExpected(arg0);
      myReplaceChildParameter1Values.addExpected(arg1);
   }
   public Node replaceChild(Node arg0, Node arg1) throws DOMException{
      myReplaceChildCalls.inc();
      myReplaceChildParameter0Values.addActual(arg0);
      myReplaceChildParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualReplaceChildReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionReplaceChild(Throwable arg){
      myActualReplaceChildReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupReplaceChild(Node arg){
      myActualReplaceChildReturnValues.add(arg);
   }
   public void setExpectedRemoveChildCalls(int calls){
      myRemoveChildCalls.setExpected(calls);
   }
   public void addExpectedRemoveChildValues(Node arg0){
      myRemoveChildParameter0Values.addExpected(arg0);
   }
   public Node removeChild(Node arg0) throws DOMException{
      myRemoveChildCalls.inc();
      myRemoveChildParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualRemoveChildReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionRemoveChild(Throwable arg){
      myActualRemoveChildReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupRemoveChild(Node arg){
      myActualRemoveChildReturnValues.add(arg);
   }
   public void setExpectedAppendChildCalls(int calls){
      myAppendChildCalls.setExpected(calls);
   }
   public void addExpectedAppendChildValues(Node arg0){
      myAppendChildParameter0Values.addExpected(arg0);
   }
   public Node appendChild(Node arg0) throws DOMException{
      myAppendChildCalls.inc();
      myAppendChildParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualAppendChildReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionAppendChild(Throwable arg){
      myActualAppendChildReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupAppendChild(Node arg){
      myActualAppendChildReturnValues.add(arg);
   }
   public void setExpectedHasChildNodesCalls(int calls){
      myHasChildNodesCalls.setExpected(calls);
   }
   public boolean hasChildNodes(){
      myHasChildNodesCalls.inc();
      Object nextReturnValue = myActualHasChildNodesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionHasChildNodes(Throwable arg){
      myActualHasChildNodesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupHasChildNodes(boolean arg){
      myActualHasChildNodesReturnValues.add(new Boolean(arg));
   }
   public void setExpectedCloneNodeCalls(int calls){
      myCloneNodeCalls.setExpected(calls);
   }
   public void addExpectedCloneNodeValues(boolean arg0){
      myCloneNodeParameter0Values.addExpected(new Boolean(arg0));
   }
   public Node cloneNode(boolean arg0){
      myCloneNodeCalls.inc();
      myCloneNodeParameter0Values.addActual(new Boolean(arg0));
      Object nextReturnValue = myActualCloneNodeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Node) nextReturnValue;
   }
   public void setupExceptionCloneNode(Throwable arg){
      myActualCloneNodeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupCloneNode(Node arg){
      myActualCloneNodeReturnValues.add(arg);
   }
   public void setExpectedNormalizeCalls(int calls){
      myNormalizeCalls.setExpected(calls);
   }
   public void normalize(){
      myNormalizeCalls.inc();
      Object nextReturnValue = myActualNormalizeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionNormalize(Throwable arg){
      myActualNormalizeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedIsSupportedCalls(int calls){
      myIsSupportedCalls.setExpected(calls);
   }
   public void addExpectedIsSupportedValues(String arg0, String arg1){
      myIsSupportedParameter0Values.addExpected(arg0);
      myIsSupportedParameter1Values.addExpected(arg1);
   }
   public boolean isSupported(String arg0, String arg1){
      myIsSupportedCalls.inc();
      myIsSupportedParameter0Values.addActual(arg0);
      myIsSupportedParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualIsSupportedReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsSupported(Throwable arg){
      myActualIsSupportedReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsSupported(boolean arg){
      myActualIsSupportedReturnValues.add(new Boolean(arg));
   }
   public void setExpectedGetNamespaceURICalls(int calls){
      myGetNamespaceURICalls.setExpected(calls);
   }
   public String getNamespaceURI(){
      myGetNamespaceURICalls.inc();
      Object nextReturnValue = myActualGetNamespaceURIReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetNamespaceURI(Throwable arg){
      myActualGetNamespaceURIReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNamespaceURI(String arg){
      myActualGetNamespaceURIReturnValues.add(arg);
   }
   public void setExpectedGetPrefixCalls(int calls){
      myGetPrefixCalls.setExpected(calls);
   }
   public String getPrefix(){
      myGetPrefixCalls.inc();
      Object nextReturnValue = myActualGetPrefixReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetPrefix(Throwable arg){
      myActualGetPrefixReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetPrefix(String arg){
      myActualGetPrefixReturnValues.add(arg);
   }
   public void setExpectedSetPrefixCalls(int calls){
      mySetPrefixCalls.setExpected(calls);
   }
   public void addExpectedSetPrefixValues(String arg0){
      mySetPrefixParameter0Values.addExpected(arg0);
   }
   public void setPrefix(String arg0) throws DOMException{
      mySetPrefixCalls.inc();
      mySetPrefixParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualSetPrefixReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof DOMException)
          throw (DOMException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetPrefix(Throwable arg){
      myActualSetPrefixReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetLocalNameCalls(int calls){
      myGetLocalNameCalls.setExpected(calls);
   }
   public String getLocalName(){
      myGetLocalNameCalls.inc();
      Object nextReturnValue = myActualGetLocalNameReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetLocalName(Throwable arg){
      myActualGetLocalNameReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetLocalName(String arg){
      myActualGetLocalNameReturnValues.add(arg);
   }
   public void setExpectedHasAttributesCalls(int calls){
      myHasAttributesCalls.setExpected(calls);
   }
   public boolean hasAttributes(){
      myHasAttributesCalls.inc();
      Object nextReturnValue = myActualHasAttributesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionHasAttributes(Throwable arg){
      myActualHasAttributesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupHasAttributes(boolean arg){
      myActualHasAttributesReturnValues.add(new Boolean(arg));
   }
   public void verify(){
      myGetNodeNameCalls.verify();
      myGetNodeValueCalls.verify();
      mySetNodeValueCalls.verify();
      mySetNodeValueParameter0Values.verify();
      myGetNodeTypeCalls.verify();
      myGetParentNodeCalls.verify();
      myGetChildNodesCalls.verify();
      myGetFirstChildCalls.verify();
      myGetLastChildCalls.verify();
      myGetPreviousSiblingCalls.verify();
      myGetNextSiblingCalls.verify();
      myGetAttributesCalls.verify();
      myGetOwnerDocumentCalls.verify();
      myInsertBeforeCalls.verify();
      myInsertBeforeParameter0Values.verify();
      myInsertBeforeParameter1Values.verify();
      myReplaceChildCalls.verify();
      myReplaceChildParameter0Values.verify();
      myReplaceChildParameter1Values.verify();
      myRemoveChildCalls.verify();
      myRemoveChildParameter0Values.verify();
      myAppendChildCalls.verify();
      myAppendChildParameter0Values.verify();
      myHasChildNodesCalls.verify();
      myCloneNodeCalls.verify();
      myCloneNodeParameter0Values.verify();
      myNormalizeCalls.verify();
      myIsSupportedCalls.verify();
      myIsSupportedParameter0Values.verify();
      myIsSupportedParameter1Values.verify();
      myGetNamespaceURICalls.verify();
      myGetPrefixCalls.verify();
      mySetPrefixCalls.verify();
      mySetPrefixParameter0Values.verify();
      myGetLocalNameCalls.verify();
      myHasAttributesCalls.verify();
   }
}
