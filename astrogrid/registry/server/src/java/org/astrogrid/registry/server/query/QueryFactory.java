package org.astrogrid.registry.server.query; 


public class QueryFactory {
    
    public static ISearch createQueryService(String contractVersion) throws IllegalAccessException, 
                                                                            InstantiationException, 
                                                                            ClassNotFoundException {
       Class cl = Class.forName("org.astrogrid.registry.server.query.v" + contractVersion.replace('.','_') + ".RegistryQueryService");
       //cl.isIstance(ISearch)
       return (ISearch)cl.newInstance();
    }
    
}