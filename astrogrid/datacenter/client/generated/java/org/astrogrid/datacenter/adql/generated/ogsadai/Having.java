/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Having.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class Having.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Having extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _intersectionSearch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.IntersectionSearch _intersectionSearch;

    /**
     * Field _closedSearch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ClosedSearch _closedSearch;

    /**
     * Field _regionSearch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.RegionSearch _regionSearch;

    /**
     * Field _inverseSearch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.InverseSearch _inverseSearch;

    /**
     * Field _predicateSearch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.PredicateSearch _predicateSearch;

    /**
     * Field _unionSearch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.UnionSearch _unionSearch;

    /**
     * Field _XMatch
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.XMatch _XMatch;


      //----------------/
     //- Constructors -/
    //----------------/

    public Having() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Having()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'closedSearch'.
     * 
     * @return the value of field 'closedSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ClosedSearch getClosedSearch()
    {
        return this._closedSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ClosedSearch getClosedSearch() 

    /**
     * Returns the value of field 'intersectionSearch'.
     * 
     * @return the value of field 'intersectionSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.IntersectionSearch getIntersectionSearch()
    {
        return this._intersectionSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.IntersectionSearch getIntersectionSearch() 

    /**
     * Returns the value of field 'inverseSearch'.
     * 
     * @return the value of field 'inverseSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.InverseSearch getInverseSearch()
    {
        return this._inverseSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.InverseSearch getInverseSearch() 

    /**
     * Returns the value of field 'predicateSearch'.
     * 
     * @return the value of field 'predicateSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.PredicateSearch getPredicateSearch()
    {
        return this._predicateSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.PredicateSearch getPredicateSearch() 

    /**
     * Returns the value of field 'regionSearch'.
     * 
     * @return the value of field 'regionSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.RegionSearch getRegionSearch()
    {
        return this._regionSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.RegionSearch getRegionSearch() 

    /**
     * Returns the value of field 'unionSearch'.
     * 
     * @return the value of field 'unionSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.UnionSearch getUnionSearch()
    {
        return this._unionSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.UnionSearch getUnionSearch() 

    /**
     * Returns the value of field 'XMatch'.
     * 
     * @return the value of field 'XMatch'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.XMatch getXMatch()
    {
        return this._XMatch;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.XMatch getXMatch() 

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
     * Sets the value of field 'closedSearch'.
     * 
     * @param closedSearch the value of field 'closedSearch'.
     */
    public void setClosedSearch(org.astrogrid.datacenter.adql.generated.ogsadai.ClosedSearch closedSearch)
    {
        this._closedSearch = closedSearch;
    } //-- void setClosedSearch(org.astrogrid.datacenter.adql.generated.ogsadai.ClosedSearch) 

    /**
     * Sets the value of field 'intersectionSearch'.
     * 
     * @param intersectionSearch the value of field
     * 'intersectionSearch'.
     */
    public void setIntersectionSearch(org.astrogrid.datacenter.adql.generated.ogsadai.IntersectionSearch intersectionSearch)
    {
        this._intersectionSearch = intersectionSearch;
    } //-- void setIntersectionSearch(org.astrogrid.datacenter.adql.generated.ogsadai.IntersectionSearch) 

    /**
     * Sets the value of field 'inverseSearch'.
     * 
     * @param inverseSearch the value of field 'inverseSearch'.
     */
    public void setInverseSearch(org.astrogrid.datacenter.adql.generated.ogsadai.InverseSearch inverseSearch)
    {
        this._inverseSearch = inverseSearch;
    } //-- void setInverseSearch(org.astrogrid.datacenter.adql.generated.ogsadai.InverseSearch) 

    /**
     * Sets the value of field 'predicateSearch'.
     * 
     * @param predicateSearch the value of field 'predicateSearch'.
     */
    public void setPredicateSearch(org.astrogrid.datacenter.adql.generated.ogsadai.PredicateSearch predicateSearch)
    {
        this._predicateSearch = predicateSearch;
    } //-- void setPredicateSearch(org.astrogrid.datacenter.adql.generated.ogsadai.PredicateSearch) 

    /**
     * Sets the value of field 'regionSearch'.
     * 
     * @param regionSearch the value of field 'regionSearch'.
     */
    public void setRegionSearch(org.astrogrid.datacenter.adql.generated.ogsadai.RegionSearch regionSearch)
    {
        this._regionSearch = regionSearch;
    } //-- void setRegionSearch(org.astrogrid.datacenter.adql.generated.ogsadai.RegionSearch) 

    /**
     * Sets the value of field 'unionSearch'.
     * 
     * @param unionSearch the value of field 'unionSearch'.
     */
    public void setUnionSearch(org.astrogrid.datacenter.adql.generated.ogsadai.UnionSearch unionSearch)
    {
        this._unionSearch = unionSearch;
    } //-- void setUnionSearch(org.astrogrid.datacenter.adql.generated.ogsadai.UnionSearch) 

    /**
     * Sets the value of field 'XMatch'.
     * 
     * @param XMatch the value of field 'XMatch'.
     */
    public void setXMatch(org.astrogrid.datacenter.adql.generated.ogsadai.XMatch XMatch)
    {
        this._XMatch = XMatch;
    } //-- void setXMatch(org.astrogrid.datacenter.adql.generated.ogsadai.XMatch) 

    /**
     * Method unmarshalHaving
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Having unmarshalHaving(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Having) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Having.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Having unmarshalHaving(java.io.Reader) 

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
