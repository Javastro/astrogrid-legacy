package org.tools.wstool;

import org.xml.sax.*;
import java.util.ArrayList;


public class ConfigFileReader extends org.xml.sax.helpers.DefaultHandler {

	private final String TESTCASE_ELEM = "TestCase";
	private final String WEBSERVICE_ELEM = "WebServiceTest";
	private final String TESTSERVICE_ELEM = "TestWebServices";
	private final String THREAD_ELEM = "Thread";
	private final String RUNNABLE_ELEM = "Runnable";
	
	private final String NUMBER_ATTR = "number";
	private final String LOOP_ATTR = "loop";
	private final String NAME_ATTR = "name";
	private final String TYPE_ATTR = "type";
	private final String FULLNAME_ATTR = "fullclassname";
	private final String THREADSTYLE_ATTR = "threadstyle";
	
	ArrayList webServices = new ArrayList();
	ServiceTest currServiceTest = null;
	ServiceTestCase currServiceTestCase = null;
	TestCaseThreads currTestCaseThreads = null;	
	public void startElement(String namespace, String localName, String fullName, Attributes atts) throws SAXException {

			String val;
			if(TESTSERVICE_ELEM.equals(localName) || TESTSERVICE_ELEM.equals(fullName) ) {
				//ignore
			}else if(WEBSERVICE_ELEM.equals(localName) || WEBSERVICE_ELEM.equals(fullName)) {
				currServiceTest = new ServiceTest();
    			if( (val = atts.getValue(NAME_ATTR)) == null || val.length() <= 0) { 
    				throwSaxException(NAME_ATTR,WEBSERVICE_ELEM);
    			}
    			currServiceTest.setName(val);
				if( (val = atts.getValue(FULLNAME_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(FULLNAME_ATTR,WEBSERVICE_ELEM);
				}
				currServiceTest.setFullClassName(val);
				if( (val = atts.getValue(THREADSTYLE_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(THREADSTYLE_ATTR,WEBSERVICE_ELEM);
				}
				currServiceTest.setThreadStyle(val);				
			}else if(RUNNABLE_ELEM.equals(localName) || RUNNABLE_ELEM.equals(fullName)) {
				//ignore
			}else if(TESTCASE_ELEM.equals(localName) || TESTCASE_ELEM.equals(fullName)) {
				if(currServiceTestCase != null) {
					currServiceTest.addTestCase(currServiceTestCase);
					currServiceTestCase = null;
				}
				if( (val = atts.getValue(NAME_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(NAME_ATTR,TESTCASE_ELEM);
				}
				currServiceTestCase = new ServiceTestCase(val);
				if( (val = atts.getValue(LOOP_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(LOOP_ATTR,TESTCASE_ELEM);
				}
				currServiceTestCase.setLoop(new Integer(val).intValue());
				if( (val = atts.getValue(THREADSTYLE_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(THREADSTYLE_ATTR,WEBSERVICE_ELEM);
				}
				currServiceTestCase.setThreadStyle(val);				
    		}else if(THREAD_ELEM.equals(localName) || THREAD_ELEM.equals(fullName)) {
    			if(currTestCaseThreads != null) {
    				currServiceTestCase.addThread(currTestCaseThreads);
    				currTestCaseThreads = null;
    			}
				if( (val = atts.getValue(NUMBER_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(NUMBER_ATTR,THREAD_ELEM); 
				}
				currTestCaseThreads = new TestCaseThreads();
				currTestCaseThreads.setConCurrentThreads(new Integer(val).intValue());
				if( (val = atts.getValue(LOOP_ATTR)) == null || val.length() <= 0) {
					throwSaxException(LOOP_ATTR,THREAD_ELEM);
				}
				currTestCaseThreads.setLoop(new Integer(val).intValue());
				if( (val = atts.getValue(THREADSTYLE_ATTR)) == null || val.length() <= 0) { 
					throwSaxException(THREADSTYLE_ATTR,WEBSERVICE_ELEM);
				}
				currTestCaseThreads.setThreadStyle(val);				
    		}
	}
	
	public ArrayList getWebServices() {
		return this.webServices;
	}
	
	public void throwSaxException(String attr,String elem) throws SAXException{
		throw new SAXException(attr + "Attribute is required for " + elem + "element");
	}
	
	public void endElement(String namespace, String localName, String fullName) throws SAXException{
		if(WEBSERVICE_ELEM.equals(localName) || WEBSERVICE_ELEM.equals(fullName)) {
			if(currServiceTest != null) {
				webServices.add(currServiceTest);
			}
		}else if(TESTCASE_ELEM.equals(localName) || TESTCASE_ELEM.equals(fullName)) {
			if(currServiceTestCase != null) {
				currServiceTest.addTestCase(currServiceTestCase);
				currServiceTestCase = null;
			}
		}else if(THREAD_ELEM.equals(localName) || THREAD_ELEM.equals(fullName)) {
			if(currTestCaseThreads != null) {
				currServiceTestCase.addThread(currTestCaseThreads);
				currTestCaseThreads = null;
			}
		}//elseif
	}	//endElement
}