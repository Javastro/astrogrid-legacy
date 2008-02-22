package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;

import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Service;
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
 * @author   Mark Taylor
 * @since    20 Feb 2008
 */
public class RetrieverService implements Service {

    private final Retriever retriever;
    private final Service service;

    public RetrieverService(Retriever retriever) {
        this.retriever = retriever;
        this.service = retriever.getService();
    }

    public Retriever getRetriever() {
        return retriever;
    }

    public Capability[] getCapabilities() {
        return new Capability[] {retriever.getCapability()};
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
    public boolean equals(Object o) {
        if (o instanceof RetrieverService) {
            RetrieverService other = (RetrieverService) o;
            return other.service.equals(this.service)
                && other.retriever.equals(this.retriever);
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return service.hashCode() + retriever.hashCode();
    }
}
