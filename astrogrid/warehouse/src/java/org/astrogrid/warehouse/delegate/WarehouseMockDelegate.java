package org.astrogrid.warehouse.delegate;

import mockmaker.ReturnValues;
import com.mockobjects.*;
import org.w3c.dom.Element;


public class WarehouseMockDelegate implements WarehouseDelegateIfc{
   private ExpectationCounter myMakeQueryCalls = new ExpectationCounter("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc MakeQueryCalls");
   private ReturnValues myActualMakeQueryReturnValues = new ReturnValues(false);
   private ExpectationList myMakeQueryParameter0Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc MakeQueryParameter0Values");
   private ExpectationList myMakeQueryParameter1Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc MakeQueryParameter1Values");
   private ExpectationCounter mySetResultsDestinationCalls = new ExpectationCounter("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc SetResultsDestinationCalls");
   private ExpectationList mySetResultsDestinationParameter0Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc SetResultsDestinationParameter0Values");
   private ExpectationList mySetResultsDestinationParameter1Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc SetResultsDestinationParameter1Values");
   private ExpectationCounter myStartQueryCalls = new ExpectationCounter("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc StartQueryCalls");
   private ExpectationList myStartQueryParameter0Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc StartQueryParameter0Values");
   private ExpectationCounter myAbortQueryCalls = new ExpectationCounter("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc AbortQueryCalls");
   private ExpectationList myAbortQueryParameter0Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc AbortQueryParameter0Values");
   private ExpectationCounter myGetStatusCalls = new ExpectationCounter("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc GetStatusCalls");
   private ReturnValues myActualGetStatusReturnValues = new ReturnValues(false);
   private ExpectationList myGetStatusParameter0Values = new ExpectationList("org.astrogrid.warehouse.delegate.WarehouseDelegateIfc GetStatusParameter0Values");
   public void setExpectedMakeQueryCalls(int calls){
      myMakeQueryCalls.setExpected(calls);
   }
   public void addExpectedMakeQueryValues(Element arg0, String arg1){
      myMakeQueryParameter0Values.addExpected(arg0);
      myMakeQueryParameter1Values.addExpected(arg1);
   }
   public String makeQuery(Element arg0, String arg1) throws WarehouseException{
      myMakeQueryCalls.inc();
      myMakeQueryParameter0Values.addActual(arg0);
      myMakeQueryParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualMakeQueryReturnValues.getNext();
      return (String) nextReturnValue;
   }
   public void setupMakeQuery(String arg){
      myActualMakeQueryReturnValues.add(arg);
   }
   public void setExpectedSetResultsDestinationCalls(int calls){
      mySetResultsDestinationCalls.setExpected(calls);
   }
   public void addExpectedSetResultsDestinationValues(String arg0, String arg1){
      mySetResultsDestinationParameter0Values.addExpected(arg0);
      mySetResultsDestinationParameter1Values.addExpected(arg1);
   }
   public void setResultsDestination(String arg0, String arg1) throws WarehouseException{
      mySetResultsDestinationCalls.inc();
      mySetResultsDestinationParameter0Values.addActual(arg0);
      mySetResultsDestinationParameter1Values.addActual(arg1);
   }
   public void setExpectedStartQueryCalls(int calls){
      myStartQueryCalls.setExpected(calls);
   }
   public void addExpectedStartQueryValues(String arg0){
      myStartQueryParameter0Values.addExpected(arg0);
   }
   public void startQuery(String arg0) throws WarehouseException{
      myStartQueryCalls.inc();
      myStartQueryParameter0Values.addActual(arg0);
   }
   public void setExpectedAbortQueryCalls(int calls){
      myAbortQueryCalls.setExpected(calls);
   }
   public void addExpectedAbortQueryValues(String arg0){
      myAbortQueryParameter0Values.addExpected(arg0);
   }
   public void abortQuery(String arg0) throws WarehouseException{
      myAbortQueryCalls.inc();
      myAbortQueryParameter0Values.addActual(arg0);
   }
   public void setExpectedGetStatusCalls(int calls){
      myGetStatusCalls.setExpected(calls);
   }
   public void addExpectedGetStatusValues(String arg0){
      myGetStatusParameter0Values.addExpected(arg0);
   }
   public Element getStatus(String arg0) throws WarehouseException{
      myGetStatusCalls.inc();
      myGetStatusParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetStatusReturnValues.getNext();
      return (Element) nextReturnValue;
   }
   public void setupGetStatus(Element arg){
      myActualGetStatusReturnValues.add(arg);
   }
   public void verify(){
      myMakeQueryCalls.verify();
      myMakeQueryParameter0Values.verify();
      myMakeQueryParameter1Values.verify();
      mySetResultsDestinationCalls.verify();
      mySetResultsDestinationParameter0Values.verify();
      mySetResultsDestinationParameter1Values.verify();
      myStartQueryCalls.verify();
      myStartQueryParameter0Values.verify();
      myAbortQueryCalls.verify();
      myAbortQueryParameter0Values.verify();
      myGetStatusCalls.verify();
      myGetStatusParameter0Values.verify();
   }
}
