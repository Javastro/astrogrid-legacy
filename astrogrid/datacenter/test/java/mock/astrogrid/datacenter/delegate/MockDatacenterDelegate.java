package mock.astrogrid.datacenter.delegate;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.w3c.dom.Element;
import java.io.IOException;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
public class MockDatacenterDelegate extends DatacenterDelegate{
   private ExpectationCounter mySetTimeoutCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate SetTimeoutCalls");
   private ReturnValues myActualSetTimeoutReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetTimeoutParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate int");
   private ExpectationCounter myQueryCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate QueryCalls");
   private ReturnValues myActualQueryReturnValues = new ReturnValues(false);
   private ExpectationList myQueryParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate org.w3c.dom.Element");
   private ExpectationCounter myAdqlCountDatacenterCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate AdqlCountDatacenterCalls");
   private ReturnValues myActualAdqlCountDatacenterReturnValues = new ReturnValues(false);
   private ExpectationList myAdqlCountDatacenterParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate org.w3c.dom.Element");
   private ExpectationCounter myMakeQueryCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate MakeQueryCalls");
   private ReturnValues myActualMakeQueryReturnValues = new ReturnValues(false);
   private ExpectationList myMakeQueryParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate org.w3c.dom.Element");
   private ExpectationCounter myStartQueryCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate StartQueryCalls");
   private ReturnValues myActualStartQueryReturnValues = new ReturnValues(false);
   private ExpectationList myStartQueryParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate java.lang.String");
   private ExpectationCounter myGetResultsCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate GetResultsCalls");
   private ReturnValues myActualGetResultsReturnValues = new ReturnValues(false);
   private ExpectationList myGetResultsParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate java.lang.String");
   private ExpectationCounter myGetRegistryMetadataCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate GetRegistryMetadataCalls");
   private ReturnValues myActualGetRegistryMetadataReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetQueryStatusCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate GetQueryStatusCalls");
   private ReturnValues myActualGetQueryStatusReturnValues = new ReturnValues(false);
   private ExpectationList myGetQueryStatusParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate java.lang.String");
   private ExpectationCounter myRegisterListenerCalls = new ExpectationCounter("org.astrogrid.datacenter.delegate.DatacenterDelegate RegisterListenerCalls");
   private ReturnValues myActualRegisterListenerReturnValues = new VoidReturnValues(false);
   private ExpectationList myRegisterListenerParameter0Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate java.lang.String");
   private ExpectationList myRegisterListenerParameter1Values = new ExpectationList("org.astrogrid.datacenter.delegate.DatacenterDelegate org.astrogrid.datacenter.delegate.DatacenterStatusListener");
   public void setExpectedSetTimeoutCalls(int calls){
      mySetTimeoutCalls.setExpected(calls);
   }
   public void addExpectedSetTimeoutValues(int arg0){
      mySetTimeoutParameter0Values.addExpected(new Integer(arg0));
   }
   public void setTimeout(int arg0){
      mySetTimeoutCalls.inc();
      mySetTimeoutParameter0Values.addActual(new Integer(arg0));
      Object nextReturnValue = myActualSetTimeoutReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetTimeout(Throwable arg){
      myActualSetTimeoutReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedQueryCalls(int calls){
      myQueryCalls.setExpected(calls);
   }
   public void addExpectedQueryValues(Element arg0){
      myQueryParameter0Values.addExpected(arg0);
   }
   public Element query(Element arg0) throws IOException{
      myQueryCalls.inc();
      myQueryParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualQueryReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Element) nextReturnValue;
   }
   public void setupExceptionQuery(Throwable arg){
      myActualQueryReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupQuery(Element arg){
      myActualQueryReturnValues.add(arg);
   }
   public void setExpectedAdqlCountDatacenterCalls(int calls){
      myAdqlCountDatacenterCalls.setExpected(calls);
   }
   public void addExpectedAdqlCountDatacenterValues(Element arg0){
      myAdqlCountDatacenterParameter0Values.addExpected(arg0);
   }
   public int adqlCountDatacenter(Element arg0) throws IOException{
      myAdqlCountDatacenterCalls.inc();
      myAdqlCountDatacenterParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualAdqlCountDatacenterReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Integer) nextReturnValue).intValue();
   }
   public void setupExceptionAdqlCountDatacenter(Throwable arg){
      myActualAdqlCountDatacenterReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupAdqlCountDatacenter(int arg){
      myActualAdqlCountDatacenterReturnValues.add(new Integer(arg));
   }
   public void setExpectedMakeQueryCalls(int calls){
      myMakeQueryCalls.setExpected(calls);
   }
   public void addExpectedMakeQueryValues(Element arg0){
      myMakeQueryParameter0Values.addExpected(arg0);
   }
   public Element makeQuery(Element arg0) throws IOException{
      myMakeQueryCalls.inc();
      myMakeQueryParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualMakeQueryReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Element) nextReturnValue;
   }
   public void setupExceptionMakeQuery(Throwable arg){
      myActualMakeQueryReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupMakeQuery(Element arg){
      myActualMakeQueryReturnValues.add(arg);
   }
   public void setExpectedStartQueryCalls(int calls){
      myStartQueryCalls.setExpected(calls);
   }
   public void addExpectedStartQueryValues(String arg0){
      myStartQueryParameter0Values.addExpected(arg0);
   }
   public Element startQuery(String arg0) throws IOException{
      myStartQueryCalls.inc();
      myStartQueryParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualStartQueryReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Element) nextReturnValue;
   }
   public void setupExceptionStartQuery(Throwable arg){
      myActualStartQueryReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupStartQuery(Element arg){
      myActualStartQueryReturnValues.add(arg);
   }
   public void setExpectedGetResultsCalls(int calls){
      myGetResultsCalls.setExpected(calls);
   }
   public void addExpectedGetResultsValues(String arg0){
      myGetResultsParameter0Values.addExpected(arg0);
   }
   public Element getResults(String arg0) throws IOException{
      myGetResultsCalls.inc();
      myGetResultsParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetResultsReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Element) nextReturnValue;
   }
   public void setupExceptionGetResults(Throwable arg){
      myActualGetResultsReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetResults(Element arg){
      myActualGetResultsReturnValues.add(arg);
   }
   public void setExpectedGetRegistryMetadataCalls(int calls){
      myGetRegistryMetadataCalls.setExpected(calls);
   }
   public Element getRegistryMetadata() throws IOException{
      myGetRegistryMetadataCalls.inc();
      Object nextReturnValue = myActualGetRegistryMetadataReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Element) nextReturnValue;
   }
   public void setupExceptionGetRegistryMetadata(Throwable arg){
      myActualGetRegistryMetadataReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRegistryMetadata(Element arg){
      myActualGetRegistryMetadataReturnValues.add(arg);
   }
   public void setExpectedGetQueryStatusCalls(int calls){
      myGetQueryStatusCalls.setExpected(calls);
   }
   public void addExpectedGetQueryStatusValues(String arg0){
      myGetQueryStatusParameter0Values.addExpected(arg0);
   }
   public QueryStatus getQueryStatus(String arg0){
      myGetQueryStatusCalls.inc();
      myGetQueryStatusParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetQueryStatusReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (QueryStatus) nextReturnValue;
   }
   public void setupExceptionGetQueryStatus(Throwable arg){
      myActualGetQueryStatusReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetQueryStatus(QueryStatus arg){
      myActualGetQueryStatusReturnValues.add(arg);
   }
   public void setExpectedRegisterListenerCalls(int calls){
      myRegisterListenerCalls.setExpected(calls);
   }
   public void addExpectedRegisterListenerValues(String arg0, DatacenterStatusListener arg1){
      myRegisterListenerParameter0Values.addExpected(arg0);
      myRegisterListenerParameter1Values.addExpected(arg1);
   }
   public void registerListener(String arg0, DatacenterStatusListener arg1){
      myRegisterListenerCalls.inc();
      myRegisterListenerParameter0Values.addActual(arg0);
      myRegisterListenerParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualRegisterListenerReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionRegisterListener(Throwable arg){
      myActualRegisterListenerReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void verify(){
      mySetTimeoutCalls.verify();
      mySetTimeoutParameter0Values.verify();
      myQueryCalls.verify();
      myQueryParameter0Values.verify();
      myAdqlCountDatacenterCalls.verify();
      myAdqlCountDatacenterParameter0Values.verify();
      myMakeQueryCalls.verify();
      myMakeQueryParameter0Values.verify();
      myStartQueryCalls.verify();
      myStartQueryParameter0Values.verify();
      myGetResultsCalls.verify();
      myGetResultsParameter0Values.verify();
      myGetRegistryMetadataCalls.verify();
      myGetQueryStatusCalls.verify();
      myGetQueryStatusParameter0Values.verify();
      myRegisterListenerCalls.verify();
      myRegisterListenerParameter0Values.verify();
      myRegisterListenerParameter1Values.verify();
   }
}
