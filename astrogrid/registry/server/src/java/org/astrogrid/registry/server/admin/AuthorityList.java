package org.astrogrid.registry.server.admin;

/**
 * Class AuthorityList
 * Purpose: Small object class to hold authorityid's and version numbers along with known owners of an authority. Currently
 * used exclusively by RegistryAdminService for storing in Hashtables for management of authority id's. 
 * @author Kevin Benson
 *
 */
public class AuthorityList {
    
    //
    private String authorityID = null;
    private String versionNumber = null;
    
    private String owner = null;
        
    public AuthorityList(String authorityID, String versionNumber, String owner) {
        this.authorityID = authorityID.trim();
        this.versionNumber = versionNumber.trim();
        this.owner = owner;        
    }
    
    public AuthorityList(String authorityID, String versionNumber) {
        this.authorityID = authorityID.trim();
        this.versionNumber = versionNumber.trim();
        this.owner = owner;        
    }    
    
    
    /**
     * Method: getAuthorityID
     * Purpose: return the authority id
     * @return String of the authorityid
     */
    public String getAuthorityID() {
        return this.authorityID;
    }
    
    /**
     * Method: getVersionNumber
     * Purpose: return the version number of the vr namespace
     * @return String of the version number.
     */    
    public String getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * Method: getOwner
     * Purpose: return the authority id's owner
     * @return String of the authorityid's owner
     */
    public String getOwner() {
        return this.owner;
    }
    
    /**
     * Method: setAuthorityID
     * Purpose: Set the authority ID
     */
    public void setAuthorityID(String authorityID) {
        this.authorityID = authorityID;
    }
    
    /**
     * Method: setVersionNumber
     * Purpose: Set the version number
     */    
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Method: setOwner
     * Purpose: Set the owner which is the owner/manager of the authority id.
     */    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * Mehod: hasOwner
     * Purpsoe: Does this authority id have a owner
     * @return boolean if the authority id has a owning authority id or not.
     */
    public boolean hasOwner(String owner) {
        return this.owner.equals(owner);
    }
    
    /**
     * Method: equals
     * Purpose: Determine if this AuthorityList object equals another AuthorityList object, used in the Hashtable index type methods a lot.
     * @return boolean if the AuthorityList equals the given AuthorityList object.
     */
    //public boolean equals(AuthorityList al) {
    public boolean equals(Object authList) {
        AuthorityList al = null;
        if(authList instanceof AuthorityList)
            al = (AuthorityList)authList;
        else
            return false;
        //System.out.println("IN EQUALS THIS- " + this.toString() + " EQUALS AL = " + al.toString());
        if(this.authorityID.equals(al.getAuthorityID()) && 
           this.versionNumber.equals(al.getVersionNumber())) {
            if(this.owner == null && al.getOwner() == null)
                return true;
            else if(this.owner.equals(al.getOwner()))
                return true;
        }
        return false;
    }
    
    public int hashCode() {
        int hashCode = (this.authorityID.hashCode() + this.versionNumber.hashCode());
        if(this.owner != null) 
            hashCode += this.owner.hashCode();
        return hashCode;
    }
    
    /**
     * Method: toString
     * Purpose: Give a string representation of the AuthorityList object.
     * @return String representation of this object.
     */
    public String toString() {
        return "AuthorityID: " + this.authorityID + 
               " VersionNumber: " + this.versionNumber + 
               " Owner: " + this.owner;
    }
    
    
}