/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PredicateSearch.java,v 1.4 2003/10/13 13:49:46 nw Exp $
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
 * Class PredicateSearch.
 * 
 * @version $Revision: 1.4 $ $Date: 2003/10/13 13:49:46 $
 */
public class PredicateSearch extends org.astrogrid.datacenter.adql.generated.Search 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _likePred
     */
    private org.astrogrid.datacenter.adql.generated.LikePred _likePred;

    /**
     * Field _comparisonPred
     */
    private org.astrogrid.datacenter.adql.generated.ComparisonPred _comparisonPred;

    /**
     * Field _betweenPred
     */
    private org.astrogrid.datacenter.adql.generated.BetweenPred _betweenPred;


      //----------------/
     //- Constructors -/
    //----------------/

    public PredicateSearch() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.PredicateSearch()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'betweenPred'.
     * 
     * @return the value of field 'betweenPred'.
     */
    public org.astrogrid.datacenter.adql.generated.BetweenPred getBetweenPred()
    {
        return this._betweenPred;
    } //-- org.astrogrid.datacenter.adql.generated.BetweenPred getBetweenPred() 

    /**
     * Returns the value of field 'comparisonPred'.
     * 
     * @return the value of field 'comparisonPred'.
     */
    public org.astrogrid.datacenter.adql.generated.ComparisonPred getComparisonPred()
    {
        return this._comparisonPred;
    } //-- org.astrogrid.datacenter.adql.generated.ComparisonPred getComparisonPred() 

    /**
     * Returns the value of field 'likePred'.
     * 
     * @return the value of field 'likePred'.
     */
    public org.astrogrid.datacenter.adql.generated.LikePred getLikePred()
    {
        return this._likePred;
    } //-- org.astrogrid.datacenter.adql.generated.LikePred getLikePred() 

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
     * Sets the value of field 'betweenPred'.
     * 
     * @param betweenPred the value of field 'betweenPred'.
     */
    public void setBetweenPred(org.astrogrid.datacenter.adql.generated.BetweenPred betweenPred)
    {
        this._betweenPred = betweenPred;
    } //-- void setBetweenPred(org.astrogrid.datacenter.adql.generated.BetweenPred) 

    /**
     * Sets the value of field 'comparisonPred'.
     * 
     * @param comparisonPred the value of field 'comparisonPred'.
     */
    public void setComparisonPred(org.astrogrid.datacenter.adql.generated.ComparisonPred comparisonPred)
    {
        this._comparisonPred = comparisonPred;
    } //-- void setComparisonPred(org.astrogrid.datacenter.adql.generated.ComparisonPred) 

    /**
     * Sets the value of field 'likePred'.
     * 
     * @param likePred the value of field 'likePred'.
     */
    public void setLikePred(org.astrogrid.datacenter.adql.generated.LikePred likePred)
    {
        this._likePred = likePred;
    } //-- void setLikePred(org.astrogrid.datacenter.adql.generated.LikePred) 

    /**
     * Method unmarshalPredicateSearch
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.PredicateSearch unmarshalPredicateSearch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.PredicateSearch) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.PredicateSearch.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.PredicateSearch unmarshalPredicateSearch(java.io.Reader) 

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
