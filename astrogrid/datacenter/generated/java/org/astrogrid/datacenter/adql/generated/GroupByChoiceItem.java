/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GroupByChoiceItem.java,v 1.4 2003/10/13 13:49:46 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.ContentHandler;
import org.astrogrid.datacenter.adql.AbstractQOM;

/**
 * Class GroupByChoiceItem.
 *edited by NWW to make instance of AbstractQOM.
 * @version $Revision: 1.4 $ $Date: 2003/10/13 13:49:46 $
 */
public class GroupByChoiceItem extends AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _allColumnReference
     */
    private org.astrogrid.datacenter.adql.generated.AllColumnReference _allColumnReference;

    /**
     * Field _singleColumnReference
     */
    private org.astrogrid.datacenter.adql.generated.SingleColumnReference _singleColumnReference;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupByChoiceItem() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoiceItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'allColumnReference'.
     * 
     * @return the value of field 'allColumnReference'.
     */
    public org.astrogrid.datacenter.adql.generated.AllColumnReference getAllColumnReference()
    {
        return this._allColumnReference;
    } //-- org.astrogrid.datacenter.adql.generated.AllColumnReference getAllColumnReference() 

    /**
     * Returns the value of field 'singleColumnReference'.
     * 
     * @return the value of field 'singleColumnReference'.
     */
    public org.astrogrid.datacenter.adql.generated.SingleColumnReference getSingleColumnReference()
    {
        return this._singleColumnReference;
    } //-- org.astrogrid.datacenter.adql.generated.SingleColumnReference getSingleColumnReference() 

    /**
     * Sets the value of field 'allColumnReference'.
     * 
     * @param allColumnReference the value of field
     * 'allColumnReference'.
     */
    public void setAllColumnReference(org.astrogrid.datacenter.adql.generated.AllColumnReference allColumnReference)
    {
        this._allColumnReference = allColumnReference;
    } //-- void setAllColumnReference(org.astrogrid.datacenter.adql.generated.AllColumnReference) 

    /**
     * Sets the value of field 'singleColumnReference'.
     * 
     * @param singleColumnReference the value of field
     * 'singleColumnReference'.
     */
    public void setSingleColumnReference(org.astrogrid.datacenter.adql.generated.SingleColumnReference singleColumnReference)
    {
        this._singleColumnReference = singleColumnReference;
    } //-- void setSingleColumnReference(org.astrogrid.datacenter.adql.generated.SingleColumnReference) 

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adql.QOM#isValid()
     */
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adql.QOM#marshal(org.xml.sax.ContentHandler)
     */
    public void marshal(ContentHandler c) throws CastorException, IOException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adql.QOM#marshal(java.io.Writer)
     */
    public void marshal(Writer w) throws CastorException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adql.QOM#validate()
     */
    public void validate() throws CastorException {
        // TODO Auto-generated method stub

    }

}
