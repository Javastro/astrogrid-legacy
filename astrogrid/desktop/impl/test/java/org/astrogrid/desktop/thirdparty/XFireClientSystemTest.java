/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import java.io.StringReader;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import junit.framework.TestCase;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.handler.AbstractHandler;
import org.codehaus.xfire.handler.Phase;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.MessageBindingProvider;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.Transport;
import org.codehaus.xfire.transport.http.SoapHttpTransport;
import org.codehaus.xfire.util.STAXUtils;

/** here I'm trying various experiments to get xfire working.
 * @author Noel Winstanley
 * @since Jul 26, 20061:15:38 AM
 */
public class XFireClientSystemTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}
//	//has difficulties with relative links.
//	public static String regWsdlURL = "http://software.astrogrid.org/schema/registry/RegistrySearch/v0.1/RegistrySearch.wsdl";
//	public static String endpointURL = "http://cdsws.u-strasbg.fr/axis/services/Sesame"; 
//		public static String wsdlURL = endpointURL + "?wsdl";
//	// vanilla approach - params in, string out.
//	public void omitTestCreateVanillaWSDLClient() throws Exception {
//		Client c = new Client(new URL(wsdlURL));
//		assertNotNull(c);
//		Object[] result = c.invoke("SesameXML",new Object[]{"m32"});
//		assertNotNull(result);
//		assertEquals(1,result.length);
//		assertTrue(result[0] instanceof String);
//	}
//	
//	// try asking it for a xstream out.. - fails (while sending..)
//	/*
//	 * org.codehaus.xfire.fault.XFireFault: Fault: java.lang.NullPointerException
//	at org.codehaus.xfire.fault.XFireFault.createFault(XFireFault.java:89)
//	at org.codehaus.xfire.client.Client.onReceive(Client.java:386)
//	at org.codehaus.xfire.transport.http.HttpChannel.sendViaClient(HttpChannel.java:134)
//	at org.codehaus.xfire.transport.http.HttpChannel.send(HttpChannel.java:48)
//	at org.codehaus.xfire.handler.OutMessageSender.invoke(OutMessageSender.java:26)
//	at org.codehaus.xfire.handler.HandlerPipeline.invoke(HandlerPipeline.java:130)
//	at org.codehaus.xfire.client.Invocation.invoke(Invocation.java:75)
//	at org.codehaus.xfire.client.Client.invoke(Client.java:335)
//	at org.codehaus.xfire.client.Client.invoke(Client.java:349)
//	at org.astrogrid.desktop.modules.ag.XFireClientUnitTest.testCreateMessageBindingWSDLClient(XFireClientUnitTest.java:44)
//	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
//	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
//	at java.lang.reflect.Method.invoke(Method.java:324)
//	at junit.framework.TestCase.runTest(TestCase.java:154)
//	at junit.framework.TestCase.runBare(TestCase.java:127)
//	at junit.framework.TestResult$1.protect(TestResult.java:106)
//	at junit.framework.TestResult.runProtected(TestResult.java:124)
//	at junit.framework.TestResult.run(TestResult.java:109)
//	at junit.framework.TestCase.run(TestCase.java:118)
//	at junit.framework.TestSuite.runTest(TestSuite.java:208)
//	at junit.framework.TestSuite.run(TestSuite.java:203)
//	at org.eclipse.jdt.internal.junit.runner.junit3.JUnit3TestReference.run(JUnit3TestReference.java:128)
//	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:460)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:673)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:386)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:196)
//Caused by: java.lang.NullPointerException
//	at org.codehaus.xfire.service.binding.MessageBindingProvider.readParameter(MessageBindingProvider.java:55)
//	at org.codehaus.xfire.service.binding.RPCBinding.readMessage(RPCBinding.java:91)
//	at org.codehaus.xfire.soap.handler.SoapBodyHandler.invoke(SoapBodyHandler.java:42)
//	at org.codehaus.xfire.handler.HandlerPipeline.invoke(HandlerPipeline.java:130)
//	at org.codehaus.xfire.client.Client.onReceive(Client.java:382)
//	... 26 more
//
//
//	 */
//	public void omitTestCreateMessageBindingWSDLClient() throws Exception {
//		Client c = new Client(new URL(wsdlURL));
//		assertNotNull(c);
//		BindingProvider bp = new MessageBindingProvider();
//		c.getService().setBindingProvider(bp);
//		Object[] result = c.invoke("SesameXML",new Object[]{"m32"});
//		assertNotNull(result);
//		assertEquals(1,result.length);
//		assertTrue(result[0] instanceof XMLStreamReader); 
//	}
///// second attempt - all based on solution in email.
//	
	// first approach suggested in email - think it means I need to write my soap totally by hand. yuk.
	public static interface RegistryQuery {
			public XMLStreamReader Search (XMLStreamReader reader);
			public XMLStreamReader XQuerySearch(XMLStreamReader reader);
			public XMLStreamReader KeywordSearch(XMLStreamReader reader);
			// more to come..
	}
