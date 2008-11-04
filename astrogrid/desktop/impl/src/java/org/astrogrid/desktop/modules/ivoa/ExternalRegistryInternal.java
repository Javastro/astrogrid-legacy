/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.w3c.dom.Document;

/** An Internal interface to the 'External Registry' service.
 * <p/>
 * Provides streaming variants of the standard query methods - allows 
 * in-vm users to stream results direct to consumers.
 * 
 * @author Noel Winstanley
 * @since Aug 1, 200612:35:01 PM
 */
public interface ExternalRegistryInternal extends ExternalRegistry {

	// not very confident about this one..
	void adqlxSearchStream(URI endpoint, Document adqlx, boolean identifiersOnly,
			StreamProcessor proc) throws ServiceException, InvalidArgumentException;

	void getIdentityStream(URI endpoint, StreamProcessor proc)
			throws ServiceException;

	void getResourceStream(URI endpoint, URI ivorn, StreamProcessor processor)
			throws ServiceException, NotFoundException;

	void keywordSearchStream(URI endpoint, String keywords, boolean orValues,
			StreamProcessor proc) throws ServiceException;

	void xquerySearchStream(URI endpoint, String xquery,
			StreamProcessor processor) throws ServiceException;

}