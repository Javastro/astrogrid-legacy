package org.astrogrid.community.delegate.authorization;

public class AuthorizationDelegate {

	public boolean checkPermission(String user, String community, 
								   String credentials, String action,
								   String resource) {

		AuthorizationSoapBindingStub binding;
		try {
			binding = (AuthorizationSoapBindingStub) new AuthorizationServiceLocator().getAuthorization();
		}catch (javax.xml.rpc.ServiceException jre) {
		if(jre.getLinkedCause()!=null)
			jre.getLinkedCause().printStackTrace();
			throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
		}
		// Time out after a minute
		binding.setTimeout(60000);

		// Test operation
		boolean value = false;
		try {
			value = binding.checkPermission(user,community, credentials,
					                        action, resource);
		}catch(java.rmi.RemoteException re) {
			re.printStackTrace();
		}

		return value;
	}
	
	public static void main(String []argv) {
		AuthorizationDelegate ad = new AuthorizationDelegate();
		System.out.println(ad.checkPermission("testuser","testcommunity","testcredentials","testaction","testresource"));
	}
}  