/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.samp.Client;
import org.astrogrid.samp.Metadata;
import org.astrogrid.samp.Subscriptions;
import org.astrogrid.samp.client.HubConnector;

/** Message target that wraps a SAMP application.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 25, 200912:48:16 PM
 */
public class SampMessageTarget implements ExternalMessageTarget {
    
    private  ImageIcon icon;
    private final SampImpl samp;
    private final Client client;

    
    /** acccess the samp implementation */
    public SampImpl getSampImpl() {
        return samp;
    }
    /**
     * @param client
     */
    public SampMessageTarget(final SampImpl samp,final Client client) {
        this.samp = samp;
        this.client = client;
        final Metadata metadata = client.getMetadata();
        if (metadata != null) {
            final URL iconUrl = metadata.getIconUrl();
            if (iconUrl != null) {
                icon = IconHelper.loadIcon(iconUrl);
            }
        }
    }
    
    public String getDescription() {
        return client.getMetadata() == null ? null : client.getMetadata().getDescriptionText();
    }

    public ImageIcon getIcon() {
         return icon; // we've pre-loaded this, so that it's not retreived on EDT. 
    }

    public String getId() {
        return client.getId();
    }

    public String getName() {
        return client.getMetadata() == null ? null : client.getMetadata().getName();
    }

    public Set<MessageType<?>> acceptedMessageTypes() {
        final Set<MessageType<?>> types = new HashSet<MessageType<?>>();
        final Subscriptions subscriptions = client.getSubscriptions();
        if (subscriptions != null) {
            // could iterate through map of subscriptions - however, some of these
            // maybe wildcarded.
            // instead, iterate through all known message types, and see which ones fit.
            for (final MessageType<?> t : samp.knownTypes) {
                if (t.getSampMType() != null) { // it's a message supported by samp
                    if (subscriptions.isSubscribed(t.getSampMType())) {
                        types.add(t);
                    }
                }
            }
        }
        return types;
    }

    public boolean accepts(final MessageType<?> type) {
        if (type.getSampMType() ==  null) { //not a message supported by samp
            return false;
        }
        final Subscriptions subscriptions = client.getSubscriptions();
        if (subscriptions != null) { 
            // assume this takes care of wildcarded messages
            return subscriptions.isSubscribed(type.getSampMType());
        } else {
            return false;
        }
    }

    public <S extends MessageSender> S createMessageSender(final MessageType<S> type)
            throws UnsupportedOperationException {
        if (! accepts(type)) {
            throw new UnsupportedOperationException(type.getClass().getName() + " not supported");
        }
        return type.createSampSender(this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<SampMessageTarget ");
        sb.append(getName());
        sb.append(", ");
        sb.append(acceptedMessageTypes());
        sb.append(">");               
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.client.getId() == null) ? 0 : this.client.getId().hashCode());
        return result;
    }

    /** equality is defined on {@link #getId} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SampMessageTarget other = (SampMessageTarget) obj;
        if (this.client.getId() == null) {
            if (other.client.getId() != null) {
                return false;
            }
        } else if (!this.client.getId().equals(other.client.getId())) {
            return false;
        }
        return true;
    }
    
    public HubConnector getHubConnector() {
        return samp.hubConnector;
    }

}
