/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataCenterParameterDefinition.java,v 1.2 2004/03/19 08:16:47 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.cea.base;

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
 * special properties for datacentre parameters
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/19 08:16:47 $
 */
public class DataCenterParameterDefinition extends org.astrogrid.registry.beans.cea.param.BaseParameterDefinition 
implements java.io.Serializable
{


      //----------------/
     //- Constructors -/
    //----------------/

    public DataCenterParameterDefinition() {
        super();
    } //-- org.astrogrid.registry.beans.cea.base.DataCenterParameterDefinition()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Method unmarshalDataCenterParameterDefinition
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.base.DataCenterParameterDefinition unmarshalDataCenterParameterDefinition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.DataCenterParameterDefinition) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.DataCenterParameterDefinition.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.DataCenterParameterDefinition unmarshalDataCenterParameterDefinition(java.io.Reader) 

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
