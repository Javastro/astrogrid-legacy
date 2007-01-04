/**
 * JesFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.v1.jobcontroller;

public class JesFault extends org.apache.axis.AxisFault {
    public java.lang.String message;
    public java.lang.String getMessage() {
        return this.message;
    }

    public JesFault() {
    }

      public JesFault(java.lang.String message) {
        this.message = message;
    }

    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, message);
    }
}