//	
	
	public static final String galahadURL = "http://galahad.star.le.ac.uk:8081/astrogrid-registry/services/RegistryQuery";
	//public static final String galahadURL = "http://localhost:1066/astrogrid-registry/services/RegistryQuery";
///** soliution from email - fails on some index out of bounds error - odd 
// * 
//org.codehaus.xfire.fault.XFireFault: Index: 1, Size: 1
//	at org.codehaus.xfire.fault.XFireFault.createFault(XFireFault.java:89)
//	at org.codehaus.xfire.client.Client.onReceive(Client.java:386)
//	at org.codehaus.xfire.transport.http.HttpChannel.sendViaClient(HttpChannel.java:134)
//	at org.codehaus.xfire.transport.http.HttpChannel.send(HttpChannel.java:48)
//	at org.codehaus.xfire.handler.OutMessageSender.invoke(OutMessageSender.java:26)
//	at org.codehaus.xfire.handler.HandlerPipeline.invoke(HandlerPipeline.java:130)
//	at org.codehaus.xfire.client.Invocation.invoke(Invocation.java:75)
//	at org.codehaus.xfire.client.Client.invoke(Client.java:335)
//	at org.codehaus.xfire.client.Client.invoke(Client.java:349)
//	at org.astrogrid.desktop.modules.ag.XFireClientUnitTest.testEmailedSolutionClient(XFireClientUnitTest.java:134)
//	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
//	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
//	at java.lang.reflect.Method.invoke(Method.java:324)
//	at junit.framework.TestCase.runTest(TestCase.java:154)
//	at junit.framework.TestCase.runBare(TestCase.java:127)
//	at junit.framework.TestResult$1.protect(TestResult.java:106)
//	at junit.framework.TestResult.runProtected(TestResult.java:124)
//	at junit.framework.TestResult.run(TestResult.java:109)
//	at junit.framework.TestCase.run(TestCase.java:118)
//	at junit.framework.TestSuite.runTest(TestSuite.java:208)
//	at junit.framework.TestSuite.run(TestSuite.java:203)
//	at org.eclipse.jdt.internal.junit.runner.junit3.JUnit3TestReference.run(JUnit3TestReference.java:128)
//	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:460)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:673)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:386)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:196)
//Caused by: java.lang.IndexOutOfBoundsException: Index: 1, Size: 1
//	at java.util.LinkedList.entry(LinkedList.java:360)
//	at java.util.LinkedList.get(LinkedList.java:303)
//	at java.util.Collections$UnmodifiableList.get(Collections.java:1139)
//	at org.codehaus.xfire.service.binding.AbstractBinding.read(AbstractBinding.java:189)
//	at org.codehaus.xfire.service.binding.DocumentBinding.readMessage(DocumentBinding.java:32)
//	at org.codehaus.xfire.soap.handler.SoapBodyHandler.invoke(SoapBodyHandler.java:42)
//	at org.codehaus.xfire.handler.HandlerPipeline.invoke(HandlerPipeline.java:130)
//	at org.codehaus.xfire.client.Client.onReceive(Client.java:382)
//	... 26 more
//
//
// * */
//	public void testEmailedSolutionClient() throws Exception {
//		ObjectServiceFactory osf = new ObjectServiceFactory();
//		osf.setStyle("document"); //message?
//		Service serv = osf.create(RegistryQuery.class,"RegistryQuery","http://www.ivoa.net/wsdl/RegistrySearch",null);
//		Transport t = new SoapHttpTransport();
//		Client c= new Client(t,serv, galahadURL);
//		assertNotNull(c);
//		XMLStreamReader input = STAXUtils.createXMLStreamReader(new StringReader("<s:KeywordSearch xmlns:s='http://www.ivoa.net/wsdl/RegistrySearch/v1.0'>" +
//				 "<s:keywords>registry</s:keywords><s:orValues>true</s:orValues>" +
//				"</s:KeywordSearch>"));
//		
//		Object[] result = c.invoke("KeywordSearch",new Object[]{input});
//		assertNotNull(result);
//		assertEquals(1,result.length);
//		assertTrue(result[0] instanceof XMLStreamReader); 		
//
//	}
//	
//
//	/** variation of above - use message binding. gets further, but stream is closed  - odd 
//	 * 
//	 * java.lang.RuntimeException: [was class java.io.IOException] Attempted read on closed stream.
//	at com.ctc.wstx.util.ExceptionUtil.throwRuntimeException(ExceptionUtil.java:18)
//	at com.ctc.wstx.sr.StreamScanner.throwLazyError(StreamScanner.java:673)
//	at com.ctc.wstx.sr.BasicStreamReader.safeFinishToken(BasicStreamReader.java:3471)
//	at com.ctc.wstx.sr.BasicStreamReader.getText(BasicStreamReader.java:776)
//	at org.codehaus.xfire.util.STAXUtils.copy(STAXUtils.java:146)
//	at org.astrogrid.desktop.modules.ag.XFireClientUnitTest.testMessageClient(XFireClientUnitTest.java:160)
//	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
//	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
//	at java.lang.reflect.Method.invoke(Method.java:324)
//	at junit.framework.TestCase.runTest(TestCase.java:154)
//	at junit.framework.TestCase.runBare(TestCase.java:127)
//	at junit.framework.TestResult$1.protect(TestResult.java:106)
//	at junit.framework.TestResult.runProtected(TestResult.java:124)
//	at junit.framework.TestResult.run(TestResult.java:109)
//	at junit.framework.TestCase.run(TestCase.java:118)
//	at junit.framework.TestSuite.runTest(TestSuite.java:208)
//	at junit.framework.TestSuite.run(TestSuite.java:203)
//	at org.eclipse.jdt.internal.junit.runner.junit3.JUnit3TestReference.run(JUnit3TestReference.java:128)
//	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:460)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:673)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:386)
//	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:196)
//Caused by: java.io.IOException: Attempted read on closed stream.
//	at org.apache.commons.httpclient.AutoCloseInputStream.isReadAllowed(AutoCloseInputStream.java:165)
//	at org.apache.commons.httpclient.AutoCloseInputStream.read(AutoCloseInputStream.java:125)
//	at com.ctc.wstx.io.UTF8Reader.loadMore(UTF8Reader.java:354)
//	at com.ctc.wstx.io.UTF8Reader.read(UTF8Reader.java:110)
//	at com.ctc.wstx.io.MergedReader.read(MergedReader.java:101)
//	at com.ctc.wstx.io.ReaderSource.readInto(ReaderSource.java:84)
//	at com.ctc.wstx.io.BranchingReaderSource.readInto(BranchingReaderSource.java:57)
//	at com.ctc.wstx.sr.StreamScanner.loadMore(StreamScanner.java:934)
//	at com.ctc.wstx.sr.BasicStreamReader.readTextSecondary(BasicStreamReader.java:4362)
//	at com.ctc.wstx.sr.BasicStreamReader.readCoalescedText(BasicStreamReader.java:3930)
//	at com.ctc.wstx.sr.BasicStreamReader.finishToken(BasicStreamReader.java:3509)
//	at com.ctc.wstx.sr.BasicStreamReader.safeFinishToken(BasicStreamReader.java:3469)
//	... 21 more
//
//
//	 * */
//	public void testMessageClient() throws Exception {
//		ObjectServiceFactory osf = new ObjectServiceFactory(new MessageBindingProvider());
//		osf.setStyle("message");
//		Service serv = osf.create(RegistryQuery.class,"RegistryQuery","http://www.ivoa.net/wsdl/RegistrySearch",null);
//		Transport t = new SoapHttpTransport();
//		Client c= new Client(t,serv, galahadURL);
//		assertNotNull(c);
//		XMLStreamReader input = STAXUtils.createXMLStreamReader(new StringReader("<s:KeywordSearch xmlns:s='http://www.ivoa.net/wsdl/RegistrySearch/v1.0'>" +
//				 "<s:keywords>registry</s:keywords><s:orValues>true</s:orValues>" +
//				"</s:KeywordSearch>"));
//		Object[] result = c.invoke("KeywordSearch",new Object[]{input});
//		assertNotNull(result);
//		assertEquals(1,result.length);
//		assertTrue(result[0] instanceof XMLStreamReader); 		
//		XMLStreamReader r = (XMLStreamReader)result[0];				
//		XMLOutputFactory fac = XMLOutputFactory.newInstance();
//		XMLStreamWriter w = fac.createXMLStreamWriter(System.out);
//		STAXUtils.copy(r,w);		
//
//		
//	}
	
	/** so, add in a handler - get to result before it closes.
	 * WORKS!! a little odd mind you. */
	public void testHandlerClient() throws Exception {
		ObjectServiceFactory osf = new ObjectServiceFactory(new MessageBindingProvider());
		osf.setStyle("message");
		Service serv = osf.create(RegistryQuery.class,"RegistryQuery","http://www.ivoa.net/wsdl/RegistrySearch",null);
		Transport t = new SoapHttpTransport();
		Client c= new Client(t,serv, galahadURL);
		assertNotNull(c);
		XMLStreamReader input = STAXUtils.createXMLStreamReader(new StringReader("<s:KeywordSearch xmlns:s='http://www.ivoa.net/wsdl/RegistrySearch/v1.0'>" +
				 "<s:keywords>registry</s:keywords><s:orValues>true</s:orValues>" +
				"</s:KeywordSearch>"));
		c.addInHandler(new AbstractHandler() {
			{
				setPhase(Phase.SERVICE);
			}
			public void invoke(MessageContext arg0) throws Exception {
				List l = (List)arg0.getCurrentMessage().getBody();
				assertEquals(1,l.size());
				Object o = l.get(0);
				assertTrue(o instanceof XMLStreamReader);
				XMLStreamReader r = (XMLStreamReader)o;				
				XMLOutputFactory fac = XMLOutputFactory.newInstance();
				XMLStreamWriter w = fac.createXMLStreamWriter(System.out);
				STAXUtils.copy(r,w);
				w.close();
			}
		});
		Object[] result = c.invoke("KeywordSearch",new Object[]{input});
		assertNotNull(result);
		assertEquals(1,result.length);
		assertTrue(result[0] instanceof XMLStreamReader); 		


	}
	
