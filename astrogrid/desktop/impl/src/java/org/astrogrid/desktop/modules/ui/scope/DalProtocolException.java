package org.astrogrid.desktop.modules.ui.scope;

import org.xml.sax.SAXException;

/** An exception produced from a DalProtocol */
public class DalProtocolException extends SAXException {

    /**
     * @param message
     */
    public DalProtocolException(final String message) {
        super(message);
    }
}