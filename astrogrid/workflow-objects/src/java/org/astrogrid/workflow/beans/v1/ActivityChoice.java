/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ActivityChoice.java,v 1.3 2004/03/02 16:50:20 nw Exp $
 */

package org.astrogrid.workflow.beans.v1;

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
 * Class ActivityChoice.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/02 16:50:20 $
 */
public class ActivityChoice extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sequence
     */
    private org.astrogrid.workflow.beans.v1.Sequence _sequence;

    /**
     * Field _flow
     */
    private org.astrogrid.workflow.beans.v1.Flow _flow;

    /**
     * Field _step
     */
    private org.astrogrid.workflow.beans.v1.Step _step;


      //----------------/
     //- Constructors -/
    //----------------/

    public ActivityChoice() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.ActivityChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'flow'.
     * 
     * @return the value of field 'flow'.
     */
    public org.astrogrid.workflow.beans.v1.Flow getFlow()
    {
        return this._flow;
    } //-- org.astrogrid.workflow.beans.v1.Flow getFlow() 

    /**
     * Returns the value of field 'sequence'.
     * 
     * @return the value of field 'sequence'.
     */
    public org.astrogrid.workflow.beans.v1.Sequence getSequence()
    {
        return this._sequence;
    } //-- org.astrogrid.workflow.beans.v1.Sequence getSequence() 

    /**
     * Returns the value of field 'step'.
     * 
     * @return the value of field 'step'.
     */
    public org.astrogrid.workflow.beans.v1.Step getStep()
    {
        return this._step;
    } //-- org.astrogrid.workflow.beans.v1.Step getStep() 

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
     * Sets the value of field 'flow'.
     * 
     * @param flow the value of field 'flow'.
     */
    public void setFlow(org.astrogrid.workflow.beans.v1.Flow flow)
    {
        this._flow = flow;
    } //-- void setFlow(org.astrogrid.workflow.beans.v1.Flow) 

    /**
     * Sets the value of field 'sequence'.
     * 
     * @param sequence the value of field 'sequence'.
     */
    public void setSequence(org.astrogrid.workflow.beans.v1.Sequence sequence)
    {
        this._sequence = sequence;
    } //-- void setSequence(org.astrogrid.workflow.beans.v1.Sequence) 

    /**
     * Sets the value of field 'step'.
     * 
     * @param step the value of field 'step'.
     */
    public void setStep(org.astrogrid.workflow.beans.v1.Step step)
    {
        this._step = step;
    } //-- void setStep(org.astrogrid.workflow.beans.v1.Step) 

    /**
     * Method unmarshalActivityChoice
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.ActivityChoice unmarshalActivityChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.ActivityChoice) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.ActivityChoice.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.ActivityChoice unmarshalActivityChoice(java.io.Reader) 

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
