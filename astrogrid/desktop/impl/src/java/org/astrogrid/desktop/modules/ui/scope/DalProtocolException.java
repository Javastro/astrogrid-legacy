package org.astrogrid.desktop.modules.ui.scope;

import org.xml.sax.SAXException;

/** exception to represent a failure of the dal request */
public class DalProtocolException extends SAXException {

    /**
     * @param message
     */
    public DalProtocolException(String message) {
        super(message);
    }
}