package mock.apache.cocoon.environment;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;

import org.apache.cocoon.environment.Cookie;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
public class MockRequest implements Request{
   private ExpectationCounter myGetCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetCalls");
   private ReturnValues myActualGetReturnValues = new ReturnValues(false);
   private ExpectationList myGetParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetAttributeCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetAttributeCalls");
   private ReturnValues myActualGetAttributeReturnValues = new ReturnValues(false);
   private ExpectationList myGetAttributeParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetAttributeNamesCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetAttributeNamesCalls");
   private ReturnValues myActualGetAttributeNamesReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetAuthTypeCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetAuthTypeCalls");
   private ReturnValues myActualGetAuthTypeReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetCharacterEncodingCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetCharacterEncodingCalls");
   private ReturnValues myActualGetCharacterEncodingReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetCharacterEncodingCalls = new ExpectationCounter("org.apache.cocoon.environment.Request SetCharacterEncodingCalls");
   private ReturnValues myActualSetCharacterEncodingReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetCharacterEncodingParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetContentLengthCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetContentLengthCalls");
   private ReturnValues myActualGetContentLengthReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetContentTypeCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetContentTypeCalls");
   private ReturnValues myActualGetContentTypeReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetParameterCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetParameterCalls");
   private ReturnValues myActualGetParameterReturnValues = new ReturnValues(false);
   private ExpectationList myGetParameterParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetParameterNamesCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetParameterNamesCalls");
   private ReturnValues myActualGetParameterNamesReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetParameterValuesCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetParameterValuesCalls");
   private ReturnValues myActualGetParameterValuesReturnValues = new ReturnValues(false);
   private ExpectationList myGetParameterValuesParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetProtocolCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetProtocolCalls");
   private ReturnValues myActualGetProtocolReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetSchemeCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetSchemeCalls");
   private ReturnValues myActualGetSchemeReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetServerNameCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetServerNameCalls");
   private ReturnValues myActualGetServerNameReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetServerPortCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetServerPortCalls");
   private ReturnValues myActualGetServerPortReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetRemoteAddrCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetRemoteAddrCalls");
   private ReturnValues myActualGetRemoteAddrReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetRemoteHostCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetRemoteHostCalls");
   private ReturnValues myActualGetRemoteHostReturnValues = new ReturnValues(false);
   private ExpectationCounter mySetAttributeCalls = new ExpectationCounter("org.apache.cocoon.environment.Request SetAttributeCalls");
   private ReturnValues myActualSetAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList mySetAttributeParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationList mySetAttributeParameter1Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.Object");
   private ExpectationCounter myRemoveAttributeCalls = new ExpectationCounter("org.apache.cocoon.environment.Request RemoveAttributeCalls");
   private ReturnValues myActualRemoveAttributeReturnValues = new VoidReturnValues(false);
   private ExpectationList myRemoveAttributeParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetLocaleCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetLocaleCalls");
   private ReturnValues myActualGetLocaleReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetLocalesCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetLocalesCalls");
   private ReturnValues myActualGetLocalesReturnValues = new ReturnValues(false);
   private ExpectationCounter myIsSecureCalls = new ExpectationCounter("org.apache.cocoon.environment.Request IsSecureCalls");
   private ReturnValues myActualIsSecureReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetCookiesCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetCookiesCalls");
   private ReturnValues myActualGetCookiesReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetCookieMapCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetCookieMapCalls");
   private ReturnValues myActualGetCookieMapReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetDateHeaderCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetDateHeaderCalls");
   private ReturnValues myActualGetDateHeaderReturnValues = new ReturnValues(false);
   private ExpectationList myGetDateHeaderParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetHeaderCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetHeaderCalls");
   private ReturnValues myActualGetHeaderReturnValues = new ReturnValues(false);
   private ExpectationList myGetHeaderParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetHeadersCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetHeadersCalls");
   private ReturnValues myActualGetHeadersReturnValues = new ReturnValues(false);
   private ExpectationList myGetHeadersParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetHeaderNamesCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetHeaderNamesCalls");
   private ReturnValues myActualGetHeaderNamesReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetMethodCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetMethodCalls");
   private ReturnValues myActualGetMethodReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetPathInfoCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetPathInfoCalls");
   private ReturnValues myActualGetPathInfoReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetPathTranslatedCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetPathTranslatedCalls");
   private ReturnValues myActualGetPathTranslatedReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetContextPathCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetContextPathCalls");
   private ReturnValues myActualGetContextPathReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetQueryStringCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetQueryStringCalls");
   private ReturnValues myActualGetQueryStringReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetRemoteUserCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetRemoteUserCalls");
   private ReturnValues myActualGetRemoteUserReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetUserPrincipalCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetUserPrincipalCalls");
   private ReturnValues myActualGetUserPrincipalReturnValues = new ReturnValues(false);
   private ExpectationCounter myIsUserInRoleCalls = new ExpectationCounter("org.apache.cocoon.environment.Request IsUserInRoleCalls");
   private ReturnValues myActualIsUserInRoleReturnValues = new ReturnValues(false);
   private ExpectationList myIsUserInRoleParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request java.lang.String");
   private ExpectationCounter myGetRequestedSessionIdCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetRequestedSessionIdCalls");
   private ReturnValues myActualGetRequestedSessionIdReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetRequestURICalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetRequestURICalls");
   private ReturnValues myActualGetRequestURIReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetSitemapURICalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetSitemapURICalls");
   private ReturnValues myActualGetSitemapURIReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetServletPathCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetServletPathCalls");
   private ReturnValues myActualGetServletPathReturnValues = new ReturnValues(false);
   private ExpectationCounter myGetSessionBooleanCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetSessionBooleanCalls");
   private ReturnValues myActualGetSessionBooleanReturnValues = new ReturnValues(false);
   private ExpectationList myGetSessionBooleanParameter0Values = new ExpectationList("org.apache.cocoon.environment.Request boolean");
   private ExpectationCounter myGetSessionCalls = new ExpectationCounter("org.apache.cocoon.environment.Request GetSessionCalls");
   private ReturnValues myActualGetSessionReturnValues = new ReturnValues(false);
   private ExpectationCounter myIsRequestedSessionIdValidCalls = new ExpectationCounter("org.apache.cocoon.environment.Request IsRequestedSessionIdValidCalls");
   private ReturnValues myActualIsRequestedSessionIdValidReturnValues = new ReturnValues(false);
   private ExpectationCounter myIsRequestedSessionIdFromCookieCalls = new ExpectationCounter("org.apache.cocoon.environment.Request IsRequestedSessionIdFromCookieCalls");
   private ReturnValues myActualIsRequestedSessionIdFromCookieReturnValues = new ReturnValues(false);
   private ExpectationCounter myIsRequestedSessionIdFromURLCalls = new ExpectationCounter("org.apache.cocoon.environment.Request IsRequestedSessionIdFromURLCalls");
   private ReturnValues myActualIsRequestedSessionIdFromURLReturnValues = new ReturnValues(false);
   public void setExpectedGetCalls(int calls){
      myGetCalls.setExpected(calls);
   }
   public void addExpectedGetValues(String arg0){
      myGetParameter0Values.addExpected(arg0);
   }
   public Object get(String arg0){
      myGetCalls.inc();
      myGetParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Object) nextReturnValue;
   }
   public void setupExceptionGet(Throwable arg){
      myActualGetReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGet(Object arg){
      myActualGetReturnValues.add(arg);
   }
   public void setExpectedGetAttributeCalls(int calls){
      myGetAttributeCalls.setExpected(calls);
   }
   public void addExpectedGetAttributeValues(String arg0){
      myGetAttributeParameter0Values.addExpected(arg0);
   }
   public Object getAttribute(String arg0){
      myGetAttributeCalls.inc();
      myGetAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Object) nextReturnValue;
   }
   public void setupExceptionGetAttribute(Throwable arg){
      myActualGetAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttribute(Object arg){
      myActualGetAttributeReturnValues.add(arg);
   }
   public void setExpectedGetAttributeNamesCalls(int calls){
      myGetAttributeNamesCalls.setExpected(calls);
   }
   public Enumeration getAttributeNames(){
      myGetAttributeNamesCalls.inc();
      Object nextReturnValue = myActualGetAttributeNamesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Enumeration) nextReturnValue;
   }
   public void setupExceptionGetAttributeNames(Throwable arg){
      myActualGetAttributeNamesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAttributeNames(Enumeration arg){
      myActualGetAttributeNamesReturnValues.add(arg);
   }
   public void setExpectedGetAuthTypeCalls(int calls){
      myGetAuthTypeCalls.setExpected(calls);
   }
   public String getAuthType(){
      myGetAuthTypeCalls.inc();
      Object nextReturnValue = myActualGetAuthTypeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetAuthType(Throwable arg){
      myActualGetAuthTypeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetAuthType(String arg){
      myActualGetAuthTypeReturnValues.add(arg);
   }
   public void setExpectedGetCharacterEncodingCalls(int calls){
      myGetCharacterEncodingCalls.setExpected(calls);
   }
   public String getCharacterEncoding(){
      myGetCharacterEncodingCalls.inc();
      Object nextReturnValue = myActualGetCharacterEncodingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetCharacterEncoding(Throwable arg){
      myActualGetCharacterEncodingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetCharacterEncoding(String arg){
      myActualGetCharacterEncodingReturnValues.add(arg);
   }
   public void setExpectedSetCharacterEncodingCalls(int calls){
      mySetCharacterEncodingCalls.setExpected(calls);
   }
   public void addExpectedSetCharacterEncodingValues(String arg0){
      mySetCharacterEncodingParameter0Values.addExpected(arg0);
   }
   public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException{
      mySetCharacterEncodingCalls.inc();
      mySetCharacterEncodingParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualSetCharacterEncodingReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof UnsupportedEncodingException)
          throw (UnsupportedEncodingException)((ExceptionalReturnValue)nextReturnValue).getException();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionSetCharacterEncoding(Throwable arg){
      myActualSetCharacterEncodingReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetContentLengthCalls(int calls){
      myGetContentLengthCalls.setExpected(calls);
   }
   public int getContentLength(){
      myGetContentLengthCalls.inc();
      Object nextReturnValue = myActualGetContentLengthReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Integer) nextReturnValue).intValue();
   }
   public void setupExceptionGetContentLength(Throwable arg){
      myActualGetContentLengthReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetContentLength(int arg){
      myActualGetContentLengthReturnValues.add(new Integer(arg));
   }
   public void setExpectedGetContentTypeCalls(int calls){
      myGetContentTypeCalls.setExpected(calls);
   }
   public String getContentType(){
      myGetContentTypeCalls.inc();
      Object nextReturnValue = myActualGetContentTypeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetContentType(Throwable arg){
      myActualGetContentTypeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetContentType(String arg){
      myActualGetContentTypeReturnValues.add(arg);
   }
   public void setExpectedGetParameterCalls(int calls){
      myGetParameterCalls.setExpected(calls);
   }
   public void addExpectedGetParameterValues(String arg0){
      myGetParameterParameter0Values.addExpected(arg0);
   }
   public String getParameter(String arg0){
      myGetParameterCalls.inc();
      myGetParameterParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetParameterReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetParameter(Throwable arg){
      myActualGetParameterReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetParameter(String arg){
      myActualGetParameterReturnValues.add(arg);
   }
   public void setExpectedGetParameterNamesCalls(int calls){
      myGetParameterNamesCalls.setExpected(calls);
   }
   public Enumeration getParameterNames(){
      myGetParameterNamesCalls.inc();
      Object nextReturnValue = myActualGetParameterNamesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Enumeration) nextReturnValue;
   }
   public void setupExceptionGetParameterNames(Throwable arg){
      myActualGetParameterNamesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetParameterNames(Enumeration arg){
      myActualGetParameterNamesReturnValues.add(arg);
   }
   public void setExpectedGetParameterValuesCalls(int calls){
      myGetParameterValuesCalls.setExpected(calls);
   }
   public void addExpectedGetParameterValuesValues(String arg0){
      myGetParameterValuesParameter0Values.addExpected(arg0);
   }
   public String[] getParameterValues(String arg0){
      myGetParameterValuesCalls.inc();
      myGetParameterValuesParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetParameterValuesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String[]) nextReturnValue;
   }
   public void setupExceptionGetParameterValues(Throwable arg){
      myActualGetParameterValuesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetParameterValues(String[] arg){
      myActualGetParameterValuesReturnValues.add(arg);
   }
   public void setExpectedGetProtocolCalls(int calls){
      myGetProtocolCalls.setExpected(calls);
   }
   public String getProtocol(){
      myGetProtocolCalls.inc();
      Object nextReturnValue = myActualGetProtocolReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetProtocol(Throwable arg){
      myActualGetProtocolReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetProtocol(String arg){
      myActualGetProtocolReturnValues.add(arg);
   }
   public void setExpectedGetSchemeCalls(int calls){
      myGetSchemeCalls.setExpected(calls);
   }
   public String getScheme(){
      myGetSchemeCalls.inc();
      Object nextReturnValue = myActualGetSchemeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetScheme(Throwable arg){
      myActualGetSchemeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetScheme(String arg){
      myActualGetSchemeReturnValues.add(arg);
   }
   public void setExpectedGetServerNameCalls(int calls){
      myGetServerNameCalls.setExpected(calls);
   }
   public String getServerName(){
      myGetServerNameCalls.inc();
      Object nextReturnValue = myActualGetServerNameReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetServerName(Throwable arg){
      myActualGetServerNameReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetServerName(String arg){
      myActualGetServerNameReturnValues.add(arg);
   }
   public void setExpectedGetServerPortCalls(int calls){
      myGetServerPortCalls.setExpected(calls);
   }
   public int getServerPort(){
      myGetServerPortCalls.inc();
      Object nextReturnValue = myActualGetServerPortReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Integer) nextReturnValue).intValue();
   }
   public void setupExceptionGetServerPort(Throwable arg){
      myActualGetServerPortReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetServerPort(int arg){
      myActualGetServerPortReturnValues.add(new Integer(arg));
   }
   public void setExpectedGetRemoteAddrCalls(int calls){
      myGetRemoteAddrCalls.setExpected(calls);
   }
   public String getRemoteAddr(){
      myGetRemoteAddrCalls.inc();
      Object nextReturnValue = myActualGetRemoteAddrReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRemoteAddr(Throwable arg){
      myActualGetRemoteAddrReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRemoteAddr(String arg){
      myActualGetRemoteAddrReturnValues.add(arg);
   }
   public void setExpectedGetRemoteHostCalls(int calls){
      myGetRemoteHostCalls.setExpected(calls);
   }
   public String getRemoteHost(){
      myGetRemoteHostCalls.inc();
      Object nextReturnValue = myActualGetRemoteHostReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRemoteHost(Throwable arg){
      myActualGetRemoteHostReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRemoteHost(String arg){
      myActualGetRemoteHostReturnValues.add(arg);
   }
   public void setExpectedSetAttributeCalls(int calls){
      mySetAttributeCalls.setExpected(calls);
   }
   public void addExpectedSetAttributeValues(String arg0, Object arg1){
      mySetAttributeParameter0Values.addExpected(arg0);
      mySetAttributeParameter1Values.addExpected(arg1);
   }
   public void setAttribute(String arg0, Object arg1){
      mySetAttributeCalls.inc();
      mySetAttributeParameter0Values.addActual(arg0);
      mySetAttributeParameter1Values.addActual(arg1);
      Object nextReturnValue = myActualSetAttributeReturnValues.getNext();
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
   public void removeAttribute(String arg0){
      myRemoveAttributeCalls.inc();
      myRemoveAttributeParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualRemoveAttributeReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
   }
   public void setupExceptionRemoveAttribute(Throwable arg){
      myActualRemoveAttributeReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setExpectedGetLocaleCalls(int calls){
      myGetLocaleCalls.setExpected(calls);
   }
   public Locale getLocale(){
      myGetLocaleCalls.inc();
      Object nextReturnValue = myActualGetLocaleReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Locale) nextReturnValue;
   }
   public void setupExceptionGetLocale(Throwable arg){
      myActualGetLocaleReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetLocale(Locale arg){
      myActualGetLocaleReturnValues.add(arg);
   }
   public void setExpectedGetLocalesCalls(int calls){
      myGetLocalesCalls.setExpected(calls);
   }
   public Enumeration getLocales(){
      myGetLocalesCalls.inc();
      Object nextReturnValue = myActualGetLocalesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Enumeration) nextReturnValue;
   }
   public void setupExceptionGetLocales(Throwable arg){
      myActualGetLocalesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetLocales(Enumeration arg){
      myActualGetLocalesReturnValues.add(arg);
   }
   public void setExpectedIsSecureCalls(int calls){
      myIsSecureCalls.setExpected(calls);
   }
   public boolean isSecure(){
      myIsSecureCalls.inc();
      Object nextReturnValue = myActualIsSecureReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsSecure(Throwable arg){
      myActualIsSecureReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsSecure(boolean arg){
      myActualIsSecureReturnValues.add(new Boolean(arg));
   }
   public void setExpectedGetCookiesCalls(int calls){
      myGetCookiesCalls.setExpected(calls);
   }
   public Cookie[] getCookies(){
      myGetCookiesCalls.inc();
      Object nextReturnValue = myActualGetCookiesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Cookie[]) nextReturnValue;
   }
   public void setupExceptionGetCookies(Throwable arg){
      myActualGetCookiesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetCookies(Cookie[] arg){
      myActualGetCookiesReturnValues.add(arg);
   }
   public void setExpectedGetCookieMapCalls(int calls){
      myGetCookieMapCalls.setExpected(calls);
   }
   public Map getCookieMap(){
      myGetCookieMapCalls.inc();
      Object nextReturnValue = myActualGetCookieMapReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Map) nextReturnValue;
   }
   public void setupExceptionGetCookieMap(Throwable arg){
      myActualGetCookieMapReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetCookieMap(Map arg){
      myActualGetCookieMapReturnValues.add(arg);
   }
   public void setExpectedGetDateHeaderCalls(int calls){
      myGetDateHeaderCalls.setExpected(calls);
   }
   public void addExpectedGetDateHeaderValues(String arg0){
      myGetDateHeaderParameter0Values.addExpected(arg0);
   }
   public long getDateHeader(String arg0){
      myGetDateHeaderCalls.inc();
      myGetDateHeaderParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetDateHeaderReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Long) nextReturnValue).longValue();
   }
   public void setupExceptionGetDateHeader(Throwable arg){
      myActualGetDateHeaderReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetDateHeader(long arg){
      myActualGetDateHeaderReturnValues.add(new Long(arg));
   }
   public void setExpectedGetHeaderCalls(int calls){
      myGetHeaderCalls.setExpected(calls);
   }
   public void addExpectedGetHeaderValues(String arg0){
      myGetHeaderParameter0Values.addExpected(arg0);
   }
   public String getHeader(String arg0){
      myGetHeaderCalls.inc();
      myGetHeaderParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetHeaderReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetHeader(Throwable arg){
      myActualGetHeaderReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetHeader(String arg){
      myActualGetHeaderReturnValues.add(arg);
   }
   public void setExpectedGetHeadersCalls(int calls){
      myGetHeadersCalls.setExpected(calls);
   }
   public void addExpectedGetHeadersValues(String arg0){
      myGetHeadersParameter0Values.addExpected(arg0);
   }
   public Enumeration getHeaders(String arg0){
      myGetHeadersCalls.inc();
      myGetHeadersParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualGetHeadersReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Enumeration) nextReturnValue;
   }
   public void setupExceptionGetHeaders(Throwable arg){
      myActualGetHeadersReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetHeaders(Enumeration arg){
      myActualGetHeadersReturnValues.add(arg);
   }
   public void setExpectedGetHeaderNamesCalls(int calls){
      myGetHeaderNamesCalls.setExpected(calls);
   }
   public Enumeration getHeaderNames(){
      myGetHeaderNamesCalls.inc();
      Object nextReturnValue = myActualGetHeaderNamesReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Enumeration) nextReturnValue;
   }
   public void setupExceptionGetHeaderNames(Throwable arg){
      myActualGetHeaderNamesReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetHeaderNames(Enumeration arg){
      myActualGetHeaderNamesReturnValues.add(arg);
   }
   public void setExpectedGetMethodCalls(int calls){
      myGetMethodCalls.setExpected(calls);
   }
   public String getMethod(){
      myGetMethodCalls.inc();
      Object nextReturnValue = myActualGetMethodReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetMethod(Throwable arg){
      myActualGetMethodReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetMethod(String arg){
      myActualGetMethodReturnValues.add(arg);
   }
   public void setExpectedGetPathInfoCalls(int calls){
      myGetPathInfoCalls.setExpected(calls);
   }
   public String getPathInfo(){
      myGetPathInfoCalls.inc();
      Object nextReturnValue = myActualGetPathInfoReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetPathInfo(Throwable arg){
      myActualGetPathInfoReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetPathInfo(String arg){
      myActualGetPathInfoReturnValues.add(arg);
   }
   public void setExpectedGetPathTranslatedCalls(int calls){
      myGetPathTranslatedCalls.setExpected(calls);
   }
   public String getPathTranslated(){
      myGetPathTranslatedCalls.inc();
      Object nextReturnValue = myActualGetPathTranslatedReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetPathTranslated(Throwable arg){
      myActualGetPathTranslatedReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetPathTranslated(String arg){
      myActualGetPathTranslatedReturnValues.add(arg);
   }
   public void setExpectedGetContextPathCalls(int calls){
      myGetContextPathCalls.setExpected(calls);
   }
   public String getContextPath(){
      myGetContextPathCalls.inc();
      Object nextReturnValue = myActualGetContextPathReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetContextPath(Throwable arg){
      myActualGetContextPathReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetContextPath(String arg){
      myActualGetContextPathReturnValues.add(arg);
   }
   public void setExpectedGetQueryStringCalls(int calls){
      myGetQueryStringCalls.setExpected(calls);
   }
   public String getQueryString(){
      myGetQueryStringCalls.inc();
      Object nextReturnValue = myActualGetQueryStringReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetQueryString(Throwable arg){
      myActualGetQueryStringReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetQueryString(String arg){
      myActualGetQueryStringReturnValues.add(arg);
   }
   public void setExpectedGetRemoteUserCalls(int calls){
      myGetRemoteUserCalls.setExpected(calls);
   }
   public String getRemoteUser(){
      myGetRemoteUserCalls.inc();
      Object nextReturnValue = myActualGetRemoteUserReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRemoteUser(Throwable arg){
      myActualGetRemoteUserReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRemoteUser(String arg){
      myActualGetRemoteUserReturnValues.add(arg);
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
   public void setExpectedIsUserInRoleCalls(int calls){
      myIsUserInRoleCalls.setExpected(calls);
   }
   public void addExpectedIsUserInRoleValues(String arg0){
      myIsUserInRoleParameter0Values.addExpected(arg0);
   }
   public boolean isUserInRole(String arg0){
      myIsUserInRoleCalls.inc();
      myIsUserInRoleParameter0Values.addActual(arg0);
      Object nextReturnValue = myActualIsUserInRoleReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsUserInRole(Throwable arg){
      myActualIsUserInRoleReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsUserInRole(boolean arg){
      myActualIsUserInRoleReturnValues.add(new Boolean(arg));
   }
   public void setExpectedGetRequestedSessionIdCalls(int calls){
      myGetRequestedSessionIdCalls.setExpected(calls);
   }
   public String getRequestedSessionId(){
      myGetRequestedSessionIdCalls.inc();
      Object nextReturnValue = myActualGetRequestedSessionIdReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRequestedSessionId(Throwable arg){
      myActualGetRequestedSessionIdReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRequestedSessionId(String arg){
      myActualGetRequestedSessionIdReturnValues.add(arg);
   }
   public void setExpectedGetRequestURICalls(int calls){
      myGetRequestURICalls.setExpected(calls);
   }
   public String getRequestURI(){
      myGetRequestURICalls.inc();
      Object nextReturnValue = myActualGetRequestURIReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetRequestURI(Throwable arg){
      myActualGetRequestURIReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetRequestURI(String arg){
      myActualGetRequestURIReturnValues.add(arg);
   }
   public void setExpectedGetSitemapURICalls(int calls){
      myGetSitemapURICalls.setExpected(calls);
   }
   public String getSitemapURI(){
      myGetSitemapURICalls.inc();
      Object nextReturnValue = myActualGetSitemapURIReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetSitemapURI(Throwable arg){
      myActualGetSitemapURIReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetSitemapURI(String arg){
      myActualGetSitemapURIReturnValues.add(arg);
   }
   public void setExpectedGetServletPathCalls(int calls){
      myGetServletPathCalls.setExpected(calls);
   }
   public String getServletPath(){
      myGetServletPathCalls.inc();
      Object nextReturnValue = myActualGetServletPathReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (String) nextReturnValue;
   }
   public void setupExceptionGetServletPath(Throwable arg){
      myActualGetServletPathReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetServletPath(String arg){
      myActualGetServletPathReturnValues.add(arg);
   }
   public void setExpectedGetSessionBooleanCalls(int calls){
      myGetSessionBooleanCalls.setExpected(calls);
   }
   public void addExpectedGetSessionBooleanValues(boolean arg0){
      myGetSessionBooleanParameter0Values.addExpected(new Boolean(arg0));
   }
   public Session getSession(boolean arg0){
      myGetSessionBooleanCalls.inc();
      myGetSessionBooleanParameter0Values.addActual(new Boolean(arg0));
      Object nextReturnValue = myActualGetSessionBooleanReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Session) nextReturnValue;
   }
   public void setupExceptionGetSessionBoolean(Throwable arg){
      myActualGetSessionBooleanReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetSessionBoolean(Session arg){
      myActualGetSessionBooleanReturnValues.add(arg);
   }
   public void setExpectedGetSessionCalls(int calls){
      myGetSessionCalls.setExpected(calls);
   }
   public Session getSession(){
      myGetSessionCalls.inc();
      Object nextReturnValue = myActualGetSessionReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return (Session) nextReturnValue;
   }
   public void setupExceptionGetSession(Throwable arg){
      myActualGetSessionReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupGetSession(Session arg){
      myActualGetSessionReturnValues.add(arg);
   }
   public void setExpectedIsRequestedSessionIdValidCalls(int calls){
      myIsRequestedSessionIdValidCalls.setExpected(calls);
   }
   public boolean isRequestedSessionIdValid(){
      myIsRequestedSessionIdValidCalls.inc();
      Object nextReturnValue = myActualIsRequestedSessionIdValidReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsRequestedSessionIdValid(Throwable arg){
      myActualIsRequestedSessionIdValidReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsRequestedSessionIdValid(boolean arg){
      myActualIsRequestedSessionIdValidReturnValues.add(new Boolean(arg));
   }
   public void setExpectedIsRequestedSessionIdFromCookieCalls(int calls){
      myIsRequestedSessionIdFromCookieCalls.setExpected(calls);
   }
   public boolean isRequestedSessionIdFromCookie(){
      myIsRequestedSessionIdFromCookieCalls.inc();
      Object nextReturnValue = myActualIsRequestedSessionIdFromCookieReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsRequestedSessionIdFromCookie(Throwable arg){
      myActualIsRequestedSessionIdFromCookieReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsRequestedSessionIdFromCookie(boolean arg){
      myActualIsRequestedSessionIdFromCookieReturnValues.add(new Boolean(arg));
   }
   public void setExpectedIsRequestedSessionIdFromURLCalls(int calls){
      myIsRequestedSessionIdFromURLCalls.setExpected(calls);
   }
   public boolean isRequestedSessionIdFromURL(){
      myIsRequestedSessionIdFromURLCalls.inc();
      Object nextReturnValue = myActualIsRequestedSessionIdFromURLReturnValues.getNext();
      if (nextReturnValue instanceof ExceptionalReturnValue && ((ExceptionalReturnValue)nextReturnValue).getException() instanceof RuntimeException)
          throw (RuntimeException)((ExceptionalReturnValue)nextReturnValue).getException();
      return ((Boolean) nextReturnValue).booleanValue();
   }
   public void setupExceptionIsRequestedSessionIdFromURL(Throwable arg){
      myActualIsRequestedSessionIdFromURLReturnValues.add(new ExceptionalReturnValue(arg));
   }
   public void setupIsRequestedSessionIdFromURL(boolean arg){
      myActualIsRequestedSessionIdFromURLReturnValues.add(new Boolean(arg));
   }
   public void verify(){
      myGetCalls.verify();
      myGetParameter0Values.verify();
      myGetAttributeCalls.verify();
      myGetAttributeParameter0Values.verify();
      myGetAttributeNamesCalls.verify();
      myGetAuthTypeCalls.verify();
      myGetCharacterEncodingCalls.verify();
      mySetCharacterEncodingCalls.verify();
      mySetCharacterEncodingParameter0Values.verify();
      myGetContentLengthCalls.verify();
      myGetContentTypeCalls.verify();
      myGetParameterCalls.verify();
      myGetParameterParameter0Values.verify();
      myGetParameterNamesCalls.verify();
      myGetParameterValuesCalls.verify();
      myGetParameterValuesParameter0Values.verify();
      myGetProtocolCalls.verify();
      myGetSchemeCalls.verify();
      myGetServerNameCalls.verify();
      myGetServerPortCalls.verify();
      myGetRemoteAddrCalls.verify();
      myGetRemoteHostCalls.verify();
      mySetAttributeCalls.verify();
      mySetAttributeParameter0Values.verify();
      mySetAttributeParameter1Values.verify();
      myRemoveAttributeCalls.verify();
      myRemoveAttributeParameter0Values.verify();
      myGetLocaleCalls.verify();
      myGetLocalesCalls.verify();
      myIsSecureCalls.verify();
      myGetCookiesCalls.verify();
      myGetCookieMapCalls.verify();
      myGetDateHeaderCalls.verify();
      myGetDateHeaderParameter0Values.verify();
      myGetHeaderCalls.verify();
      myGetHeaderParameter0Values.verify();
      myGetHeadersCalls.verify();
      myGetHeadersParameter0Values.verify();
      myGetHeaderNamesCalls.verify();
      myGetMethodCalls.verify();
      myGetPathInfoCalls.verify();
      myGetPathTranslatedCalls.verify();
      myGetContextPathCalls.verify();
      myGetQueryStringCalls.verify();
      myGetRemoteUserCalls.verify();
      myGetUserPrincipalCalls.verify();
      myIsUserInRoleCalls.verify();
      myIsUserInRoleParameter0Values.verify();
      myGetRequestedSessionIdCalls.verify();
      myGetRequestURICalls.verify();
      myGetSitemapURICalls.verify();
      myGetServletPathCalls.verify();
      myGetSessionBooleanCalls.verify();
      myGetSessionBooleanParameter0Values.verify();
      myGetSessionCalls.verify();
      myIsRequestedSessionIdValidCalls.verify();
      myIsRequestedSessionIdFromCookieCalls.verify();
      myIsRequestedSessionIdFromURLCalls.verify();
   }
}
