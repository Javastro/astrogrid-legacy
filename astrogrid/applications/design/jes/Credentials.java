/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Credentials.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 */

package jes;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Group;

/**
 * The full authorization and authentication credentials.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class Credentials extends org.astrogrid.common.bean.BaseBean 
implements Serializable
{
      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

//-- org.astrogrid.community.beans.v1.Credentials()


      //-----------/
     //- Methods -/
    //-----------/

//-- boolean equals(java.lang.Object) 


    /**
     * Returns the value of field 'account'. The field 'account'
     * has the following description: The user account.
     * 
     * @return the value of field 'account'.
     */
    public Account getAccount()
    {
        return this._account;
    } //-- org.astrogrid.community.beans.v1.Account getAccount() 

    /**
     * Returns the value of field 'group'. The field 'group' has
     * the following description: A security group used in
     * authorization.
     * 
     * @return the value of field 'group'.
     */
    public Group getGroup()
    {
        return this._group;
    } //-- org.astrogrid.community.beans.v1.Group getGroup() 

    /**
     * Returns the value of field 'securityToken'. The field
     * 'securityToken' has the following description: The security
     * token used in authentication.
     * 
     * @return the value of field 'securityToken'.
     */
    public String getSecurityToken()
    {
        return this._securityToken;
    } //-- java.lang.String getSecurityToken() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

 //-- boolean isValid() 


//-- void marshal(java.io.Writer) 

//-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'account'. The field 'account' has
     * the following description: The user account.
     * 
     * @param account the value of field 'account'.
     */
    public void setAccount(Account account)
    {
        this._account = account;
    } //-- void setAccount(org.astrogrid.community.beans.v1.Account) 

    /**
     * Sets the value of field 'group'. The field 'group' has the
     * following description: A security group used in
     * authorization.
     * 
     * @param group the value of field 'group'.
     */
    public void setGroup(Group group)
    {
        this._group = group;
    } //-- void setGroup(org.astrogrid.community.beans.v1.Group) 

    /**
     * Sets the value of field 'securityToken'. The field
     * 'securityToken' has the following description: The security
     * token used in authentication.
     * 
     * @param securityToken the value of field 'securityToken'.
     */
    public void setSecurityToken(String securityToken)
    {
        this._securityToken = securityToken;
    }

 //-- void setSecurityToken(java.lang.String) 


//-- org.astrogrid.community.beans.v1.Credentials unmarshalCredentials(java.io.Reader) 

//-- void validate() 

}
