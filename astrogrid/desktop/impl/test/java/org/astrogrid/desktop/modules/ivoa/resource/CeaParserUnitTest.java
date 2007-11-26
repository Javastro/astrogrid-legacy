/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;

/** unit tests for cea application and server resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20079:25:14 PM
 */
public class CeaParserUnitTest extends AbstractTestForParser {


	public void testCeaService() throws Exception {
		ResourceStreamParser p = parse("multiple.xml");
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		validateResource(arr[0]);
		Service s = validateService(arr[0]);
		assertEmpty(s.getRights());
		assertNotNull(s.getCapabilities());
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNotNull(c);
		assertTrue(s instanceof CeaService);
		assertTrue(c instanceof CeaServerCapability);
		assertSame(c,((CeaService)arr[0]).findCeaServerCapability());
		
		// check the servicer capability.
		CeaServerCapability cap = (CeaServerCapability)c;
		assertNotNull(cap.getManagedApplications());
		assertEquals(15,cap.getManagedApplications().length);
		assertEquals("ivo://obspm.fr/PegaseHR_full",cap.getManagedApplications()[3].toString());
	}
	

	public void testCeaApplication() throws Exception {
		ResourceStreamParser p = parse("pegase.xml");
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		
		assertTrue(arr[0] instanceof CeaApplication) ;
		CeaApplication a = (CeaApplication)arr[0];
		validateResource(a);
		assertNotNull(a.getInterfaces());
		assertEquals(1,a.getInterfaces().length);
		final InterfaceBean iface = a.getInterfaces()[0];
		assertEquals("simple",iface.getName());
		
		assertNotNull(iface.getInputs());
		assertEquals(18,iface.getInputs().length);		
		assertNotNull(iface.getOutputs());
		assertEquals(2,iface.getOutputs().length);
		
		ParameterReferenceBean ref = iface.getInputs()[4];
		assertNotNull(ref);
		assertEquals("WINDS",ref.getRef());
		assertEquals(1, ref.getMax());
		assertEquals(1,ref.getMin());
		
		assertNotNull(a.getParameters());
		assertEquals(20,a.getParameters().length);
		
		ParameterBean pb = a.getParameters()[6];
		assertNotNull(pb);
		assertEquals("0",pb.getDefaultValue());
		assertEquals("Mass fraction of substellar objects formed (real in [0.,1.])",pb.getDescription());
		assertEquals("FRACSUB",pb.getName());
//dunno if ui relies on this being null at the oment.		assertEmpty(pb.getOptions());
		assertNull(pb.getSubType());
		assertEquals("double",pb.getType());
	//	assertNotNull(pb.getUcd());
		assertEquals("SubStellar fraction",pb.getUiName());
		//assertNotNull(pb.getUnits());
	}


	

}