//	
//	// older apporach..
//	
//	public static class ReturnMessageHandler extends AbstractHandler {
//		public ReturnMessageHandler() {
//			setPhase(Phase.DISPATCH); // after correlator handler, but before the ClientReceive Handler.
//		}
//		
//		public void invoke(MessageContext context) throws Exception {
//			// rats - unable to remove a previous handler.
//			//wonder if there's a way to halt the pipeline. - seems not.
//		//	context.getInPipeline().
//		}
//	}
//	
//	/** existing handler for incoming messages - the correlator - we want to keep.
//	 * as can't replicate or extend - has private members.
//	 * however, the correlator registers a continuationhandler which we want to 
//	 * override. Wonder whether it's possible to remove the registered handler 
//	 * - or abort processing of the pipeline befiore it gets to this hand.er
//	 * @throws Exception
//	 */
//	public void omitTestCreateHandlerDrivenServiceClient() throws Exception {
//		Client c = new Client(new URL(wsdlURL));
//		assertNotNull(c);
//		List l = c.getInHandlers();
//		for (int i = 0; i < l.size(); i++) {
//			if (l.get(i) instanceof CorrelatorHandler) { // this is the one we want to replace
//			//	Handler myCorrelatorHandler = 
//			//	l.set(i,myCorrelatorHandler);
//			}
//		}
//		c.setInHandlers(l);
//		Object[] result = c.invoke("SesameXML",new Object[]{"m32"});
//		
//		assertNotNull(result);
//		assertEquals(1,result.length);
//		assertTrue(result[0] instanceof String);
//		
//	}
//	
//	/** try building services from wsdl by hand, and then wrapping a client around it */
//	public void omitTestHandRollClient() throws Exception {
//		// needs a url anchor in the same jar...
//		URL base =SchemaMap.class.getResource("/schema/registry/RegistrySearch/v0.1/Search.wsdl");
//		assertNotNull(base);
//		WSDLServiceBuilder builder = new WSDLServiceBuilder(base.toString(), base.openStream());
//		// later - adjust to messageBining provider first..
//		//builder.setBindingProvider(new MessageBindingProvider());
//		builder.build();
//		List l = builder.getAllServices();
//		assertEquals(1,l.size());
//	//	System.out.println(builder.getServices());
//		// try building client around this..
//		Client c = new Client(new SoapHttpTransport(),(Service)l.get(0),endpointURL);
//		XMLStreamReader r = STAXUtils.createXMLStreamReader(new StringReader("<s:SesameXMLRequest xmlns:s='urn:Sesame'>m32</s:SesameXMLRequest>"));
//		Object[] result = c.invoke("SesameXML",new Object[]{r});
//		assertNotNull(result);
//		assertEquals(1,result.length);
//		assertTrue(result[0] instanceof String);		
//		System.out.println(result[0]);
//	}
//	
}


