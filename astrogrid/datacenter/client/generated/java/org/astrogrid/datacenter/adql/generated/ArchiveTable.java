/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ArchiveTable.java,v 1.11 2003/12/01 16:30:01 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

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
 * Class ArchiveTable.
 * 
 * @version $Revision: 1.11 $ $Date: 2003/12/01 16:30:01 $
 */
public class ArchiveTable extends org.astrogrid.datacenter.adql.generated.Table 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _archive
     */
    private java.lang.String _archive;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArchiveTable() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ArchiveTable()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'archive'.
     * 
     * @return the value of field 'archive'.
     */
    public java.lang.String getArchive()
    {
        return this._archive;
    } //-- java.lang.String getArchive() 

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
     * Sets the value of field 'archive'.
     * 
     * @param archive the value of field 'archive'.
     */
    public void setArchive(java.lang.String archive)
    {
        this._archive = archive;
    } //-- void setArchive(java.lang.String) 

    /**
     * Method unmarshalArchiveTable
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ArchiveTable unmarshalArchiveTable(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ArchiveTable) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ArchiveTable.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ArchiveTable unmarshalArchiveTable(java.io.Reader) 

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
