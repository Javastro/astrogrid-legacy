/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SelectionListChoiceItem.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SelectionListChoiceItem.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class SelectionListChoiceItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _aliasSelectionItem
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem _aliasSelectionItem;

    /**
     * Field _exprSelectionItem
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem _exprSelectionItem;

    /**
     * Field _allSelectionItem
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem _allSelectionItem;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectionListChoiceItem() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SelectionListChoiceItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aliasSelectionItem'.
     * 
     * @return the value of field 'aliasSelectionItem'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem getAliasSelectionItem()
    {
        return this._aliasSelectionItem;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem getAliasSelectionItem() 

    /**
     * Returns the value of field 'allSelectionItem'.
     * 
     * @return the value of field 'allSelectionItem'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem getAllSelectionItem()
    {
        return this._allSelectionItem;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem getAllSelectionItem() 

    /**
     * Returns the value of field 'exprSelectionItem'.
     * 
     * @return the value of field 'exprSelectionItem'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem getExprSelectionItem()
    {
        return this._exprSelectionItem;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem getExprSelectionItem() 

    /**
     * Sets the value of field 'aliasSelectionItem'.
     * 
     * @param aliasSelectionItem the value of field
     * 'aliasSelectionItem'.
     */
    public void setAliasSelectionItem(org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem aliasSelectionItem)
    {
        this._aliasSelectionItem = aliasSelectionItem;
    } //-- void setAliasSelectionItem(org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem) 

    /**
     * Sets the value of field 'allSelectionItem'.
     * 
     * @param allSelectionItem the value of field 'allSelectionItem'
     */
    public void setAllSelectionItem(org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem allSelectionItem)
    {
        this._allSelectionItem = allSelectionItem;
    } //-- void setAllSelectionItem(org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem) 

    /**
     * Sets the value of field 'exprSelectionItem'.
     * 
     * @param exprSelectionItem the value of field
     * 'exprSelectionItem'.
     */
    public void setExprSelectionItem(org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem exprSelectionItem)
    {
        this._exprSelectionItem = exprSelectionItem;
    } //-- void setExprSelectionItem(org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem) 

}
