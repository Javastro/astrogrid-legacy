/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BaseParameterDefinition.java,v 1.19 2004/04/05 15:17:59 nw Exp $
 */

package org.astrogrid.applications.beans.v1.parameters;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class BaseParameterDefinition.
 * 
 * @version $Revision: 1.19 $ $Date: 2004/04/05 15:17:59 $
 */
public class BaseParameterDefinition extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _type
     */
    private org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes _type;

    /**
     * The name that is to be used to display this parameter in te U
     */
    private java.lang.String _UI_Name;

    /**
     * A long description of the parameter that might be displayed
     * in the UI to help the user
     */
    private org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation _UI_Description;

    /**
     * If the parameter has a UCD then use the reference here - it
     * could help in workflow typing...
     */
    private java.lang.String _UCD;

    /**
     * a possible default for this type of parameter
     */
    private java.lang.Object _defaultValue;

    /**
     * This would ideally be an enumeration of all the possible
     * types of units?
     */
    private java.lang.String _units;


      //----------------/
     //- Constructors -/
    //----------------/

    public BaseParameterDefinition() {
        super();
    } //-- org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof BaseParameterDefinition) {
        
            BaseParameterDefinition temp = (BaseParameterDefinition)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._type != null) {
                if (temp._type == null) return false;
                else if (!(this._type.equals(temp._type))) 
                    return false;
            }
            else if (temp._type != null)
                return false;
            if (this._UI_Name != null) {
                if (temp._UI_Name == null) return false;
                else if (!(this._UI_Name.equals(temp._UI_Name))) 
                    return false;
            }
            else if (temp._UI_Name != null)
                return false;
            if (this._UI_Description != null) {
                if (temp._UI_Description == null) return false;
                else if (!(this._UI_Description.equals(temp._UI_Description))) 
                    return false;
            }
            else if (temp._UI_Description != null)
                return false;
            if (this._UCD != null) {
                if (temp._UCD == null) return false;
                else if (!(this._UCD.equals(temp._UCD))) 
                    return false;
            }
            else if (temp._UCD != null)
                return false;
            if (this._defaultValue != null) {
                if (temp._defaultValue == null) return false;
                else if (!(this._defaultValue.equals(temp._defaultValue))) 
                    return false;
            }
            else if (temp._defaultValue != null)
                return false;
            if (this._units != null) {
                if (temp._units == null) return false;
                else if (!(this._units.equals(temp._units))) 
                    return false;
            }
            else if (temp._units != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'defaultValue'. The field
     * 'defaultValue' has the following description: a possible
     * default for this type of parameter
     * 
     * @return the value of field 'defaultValue'.
     */
    public java.lang.Object getDefaultValue()
    {
        return this._defaultValue;
    } //-- java.lang.Object getDefaultValue() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes getType()
    {
        return this._type;
    } //-- org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes getType() 

    /**
     * Returns the value of field 'UCD'. The field 'UCD' has the
     * following description: If the parameter has a UCD then use
     * the reference here - it could help in workflow typing...
     * 
     * @return the value of field 'UCD'.
     */
    public java.lang.String getUCD()
    {
        return this._UCD;
    } //-- java.lang.String getUCD() 

    /**
     * Returns the value of field 'UI_Description'. The field
     * 'UI_Description' has the following description: A long
     * description of the parameter that might be displayed in the
     * UI to help the user
     * 
     * @return the value of field 'UI_Description'.
     */
    public org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation getUI_Description()
    {
        return this._UI_Description;
    } //-- org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation getUI_Description() 

    /**
     * Returns the value of field 'UI_Name'. The field 'UI_Name'
     * has the following description: The name that is to be used
     * to display this parameter in te UI
     * 
     * @return the value of field 'UI_Name'.
     */
    public java.lang.String getUI_Name()
    {
        return this._UI_Name;
    } //-- java.lang.String getUI_Name() 

    /**
     * Returns the value of field 'units'. The field 'units' has
     * the following description: This would ideally be an
     * enumeration of all the possible types of units?
     * 
     * @return the value of field 'units'.
     */
    public java.lang.String getUnits()
    {
        return this._units;
    } //-- java.lang.String getUnits() 

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
     * Sets the value of field 'defaultValue'. The field
     * 'defaultValue' has the following description: a possible
     * default for this type of parameter
     * 
     * @param defaultValue the value of field 'defaultValue'.
     */
    public void setDefaultValue(java.lang.Object defaultValue)
    {
        this._defaultValue = defaultValue;
    } //-- void setDefaultValue(java.lang.Object) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes) 

    /**
     * Sets the value of field 'UCD'. The field 'UCD' has the
     * following description: If the parameter has a UCD then use
     * the reference here - it could help in workflow typing...
     * 
     * @param UCD the value of field 'UCD'.
     */
    public void setUCD(java.lang.String UCD)
    {
        this._UCD = UCD;
    } //-- void setUCD(java.lang.String) 

    /**
     * Sets the value of field 'UI_Description'. The field
     * 'UI_Description' has the following description: A long
     * description of the parameter that might be displayed in the
     * UI to help the user
     * 
     * @param UI_Description the value of field 'UI_Description'.
     */
    public void setUI_Description(org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation UI_Description)
    {
        this._UI_Description = UI_Description;
    } //-- void setUI_Description(org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation) 

    /**
     * Sets the value of field 'UI_Name'. The field 'UI_Name' has
     * the following description: The name that is to be used to
     * display this parameter in te UI
     * 
     * @param UI_Name the value of field 'UI_Name'.
     */
    public void setUI_Name(java.lang.String UI_Name)
    {
        this._UI_Name = UI_Name;
    } //-- void setUI_Name(java.lang.String) 

    /**
     * Sets the value of field 'units'. The field 'units' has the
     * following description: This would ideally be an enumeration
     * of all the possible types of units?
     * 
     * @param units the value of field 'units'.
     */
    public void setUnits(java.lang.String units)
    {
        this._units = units;
    } //-- void setUnits(java.lang.String) 

    /**
     * Method unmarshalBaseParameterDefinition
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition unmarshalBaseParameterDefinition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition.class, reader);
    } //-- org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition unmarshalBaseParameterDefinition(java.io.Reader) 

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
