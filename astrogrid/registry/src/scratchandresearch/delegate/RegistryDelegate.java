/**
 *  Delegate for RegistryInterface3_0, the It03 Registry web service.
 *  @author=Elizabeth Auden
 *  22 October 2003
 */

package org.astrogrid.registry.delegate.registry;
import org.astrogrid.registry.delegate.stubs.impl.* ;

public class RegistryDelegate {
   
	private String targetEndPoint = null;
	public RegistryDelegate(String targetEndPoint) {
	  this.targetEndPoint = targetEndPoint;
	}
    
	public String submitQuery(String query) throws Exception {
		RegistryInterface3_0SoapBindingStub binding;
		try {
			binding = (RegistryInterface3_0SoapBindingStub)
						  new RegistryInterface3_0ServiceLocator().getRegistryInterface3_0(targetEndPoint);
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}

		// Time out after a minute
		binding.setTimeout(60000);
		String value = binding.submitJob(req);
		return (String)value;
		//After returning the response you may have validate or
		//some other helper methods in this delegate to help you.
	}
	
	public String fullNodeQuery(String query) throws Exception {
		RegistryInterface3_0SoapBindingStub binding;
		try {
			binding = (RegistryInterface3_0SoapBindingStub)
						  new RegistryInterface3_0ServiceLocator().getRegistryInterface3_0(targetEndPoint);
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}

		// Time out after a minute
		binding.setTimeout(60000);
		String value = binding.submitJob(req);
		return (String)value;
		//After returning the response you may have validate or
		//some other helper methods in this delegate to help you.
	}
}
