package mock.astrogrid.datacenter.cocoon.action;

import mockmaker.ReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;

import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtils;
import org.astrogrid.datacenter.cocoon.acting.utils.ValidationHandler;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import java.util.List;
import org.astrogrid.common.creator.CreatorException;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
public class MockActionUtils implements ActionUtils{
   private ExpectationCounter myGetRequestParameterStringStringParametersRequestCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils GetRequestParameterStringStringParametersRequestCalls");
   private ReturnValues myActualGetRequestParameterStringStringParametersRequestReturnValues = new ReturnValues(false);
   private ExpectationList myGetRequestParameterStringStringParametersRequestParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationList myGetRequestParameterStringStringParametersRequestParameter1Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationList myGetRequestParameterStringStringParametersRequestParameter2Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.avalon.framework.parameters.Parameters");
   private ExpectationList myGetRequestParameterStringStringParametersRequestParameter3Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.cocoon.environment.Request");
   private ExpectationCounter myGetRequestParameterStringParametersRequestCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils GetRequestParameterStringParametersRequestCalls");
   private ReturnValues myActualGetRequestParameterStringParametersRequestReturnValues = new ReturnValues(false);
   private ExpectationList myGetRequestParameterStringParametersRequestParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationList myGetRequestParameterStringParametersRequestParameter1Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.avalon.framework.parameters.Parameters");
   private ExpectationList myGetRequestParameterStringParametersRequestParameter2Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.cocoon.environment.Request");
   private ExpectationCounter myGetNewObjectStringParametersRequestListCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils GetNewObjectStringParametersRequestListCalls");
   private ReturnValues myActualGetNewObjectStringParametersRequestListReturnValues = new ReturnValues(false);
   private ExpectationList myGetNewObjectStringParametersRequestListParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationList myGetNewObjectStringParametersRequestListParameter1Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.avalon.framework.parameters.Parameters");
   private ExpectationList myGetNewObjectStringParametersRequestListParameter2Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.cocoon.environment.Request");
   private ExpectationList myGetNewObjectStringParametersRequestListParameter3Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.util.List");
   private ExpectationCounter myGetNewObjectStringStringParametersRequestListCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils GetNewObjectStringStringParametersRequestListCalls");
   private ReturnValues myActualGetNewObjectStringStringParametersRequestListReturnValues = new ReturnValues(false);
   private ExpectationList myGetNewObjectStringStringParametersRequestListParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationList myGetNewObjectStringStringParametersRequestListParameter1Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationList myGetNewObjectStringStringParametersRequestListParameter2Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.avalon.framework.parameters.Parameters");
   private ExpectationList myGetNewObjectStringStringParametersRequestListParameter3Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils org.apache.cocoon.environment.Request");
   private ExpectationList myGetNewObjectStringStringParametersRequestListParameter4Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.util.List");
   private ExpectationCounter myGetDomElementCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils GetDomElementCalls");
   private ReturnValues myActualGetDomElementReturnValues = new ReturnValues(false);
   private ExpectationList myGetDomElementParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationCounter myValidateCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils ValidateCalls");
   private ReturnValues myActualValidateReturnValues = new ReturnValues(false);
   private ExpectationList myValidateParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationCounter myShouldValidateCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils ShouldValidateCalls");
   private ReturnValues myActualShouldValidateReturnValues = new ReturnValues(false);
   private ExpectationList myShouldValidateParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   private ExpectationCounter myShouldBeNSAwareCalls = new ExpectationCounter("org.astrogrid.datacenter.cocoon.action.ActionUtils ShouldBeNSAwareCalls");
   private ReturnValues myActualShouldBeNSAwareReturnValues = new ReturnValues(false);
   private ExpectationList myShouldBeNSAwareParameter0Values = new ExpectationList("org.astrogrid.datacenter.cocoon.action.ActionUtils java.lang.String");
   public void setExpectedGetRequestParameterStringStringParametersRequestCalls(int calls){
      myGetRequestParameterStringStringParametersRequestCalls.setExpected(calls);
   }
   public void addExpectedGetRequestParameterStringStringParametersRequestValues(String arg0, String arg1, Parameters arg2, Request arg3){
      myGetRequestParameterStringStringParametersRequestParameter0Values.addExpected(arg0);
      myGetRequestParameterStringStringParametersRequestParameter1Values.addExpected(arg1);
      myGetRequestParameterStringStringParametersRequestParameter2Values.addExpected(arg2);
      myGetRequestParameterStringStringParametersRequestParameter3Values.addExpected(arg3);
   }
   public String getRequestParameter(String arg0, String arg1, Parameters arg2, Request arg3){
      myGetRequestParameterStringStringParametersRequestCalls.inc();
      myGetRequestParameterStringStringParametersRequestParameter0Values.addActual(arg0);
      myGetRequestParameterStringStringParametersRequestParameter1Values.addActual(arg1);
      myGetRequestParameterStringStringParametersRequestParameter2Values.addActual(arg2);
      myGetRequestParameterStringStringParametersRequestParameter3Values.addActual(arg3);
      Object nextReturnValue = myActualGetRequestParameterStringStringParametersRequestReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRequestParameterStringStringParametersRequest(Throwable arg){
      myActualGetRequestParameterStringStringParametersRequestReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRequestParameterStringStringParametersRequest(String arg){
      myActualGetRequestParameterStringStringParametersRequestReturnValues.add(arg);
   }
   public void setExpectedGetRequestParameterStringParametersRequestCalls(int calls){
      myGetRequestParameterStringParametersRequestCalls.setExpected(calls);
   }
   public void addExpectedGetRequestParameterStringParametersRequestValues(String arg0, Parameters arg1, Request arg2){
      myGetRequestParameterStringParametersRequestParameter0Values.addExpected(arg0);
      myGetRequestParameterStringParametersRequestParameter1Values.addExpected(arg1);
      myGetRequestParameterStringParametersRequestParameter2Values.addExpected(arg2);
   }
   public String getRequestParameter(String arg0, Parameters arg1, Request arg2){
      myGetRequestParameterStringParametersRequestCalls.inc();
      myGetRequestParameterStringParametersRequestParameter0Values.addActual(arg0);
      myGetRequestParameterStringParametersRequestParameter1Values.addActual(arg1);
      myGetRequestParameterStringParametersRequestParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualGetRequestParameterStringParametersRequestReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRequestParameterStringParametersRequest(Throwable arg){
      myActualGetRequestParameterStringParametersRequestReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRequestParameterStringParametersRequest(String arg){
      myActualGetRequestParameterStringParametersRequestReturnValues.add(arg);
   }
   public void setExpectedGetNewObjectStringParametersRequestListCalls(int calls){
      myGetNewObjectStringParametersRequestListCalls.setExpected(calls);
   }
   public void addExpectedGetNewObjectStringParametersRequestListValues(String arg0, Parameters arg1, Request arg2, List arg3){
      myGetNewObjectStringParametersRequestListParameter0Values.addExpected(arg0);
      myGetNewObjectStringParametersRequestListParameter1Values.addExpected(arg1);
      myGetNewObjectStringParametersRequestListParameter2Values.addExpected(arg2);
      myGetNewObjectStringParametersRequestListParameter3Values.addExpected(arg3);
   }
   public Object getNewObject(String arg0, Parameters arg1, Request arg2, List arg3) throws CreatorException{
      myGetNewObjectStringParametersRequestListCalls.inc();
      myGetNewObjectStringParametersRequestListParameter0Values.addActual(arg0);
      myGetNewObjectStringParametersRequestListParameter1Values.addActual(arg1);
      myGetNewObjectStringParametersRequestListParameter2Values.addActual(arg2);
      myGetNewObjectStringParametersRequestListParameter3Values.addActual(arg3);
      Object nextReturnValue = myActualGetNewObjectStringParametersRequestListReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof CreatorException)
          throw (CreatorException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Object) nextReturnValue;
   }
   public void setupExceptionGetNewObjectStringParametersRequestList(Throwable arg){
      myActualGetNewObjectStringParametersRequestListReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNewObjectStringParametersRequestList(Object arg){
      myActualGetNewObjectStringParametersRequestListReturnValues.add(arg);
   }
   public void setExpectedGetNewObjectStringStringParametersRequestListCalls(int calls){
      myGetNewObjectStringStringParametersRequestListCalls.setExpected(calls);
   }
   public void addExpectedGetNewObjectStringStringParametersRequestListValues(String arg0, String arg1, Parameters arg2, Request arg3, List arg4){
      myGetNewObjectStringStringParametersRequestListParameter0Values.addExpected(arg0);
      myGetNewObjectStringStringParametersRequestListParameter1Values.addExpected(arg1);
      myGetNewObjectStringStringParametersRequestListParameter2Values.addExpected(arg2);
      myGetNewObjectStringStringParametersRequestListParameter3Values.addExpected(arg3);
      myGetNewObjectStringStringParametersRequestListParameter4Values.addExpected(arg4);
   }
   public Object getNewObject(String arg0, String arg1, Parameters arg2, Request arg3, List arg4) throws CreatorException{
      myGetNewObjectStringStringParametersRequestListCalls.inc();
      myGetNewObjectStringStringParametersRequestListParameter0Values.addActual(arg0);
      myGetNewObjectStringStringParametersRequestListParameter1Values.addActual(arg1);
      myGetNewObjectStringStringParametersRequestListParameter2Values.addActual(arg2);
      myGetNewObjectStringStringParametersRequestListParameter3Values.addActual(arg3);
      myGetNewObjectStringStringParametersRequestListParameter4Values.addActual(arg4);
      Object nextReturnValue = myActualGetNewObjectStringStringParametersRequestListReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof CreatorException)
          throw (CreatorException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Object) nextReturnValue;
   }
   public void setupExceptionGetNewObjectStringStringParametersRequestList(Throwable arg){
      myActualGetNewObjectStringStringParametersRequestListReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetNewObjectStringStringParametersRequestList(Object arg){
      myActualGetNewObjectStringStringParametersRequestListReturnValues.add(arg);
   }
   public void setExpectedGetDomElementCalls(int calls){
      myGetDomElementCalls.setExpected(calls);
   }
   public void addExpectedGetDomElementValues(String arg0){
      myGetDomElementParameter0Values.addExpected(arg0);
   }
   public Element getDomElement(String arg0) throws ParserConfigurationException, SAXException, IOException{
      myGetDomElementCalls.inc();
      myGetDomElementParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetDomElementReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof ParserConfigurationException)
          throw (ParserConfigurationException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof SAXException)
          throw (SAXException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Element) nextReturnValue;
   }
   public void setupExceptionGetDomElement(Throwable arg){
      myActualGetDomElementReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetDomElement(Element arg){
      myActualGetDomElementReturnValues.add(arg);
   }
   public void setExpectedValidateCalls(int calls){
      myValidateCalls.setExpected(calls);
   }
   public void addExpectedValidateValues(String arg0){
      myValidateParameter0Values.addExpected(arg0);
   }
   public ValidationHandler validate(String arg0){
      myValidateCalls.inc();
      myValidateParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualValidateReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (ValidationHandler) nextReturnValue;
   }
   public void setupExceptionValidate(Throwable arg){
      myActualValidateReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupValidate(ValidationHandler arg){
      myActualValidateReturnValues.add(arg);
   }
   public void setExpectedShouldValidateCalls(int calls){
      myShouldValidateCalls.setExpected(calls);
   }
   public void addExpectedShouldValidateValues(String arg0){
      myShouldValidateParameter0Values.addExpected(arg0);
   }
   public boolean shouldValidate(String arg0){
      myShouldValidateCalls.inc();
      myShouldValidateParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualShouldValidateReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionShouldValidate(Throwable arg){
      myActualShouldValidateReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupShouldValidate(boolean arg){
      myActualShouldValidateReturnValues.add(new Boolean(arg));
   }
   public void setExpectedShouldBeNSAwareCalls(int calls){
      myShouldBeNSAwareCalls.setExpected(calls);
   }
   public void addExpectedShouldBeNSAwareValues(String arg0){
      myShouldBeNSAwareParameter0Values.addExpected(arg0);
   }
   public boolean shouldBeNSAware(String arg0){
      myShouldBeNSAwareCalls.inc();
      myShouldBeNSAwareParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualShouldBeNSAwareReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionShouldBeNSAware(Throwable arg){
      myActualShouldBeNSAwareReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupShouldBeNSAware(boolean arg){
      myActualShouldBeNSAwareReturnValues.add(new Boolean(arg));
   }
   public void verify(){
      myGetRequestParameterStringStringParametersRequestCalls.verify();
      myGetRequestParameterStringStringParametersRequestParameter0Values.verify();
      myGetRequestParameterStringStringParametersRequestParameter1Values.verify();
      myGetRequestParameterStringStringParametersRequestParameter2Values.verify();
      myGetRequestParameterStringStringParametersRequestParameter3Values.verify();
      myGetRequestParameterStringParametersRequestCalls.verify();
      myGetRequestParameterStringParametersRequestParameter0Values.verify();
      myGetRequestParameterStringParametersRequestParameter1Values.verify();
      myGetRequestParameterStringParametersRequestParameter2Values.verify();
      myGetNewObjectStringParametersRequestListCalls.verify();
      myGetNewObjectStringParametersRequestListParameter0Values.verify();
      myGetNewObjectStringParametersRequestListParameter1Values.verify();
      myGetNewObjectStringParametersRequestListParameter2Values.verify();
      myGetNewObjectStringParametersRequestListParameter3Values.verify();
      myGetNewObjectStringStringParametersRequestListCalls.verify();
      myGetNewObjectStringStringParametersRequestListParameter0Values.verify();
      myGetNewObjectStringStringParametersRequestListParameter1Values.verify();
      myGetNewObjectStringStringParametersRequestListParameter2Values.verify();
      myGetNewObjectStringStringParametersRequestListParameter3Values.verify();
      myGetNewObjectStringStringParametersRequestListParameter4Values.verify();
      myGetDomElementCalls.verify();
      myGetDomElementParameter0Values.verify();
      myValidateCalls.verify();
      myValidateParameter0Values.verify();
      myShouldValidateCalls.verify();
      myShouldValidateParameter0Values.verify();
      myShouldBeNSAwareCalls.verify();
      myShouldBeNSAwareParameter0Values.verify();
   }
}
