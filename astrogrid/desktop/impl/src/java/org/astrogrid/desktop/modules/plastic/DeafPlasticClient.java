package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;

import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.CommonMessageConstants;

/**
 * Represents a client such as a scripting environment that can't be called back.
 * 
 * @author jdt@roe.ac.uk
 * @date 18-Nov-2005
 */
class DeafPlasticClient extends PlasticClientProxy {

    /**
     * A proxy for a client that can't receive callbacks.
     * 
     * @param name
     */
    public DeafPlasticClient(NameGen gen, String name) {
        super(gen, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.PlasticHubImpl.PlasticClientProxy#perform(java.lang.String,
     *      java.lang.String, java.util.Vector)
     */
    public Object perform(URI sender, URI message, List args) {
        // do nothing
        return CommonMessageConstants.RPCNULL;
    }

}