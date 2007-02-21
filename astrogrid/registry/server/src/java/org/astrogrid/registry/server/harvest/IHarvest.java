package org.astrogrid.registry.server.harvest;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.w3c.dom.Node;
import org.astrogrid.registry.RegistryException;

public interface IHarvest {
    
    public void harvestAll();
    public void harvestAll(boolean onlyRegistries,boolean useDates) throws RegistryException;
    public void harvestURL(URL url, String invocationType, Date dt, String setName) throws RegistryException, IOException;
    public void beginHarvest(Node resource, Date dt, String lastResumptionToken) throws RegistryException, IOException;
    public String beginHarvest(String accessURL, String invocationType, Date dt, String lastResumptionToken, String identifier, String setName) throws RegistryException, IOException;
    
}