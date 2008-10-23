/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import static org.easymock.EasyMock.*;

import java.net.URI;
import java.security.PrivateKey;

import junit.framework.TestCase;

import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

/** Unit test for cea helper.
 * @author Noel Winstanley
 * @since Jun 13, 20064:37:12 PM
 */
public class CeaHelperUnitTest extends TestCase {


    protected void setUp() throws Exception {
		reg = createMock(Registry.class);
		community = createMock(CommunityInternal.class);
		cea = new CeaHelper(reg,community);
		serverId = new URI("ivo://wibble/bing");
		endpoint = new URI("http://www.slashdot.org");
        service = createMock(CeaService.class);
	}
	
	protected CommunityInternal community;
	protected Registry reg;
	protected CeaHelper cea;
	protected URI serverId;
	protected URI endpoint;
    private CeaService service;
	
	protected void tearDown() throws Exception {
		super.tearDown();
		reg = null;
		cea = null;
		serverId = null;
		endpoint = null;
	}
	
	private void replayAll() {
	    replay(reg,community,service);
	}

	   private void verifyAll() {
	        verify(reg,community,service);
	    }
	
	   /** test all cases that the resource passed in could be faulty */
	   public void testCreateDelegateFaultyInput() throws Exception {
	       // null input
        try {
            cea.createCEADelegate((CeaService)null);
            fail("expected to fail");
        } catch (final IllegalArgumentException e) {
            //ok
        }
        // serice with no capabilties
        doTestAndExpectToFail(new Capability[]{});

        //no cea capabilities
        doTestAndExpectToFail(new Capability[]{
                    new Capability()
            });
       
        // cea capability with no interfaces

        doTestAndExpectToFail(new Capability[]{
                new Capability() {{
                    setStandardID(URI.create(StandardIds.CEA_1_0));
                    setInterfaces(new Interface[]{                            
                    });
                }}
        });           
        // no accessurl
        doTestAndExpectToFail(new Capability[]{
                new Capability() {{
                    setStandardID(URI.create(StandardIds.CEA_1_0));
                    setInterfaces(new Interface[]{
                            new Interface() {{
                                setAccessUrls(new AccessURL[]{
                                });
                            }}
                    });
                }}
        });        
        // no value for accessURL
        doTestAndExpectToFail(new Capability[]{
                new Capability() {{
                    setStandardID(URI.create(StandardIds.CEA_1_0));
                    setInterfaces(new Interface[]{
                            new Interface() {{
                                setAccessUrls(new AccessURL[]{
                                        new AccessURL() 
                                });
                            }}
                    });
                }}
        });
          
    }
	   
	   private void doTestAndExpectToFail(final Capability[] caps) throws CEADelegateException {
	       reset(service);
	       expect(service.getCapabilities()).andStubReturn(caps);
	       replay(service);
	       try {
	           cea.createCEADelegate(service);            
	           fail("expected to fail");
	       } catch (final IllegalArgumentException e) {
	           //ok
	       }      	    
	   }
	   
	public void testCreateDelegate() throws Exception {
	    // a guard that should fail.
	    final SecurityGuard sec1 = new SecurityGuard() {
	        public java.security.cert.X509Certificate[] getCertificateChain() {
	            throw new RuntimeException("e");
	        }
	    };
	    // a guard that should succeed - returns anything.
        final SecurityGuard sec2 = new SecurityGuard() {
            public java.security.cert.X509Certificate[] getCertificateChain() {
               return null;
            }
            @Override
            public PrivateKey getPrivateKey() {
                return null;
            }
        };        
        expect(community.isLoggedIn()).andReturn(false); // at first we're not logged in.
        expect(community.isLoggedIn()).andReturn(true).times(2);
        expect(community.getSecurityGuard()).andReturn(sec1); // first time no dig sig
        expect(community.getSecurityGuard()).andReturn(sec2).times(2); // second time a dig sig
        
        // skeleton resource object.
        final Capability[] caps = new Capability[]{
                new Capability() {{
                    setStandardID(URI.create(StandardIds.CEA_1_0));
                    setInterfaces(new Interface[]{
                            new Interface() {{
                                setAccessUrls(new AccessURL[]{
                                        new AccessURL() {{
                                            setValueURI(endpoint);
                                        }}
                                });
                            }}
                    });
                }}
        };                                           
        expect(service.getCapabilities()).andStubReturn(caps);
        replayAll();
        CommonExecutionConnectorClient client = cea.createCEADelegate(service);
        verifyDelegate(client);
        // no way of checking whether credentials are set or not.
        client = cea.createCEADelegate(service);
        verifyDelegate(client);
        client = cea.createCEADelegate(service);
        verifyDelegate(client);      
        verifyAll();
    }

    /**
     * @param client
     */
    private void verifyDelegate(final CommonExecutionConnectorClient client) {
        assertNotNull(client);
        assertEquals(endpoint.toString(),client.getTargetEndPoint());
    }
	


	
	public void testParseTool() throws Exception {
	    // create a tool document
        final Tool t = new Tool();
        t.setInput(new Input());
        t.setOutput(new Output());
        t.setInterface("iface");
        t.setName("ivo://org.astrogrid.Galaxev"); // erroneous
        
        assertTrue(t.isValid());
        // convert it to a document..
        Document document = DomHelper.newDocument();
        Marshaller.marshal(t,document);
        Tool t1 = cea.parseTool(document);
        assertEquals(t.getName(),"ivo://" + t1.getName());
        
        t.setName("org.astrogrid.Galaxev"); // correct
        
        assertTrue(t.isValid());
        // convert it to a document..
        document = DomHelper.newDocument();
        Marshaller.marshal(t,document);
        t1 = cea.parseTool(document);
        assertEquals(t.getName(),t1.getName());
    }
	
	// can't test - as comunitrAccountSpaceResolver is created inline, and so can't be mocked.
//	   public void testMakeMyspaceIvornsConcrete() throws Exception {
//	        // create a tool document
//	        Tool t = new Tool();
//	        t.setInput(new Input());
//	        t.setOutput(new Output());
//	        t.setInterface("iface");
//	        t.setName("org.astrogrid.Galaxev"); // correct
//	        
//	        ParameterValue pv = new ParameterValue();
//	        pv.setIndirect(true);
//	        pv.setName("foo");
//	        t.getInput().addParameter(pv);
//	        
//	      
//	        
//	        
//	        assertTrue(t.isValid());
//	        // convert it to a document..
//	        Tool t1 = cea.makeMySpaceIvornsConcrete(t);
//	    
//	    }
	
}
