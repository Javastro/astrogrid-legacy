/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;
import java.util.Arrays;

/** A service invoked using SOAP.
 * @author Noel.Winstanley@manchester.ac.uk
 */
public class WebServiceInterface extends Interface {
    /**
     * 
     */
    private static final long serialVersionUID = -5377409351653598800L;

    private static int hashCode(final Object[] array) {
        final int prime = 31;
        if (array == null) {
            return 0;
        }
        int result = 1;
        for (int index = 0; index < array.length; index++) {
            result = prime * result
                    + (array[index] == null ? 0 : array[index].hashCode());
        }
        return result;
    }

    private URI[] wsdlURLs;

    /**The location of the WSDL that describes this
                        Web Service.  If not provided, the location is 
                        assumed to be the accessURL with "?wsdl" appended.*/
    public final URI[] getWsdlURLs() {
        return this.wsdlURLs;
    }
    /** @exclude */
    public final void setWsdlURLs(final URI[] wsdlURLs) {
        this.wsdlURLs = wsdlURLs;
    }
    /** @exclude */
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + WebServiceInterface.hashCode(this.wsdlURLs);
        return result;
    }
    /** @exclude */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WebServiceInterface other = (WebServiceInterface) obj;
        if (!Arrays.equals(this.wsdlURLs, other.wsdlURLs)) {
            return false;
        }
        return true;
    }
}
