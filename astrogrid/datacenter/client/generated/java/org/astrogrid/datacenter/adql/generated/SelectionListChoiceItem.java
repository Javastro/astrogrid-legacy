/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SelectionListChoiceItem.java,v 1.10 2003/11/27 17:27:15 nw Exp $
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
/**EDITED by HAND - work around for bug - made a subclass of AbstractQOM.
 * Class SelectionListChoiceItem.
 * 
 * @version $Revision: 1.10 $ $Date: 2003/11/27 17:27:15 $
 */
public class SelectionListChoiceItem extends AbstractQOM  implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _unaryExpr
     */
    private org.astrogrid.datacenter.adql.generated.UnaryExpr _unaryExpr;

    /**
     * Field _binaryExpr
     */
    private org.astrogrid.datacenter.adql.generated.BinaryExpr _binaryExpr;

    /**
     * Field _closedExpr
     */
    private org.astrogrid.datacenter.adql.generated.ClosedExpr _closedExpr;

    /**
     * Field _columnExpr
     */
    private org.astrogrid.datacenter.adql.generated.ColumnExpr _columnExpr;

    /**
     * Field _functionExpr
     */
    private org.astrogrid.datacenter.adql.generated.FunctionExpr _functionExpr;

    /**
     * Field _atomExpr
     */
    private org.astrogrid.datacenter.adql.generated.AtomExpr _atomExpr;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectionListChoiceItem() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoiceItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'atomExpr'.
     * 
     * @return the value of field 'atomExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.AtomExpr getAtomExpr()
    {
        return this._atomExpr;
    } //-- org.astrogrid.datacenter.adql.generated.AtomExpr getAtomExpr() 

    /**
     * Returns the value of field 'binaryExpr'.
     * 
     * @return the value of field 'binaryExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.BinaryExpr getBinaryExpr()
    {
        return this._binaryExpr;
    } //-- org.astrogrid.datacenter.adql.generated.BinaryExpr getBinaryExpr() 

    /**
     * Returns the value of field 'closedExpr'.
     * 
     * @return the value of field 'closedExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ClosedExpr getClosedExpr()
    {
        return this._closedExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ClosedExpr getClosedExpr() 

    /**
     * Returns the value of field 'columnExpr'.
     * 
     * @return the value of field 'columnExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ColumnExpr getColumnExpr()
    {
        return this._columnExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ColumnExpr getColumnExpr() 

    /**
     * Returns the value of field 'functionExpr'.
     * 
     * @return the value of field 'functionExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.FunctionExpr getFunctionExpr()
    {
        return this._functionExpr;
    } //-- org.astrogrid.datacenter.adql.generated.FunctionExpr getFunctionExpr() 

    /**
     * Returns the value of field 'unaryExpr'.
     * 
     * @return the value of field 'unaryExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.UnaryExpr getUnaryExpr()
    {
        return this._unaryExpr;
    } //-- org.astrogrid.datacenter.adql.generated.UnaryExpr getUnaryExpr() 

    /**
     * Sets the value of field 'atomExpr'.
     * 
     * @param atomExpr the value of field 'atomExpr'.
     */
    public void setAtomExpr(org.astrogrid.datacenter.adql.generated.AtomExpr atomExpr)
    {
        this._atomExpr = atomExpr;
    } //-- void setAtomExpr(org.astrogrid.datacenter.adql.generated.AtomExpr) 

    /**
     * Sets the value of field 'binaryExpr'.
     * 
     * @param binaryExpr the value of field 'binaryExpr'.
     */
    public void setBinaryExpr(org.astrogrid.datacenter.adql.generated.BinaryExpr binaryExpr)
    {
        this._binaryExpr = binaryExpr;
    } //-- void setBinaryExpr(org.astrogrid.datacenter.adql.generated.BinaryExpr) 

    /**
     * Sets the value of field 'closedExpr'.
     * 
     * @param closedExpr the value of field 'closedExpr'.
     */
    public void setClosedExpr(org.astrogrid.datacenter.adql.generated.ClosedExpr closedExpr)
    {
        this._closedExpr = closedExpr;
    } //-- void setClosedExpr(org.astrogrid.datacenter.adql.generated.ClosedExpr) 

    /**
     * Sets the value of field 'columnExpr'.
     * 
     * @param columnExpr the value of field 'columnExpr'.
     */
    public void setColumnExpr(org.astrogrid.datacenter.adql.generated.ColumnExpr columnExpr)
    {
        this._columnExpr = columnExpr;
    } //-- void setColumnExpr(org.astrogrid.datacenter.adql.generated.ColumnExpr) 

    /**
     * Sets the value of field 'functionExpr'.
     * 
     * @param functionExpr the value of field 'functionExpr'.
     */
    public void setFunctionExpr(org.astrogrid.datacenter.adql.generated.FunctionExpr functionExpr)
    {
        this._functionExpr = functionExpr;
    } //-- void setFunctionExpr(org.astrogrid.datacenter.adql.generated.FunctionExpr) 

    /**
     * Sets the value of field 'unaryExpr'.
     * 
     * @param unaryExpr the value of field 'unaryExpr'.
     */
    public void setUnaryExpr(org.astrogrid.datacenter.adql.generated.UnaryExpr unaryExpr)
    {
        this._unaryExpr = unaryExpr;
    } //-- void setUnaryExpr(org.astrogrid.datacenter.adql.generated.UnaryExpr) 

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
