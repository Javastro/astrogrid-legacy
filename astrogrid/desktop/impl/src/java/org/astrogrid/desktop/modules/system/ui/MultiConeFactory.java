/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.desktop.modules.system.messaging.AbstractMessageSender;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.MessageSender;
import org.astrogrid.desktop.modules.system.messaging.MessageType;
import org.astrogrid.desktop.modules.system.messaging.VotableMessageSender;
import org.astrogrid.desktop.modules.system.messaging.VotableMessageType;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.samp.ErrInfo;
import org.astrogrid.samp.Response;

/** Factory for creating MultiCone UI windows.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 15, 20094:15:23 PM
 */
public class MultiConeFactory implements MultiConeInternal {

    private final static Set<MessageType<?>> myMessages;
    private final List<ExternalMessageTarget> apps;
    private final TypesafeObjectBuilder builder;
    static {
        final Set<MessageType<?>> m = new HashSet();
        m.add(VotableMessageType.instance);
        myMessages = Collections.unmodifiableSet(m);
    }
    
    public MultiConeFactory(final List<ExternalMessageTarget> apps,final TypesafeObjectBuilder builder) {
        this.apps = apps;
        this.builder = builder;
    }
    
    
    public void multiCone(final ConeService s) {
        final MultiConeImpl window = newWindow();
        window.multiCone(s);
    }

    public void multiCone(final URI file) {
        final MultiConeImpl window = newWindow();
        window.multiCone(file);
    }

    public Object create() {
        return newWindow();
    }

    private MultiConeImpl newWindow() {
        final MultiConeImpl mu = builder.createMultiCone();
        mu.setVisible(true);
        return mu;
    }
    
    public Set<MessageType<?>> acceptedMessageTypes() {
        return myMessages;
    }

    public boolean accepts(final MessageType<?> type) {
        return myMessages.contains(type);
    }

    public <S extends MessageSender> S createMessageSender(final MessageType<S> type)
            throws UnsupportedOperationException {
        if (type instanceof VotableMessageType) {
            return (S) new MyVotableConsumer();
        } else{
            throw new UnsupportedOperationException(type.toString());
        }
    }
    
    private class MyVotableConsumer extends AbstractMessageSender implements VotableMessageSender {

        /**
         * @param target
         */
        public MyVotableConsumer() {
            super(MultiConeFactory.this);
        }

        public Response sendVotable(final URL votableURL, final String tableID,
                final String tableName) {
            try {
                final URI u = votableURL.toURI();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {                  
                        multiCone(u);
                    }
                });
                return Response.createSuccessResponse(null);
            } catch (final URISyntaxException x) {
                final ErrInfo i = new ErrInfo(x);                    
                return Response.createErrorResponse(i );                    
            }
        }
    }

}
