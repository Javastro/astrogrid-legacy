package org.astrogrid.registry.server.query; 


public class OAIHarvestFactory {
    
    public static IOAIHarvestService createOAIHarvestService(String contractVersion) throws IllegalAccessException, 
                                                                            InstantiationException, 
                                                                            ClassNotFoundException {
       Class cl = Class.forName("org.astrogrid.registry.server.query.v" + contractVersion.replace('.','_') + ".OAIService");
       //cl.isIstance(ISearch)
       return (IOAIHarvestService)cl.newInstance();
    }
    
}