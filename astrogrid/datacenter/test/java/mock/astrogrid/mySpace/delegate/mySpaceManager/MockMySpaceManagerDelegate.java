package mock.astrogrid.mySpace.delegate.mySpaceManager;

import mockmaker.ReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import java.util.Vector;
public class MockMySpaceManagerDelegate extends MySpaceManagerDelegate{
   private ExpectationCounter myListDataHoldingsCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate ListDataHoldingsCalls");
   private ReturnValues myActualListDataHoldingsReturnValues = new ReturnValues(false);
   private ExpectationList myListDataHoldingsParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myListDataHoldingsParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myListDataHoldingsParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myListDataHoldingCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate ListDataHoldingCalls");
   private ReturnValues myActualListDataHoldingReturnValues = new ReturnValues(false);
   private ExpectationList myListDataHoldingParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myListDataHoldingParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myListDataHoldingParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myCopyDataHoldingCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate CopyDataHoldingCalls");
   private ReturnValues myActualCopyDataHoldingReturnValues = new ReturnValues(false);
   private ExpectationList myCopyDataHoldingParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myCopyDataHoldingParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myCopyDataHoldingParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myCopyDataHoldingParameter3Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myRenameDataHoldingCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate RenameDataHoldingCalls");
   private ReturnValues myActualRenameDataHoldingReturnValues = new ReturnValues(false);
   private ExpectationList myRenameDataHoldingParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myRenameDataHoldingParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myRenameDataHoldingParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myRenameDataHoldingParameter3Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myDeleteDataHoldingCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate DeleteDataHoldingCalls");
   private ReturnValues myActualDeleteDataHoldingReturnValues = new ReturnValues(false);
   private ExpectationList myDeleteDataHoldingParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myDeleteDataHoldingParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myDeleteDataHoldingParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter mySaveDataHoldingCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate SaveDataHoldingCalls");
   private ReturnValues myActualSaveDataHoldingReturnValues = new ReturnValues(false);
   private ExpectationList mySaveDataHoldingParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList mySaveDataHoldingParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList mySaveDataHoldingParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList mySaveDataHoldingParameter3Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList mySaveDataHoldingParameter4Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList mySaveDataHoldingParameter5Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myGetDataHoldingCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate GetDataHoldingCalls");
   private ReturnValues myActualGetDataHoldingReturnValues = new ReturnValues(false);
   private ExpectationList myGetDataHoldingParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myGetDataHoldingParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myGetDataHoldingParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myExtendLeaseCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate ExtendLeaseCalls");
   private ReturnValues myActualExtendLeaseReturnValues = new ReturnValues(false);
   private ExpectationList myExtendLeaseParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myExtendLeaseParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myExtendLeaseParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myExtendLeaseParameter3Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate int");
   private ExpectationCounter myPublishCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate PublishCalls");
   private ReturnValues myActualPublishReturnValues = new ReturnValues(false);
   private ExpectationList myPublishParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myCreateContainerCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate CreateContainerCalls");
   private ReturnValues myActualCreateContainerReturnValues = new ReturnValues(false);
   private ExpectationList myCreateContainerParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myCreateContainerParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myCreateContainerParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myCreateUserCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate CreateUserCalls");
   private ReturnValues myActualCreateUserReturnValues = new ReturnValues(false);
   private ExpectationList myCreateUserParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myCreateUserParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.util.Vector");
   private ExpectationCounter myDeleteUserCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate DeleteUserCalls");
   private ReturnValues myActualDeleteUserReturnValues = new ReturnValues(false);
   private ExpectationList myDeleteUserParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationCounter myChangeOwnerCalls = new ExpectationCounter("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate ChangeOwnerCalls");
   private ReturnValues myActualChangeOwnerReturnValues = new ReturnValues(false);
   private ExpectationList myChangeOwnerParameter0Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myChangeOwnerParameter1Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myChangeOwnerParameter2Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   private ExpectationList myChangeOwnerParameter3Values = new ExpectationList("org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate java.lang.String");
   public void setExpectedListDataHoldingsCalls(int calls){
      myListDataHoldingsCalls.setExpected(calls);
   }
   public void addExpectedListDataHoldingsValues(String arg0, String arg1, String arg2){
      myListDataHoldingsParameter0Values.addExpected(arg0);
      myListDataHoldingsParameter1Values.addExpected(arg1);
      myListDataHoldingsParameter2Values.addExpected(arg2);
   }
   public Vector listDataHoldings(String arg0, String arg1, String arg2) throws Exception{
      myListDataHoldingsCalls.inc();
      myListDataHoldingsParameter0Values.addActual(arg0);
      myListDataHoldingsParameter1Values.addActual(arg1);
      myListDataHoldingsParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualListDataHoldingsReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Vector) nextReturnValue;
   }
   public void setupExceptionListDataHoldings(Throwable arg){
      myActualListDataHoldingsReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupListDataHoldings(Vector arg){
      myActualListDataHoldingsReturnValues.add(arg);
   }
   public void setExpectedListDataHoldingCalls(int calls){
      myListDataHoldingCalls.setExpected(calls);
   }
   public void addExpectedListDataHoldingValues(String arg0, String arg1, String arg2){
      myListDataHoldingParameter0Values.addExpected(arg0);
      myListDataHoldingParameter1Values.addExpected(arg1);
      myListDataHoldingParameter2Values.addExpected(arg2);
   }
   public String listDataHolding(String arg0, String arg1, String arg2) throws Exception{
      myListDataHoldingCalls.inc();
      myListDataHoldingParameter0Values.addActual(arg0);
      myListDataHoldingParameter1Values.addActual(arg1);
      myListDataHoldingParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualListDataHoldingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionListDataHolding(Throwable arg){
      myActualListDataHoldingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupListDataHolding(String arg){
      myActualListDataHoldingReturnValues.add(arg);
   }
   public void setExpectedCopyDataHoldingCalls(int calls){
      myCopyDataHoldingCalls.setExpected(calls);
   }
   public void addExpectedCopyDataHoldingValues(String arg0, String arg1, String arg2, String arg3){
      myCopyDataHoldingParameter0Values.addExpected(arg0);
      myCopyDataHoldingParameter1Values.addExpected(arg1);
      myCopyDataHoldingParameter2Values.addExpected(arg2);
      myCopyDataHoldingParameter3Values.addExpected(arg3);
   }
   public String copyDataHolding(String arg0, String arg1, String arg2, String arg3) throws Exception{
      myCopyDataHoldingCalls.inc();
      myCopyDataHoldingParameter0Values.addActual(arg0);
      myCopyDataHoldingParameter1Values.addActual(arg1);
      myCopyDataHoldingParameter2Values.addActual(arg2);
      myCopyDataHoldingParameter3Values.addActual(arg3);
      Object nextReturnValue = myActualCopyDataHoldingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionCopyDataHolding(Throwable arg){
      myActualCopyDataHoldingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupCopyDataHolding(String arg){
      myActualCopyDataHoldingReturnValues.add(arg);
   }
   public void setExpectedRenameDataHoldingCalls(int calls){
      myRenameDataHoldingCalls.setExpected(calls);
   }
   public void addExpectedRenameDataHoldingValues(String arg0, String arg1, String arg2, String arg3){
      myRenameDataHoldingParameter0Values.addExpected(arg0);
      myRenameDataHoldingParameter1Values.addExpected(arg1);
      myRenameDataHoldingParameter2Values.addExpected(arg2);
      myRenameDataHoldingParameter3Values.addExpected(arg3);
   }
   public String renameDataHolding(String arg0, String arg1, String arg2, String arg3) throws Exception{
      myRenameDataHoldingCalls.inc();
      myRenameDataHoldingParameter0Values.addActual(arg0);
      myRenameDataHoldingParameter1Values.addActual(arg1);
      myRenameDataHoldingParameter2Values.addActual(arg2);
      myRenameDataHoldingParameter3Values.addActual(arg3);
      Object nextReturnValue = myActualRenameDataHoldingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionRenameDataHolding(Throwable arg){
      myActualRenameDataHoldingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupRenameDataHolding(String arg){
      myActualRenameDataHoldingReturnValues.add(arg);
   }
   public void setExpectedDeleteDataHoldingCalls(int calls){
      myDeleteDataHoldingCalls.setExpected(calls);
   }
   public void addExpectedDeleteDataHoldingValues(String arg0, String arg1, String arg2){
      myDeleteDataHoldingParameter0Values.addExpected(arg0);
      myDeleteDataHoldingParameter1Values.addExpected(arg1);
      myDeleteDataHoldingParameter2Values.addExpected(arg2);
   }
   public String deleteDataHolding(String arg0, String arg1, String arg2) throws Exception{
      myDeleteDataHoldingCalls.inc();
      myDeleteDataHoldingParameter0Values.addActual(arg0);
      myDeleteDataHoldingParameter1Values.addActual(arg1);
      myDeleteDataHoldingParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualDeleteDataHoldingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionDeleteDataHolding(Throwable arg){
      myActualDeleteDataHoldingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupDeleteDataHolding(String arg){
      myActualDeleteDataHoldingReturnValues.add(arg);
   }
   public void setExpectedSaveDataHoldingCalls(int calls){
      mySaveDataHoldingCalls.setExpected(calls);
   }
   public void addExpectedSaveDataHoldingValues(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5){
      mySaveDataHoldingParameter0Values.addExpected(arg0);
      mySaveDataHoldingParameter1Values.addExpected(arg1);
      mySaveDataHoldingParameter2Values.addExpected(arg2);
      mySaveDataHoldingParameter3Values.addExpected(arg3);
      mySaveDataHoldingParameter4Values.addExpected(arg4);
      mySaveDataHoldingParameter5Values.addExpected(arg5);
   }
   public boolean saveDataHolding(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws Exception{
      mySaveDataHoldingCalls.inc();
      mySaveDataHoldingParameter0Values.addActual(arg0);
      mySaveDataHoldingParameter1Values.addActual(arg1);
      mySaveDataHoldingParameter2Values.addActual(arg2);
      mySaveDataHoldingParameter3Values.addActual(arg3);
      mySaveDataHoldingParameter4Values.addActual(arg4);
      mySaveDataHoldingParameter5Values.addActual(arg5);
      Object nextReturnValue = myActualSaveDataHoldingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionSaveDataHolding(Throwable arg){
      myActualSaveDataHoldingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupSaveDataHolding(boolean arg){
      myActualSaveDataHoldingReturnValues.add(new Boolean(arg));
   }
   public void setExpectedGetDataHoldingCalls(int calls){
      myGetDataHoldingCalls.setExpected(calls);
   }
   public void addExpectedGetDataHoldingValues(String arg0, String arg1, String arg2){
      myGetDataHoldingParameter0Values.addExpected(arg0);
      myGetDataHoldingParameter1Values.addExpected(arg1);
      myGetDataHoldingParameter2Values.addExpected(arg2);
   }
   public String getDataHolding(String arg0, String arg1, String arg2) throws Exception{
      myGetDataHoldingCalls.inc();
      myGetDataHoldingParameter0Values.addActual(arg0);
      myGetDataHoldingParameter1Values.addActual(arg1);
      myGetDataHoldingParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualGetDataHoldingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetDataHolding(Throwable arg){
      myActualGetDataHoldingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetDataHolding(String arg){
      myActualGetDataHoldingReturnValues.add(arg);
   }
   public void setExpectedExtendLeaseCalls(int calls){
      myExtendLeaseCalls.setExpected(calls);
   }
   public void addExpectedExtendLeaseValues(String arg0, String arg1, String arg2, int arg3){
      myExtendLeaseParameter0Values.addExpected(arg0);
      myExtendLeaseParameter1Values.addExpected(arg1);
      myExtendLeaseParameter2Values.addExpected(arg2);
      myExtendLeaseParameter3Values.addExpected(new Integer(arg3));
   }
   public String extendLease(String arg0, String arg1, String arg2, int arg3) throws Exception{
      myExtendLeaseCalls.inc();
      myExtendLeaseParameter0Values.addActual(arg0);
      myExtendLeaseParameter1Values.addActual(arg1);
      myExtendLeaseParameter2Values.addActual(arg2);
      myExtendLeaseParameter3Values.addActual(new Integer(arg3));
      Object nextReturnValue = myActualExtendLeaseReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionExtendLease(Throwable arg){
      myActualExtendLeaseReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupExtendLease(String arg){
      myActualExtendLeaseReturnValues.add(arg);
   }
   public void setExpectedPublishCalls(int calls){
      myPublishCalls.setExpected(calls);
   }
   public void addExpectedPublishValues(String arg0){
      myPublishParameter0Values.addExpected(arg0);
   }
   public String publish(String arg0) throws Exception{
      myPublishCalls.inc();
      myPublishParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualPublishReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionPublish(Throwable arg){
      myActualPublishReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupPublish(String arg){
      myActualPublishReturnValues.add(arg);
   }
   public void setExpectedCreateContainerCalls(int calls){
      myCreateContainerCalls.setExpected(calls);
   }
   public void addExpectedCreateContainerValues(String arg0, String arg1, String arg2){
      myCreateContainerParameter0Values.addExpected(arg0);
      myCreateContainerParameter1Values.addExpected(arg1);
      myCreateContainerParameter2Values.addExpected(arg2);
   }
   public String createContainer(String arg0, String arg1, String arg2) throws Exception{
      myCreateContainerCalls.inc();
      myCreateContainerParameter0Values.addActual(arg0);
      myCreateContainerParameter1Values.addActual(arg1);
      myCreateContainerParameter2Values.addActual(arg2);
      Object nextReturnValue = myActualCreateContainerReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionCreateContainer(Throwable arg){
      myActualCreateContainerReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupCreateContainer(String arg){
      myActualCreateContainerReturnValues.add(arg);
   }
   public void setExpectedCreateUserCalls(int calls){
      myCreateUserCalls.setExpected(calls);
   }
   public void addExpectedCreateUserValues(String arg0, Vector arg1){
      myCreateUserParameter0Values.addExpected(arg0);
      myCreateUserParameter1Values.addExpected(arg1);
   }
   public String createUser(String arg0, Vector arg1) throws Exception{
      myCreateUserCalls.inc();
      myCreateUserParameter0Values.addActual(arg0);
      myCreateUserParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualCreateUserReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionCreateUser(Throwable arg){
      myActualCreateUserReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupCreateUser(String arg){
      myActualCreateUserReturnValues.add(arg);
   }
   public void setExpectedDeleteUserCalls(int calls){
      myDeleteUserCalls.setExpected(calls);
   }
   public void addExpectedDeleteUserValues(String arg0){
      myDeleteUserParameter0Values.addExpected(arg0);
   }
   public String deleteUser(String arg0) throws Exception{
      myDeleteUserCalls.inc();
      myDeleteUserParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualDeleteUserReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionDeleteUser(Throwable arg){
      myActualDeleteUserReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupDeleteUser(String arg){
      myActualDeleteUserReturnValues.add(arg);
   }
   public void setExpectedChangeOwnerCalls(int calls){
      myChangeOwnerCalls.setExpected(calls);
   }
   public void addExpectedChangeOwnerValues(String arg0, String arg1, String arg2, String arg3){
      myChangeOwnerParameter0Values.addExpected(arg0);
      myChangeOwnerParameter1Values.addExpected(arg1);
      myChangeOwnerParameter2Values.addExpected(arg2);
      myChangeOwnerParameter3Values.addExpected(arg3);
   }
   public String changeOwner(String arg0, String arg1, String arg2, String arg3) throws Exception{
      myChangeOwnerCalls.inc();
      myChangeOwnerParameter0Values.addActual(arg0);
      myChangeOwnerParameter1Values.addActual(arg1);
      myChangeOwnerParameter2Values.addActual(arg2);
      myChangeOwnerParameter3Values.addActual(arg3);
      Object nextReturnValue = myActualChangeOwnerReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof Exception)
          throw (Exception)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionChangeOwner(Throwable arg){
      myActualChangeOwnerReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupChangeOwner(String arg){
      myActualChangeOwnerReturnValues.add(arg);
   }
   public void verify(){
      myListDataHoldingsCalls.verify();
      myListDataHoldingsParameter0Values.verify();
      myListDataHoldingsParameter1Values.verify();
      myListDataHoldingsParameter2Values.verify();
      myListDataHoldingCalls.verify();
      myListDataHoldingParameter0Values.verify();
      myListDataHoldingParameter1Values.verify();
      myListDataHoldingParameter2Values.verify();
      myCopyDataHoldingCalls.verify();
      myCopyDataHoldingParameter0Values.verify();
      myCopyDataHoldingParameter1Values.verify();
      myCopyDataHoldingParameter2Values.verify();
      myCopyDataHoldingParameter3Values.verify();
      myRenameDataHoldingCalls.verify();
      myRenameDataHoldingParameter0Values.verify();
      myRenameDataHoldingParameter1Values.verify();
      myRenameDataHoldingParameter2Values.verify();
      myRenameDataHoldingParameter3Values.verify();
      myDeleteDataHoldingCalls.verify();
      myDeleteDataHoldingParameter0Values.verify();
      myDeleteDataHoldingParameter1Values.verify();
      myDeleteDataHoldingParameter2Values.verify();
      mySaveDataHoldingCalls.verify();
      mySaveDataHoldingParameter0Values.verify();
      mySaveDataHoldingParameter1Values.verify();
      mySaveDataHoldingParameter2Values.verify();
      mySaveDataHoldingParameter3Values.verify();
      mySaveDataHoldingParameter4Values.verify();
      mySaveDataHoldingParameter5Values.verify();
      myGetDataHoldingCalls.verify();
      myGetDataHoldingParameter0Values.verify();
      myGetDataHoldingParameter1Values.verify();
      myGetDataHoldingParameter2Values.verify();
      myExtendLeaseCalls.verify();
      myExtendLeaseParameter0Values.verify();
      myExtendLeaseParameter1Values.verify();
      myExtendLeaseParameter2Values.verify();
      myExtendLeaseParameter3Values.verify();
      myPublishCalls.verify();
      myPublishParameter0Values.verify();
      myCreateContainerCalls.verify();
      myCreateContainerParameter0Values.verify();
      myCreateContainerParameter1Values.verify();
      myCreateContainerParameter2Values.verify();
      myCreateUserCalls.verify();
      myCreateUserParameter0Values.verify();
      myCreateUserParameter1Values.verify();
      myDeleteUserCalls.verify();
      myDeleteUserParameter0Values.verify();
      myChangeOwnerCalls.verify();
      myChangeOwnerParameter0Values.verify();
      myChangeOwnerParameter1Values.verify();
      myChangeOwnerParameter2Values.verify();
      myChangeOwnerParameter3Values.verify();
   }
public MockMySpaceManagerDelegate(String arg0){
   super(arg0);
}
}
