/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.outgoing.PlasticException;

class XMLRPCPlasticClient extends PlasticClientProxy {

    /**
     *  The xml-rpc method name that a Plastic app offers
     */
    private static final String PLASTIC_CLIENT_PERFORM = "plastic.client.perform";

    /**
     * Logger for this class
     */
    private final Log logger = LogFactory.getLog(XMLRPCPlasticClient.class);

    private XmlRpcClient xmlrpc;

    private boolean validURL;

    public XMLRPCPlasticClient(NameGen gen, String name, List supportedMessages, URL callbackURL) {
        super(gen, name, supportedMessages);

        logger.info("Ctor: XMLRPCPlasticClient with callBackURL " + callbackURL + " and supports messages: "
                + supportedMessages);

        xmlrpc = new XmlRpcClient(callbackURL);
        validURL = true;
    }

    public Object perform(URI sender, URI message, List args) throws PlasticException {
        if (!validURL)
            throw new PlasticException("Cannot send message to " + getId() + " due to invalid callback URL");
        logger.info("Performing " + message + " from " + sender);
        try {
            Vector xmlrpcArgs = new Vector();
            xmlrpcArgs.add(sender.toString());
            xmlrpcArgs.add(message.toString());
            xmlrpcArgs.add(new Vector(args));
            setResponding(true);
            return xmlrpc.execute(PLASTIC_CLIENT_PERFORM, xmlrpcArgs);
        } catch (Exception e) {
            setResponding(false);
            logger.warn("Got " + e + " trying to send message to " + getId());
            throw new PlasticException(e);
        }
    }

	public boolean canRespond() {
		return true;
	}

}