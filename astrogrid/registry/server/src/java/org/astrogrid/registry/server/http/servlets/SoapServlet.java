package org.astrogrid.registry.server.http.servlets;

import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.service.binding.MessageBindingProvider;

import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.transport.http.XFireServlet;

import org.astrogrid.registry.server.SoapDispatcher;
import org.astrogrid.registry.server.SoapAdminDispatcher;
import org.astrogrid.registry.server.SoapHarvestDispatcher;

import javax.servlet.ServletException;

public class SoapServlet extends XFireServlet
{
	
  public void init() throws ServletException
  {
    super.init();
    System.out.println("done with init() doing objectffservicefactory");
    //ObjectServiceFactory factory = new ObjectServiceFactory(getXFire().getTransportManager(), null);
    ObjectServiceFactory factory = new ObjectServiceFactory(new MessageBindingProvider());
    factory.setStyle("message");
    Service searchServicev1_0 = factory.create(SoapDispatcher.class,"RegistryQueryv1_0",
    		org.astrogrid.registry.server.query.v1_0.RegistryQueryService.QUERY_WSDL_NS,null);
    Service searchServicev0_1 = factory.create(SoapDispatcher.class,"RegistryQueryv0_1",
    		org.astrogrid.registry.server.query.v0_1.RegistryQueryService.QUERY_WSDL_NS,null);

    Service adminServicev0_1 = factory.create(SoapAdminDispatcher.class,"RegistryUpdatev0_1",
    		org.astrogrid.registry.server.admin.v0_1.RegistryAdminService.ADMIN_WSDL_NS,null);
    Service adminServicev1_0 = factory.create(SoapAdminDispatcher.class,"RegistryUpdatev1_0",
    		org.astrogrid.registry.server.admin.v1_0.RegistryAdminService.ADMIN_WSDL_NS,null);
    
    
    Service searchHarvestv1_0 = factory.create(SoapHarvestDispatcher.class,"RegistryHarvestv1_0",
    		org.astrogrid.registry.server.query.v1_0.OAIService.OAI_WSDL_NS,null);
    Service searchHarvestv0_1 = factory.create(SoapHarvestDispatcher.class,"RegistryHarvestv0_1",
    		org.astrogrid.registry.server.query.v0_1.OAIService.OAI_WSDL_NS,null);
    //Service searchHarvestv0_9 = factory.create(SoapHarvestDispatcher.class,"RegistryHarvestv0_9",
    //		org.astrogrid.registry.server.query.v0_9.OAIService.OAI_WSDL_NS,null);
    
    
    Service searchServiceOld = factory.create(SoapDispatcher.class,"RegistryQuery",
    		org.astrogrid.registry.server.query.v0_1.RegistryQueryService.QUERY_WSDL_NS,null);
    Service adminServiceOld = factory.create(SoapAdminDispatcher.class,"RegistryUpdate",
    		org.astrogrid.registry.server.admin.v0_1.RegistryAdminService.ADMIN_WSDL_NS,null);    
    Service searchHarvestOld = factory.create(SoapHarvestDispatcher.class,"RegistryHarvest",
    		org.astrogrid.registry.server.query.v0_1.OAIService.OAI_WSDL_NS,null);    
    
    
    //service.setProperty(ObjectInvoker.SERVICE_IMPL_CLASS, HelloImpl.class);
    //we register the service with the controller that handles soap requests
    getController().getServiceRegistry().register(searchServiceOld);    
    getController().getServiceRegistry().register(searchServicev1_0);
    getController().getServiceRegistry().register(searchServicev0_1);    
    getController().getServiceRegistry().register(adminServiceOld);
    getController().getServiceRegistry().register(adminServicev0_1);
    getController().getServiceRegistry().register(adminServicev1_0);
    getController().getServiceRegistry().register(searchHarvestOld);    
    getController().getServiceRegistry().register(searchHarvestv1_0);
    getController().getServiceRegistry().register(searchHarvestv0_1);    
    
  }
}