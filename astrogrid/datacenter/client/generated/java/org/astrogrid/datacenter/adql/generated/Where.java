/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Where.java,v 1.8 2003/11/26 17:01:37 nw Exp $
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
 * Class Where.
 * 
 * @version $Revision: 1.8 $ $Date: 2003/11/26 17:01:37 $
 */
public class Where extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _intersectionSearch
     */
    private org.astrogrid.datacenter.adql.generated.IntersectionSearch _intersectionSearch;

    /**
     * Field _closedSearch
     */
    private org.astrogrid.datacenter.adql.generated.ClosedSearch _closedSearch;

    /**
     * Field _predicateSearch
     */
    private org.astrogrid.datacenter.adql.generated.PredicateSearch _predicateSearch;

    /**
     * Field _circle
     */
    private org.astrogrid.datacenter.adql.generated.Circle _circle;

    /**
     * Field _area
     */
    private org.astrogrid.datacenter.adql.generated.Area _area;

    /**
     * Field _inverseSearch
     */
    private org.astrogrid.datacenter.adql.generated.InverseSearch _inverseSearch;

    /**
     * Field _unionSearch
     */
    private org.astrogrid.datacenter.adql.generated.UnionSearch _unionSearch;


      //----------------/
     //- Constructors -/
    //----------------/

    public Where() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.Where()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'area'.
     * 
     * @return the value of field 'area'.
     */
    public org.astrogrid.datacenter.adql.generated.Area getArea()
    {
        return this._area;
    } //-- org.astrogrid.datacenter.adql.generated.Area getArea() 

    /**
     * Returns the value of field 'circle'.
     * 
     * @return the value of field 'circle'.
     */
    public org.astrogrid.datacenter.adql.generated.Circle getCircle()
    {
        return this._circle;
    } //-- org.astrogrid.datacenter.adql.generated.Circle getCircle() 

    /**
     * Returns the value of field 'closedSearch'.
     * 
     * @return the value of field 'closedSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.ClosedSearch getClosedSearch()
    {
        return this._closedSearch;
    } //-- org.astrogrid.datacenter.adql.generated.ClosedSearch getClosedSearch() 

    /**
     * Returns the value of field 'intersectionSearch'.
     * 
     * @return the value of field 'intersectionSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.IntersectionSearch getIntersectionSearch()
    {
        return this._intersectionSearch;
    } //-- org.astrogrid.datacenter.adql.generated.IntersectionSearch getIntersectionSearch() 

    /**
     * Returns the value of field 'inverseSearch'.
     * 
     * @return the value of field 'inverseSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.InverseSearch getInverseSearch()
    {
        return this._inverseSearch;
    } //-- org.astrogrid.datacenter.adql.generated.InverseSearch getInverseSearch() 

    /**
     * Returns the value of field 'predicateSearch'.
     * 
     * @return the value of field 'predicateSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.PredicateSearch getPredicateSearch()
    {
        return this._predicateSearch;
    } //-- org.astrogrid.datacenter.adql.generated.PredicateSearch getPredicateSearch() 

    /**
     * Returns the value of field 'unionSearch'.
     * 
     * @return the value of field 'unionSearch'.
     */
    public org.astrogrid.datacenter.adql.generated.UnionSearch getUnionSearch()
    {
        return this._unionSearch;
    } //-- org.astrogrid.datacenter.adql.generated.UnionSearch getUnionSearch() 

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
     * Sets the value of field 'area'.
     * 
     * @param area the value of field 'area'.
     */
    public void setArea(org.astrogrid.datacenter.adql.generated.Area area)
    {
        this._area = area;
    } //-- void setArea(org.astrogrid.datacenter.adql.generated.Area) 

    /**
     * Sets the value of field 'circle'.
     * 
     * @param circle the value of field 'circle'.
     */
    public void setCircle(org.astrogrid.datacenter.adql.generated.Circle circle)
    {
        this._circle = circle;
    } //-- void setCircle(org.astrogrid.datacenter.adql.generated.Circle) 

    /**
     * Sets the value of field 'closedSearch'.
     * 
     * @param closedSearch the value of field 'closedSearch'.
     */
    public void setClosedSearch(org.astrogrid.datacenter.adql.generated.ClosedSearch closedSearch)
    {
        this._closedSearch = closedSearch;
    } //-- void setClosedSearch(org.astrogrid.datacenter.adql.generated.ClosedSearch) 

    /**
     * Sets the value of field 'intersectionSearch'.
     * 
     * @param intersectionSearch the value of field
     * 'intersectionSearch'.
     */
    public void setIntersectionSearch(org.astrogrid.datacenter.adql.generated.IntersectionSearch intersectionSearch)
    {
        this._intersectionSearch = intersectionSearch;
    } //-- void setIntersectionSearch(org.astrogrid.datacenter.adql.generated.IntersectionSearch) 

    /**
     * Sets the value of field 'inverseSearch'.
     * 
     * @param inverseSearch the value of field 'inverseSearch'.
     */
    public void setInverseSearch(org.astrogrid.datacenter.adql.generated.InverseSearch inverseSearch)
    {
        this._inverseSearch = inverseSearch;
    } //-- void setInverseSearch(org.astrogrid.datacenter.adql.generated.InverseSearch) 

    /**
     * Sets the value of field 'predicateSearch'.
     * 
     * @param predicateSearch the value of field 'predicateSearch'.
     */
    public void setPredicateSearch(org.astrogrid.datacenter.adql.generated.PredicateSearch predicateSearch)
    {
        this._predicateSearch = predicateSearch;
    } //-- void setPredicateSearch(org.astrogrid.datacenter.adql.generated.PredicateSearch) 

    /**
     * Sets the value of field 'unionSearch'.
     * 
     * @param unionSearch the value of field 'unionSearch'.
     */
    public void setUnionSearch(org.astrogrid.datacenter.adql.generated.UnionSearch unionSearch)
    {
        this._unionSearch = unionSearch;
    } //-- void setUnionSearch(org.astrogrid.datacenter.adql.generated.UnionSearch) 

    /**
     * Method unmarshalWhere
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.Where unmarshalWhere(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.Where) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.Where.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.Where unmarshalWhere(java.io.Reader) 

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
