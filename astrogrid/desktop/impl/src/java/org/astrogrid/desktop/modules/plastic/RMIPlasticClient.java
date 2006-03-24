/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;

import net.ladypleaser.rmilite.RemoteInvocationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.outgoing.PlasticException;

class RMIPlasticClient extends PlasticClientProxy {


    /**
     * Logger for this class
     */
    private final Log logger = LogFactory.getLog(RMIPlasticClient.class);

    private PlasticListener remoteClient;

    public RMIPlasticClient(NameGen gen, String name, List supportedMessages, PlasticListener plastic) {
        super(gen, name, supportedMessages);
        logger.debug("Ctor: RMIPlasticClient supports messages: " + supportedMessages);
        this.remoteClient = plastic;
    }

    /**
     * Constructor assuming interested in all messages.
     * @param gen
     * @param name
     * @param plastic
     */
	public RMIPlasticClient(NameGen gen, String name, PlasticListener plastic) {
		super(gen, name);
		this.remoteClient = plastic;
	}

	public Object perform(URI sender, URI message, List args) throws PlasticException {
        try {
            Object response = remoteClient.perform(sender, message, args);
            setResponding(true);
            return response;
        } catch (RemoteInvocationException e) {
            setResponding(false);
            throw new PlasticException(e);
        } catch (RuntimeException re) {
        	//the client application didn't behave properly, but that's not our fault.  Report the error and
        	//continue
        	setResponding(true); //it's alive, even if buggy
        	logger.error("Application "+sender+" responded with a RuntimeException to message "+message,re);
        	return CommonMessageConstants.RPCNULL;
        }
    }

	public boolean canRespond() {
		return true;
	}

}