/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/** Model of a STC resource profile.
 * 
 * At the moment STC is left unparsed - there's a lot of it to get at.
 * we just provide access to a Node object representing the STC - from this, you're on your
 * own.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 20, 20081:16:26 PM
 */
public class StcResourceProfile implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -19135650816374314L;
    private Document stcNode;
    private boolean allSky;
    
    /** returns true if, to a first reckoning, this coverage is 'allSky'
     * implemented by ignoring almost all of STC and just looking for an 'allSky' element anywhere in the STC document
     * 
     * @return
     */
    public final boolean isAllSky() {
        return this.allSky;
    }

    public final void setAllSky(boolean allSky) {
        this.allSky = allSky;
    }

    /** access the root of the STC document */
    public Document getStcDocument() {
        return stcNode;
    }

    public final void setStcDocument(Document stcNode) {
        this.stcNode = stcNode;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.allSky ? 1231 : 1237);
        result = prime * result
                + ((this.stcNode == null) ? 0 : this.stcNode.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final StcResourceProfile other = (StcResourceProfile) obj;
        if (this.allSky != other.allSky)
            return false;
        if (this.stcNode == null) {
            if (other.stcNode != null)
                return false;
        } else if (!this.stcNode.equals(other.stcNode))
            return false;
        return true;
    }
    
   
    
    
}
