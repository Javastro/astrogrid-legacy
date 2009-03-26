/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.Set;

import javax.swing.ImageIcon;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/** Description of an application that's connected via plastic.
 * 
 * @author John Taylor
 * @modified Noel Winstanley - renamed to remove ambiguity. added some extra info methods, 
 * strengthened types (more pressing if used by more clients)
 * @modified - made package-private, hid behind facade of MessageTarget
 * @since Jun 16, 20061:19:37 PM
 */
public class PlasticApplicationDescription implements ExternalMessageTarget {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
    .getLog(PlasticApplicationDescription.class);

    final URI id; // package visibility.
    private final String name;
    private final ImageIcon icon;
    private final String description;
    private final Set<MessageType<?>> mtypes;
    private final TupperwareInternal tupperware;

    
    

    @SuppressWarnings({ "unchecked", "cast" })
    public PlasticApplicationDescription(final URI id, final String name,final String description
            , final Set<MessageType<?>> mtypes
            ,final ImageIcon icon
            ,final TupperwareInternal tupperware) {
        this.mtypes = (Set<MessageType<?>>)SetUtils.unmodifiableSet(mtypes); // never change for a plastic application.
        this.id = id;
        this.name = name;       
        this.description = description;
        this.tupperware = tupperware;
        this.icon = icon;
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }
    
   @Override
public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("<PlasticApplicationDescription ");
    sb.append(getName());
    sb.append(", ");
    sb.append(acceptedMessageTypes());
    sb.append(">");
            
    return sb.toString();
}

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    public ImageIcon getIcon() {
        return icon;
    }
    
    public final Set<MessageType<?>> acceptedMessageTypes() {
        return mtypes;
    }

    public final boolean accepts(final MessageType<?> type) {
        return mtypes.contains(type);
    }


    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        final PlasticApplicationDescription other = (PlasticApplicationDescription) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public <M extends MessageSender> M createMessageSender(final MessageType<M> type)
            throws UnsupportedOperationException {
        if (! accepts(type)) {
            throw new UnsupportedOperationException(type.getClass().getName() + " not supported");
        }
        return type.createPlasticSender(this);
    }

    /** Access tupperware - the messaging component specifc to plastic.
     * @return the tupperware
     */
    public TupperwareInternal getTupperware() {
        return tupperware;
    }


   

}