/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GroupType.java,v 1.2 2004/03/02 14:09:49 pah Exp $
 */

package org.astrogrid.community.beans.v1;

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

/**
 * The security group
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class GroupType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _groupId
     */
    private java.lang.String _groupId;

    /**
     * Field _community
     */
    private org.astrogrid.community.beans.v1.Community _community;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupType() {
        super();
    } //-- org.astrogrid.community.beans.v1.GroupType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'community'.
     * 
     * @return the value of field 'community'.
     */
    public org.astrogrid.community.beans.v1.Community getCommunity()
    {
        return this._community;
    } //-- org.astrogrid.community.beans.v1.Community getCommunity() 

    /**
     * Returns the value of field 'groupId'.
     * 
     * @return the value of field 'groupId'.
     */
    public java.lang.String getGroupId()
    {
        return this._groupId;
    } //-- java.lang.String getGroupId() 

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
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'community'.
     * 
     * @param community the value of field 'community'.
     */
    public void setCommunity(org.astrogrid.community.beans.v1.Community community)
    {
        this._community = community;
    } //-- void setCommunity(org.astrogrid.community.beans.v1.Community) 

    /**
     * Sets the value of field 'groupId'.
     * 
     * @param groupId the value of field 'groupId'.
     */
    public void setGroupId(java.lang.String groupId)
    {
        this._groupId = groupId;
    } //-- void setGroupId(java.lang.String) 

    /**
     * Method unmarshalGroupType
     * 
     * @param reader
     */
    public static org.astrogrid.community.beans.v1.GroupType unmarshalGroupType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.community.beans.v1.GroupType) Unmarshaller.unmarshal(org.astrogrid.community.beans.v1.GroupType.class, reader);
    } //-- org.astrogrid.community.beans.v1.GroupType unmarshalGroupType(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
