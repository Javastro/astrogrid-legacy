/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ResourceType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.registry.generated.package.types.CategoryType;
import org.astrogrid.registry.generated.package.types.ContentLevelType;
import org.astrogrid.registry.generated.package.types.ResourceTypeStatusType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ResourceType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class ResourceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The date this resource metadata description was created.
     *  
     */
    private org.exolab.castor.types.Date _created;

    /**
     * The date this resource metadata description was last
     * updated.
     *  
     */
    private org.exolab.castor.types.Date _updated;

    /**
     * a tag indicating whether this resource is believed to be
     * still
     *  actively maintained.
     *  
     */
    private org.astrogrid.registry.generated.package.types.ResourceTypeStatusType _status = org.astrogrid.registry.generated.package.types.ResourceTypeStatusType.valueOf("active");

    /**
     * Unambiguous reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     */
    private org.astrogrid.registry.generated.package.Identifier _identifier;

    /**
     * the full name of a resource
     */
    private java.lang.String _title;

    /**
     * a short name or abbreviation for this resource that
     * applications 
     *  may use to refer to this resource in a compact display. 
     *  
     */
    private java.lang.String _shortName;

    /**
     * An informational, human-readable overview of a resource.
     *  
     */
    private org.astrogrid.registry.generated.package.Summary _summary;

    /**
     * Nature or genre of the content of the resource
     *  
     */
    private java.util.Vector _typeList;

    /**
     * an description of a relationship to another resource. 
     *  
     */
    private java.util.Vector _relatedResourceList;

    /**
     * This is typically not provided unless this resource is 
     *  claiming to be a mirror of another. Multiple 
     *  LogicalIdentifiers can be present, usually with different
     *  role attributes. For each role, there should be a 
     *  corresponding relationship described under a
     * RelatedResource,
     *  when applicable.
     *  
     */
    private java.util.Vector _logicalIdentifierList;

    /**
     * Information regarding the general curation of the resource
     *  
     */
    private org.astrogrid.registry.generated.package.Curation _curation;

    /**
     * List of topics, object types, or other descriptive keywords 
     *  about the resource. 
     *  
     */
    private java.util.Vector _subjectList;

    /**
     * Description of the content level or intended audience
     *  
     */
    private java.util.Vector _contentLevelList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResourceType() {
        super();
        setStatus(org.astrogrid.registry.generated.package.types.ResourceTypeStatusType.valueOf("active"));
        _typeList = new Vector();
        _relatedResourceList = new Vector();
        _logicalIdentifierList = new Vector();
        _subjectList = new Vector();
        _contentLevelList = new Vector();
    } //-- org.astrogrid.registry.generated.package.ResourceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContentLevel
     * 
     * @param vContentLevel
     */
    public void addContentLevel(org.astrogrid.registry.generated.package.types.ContentLevelType vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        _contentLevelList.addElement(vContentLevel);
    } //-- void addContentLevel(org.astrogrid.registry.generated.package.types.ContentLevelType) 

    /**
     * Method addContentLevel
     * 
     * @param index
     * @param vContentLevel
     */
    public void addContentLevel(int index, org.astrogrid.registry.generated.package.types.ContentLevelType vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        _contentLevelList.insertElementAt(vContentLevel, index);
    } //-- void addContentLevel(int, org.astrogrid.registry.generated.package.types.ContentLevelType) 

    /**
     * Method addLogicalIdentifier
     * 
     * @param vLogicalIdentifier
     */
    public void addLogicalIdentifier(org.astrogrid.registry.generated.package.LogicalIdentifier vLogicalIdentifier)
        throws java.lang.IndexOutOfBoundsException
    {
        _logicalIdentifierList.addElement(vLogicalIdentifier);
    } //-- void addLogicalIdentifier(org.astrogrid.registry.generated.package.LogicalIdentifier) 

    /**
     * Method addLogicalIdentifier
     * 
     * @param index
     * @param vLogicalIdentifier
     */
    public void addLogicalIdentifier(int index, org.astrogrid.registry.generated.package.LogicalIdentifier vLogicalIdentifier)
        throws java.lang.IndexOutOfBoundsException
    {
        _logicalIdentifierList.insertElementAt(vLogicalIdentifier, index);
    } //-- void addLogicalIdentifier(int, org.astrogrid.registry.generated.package.LogicalIdentifier) 

    /**
     * Method addRelatedResource
     * 
     * @param vRelatedResource
     */
    public void addRelatedResource(org.astrogrid.registry.generated.package.RelatedResource vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _relatedResourceList.addElement(vRelatedResource);
    } //-- void addRelatedResource(org.astrogrid.registry.generated.package.RelatedResource) 

    /**
     * Method addRelatedResource
     * 
     * @param index
     * @param vRelatedResource
     */
    public void addRelatedResource(int index, org.astrogrid.registry.generated.package.RelatedResource vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _relatedResourceList.insertElementAt(vRelatedResource, index);
    } //-- void addRelatedResource(int, org.astrogrid.registry.generated.package.RelatedResource) 

    /**
     * Method addSubject
     * 
     * @param vSubject
     */
    public void addSubject(java.lang.String vSubject)
        throws java.lang.IndexOutOfBoundsException
    {
        _subjectList.addElement(vSubject);
    } //-- void addSubject(java.lang.String) 

    /**
     * Method addSubject
     * 
     * @param index
     * @param vSubject
     */
    public void addSubject(int index, java.lang.String vSubject)
        throws java.lang.IndexOutOfBoundsException
    {
        _subjectList.insertElementAt(vSubject, index);
    } //-- void addSubject(int, java.lang.String) 

    /**
     * Method addType
     * 
     * @param vType
     */
    public void addType(org.astrogrid.registry.generated.package.types.CategoryType vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeList.addElement(vType);
    } //-- void addType(org.astrogrid.registry.generated.package.types.CategoryType) 

    /**
     * Method addType
     * 
     * @param index
     * @param vType
     */
    public void addType(int index, org.astrogrid.registry.generated.package.types.CategoryType vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeList.insertElementAt(vType, index);
    } //-- void addType(int, org.astrogrid.registry.generated.package.types.CategoryType) 

    /**
     * Method enumerateContentLevel
     */
    public java.util.Enumeration enumerateContentLevel()
    {
        return _contentLevelList.elements();
    } //-- java.util.Enumeration enumerateContentLevel() 

    /**
     * Method enumerateLogicalIdentifier
     */
    public java.util.Enumeration enumerateLogicalIdentifier()
    {
        return _logicalIdentifierList.elements();
    } //-- java.util.Enumeration enumerateLogicalIdentifier() 

    /**
     * Method enumerateRelatedResource
     */
    public java.util.Enumeration enumerateRelatedResource()
    {
        return _relatedResourceList.elements();
    } //-- java.util.Enumeration enumerateRelatedResource() 

    /**
     * Method enumerateSubject
     */
    public java.util.Enumeration enumerateSubject()
    {
        return _subjectList.elements();
    } //-- java.util.Enumeration enumerateSubject() 

    /**
     * Method enumerateType
     */
    public java.util.Enumeration enumerateType()
    {
        return _typeList.elements();
    } //-- java.util.Enumeration enumerateType() 

    /**
     * Method getContentLevel
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.types.ContentLevelType getContentLevel(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contentLevelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.types.ContentLevelType) _contentLevelList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.types.ContentLevelType getContentLevel(int) 

    /**
     * Method getContentLevel
     */
    public org.astrogrid.registry.generated.package.types.ContentLevelType[] getContentLevel()
    {
        int size = _contentLevelList.size();
        org.astrogrid.registry.generated.package.types.ContentLevelType[] mArray = new org.astrogrid.registry.generated.package.types.ContentLevelType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.types.ContentLevelType) _contentLevelList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.types.ContentLevelType[] getContentLevel() 

    /**
     * Method getContentLevelCount
     */
    public int getContentLevelCount()
    {
        return _contentLevelList.size();
    } //-- int getContentLevelCount() 

    /**
     * Returns the value of field 'created'. The field 'created'
     * has the following description: The date this resource
     * metadata description was created.
     *  
     * 
     * @return the value of field 'created'.
     */
    public org.exolab.castor.types.Date getCreated()
    {
        return this._created;
    } //-- org.exolab.castor.types.Date getCreated() 

    /**
     * Returns the value of field 'curation'. The field 'curation'
     * has the following description: Information regarding the
     * general curation of the resource
     *  
     * 
     * @return the value of field 'curation'.
     */
    public org.astrogrid.registry.generated.package.Curation getCuration()
    {
        return this._curation;
    } //-- org.astrogrid.registry.generated.package.Curation getCuration() 

    /**
     * Returns the value of field 'identifier'. The field
     * 'identifier' has the following description: Unambiguous
     * reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @return the value of field 'identifier'.
     */
    public org.astrogrid.registry.generated.package.Identifier getIdentifier()
    {
        return this._identifier;
    } //-- org.astrogrid.registry.generated.package.Identifier getIdentifier() 

    /**
     * Method getLogicalIdentifier
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.LogicalIdentifier getLogicalIdentifier(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logicalIdentifierList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.LogicalIdentifier) _logicalIdentifierList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.LogicalIdentifier getLogicalIdentifier(int) 

    /**
     * Method getLogicalIdentifier
     */
    public org.astrogrid.registry.generated.package.LogicalIdentifier[] getLogicalIdentifier()
    {
        int size = _logicalIdentifierList.size();
        org.astrogrid.registry.generated.package.LogicalIdentifier[] mArray = new org.astrogrid.registry.generated.package.LogicalIdentifier[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.LogicalIdentifier) _logicalIdentifierList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.LogicalIdentifier[] getLogicalIdentifier() 

    /**
     * Method getLogicalIdentifierCount
     */
    public int getLogicalIdentifierCount()
    {
        return _logicalIdentifierList.size();
    } //-- int getLogicalIdentifierCount() 

    /**
     * Method getRelatedResource
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.RelatedResource getRelatedResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relatedResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.RelatedResource) _relatedResourceList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.RelatedResource getRelatedResource(int) 

    /**
     * Method getRelatedResource
     */
    public org.astrogrid.registry.generated.package.RelatedResource[] getRelatedResource()
    {
        int size = _relatedResourceList.size();
        org.astrogrid.registry.generated.package.RelatedResource[] mArray = new org.astrogrid.registry.generated.package.RelatedResource[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.RelatedResource) _relatedResourceList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.RelatedResource[] getRelatedResource() 

    /**
     * Method getRelatedResourceCount
     */
    public int getRelatedResourceCount()
    {
        return _relatedResourceList.size();
    } //-- int getRelatedResourceCount() 

    /**
     * Returns the value of field 'shortName'. The field
     * 'shortName' has the following description: a short name or
     * abbreviation for this resource that applications 
     *  may use to refer to this resource in a compact display. 
     *  
     * 
     * @return the value of field 'shortName'.
     */
    public java.lang.String getShortName()
    {
        return this._shortName;
    } //-- java.lang.String getShortName() 

    /**
     * Returns the value of field 'status'. The field 'status' has
     * the following description: a tag indicating whether this
     * resource is believed to be still
     *  actively maintained.
     *  
     * 
     * @return the value of field 'status'.
     */
    public org.astrogrid.registry.generated.package.types.ResourceTypeStatusType getStatus()
    {
        return this._status;
    } //-- org.astrogrid.registry.generated.package.types.ResourceTypeStatusType getStatus() 

    /**
     * Method getSubject
     * 
     * @param index
     */
    public java.lang.String getSubject(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _subjectList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_subjectList.elementAt(index);
    } //-- java.lang.String getSubject(int) 

    /**
     * Method getSubject
     */
    public java.lang.String[] getSubject()
    {
        int size = _subjectList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_subjectList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getSubject() 

    /**
     * Method getSubjectCount
     */
    public int getSubjectCount()
    {
        return _subjectList.size();
    } //-- int getSubjectCount() 

    /**
     * Returns the value of field 'summary'. The field 'summary'
     * has the following description: An informational,
     * human-readable overview of a resource.
     *  
     * 
     * @return the value of field 'summary'.
     */
    public org.astrogrid.registry.generated.package.Summary getSummary()
    {
        return this._summary;
    } //-- org.astrogrid.registry.generated.package.Summary getSummary() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: the full name of a resource
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Method getType
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.types.CategoryType getType(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.types.CategoryType) _typeList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.types.CategoryType getType(int) 

    /**
     * Method getType
     */
    public org.astrogrid.registry.generated.package.types.CategoryType[] getType()
    {
        int size = _typeList.size();
        org.astrogrid.registry.generated.package.types.CategoryType[] mArray = new org.astrogrid.registry.generated.package.types.CategoryType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.types.CategoryType) _typeList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.types.CategoryType[] getType() 

    /**
     * Method getTypeCount
     */
    public int getTypeCount()
    {
        return _typeList.size();
    } //-- int getTypeCount() 

    /**
     * Returns the value of field 'updated'. The field 'updated'
     * has the following description: The date this resource
     * metadata description was last updated.
     *  
     * 
     * @return the value of field 'updated'.
     */
    public org.exolab.castor.types.Date getUpdated()
    {
        return this._updated;
    } //-- org.exolab.castor.types.Date getUpdated() 

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
     * Method removeAllContentLevel
     */
    public void removeAllContentLevel()
    {
        _contentLevelList.removeAllElements();
    } //-- void removeAllContentLevel() 

    /**
     * Method removeAllLogicalIdentifier
     */
    public void removeAllLogicalIdentifier()
    {
        _logicalIdentifierList.removeAllElements();
    } //-- void removeAllLogicalIdentifier() 

    /**
     * Method removeAllRelatedResource
     */
    public void removeAllRelatedResource()
    {
        _relatedResourceList.removeAllElements();
    } //-- void removeAllRelatedResource() 

    /**
     * Method removeAllSubject
     */
    public void removeAllSubject()
    {
        _subjectList.removeAllElements();
    } //-- void removeAllSubject() 

    /**
     * Method removeAllType
     */
    public void removeAllType()
    {
        _typeList.removeAllElements();
    } //-- void removeAllType() 

    /**
     * Method removeContentLevel
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.types.ContentLevelType removeContentLevel(int index)
    {
        java.lang.Object obj = _contentLevelList.elementAt(index);
        _contentLevelList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.types.ContentLevelType) obj;
    } //-- org.astrogrid.registry.generated.package.types.ContentLevelType removeContentLevel(int) 

    /**
     * Method removeLogicalIdentifier
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.LogicalIdentifier removeLogicalIdentifier(int index)
    {
        java.lang.Object obj = _logicalIdentifierList.elementAt(index);
        _logicalIdentifierList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.LogicalIdentifier) obj;
    } //-- org.astrogrid.registry.generated.package.LogicalIdentifier removeLogicalIdentifier(int) 

    /**
     * Method removeRelatedResource
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.RelatedResource removeRelatedResource(int index)
    {
        java.lang.Object obj = _relatedResourceList.elementAt(index);
        _relatedResourceList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.RelatedResource) obj;
    } //-- org.astrogrid.registry.generated.package.RelatedResource removeRelatedResource(int) 

    /**
     * Method removeSubject
     * 
     * @param index
     */
    public java.lang.String removeSubject(int index)
    {
        java.lang.Object obj = _subjectList.elementAt(index);
        _subjectList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeSubject(int) 

    /**
     * Method removeType
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.types.CategoryType removeType(int index)
    {
        java.lang.Object obj = _typeList.elementAt(index);
        _typeList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.types.CategoryType) obj;
    } //-- org.astrogrid.registry.generated.package.types.CategoryType removeType(int) 

    /**
     * Method setContentLevel
     * 
     * @param index
     * @param vContentLevel
     */
    public void setContentLevel(int index, org.astrogrid.registry.generated.package.types.ContentLevelType vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contentLevelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _contentLevelList.setElementAt(vContentLevel, index);
    } //-- void setContentLevel(int, org.astrogrid.registry.generated.package.types.ContentLevelType) 

    /**
     * Method setContentLevel
     * 
     * @param contentLevelArray
     */
    public void setContentLevel(org.astrogrid.registry.generated.package.types.ContentLevelType[] contentLevelArray)
    {
        //-- copy array
        _contentLevelList.removeAllElements();
        for (int i = 0; i < contentLevelArray.length; i++) {
            _contentLevelList.addElement(contentLevelArray[i]);
        }
    } //-- void setContentLevel(org.astrogrid.registry.generated.package.types.ContentLevelType) 

    /**
     * Sets the value of field 'created'. The field 'created' has
     * the following description: The date this resource metadata
     * description was created.
     *  
     * 
     * @param created the value of field 'created'.
     */
    public void setCreated(org.exolab.castor.types.Date created)
    {
        this._created = created;
    } //-- void setCreated(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'curation'. The field 'curation' has
     * the following description: Information regarding the general
     * curation of the resource
     *  
     * 
     * @param curation the value of field 'curation'.
     */
    public void setCuration(org.astrogrid.registry.generated.package.Curation curation)
    {
        this._curation = curation;
    } //-- void setCuration(org.astrogrid.registry.generated.package.Curation) 

    /**
     * Sets the value of field 'identifier'. The field 'identifier'
     * has the following description: Unambiguous reference to the
     * resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @param identifier the value of field 'identifier'.
     */
    public void setIdentifier(org.astrogrid.registry.generated.package.Identifier identifier)
    {
        this._identifier = identifier;
    } //-- void setIdentifier(org.astrogrid.registry.generated.package.Identifier) 

    /**
     * Method setLogicalIdentifier
     * 
     * @param index
     * @param vLogicalIdentifier
     */
    public void setLogicalIdentifier(int index, org.astrogrid.registry.generated.package.LogicalIdentifier vLogicalIdentifier)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logicalIdentifierList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _logicalIdentifierList.setElementAt(vLogicalIdentifier, index);
    } //-- void setLogicalIdentifier(int, org.astrogrid.registry.generated.package.LogicalIdentifier) 

    /**
     * Method setLogicalIdentifier
     * 
     * @param logicalIdentifierArray
     */
    public void setLogicalIdentifier(org.astrogrid.registry.generated.package.LogicalIdentifier[] logicalIdentifierArray)
    {
        //-- copy array
        _logicalIdentifierList.removeAllElements();
        for (int i = 0; i < logicalIdentifierArray.length; i++) {
            _logicalIdentifierList.addElement(logicalIdentifierArray[i]);
        }
    } //-- void setLogicalIdentifier(org.astrogrid.registry.generated.package.LogicalIdentifier) 

    /**
     * Method setRelatedResource
     * 
     * @param index
     * @param vRelatedResource
     */
    public void setRelatedResource(int index, org.astrogrid.registry.generated.package.RelatedResource vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relatedResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relatedResourceList.setElementAt(vRelatedResource, index);
    } //-- void setRelatedResource(int, org.astrogrid.registry.generated.package.RelatedResource) 

    /**
     * Method setRelatedResource
     * 
     * @param relatedResourceArray
     */
    public void setRelatedResource(org.astrogrid.registry.generated.package.RelatedResource[] relatedResourceArray)
    {
        //-- copy array
        _relatedResourceList.removeAllElements();
        for (int i = 0; i < relatedResourceArray.length; i++) {
            _relatedResourceList.addElement(relatedResourceArray[i]);
        }
    } //-- void setRelatedResource(org.astrogrid.registry.generated.package.RelatedResource) 

    /**
     * Sets the value of field 'shortName'. The field 'shortName'
     * has the following description: a short name or abbreviation
     * for this resource that applications 
     *  may use to refer to this resource in a compact display. 
     *  
     * 
     * @param shortName the value of field 'shortName'.
     */
    public void setShortName(java.lang.String shortName)
    {
        this._shortName = shortName;
    } //-- void setShortName(java.lang.String) 

    /**
     * Sets the value of field 'status'. The field 'status' has the
     * following description: a tag indicating whether this
     * resource is believed to be still
     *  actively maintained.
     *  
     * 
     * @param status the value of field 'status'.
     */
    public void setStatus(org.astrogrid.registry.generated.package.types.ResourceTypeStatusType status)
    {
        this._status = status;
    } //-- void setStatus(org.astrogrid.registry.generated.package.types.ResourceTypeStatusType) 

    /**
     * Method setSubject
     * 
     * @param index
     * @param vSubject
     */
    public void setSubject(int index, java.lang.String vSubject)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _subjectList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _subjectList.setElementAt(vSubject, index);
    } //-- void setSubject(int, java.lang.String) 

    /**
     * Method setSubject
     * 
     * @param subjectArray
     */
    public void setSubject(java.lang.String[] subjectArray)
    {
        //-- copy array
        _subjectList.removeAllElements();
        for (int i = 0; i < subjectArray.length; i++) {
            _subjectList.addElement(subjectArray[i]);
        }
    } //-- void setSubject(java.lang.String) 

    /**
     * Sets the value of field 'summary'. The field 'summary' has
     * the following description: An informational, human-readable
     * overview of a resource.
     *  
     * 
     * @param summary the value of field 'summary'.
     */
    public void setSummary(org.astrogrid.registry.generated.package.Summary summary)
    {
        this._summary = summary;
    } //-- void setSummary(org.astrogrid.registry.generated.package.Summary) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: the full name of a resource
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Method setType
     * 
     * @param index
     * @param vType
     */
    public void setType(int index, org.astrogrid.registry.generated.package.types.CategoryType vType)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _typeList.setElementAt(vType, index);
    } //-- void setType(int, org.astrogrid.registry.generated.package.types.CategoryType) 

    /**
     * Method setType
     * 
     * @param typeArray
     */
    public void setType(org.astrogrid.registry.generated.package.types.CategoryType[] typeArray)
    {
        //-- copy array
        _typeList.removeAllElements();
        for (int i = 0; i < typeArray.length; i++) {
            _typeList.addElement(typeArray[i]);
        }
    } //-- void setType(org.astrogrid.registry.generated.package.types.CategoryType) 

    /**
     * Sets the value of field 'updated'. The field 'updated' has
     * the following description: The date this resource metadata
     * description was last updated.
     *  
     * 
     * @param updated the value of field 'updated'.
     */
    public void setUpdated(org.exolab.castor.types.Date updated)
    {
        this._updated = updated;
    } //-- void setUpdated(org.exolab.castor.types.Date) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.ResourceType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.ResourceType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
