package mock.org.astrogrid.store.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;

import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreFile;
public class MockStoreClient implements StoreClient{
   private ExpectationCounter myGetOperatorCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient GetOperatorCalls");
   private ReturnValues myActualGetOperatorReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetEndpointCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient GetEndpointCalls");
   private ReturnValues myActualGetEndpointReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetFilesCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient GetFilesCalls");
   private ReturnValues myActualGetFilesReturnValues = new ReturnValues(false);
   private ExpectationList myGetFilesParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myListFilesCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient ListFilesCalls");
   private ReturnValues myActualListFilesReturnValues = new ReturnValues(false);
   private ExpectationList myListFilesParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myGetFileCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient GetFileCalls");
   private ReturnValues myActualGetFileReturnValues = new ReturnValues(false);
   private ExpectationList myGetFileParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myPutBytesCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient PutBytesCalls");
   private ReturnValues myActualPutBytesReturnValues = new VoidReturnValues(false);
   private ExpectationList myPutBytesParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient byte");
   private ExpectationList myPutBytesParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient int");
   private ExpectationList myPutBytesParameter2Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient int");
   private ExpectationList myPutBytesParameter3Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myPutBytesParameter4Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient boolean");
   private ExpectationCounter myPutStringCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient PutStringCalls");
   private ReturnValues myActualPutStringReturnValues = new VoidReturnValues(false);
   private ExpectationList myPutStringParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myPutStringParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myPutStringParameter2Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient boolean");
   private ExpectationCounter myPutUrlCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient PutUrlCalls");
   private ReturnValues myActualPutUrlReturnValues = new VoidReturnValues(false);
   private ExpectationList myPutUrlParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.net.URL");
   private ExpectationList myPutUrlParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myPutUrlParameter2Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient boolean");
   private ExpectationCounter myPutStreamCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient PutStreamCalls");
   private ReturnValues myActualPutStreamReturnValues = new ReturnValues(false);
   private ExpectationList myPutStreamParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myPutStreamParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient boolean");
   private ExpectationCounter myGetStreamCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient GetStreamCalls");
   private ReturnValues myActualGetStreamReturnValues = new ReturnValues(false);
   private ExpectationList myGetStreamParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myGetUrlCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient GetUrlCalls");
   private ReturnValues myActualGetUrlReturnValues = new ReturnValues(false);
   private ExpectationList myGetUrlParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myDeleteCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient DeleteCalls");
   private ReturnValues myActualDeleteReturnValues = new VoidReturnValues(false);
   private ExpectationList myDeleteParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myCopyStringAgslCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient CopyStringAgslCalls");
   private ReturnValues myActualCopyStringAgslReturnValues = new VoidReturnValues(false);
   private ExpectationList myCopyStringAgslParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myCopyStringAgslParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient org.astrogrid.store.Agsl");
   private ExpectationCounter myCopyAgslStringCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient CopyAgslStringCalls");
   private ReturnValues myActualCopyAgslStringReturnValues = new VoidReturnValues(false);
   private ExpectationList myCopyAgslStringParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient org.astrogrid.store.Agsl");
   private ExpectationList myCopyAgslStringParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myMoveStringAgslCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient MoveStringAgslCalls");
   private ReturnValues myActualMoveStringAgslReturnValues = new VoidReturnValues(false);
   private ExpectationList myMoveStringAgslParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationList myMoveStringAgslParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient org.astrogrid.store.Agsl");
   private ExpectationCounter myMoveAgslStringCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient MoveAgslStringCalls");
   private ReturnValues myActualMoveAgslStringReturnValues = new VoidReturnValues(false);
   private ExpectationList myMoveAgslStringParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient org.astrogrid.store.Agsl");
   private ExpectationList myMoveAgslStringParameter1Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   private ExpectationCounter myNewFolderCalls = new ExpectationCounter("org.astrogrid.store.delegate.StoreClient NewFolderCalls");
   private ReturnValues myActualNewFolderReturnValues = new VoidReturnValues(false);
   private ExpectationList myNewFolderParameter0Values = new ExpectationList("org.astrogrid.store.delegate.StoreClient java.lang.String");
   public void setExpectedGetOperatorCalls(int calls){
      myGetOperatorCalls.setExpected(calls);
   }
   public User getOperator(){
      myGetOperatorCalls.inc();
      Object nextReturnValue = myActualGetOperatorReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (User) nextReturnValue;
   }
   public void setupExceptionGetOperator(Throwable arg){
      myActualGetOperatorReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetOperator(User arg){
      myActualGetOperatorReturnValues.add(arg);
   }
   public void setExpectedGetEndpointCalls(int calls){
      myGetEndpointCalls.setExpected(calls);
   }
   public Agsl getEndpoint(){
      myGetEndpointCalls.inc();
      Object nextReturnValue = myActualGetEndpointReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Agsl) nextReturnValue;
   }
   public void setupExceptionGetEndpoint(Throwable arg){
      myActualGetEndpointReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetEndpoint(Agsl arg){
      myActualGetEndpointReturnValues.add(arg);
   }
   public void setExpectedGetFilesCalls(int calls){
      myGetFilesCalls.setExpected(calls);
   }
   public void addExpectedGetFilesValues(String arg0){
      myGetFilesParameter0Values.addExpected(arg0);
   }
   public StoreFile getFiles(String arg0) throws IOException{
      myGetFilesCalls.inc();
      myGetFilesParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetFilesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (StoreFile) nextReturnValue;
   }
   public void setupExceptionGetFiles(Throwable arg){
      myActualGetFilesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetFiles(StoreFile arg){
      myActualGetFilesReturnValues.add(arg);
   }
   public void setExpectedListFilesCalls(int calls){
      myListFilesCalls.setExpected(calls);
   }
   public void addExpectedListFilesValues(String arg0){
      myListFilesParameter0Values.addExpected(arg0);
   }
   public StoreFile[] listFiles(String arg0) throws IOException{
      myListFilesCalls.inc();
      myListFilesParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualListFilesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (StoreFile[]) nextReturnValue;
   }
   public void setupExceptionListFiles(Throwable arg){
      myActualListFilesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupListFiles(StoreFile[] arg){
      myActualListFilesReturnValues.add(arg);
   }
   public void setExpectedGetFileCalls(int calls){
      myGetFileCalls.setExpected(calls);
   }
   public void addExpectedGetFileValues(String arg0){
      myGetFileParameter0Values.addExpected(arg0);
   }
   public StoreFile getFile(String arg0) throws IOException{
      myGetFileCalls.inc();
      myGetFileParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetFileReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (StoreFile) nextReturnValue;
   }
   public void setupExceptionGetFile(Throwable arg){
      myActualGetFileReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetFile(StoreFile arg){
      myActualGetFileReturnValues.add(arg);
   }
   public void setExpectedPutBytesCalls(int calls){
      myPutBytesCalls.setExpected(calls);
   }
   public void addExpectedPutBytesValues(byte[] arg0, int arg1, int arg2, String arg3, boolean arg4){
      myPutBytesParameter0Values.addExpected(arg0);
      myPutBytesParameter1Values.addExpected(new Integer(arg1));
      myPutBytesParameter2Values.addExpected(new Integer(arg2));
      myPutBytesParameter3Values.addExpected(arg3);
      myPutBytesParameter4Values.addExpected(new Boolean(arg4));
   }
   public void putBytes(byte[] arg0, int arg1, int arg2, String arg3, boolean arg4) throws IOException{
      myPutBytesCalls.inc();
      myPutBytesParameter0Values.addActual(arg0);
      myPutBytesParameter1Values.addActual(new Integer(arg1));
      myPutBytesParameter2Values.addActual(new Integer(arg2));
      myPutBytesParameter3Values.addActual(arg3);
      myPutBytesParameter4Values.addActual(new Boolean(arg4));
      Object nextReturnValue = myActualPutBytesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionPutBytes(Throwable arg){
      myActualPutBytesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedPutStringCalls(int calls){
      myPutStringCalls.setExpected(calls);
   }
   public void addExpectedPutStringValues(String arg0, String arg1, boolean arg2){
      myPutStringParameter0Values.addExpected(arg0);
      myPutStringParameter1Values.addExpected(arg1);
      myPutStringParameter2Values.addExpected(new Boolean(arg2));
   }
   public void putString(String arg0, String arg1, boolean arg2) throws IOException{
      myPutStringCalls.inc();
      myPutStringParameter0Values.addActual(arg0);
      myPutStringParameter1Values.addActual(arg1);
      myPutStringParameter2Values.addActual(new Boolean(arg2));
      Object nextReturnValue = myActualPutStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionPutString(Throwable arg){
      myActualPutStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedPutUrlCalls(int calls){
      myPutUrlCalls.setExpected(calls);
   }
   public void addExpectedPutUrlValues(URL arg0, String arg1, boolean arg2){
      myPutUrlParameter0Values.addExpected(arg0);
      myPutUrlParameter1Values.addExpected(arg1);
      myPutUrlParameter2Values.addExpected(new Boolean(arg2));
   }
   public void putUrl(URL arg0, String arg1, boolean arg2) throws IOException{
      myPutUrlCalls.inc();
      myPutUrlParameter0Values.addActual(arg0);
      myPutUrlParameter1Values.addActual(arg1);
      myPutUrlParameter2Values.addActual(new Boolean(arg2));
      Object nextReturnValue = myActualPutUrlReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionPutUrl(Throwable arg){
      myActualPutUrlReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedPutStreamCalls(int calls){
      myPutStreamCalls.setExpected(calls);
   }
   public void addExpectedPutStreamValues(String arg0, boolean arg1){
      myPutStreamParameter0Values.addExpected(arg0);
      myPutStreamParameter1Values.addExpected(new Boolean(arg1));
   }
   public OutputStream putStream(String arg0, boolean arg1) throws IOException{
      myPutStreamCalls.inc();
      myPutStreamParameter0Values.addActual(arg0);
      myPutStreamParameter1Values.addActual(new Boolean(arg1));
      Object nextReturnValue = myActualPutStreamReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (OutputStream) nextReturnValue;
   }
   public void setupExceptionPutStream(Throwable arg){
      myActualPutStreamReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupPutStream(OutputStream arg){
      myActualPutStreamReturnValues.add(arg);
   }
   public void setExpectedGetStreamCalls(int calls){
      myGetStreamCalls.setExpected(calls);
   }
   public void addExpectedGetStreamValues(String arg0){
      myGetStreamParameter0Values.addExpected(arg0);
   }
   public InputStream getStream(String arg0) throws IOException{
      myGetStreamCalls.inc();
      myGetStreamParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetStreamReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (InputStream) nextReturnValue;
   }
   public void setupExceptionGetStream(Throwable arg){
      myActualGetStreamReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetStream(InputStream arg){
      myActualGetStreamReturnValues.add(arg);
   }
   public void setExpectedGetUrlCalls(int calls){
      myGetUrlCalls.setExpected(calls);
   }
   public void addExpectedGetUrlValues(String arg0){
      myGetUrlParameter0Values.addExpected(arg0);
   }
   public URL getUrl(String arg0) throws IOException{
      myGetUrlCalls.inc();
      myGetUrlParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetUrlReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (URL) nextReturnValue;
   }
   public void setupExceptionGetUrl(Throwable arg){
      myActualGetUrlReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetUrl(URL arg){
      myActualGetUrlReturnValues.add(arg);
   }
   public void setExpectedDeleteCalls(int calls){
      myDeleteCalls.setExpected(calls);
   }
   public void addExpectedDeleteValues(String arg0){
      myDeleteParameter0Values.addExpected(arg0);
   }
   public void delete(String arg0) throws IOException{
      myDeleteCalls.inc();
      myDeleteParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualDeleteReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionDelete(Throwable arg){
      myActualDeleteReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedCopyStringAgslCalls(int calls){
      myCopyStringAgslCalls.setExpected(calls);
   }
   public void addExpectedCopyStringAgslValues(String arg0, Agsl arg1){
      myCopyStringAgslParameter0Values.addExpected(arg0);
      myCopyStringAgslParameter1Values.addExpected(arg1);
   }
   public void copy(String arg0, Agsl arg1) throws IOException{
      myCopyStringAgslCalls.inc();
      myCopyStringAgslParameter0Values.addActual(arg0);
      myCopyStringAgslParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualCopyStringAgslReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionCopyStringAgsl(Throwable arg){
      myActualCopyStringAgslReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedCopyAgslStringCalls(int calls){
      myCopyAgslStringCalls.setExpected(calls);
   }
   public void addExpectedCopyAgslStringValues(Agsl arg0, String arg1){
      myCopyAgslStringParameter0Values.addExpected(arg0);
      myCopyAgslStringParameter1Values.addExpected(arg1);
   }
   public void copy(Agsl arg0, String arg1) throws IOException{
      myCopyAgslStringCalls.inc();
      myCopyAgslStringParameter0Values.addActual(arg0);
      myCopyAgslStringParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualCopyAgslStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionCopyAgslString(Throwable arg){
      myActualCopyAgslStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedMoveStringAgslCalls(int calls){
      myMoveStringAgslCalls.setExpected(calls);
   }
   public void addExpectedMoveStringAgslValues(String arg0, Agsl arg1){
      myMoveStringAgslParameter0Values.addExpected(arg0);
      myMoveStringAgslParameter1Values.addExpected(arg1);
   }
   public void move(String arg0, Agsl arg1) throws IOException{
      myMoveStringAgslCalls.inc();
      myMoveStringAgslParameter0Values.addActual(arg0);
      myMoveStringAgslParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualMoveStringAgslReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionMoveStringAgsl(Throwable arg){
      myActualMoveStringAgslReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedMoveAgslStringCalls(int calls){
      myMoveAgslStringCalls.setExpected(calls);
   }
   public void addExpectedMoveAgslStringValues(Agsl arg0, String arg1){
      myMoveAgslStringParameter0Values.addExpected(arg0);
      myMoveAgslStringParameter1Values.addExpected(arg1);
   }
   public void move(Agsl arg0, String arg1) throws IOException{
      myMoveAgslStringCalls.inc();
      myMoveAgslStringParameter0Values.addActual(arg0);
      myMoveAgslStringParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualMoveAgslStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionMoveAgslString(Throwable arg){
      myActualMoveAgslStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedNewFolderCalls(int calls){
      myNewFolderCalls.setExpected(calls);
   }
   public void addExpectedNewFolderValues(String arg0){
      myNewFolderParameter0Values.addExpected(arg0);
   }
   public void newFolder(String arg0) throws IOException{
      myNewFolderCalls.inc();
      myNewFolderParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualNewFolderReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof IOException)
          throw (IOException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionNewFolder(Throwable arg){
      myActualNewFolderReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void verify(){
      myGetOperatorCalls.verify();
      myGetEndpointCalls.verify();
      myGetFilesCalls.verify();
      myGetFilesParameter0Values.verify();
      myListFilesCalls.verify();
      myListFilesParameter0Values.verify();
      myGetFileCalls.verify();
      myGetFileParameter0Values.verify();
      myPutBytesCalls.verify();
      myPutBytesParameter0Values.verify();
      myPutBytesParameter1Values.verify();
      myPutBytesParameter2Values.verify();
      myPutBytesParameter3Values.verify();
      myPutBytesParameter4Values.verify();
      myPutStringCalls.verify();
      myPutStringParameter0Values.verify();
      myPutStringParameter1Values.verify();
      myPutStringParameter2Values.verify();
      myPutUrlCalls.verify();
      myPutUrlParameter0Values.verify();
      myPutUrlParameter1Values.verify();
      myPutUrlParameter2Values.verify();
      myPutStreamCalls.verify();
      myPutStreamParameter0Values.verify();
      myPutStreamParameter1Values.verify();
      myGetStreamCalls.verify();
      myGetStreamParameter0Values.verify();
      myGetUrlCalls.verify();
      myGetUrlParameter0Values.verify();
      myDeleteCalls.verify();
      myDeleteParameter0Values.verify();
      myCopyStringAgslCalls.verify();
      myCopyStringAgslParameter0Values.verify();
      myCopyStringAgslParameter1Values.verify();
      myCopyAgslStringCalls.verify();
      myCopyAgslStringParameter0Values.verify();
      myCopyAgslStringParameter1Values.verify();
      myMoveStringAgslCalls.verify();
      myMoveStringAgslParameter0Values.verify();
      myMoveStringAgslParameter1Values.verify();
      myMoveAgslStringCalls.verify();
      myMoveAgslStringParameter0Values.verify();
      myMoveAgslStringParameter1Values.verify();
      myNewFolderCalls.verify();
      myNewFolderParameter0Values.verify();
   }
}
