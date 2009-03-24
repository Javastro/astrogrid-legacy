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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.messaging.AbstractMessageSender;
import org.astrogrid.desktop.modules.system.messaging.AbstractMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageSender;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.MessageSender;
import org.astrogrid.desktop.modules.system.messaging.MessageType;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageSender;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageType;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

/** Factory for voexplorers.
 *  - also handles some plastic messages. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class VOExplorerFactoryImpl extends AbstractMessageTarget  implements VOExplorerFactoryInternal {
	
	public final TypesafeObjectBuilder builder;

	private static final Log logger = LogFactory.getLog(VOExplorerFactoryImpl.class);

    private final List<ExternalMessageTarget> apps; // dynamic list model of currently registered applications.
	
    private final static Set<MessageType<?>> myMessages;
    static {
        final Set<MessageType<?>> m = new HashSet();
        m.add(ResourceSetMessageType.instance);
        m.add(BibcodeMessageType.instance);
        myMessages = Collections.unmodifiableSet(m);
    }
    
	public VOExplorerFactoryImpl(final List<ExternalMessageTarget> apps,final TypesafeObjectBuilder builder) {
		super(myMessages);
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
        if (type instanceof ResourceSetMessageType) {
            return (S) new MyResourceConsumer();
        } else if (type instanceof BibcodeMessageType) {
            return (S) new MyBibcodeConsumer();
        
        } else {
            throw new UnsupportedOperationException(type.toString());
        }
    }


    /** consumes a resourceset message */
    private class MyResourceConsumer extends AbstractMessageSender implements ResourceSetMessageSender {

        public MyResourceConsumer() {
            super(VOExplorerFactoryImpl.this);
        }

        public void sendResourceSet(final List<URI> resList, final String setId,
                final String setName) {
          
            final String title;
            if (setName != null) {
                title = "Resource Set : " + setName;
            } else if (getSource() != null) {
                title = "Resources from " + getSource().getName();
            } else {
                title = "Resource Set";
            }
                       
            // got all the info we need. display the ui on the EDT.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    final VOExplorerImpl ve = newWindow();
                    ve.displayResources(title,resList);
                }
            });                        
        }

    }

    
    /** consumes a bibcode message */
    private class MyBibcodeConsumer extends AbstractMessageSender implements BibcodeMessageSender {

  
        public MyBibcodeConsumer() {
            super(VOExplorerFactoryImpl.this);
        }

        public void sendBibcode(final String bibcode) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        final VOExplorerImpl ve = newWindow();
                        ve.doQuery("Resources for " + bibcode,
                                "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] \n "+
                                " where $r/content/source =  '" + StringEscapeUtils.escapeSql(bibcode) + "' \n" +
                                " return $r" );
                    }
                });                
        }

    }
}
