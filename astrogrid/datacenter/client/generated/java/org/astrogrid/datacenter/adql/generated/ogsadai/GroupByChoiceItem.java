/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GroupByChoiceItem.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class GroupByChoiceItem.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class GroupByChoiceItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _allColumnReference
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AllColumnReference _allColumnReference;

    /**
     * Field _singleColumnReference
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.SingleColumnReference _singleColumnReference;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupByChoiceItem() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.GroupByChoiceItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'allColumnReference'.
     * 
     * @return the value of field 'allColumnReference'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AllColumnReference getAllColumnReference()
    {
        return this._allColumnReference;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AllColumnReference getAllColumnReference() 

    /**
     * Returns the value of field 'singleColumnReference'.
     * 
     * @return the value of field 'singleColumnReference'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.SingleColumnReference getSingleColumnReference()
    {
        return this._singleColumnReference;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SingleColumnReference getSingleColumnReference() 

    /**
     * Sets the value of field 'allColumnReference'.
     * 
     * @param allColumnReference the value of field
     * 'allColumnReference'.
     */
    public void setAllColumnReference(org.astrogrid.datacenter.adql.generated.ogsadai.AllColumnReference allColumnReference)
    {
        this._allColumnReference = allColumnReference;
    } //-- void setAllColumnReference(org.astrogrid.datacenter.adql.generated.ogsadai.AllColumnReference) 

    /**
     * Sets the value of field 'singleColumnReference'.
     * 
     * @param singleColumnReference the value of field
     * 'singleColumnReference'.
     */
    public void setSingleColumnReference(org.astrogrid.datacenter.adql.generated.ogsadai.SingleColumnReference singleColumnReference)
    {
        this._singleColumnReference = singleColumnReference;
    } //-- void setSingleColumnReference(org.astrogrid.datacenter.adql.generated.ogsadai.SingleColumnReference) 

}
