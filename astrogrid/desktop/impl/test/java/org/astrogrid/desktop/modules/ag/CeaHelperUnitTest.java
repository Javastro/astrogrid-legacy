/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import static org.easymock.EasyMock.*;

/** Unit test for cea helper.
 * @author Noel Winstanley
 * @since Jun 13, 20064:37:12 PM
 * @TEST complete.
 */
public class CeaHelperUnitTest extends TestCase {

	protected void setUp() throws Exception {
		reg = createMock(Registry.class);
		community = createMock(CommunityInternal.class);
		cea = new CeaHelper(reg,community);
		serverId = new URI("ivo://wibble/bing");
		endpoint = new URL("http://www.slashdot.org");
	}
	
	protected CommunityInternal community;
	protected Registry reg;
	protected CeaHelper cea;
	protected URI serverId;
	protected URL endpoint;
	
	protected void tearDown() throws Exception {
		super.tearDown();
		reg = null;
		cea = null;
		serverId = null;
		endpoint = null;
	}
	
	/* @todo 
	
	public void testCreateDelegate() throws MalformedURLException {
		ResourceInformation ri = new ResourceInformation(serverId,null,null,endpoint,null);
		CommonExecutionConnectorClient c = cea.createCEADelegate(ri);
		assertNotNull(c);
		assertEquals(endpoint.toString(),c.getTargetEndPoint());
	}
	*/
	public void testCreateDelegateNoAccessURL() throws Exception{
		try {
			cea.createCEADelegate((CeaService)null);
			fail("expected to chuck");
		} catch (IllegalArgumentException e) {
		}
		/* todo
		ResourceInformation ri = new ResourceInformation(serverId,null,null,null,null);
		try {
			cea.createCEADelegate(ri);
			fail("expected to chuck");
		} catch (IllegalArgumentException e) {
		}		
		*/
	}
	
	/** basically tests that we can round trip - between endpoint/id and execId */
	/* @todo 
	public void testCreateDelegateExecId() throws NotFoundException, ServiceException, NotApplicableException {
		reg.resolveIdentifier(serverId);
		regControl.setReturnValue(endpoint);
		regControl.replay();
		URI execId = cea.mkRemoteTaskURI("fred",new ResourceInformation(serverId,null,null,null,null));
		CommonExecutionConnectorClient client = cea.createCEADelegate(execId);
		assertNotNull(execId);
		assertEquals(endpoint.toString(),client.getTargetEndPoint());
	}
	*/
	
	public void testMkLocalTaskURI() throws ServiceException {
		String appId = "fred";
		URI u = cea.mkLocalTaskURI(appId);
		assertNotNull(u);
		assertEquals("local",u.getScheme());
		assertEquals("//",u.getSchemeSpecificPart());
		assertEquals(appId,u.getFragment());
		assertEquals(appId,cea.getAppId(u));
	}
/* @todo 	
	public void testMkRemoteTaskURI() throws ServiceException, URISyntaxException {
		String appId = "fred";

		ResourceInformation server = new ResourceInformation(serverId,null,null,null,null);
		URI u = cea.mkRemoteTaskURI(appId,server);
		assertNotNull(u);
		
		assertEquals(serverId.toString(),u.getScheme() + ":" + u.getSchemeSpecificPart());
		assertEquals(appId,u.getFragment());
		assertEquals(appId,cea.getAppId(u));
	}
	*/
}
