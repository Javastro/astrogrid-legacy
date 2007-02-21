package org.astrogrid.registry.server.harvest; 


public class HarvestFactory {
    
    public static IHarvest createHarvestService(String contractVersion) throws IllegalAccessException, 
                                                                            InstantiationException, 
                                                                            ClassNotFoundException {
       Class cl = Class.forName("org.astrogrid.registry.server.harvest.v" + contractVersion.replace('.','_') + ".RegistryHarvestService");
       //cl.isIstance(ISearch)
       return (IHarvest)cl.newInstance();
    }
    
}