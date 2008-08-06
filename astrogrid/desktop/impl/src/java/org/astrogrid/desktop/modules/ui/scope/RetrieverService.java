package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;

import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.DataService;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.TableService;
import org.astrogrid.acr.ivoa.resource.Validation;

/**
 * Adapter class that represents a Retriever as a Service.
 * The {@link #getCapabilities} method will only return a single capability.
 * Currently delegates all other methods to a base service.
 * If it turns out that these Service objects are interrogated for what
 * subclasses/subinterfaces they implement it will be necessary to implement
 * this adapter behaviour using Proxies instead.
 *
 * <p>Two instances of this class are considered equal if they have the same
 * retriever.
 *
 *NWW: there's only a few sub-interfaces that are used in introspection
 *- rather than bother with proxies, can do this with a factory method that
 *- selects the correct one of a bunch of interfaces.
 * @author   Mark Taylor
 * @since    20 Feb 2008
 */
public class RetrieverService implements Service {

    /** create an instance of Service, or one of it's subclasses, 
     * depending on the service wrapped by the retriever
     * @param ret
     * @return
     */
    public static Service create(final Retriever ret) {
        final Service s = ret.getService();
        if (s instanceof CatalogService) { // first - as is subclass of dataservice
            return new CatalogRetrieverService(ret);
        } else if (s instanceof DataService) {
            return new DataRetrieverService(ret);
        } else if (s instanceof TableService) {
            return new TableRetrieverService(ret);
        } else { // just a service
            return new RetrieverService(ret);
        }
    }
     
   /** delegate wrapper class for TableService */
   private static class TableRetrieverService extends RetrieverService implements TableService {

    private TableRetrieverService(final Retriever abstractRetriever) {
        super(abstractRetriever);
    }

    public ResourceName[] getFacilities() {
        return ((TableService)service).getFacilities();
    }

    public ResourceName[] getInstruments() {
        return ((TableService)service).getInstruments();
    }

    public TableBean[] getTables() {
        return ((TableService)service).getTables();
    }
   }
   
   /** delegate wrapper class for CatalogService */
   private static class CatalogRetrieverService extends DataRetrieverService implements CatalogService {

    private CatalogRetrieverService(final Retriever abstractRetriever) {
        super(abstractRetriever);
    }

    public TableBean[] getTables() {
        return ((CatalogService)service).getTables();
    }
   }
   
   
   /** delegate wrapper class for DataRetrieverService */
   private static class DataRetrieverService extends RetrieverService implements DataService {

    private DataRetrieverService(final Retriever abstractRetriever) {
        super(abstractRetriever);
    }

    public Coverage getCoverage() {
        return ((DataService)service).getCoverage();
    }

    public ResourceName[] getFacilities() {
        return ((DataService)service).getFacilities();
    }

    public ResourceName[] getInstruments() {
        return ((DataService)service).getInstruments();
    }
   }
    
    protected final Retriever abstractRetriever;
    protected final Service service;

    private RetrieverService(final Retriever abstractRetriever) {
        this.abstractRetriever = abstractRetriever;
        this.service = abstractRetriever.getService();
    }

    public Retriever getRetriever() {
        return abstractRetriever;
    }

    public Capability[] getCapabilities() {
        return new Capability[] {abstractRetriever.getCapability()};
    }

    public String[] getRights() {
        return service.getRights();
    }

    public Validation[] getValidationLevel() {
        return service.getValidationLevel();
    }

    public String getTitle() {
        return service.getTitle();
    }

    public URI getId() {
        return service.getId();
    }

    public String getShortName() {
        return service.getShortName();
    }

    public Curation getCuration() {
        return service.getCuration();
    }

    public Content getContent() {
        return service.getContent();
    }

    public String getStatus() {
        return service.getStatus();
    }

    public String getCreated() {
        return service.getCreated();
    }

    public String getUpdated() {
        return service.getUpdated();
    }

    public String getType() {
        return service.getType();
    }

    /**
     * Returns true iff o has the same retriever as this.
     */
    @Override
    public boolean equals(final Object o) {
        if (o instanceof RetrieverService) {
            final RetrieverService other = (RetrieverService) o;
            return other.service.equals(this.service)
                && other.abstractRetriever.equals(this.abstractRetriever);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return service.hashCode() + abstractRetriever.hashCode();
    }
}
