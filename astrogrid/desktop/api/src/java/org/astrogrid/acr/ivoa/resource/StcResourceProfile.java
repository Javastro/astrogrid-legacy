/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

import org.w3c.dom.Document;

/** Spatial Coverage.

 * Described using STC, which is too complex to usefully parse into arbitrary datastructures. 

 * @see <a href='http://www.ivoa.net/Documents/latest/STC.html'>IVOA STC specification</a>
 * @author Noel.Winstanley@manchester.ac.uk
 * @see Coverage
 */
public class StcResourceProfile implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -19135650816374314L;
    private Document stcNode;
    private boolean allSky;
    
    /** Heuristic: is this coverage 'AllSky'
     * 
     * @warning implemented by ignoring almost all of STC and just looking for an 'allSky' element anywhere in the STC document. Use with caution.
     * 
     * @return boolean if all sky
     */
    public final boolean isAllSky() {
        return this.allSky;
    }
    /** @exclude */
    public final void setAllSky(final boolean allSky) {
        this.allSky = allSky;
    }

    /** access the original STC document
     * @return an xml Document that conforms to the STC specification
     * 
 * @see <a href='http://www.ivoa.net/Documents/latest/STC.html'>IVOA STC specification</a>    
     *  */
    public Document getStcDocument() {
        return stcNode;
    }
    /** @exclude */
    public final void setStcDocument(final Document stcNode) {
        this.stcNode = stcNode;
    }
    /** @exclude */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.allSky ? 1231 : 1237);
        result = prime * result
                + ((this.stcNode == null) ? 0 : this.stcNode.hashCode());
        return result;
    }
    /** @exclude */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StcResourceProfile other = (StcResourceProfile) obj;
        if (this.allSky != other.allSky) {
            return false;
        }
        if (this.stcNode == null) {
            if (other.stcNode != null) {
                return false;
            }
        } else if (!this.stcNode.equals(other.stcNode)) {
            return false;
        }
        return true;
    }
    
   
    
    
}
