package org.astrogrid.registry.server.admin; 


public class AdminFactory {
    
    public static IAdmin createAdminService(String contractVersion) throws IllegalAccessException, 
                                                                            InstantiationException, 
                                                                            ClassNotFoundException {
       Class cl = Class.forName("org.astrogrid.registry.server.admin.v" + contractVersion.replace('.','_') + ".RegistryAdminService");
       return (IAdmin)cl.newInstance();
    }
    
}