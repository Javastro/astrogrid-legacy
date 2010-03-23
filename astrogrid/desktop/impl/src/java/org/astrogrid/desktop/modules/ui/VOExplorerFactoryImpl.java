/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.messaging.AbstractMessageSender;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageSender;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageType;
import org.astrogrid.desktop.modules.system.messaging.CeaAdqlSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.CeaSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ConeSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.MessageSender;
import org.astrogrid.desktop.modules.system.messaging.MessageType;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageSender;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.SiapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.SsapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.StapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.TapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.TypedResourceSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.VospaceSetMessageType;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
import org.astrogrid.samp.Response;

/** Factory for voexplorers.
 *  - also handles some plastic messages. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class VOExplorerFactoryImpl  implements VOExplorerFactoryInternal {
	
	public final TypesafeObjectBuilder builder;

	private static final Log logger = LogFactory.getLog(VOExplorerFactoryImpl.class);

    private final List<ExternalMessageTarget> apps; // dynamic list model of currently registered applications.
	
    private final static Set<MessageType<?>> myMessages;
    static {
        final Set<MessageType<?>> m = new HashSet();
        m.add(ResourceSetMessageType.instance);
        m.add(ConeSetMessageType.instance);
        m.add(SiapSetMessageType.instance);
        m.add(SsapSetMessageType.instance);
        m.add(TapSetMessageType.instance);
        m.add(StapSetMessageType.instance);
        m.add(VospaceSetMessageType.instance);
        m.add(CeaSetMessageType.instance);
        m.add(CeaAdqlSetMessageType.instance);
        m.add(BibcodeMessageType.instance);
        myMessages = Collections.unmodifiableSet(m);
    }
    
	public VOExplorerFactoryImpl(final List<ExternalMessageTarget> apps,final TypesafeObjectBuilder builder) {		
	    this.apps = apps;
        this.builder = builder;
	}	
	private VOExplorerImpl newWindow() {
		final VOExplorerImpl vo = builder.createVoExplorer();
		vo.setVisible(true);
		return vo;
	}
// runnable interface.
	public void run() {
		create();
	}
//Factory Interface
	// create a new voexplorer
	public Object create() {
		return newWindow();
	}
	
//RegistryBrowser interface.
	public void show() {
		newWindow();
	}
	public void hide() {
		// not implemented.
	}
	public void open(final URI arg0) {
		final VOExplorerImpl impl = newWindow();
		impl.doOpen(arg0);
	}
	public void search(final String arg0) {
		final VOExplorerImpl impl = newWindow();
		impl.doQuery("Search",arg0);
	}

    /**
     * @param sender
     * @return
     */
    private String findSenderName(final URI senderId) {
        String appName = "unknown application";
        for (final ExternalMessageTarget app : apps) {
            if (app.getId().equals(senderId.toString())) {
                appName = app.getName();
                break;
            }
        }
        return appName;
    }
    // messaging components.

    public <S extends MessageSender> S createMessageSender(final MessageType<S> type)
            throws UnsupportedOperationException {
        if (type instanceof ResourceSetMessageType) { // and all it's subtypes.
            return (S) new MyResourceConsumer((ResourceSetMessageType) type);
        } else if (type instanceof BibcodeMessageType) {
            return (S) new MyBibcodeConsumer();
        
        } else {
            throw new UnsupportedOperationException(type.toString());
        }
    }


    /** consumes a resourceset message */
    private class MyResourceConsumer extends AbstractMessageSender implements ResourceSetMessageSender {

        // the type of the received message. - indicates which subtype it is.
        private final ResourceSetMessageType mtype;

        public MyResourceConsumer(final ResourceSetMessageType mtype) {
            super(VOExplorerFactoryImpl.this);
            this.mtype = mtype;
        }

        public Response sendResourceSet(final List<URI> resList, final String setName) {
          
            final String title;
            String prefix = "Resources";
            // if it's a subclass mtype, we can produce a nicer prefix..
            if (mtype instanceof TypedResourceSetMessageType) {
                final String s = ((TypedResourceSetMessageType)mtype).suffix().substring(1); // drop leading '.'
                prefix = StringUtils.capitalize(s) + " " + prefix;
            }
            if (setName != null) {
                title = prefix +  ": " + setName;
            } else if (getSource() != null) {
                title = prefix + " from " + getSource().getName();
            } else {
                title = prefix;
            }
                       
            // got all the info we need. display the ui on the EDT.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    final VOExplorerImpl ve = newWindow();
                    ve.displayResources(title,resList);
                }
            });  
            return Response.createSuccessResponse(null);
        }

    }

    
    /** consumes a bibcode message */
    private class MyBibcodeConsumer extends AbstractMessageSender implements BibcodeMessageSender {

  
        public MyBibcodeConsumer() {
            super(VOExplorerFactoryImpl.this);
        }

        public Response sendBibcode(final String bibcode) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        final VOExplorerImpl ve = newWindow();
                        ve.doQuery("Resources for " + bibcode,
                                "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] \n "+
                                " where $r/content/source =  '" + StringEscapeUtils.escapeSql(bibcode) + "' \n" +
                                " return $r" );
                    }
                });
                return Response.createSuccessResponse(null);
        }

    }


    public Set<MessageType<?>> acceptedMessageTypes() {
        return myMessages;
    }
    public boolean accepts(final MessageType<?> type) {
        return myMessages.contains(type);
    }
}
