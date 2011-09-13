/*
 * $Id: DateTimeConverter.java,v 1.2 2011/09/13 13:43:32 pah Exp $
 * 
 * Created on 8 Jun 2011 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2011 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jpa.eclipselink;

import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.persistence.exceptions.ConversionException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.DateTime;

public class DateTimeConverter implements Converter {

    protected DatabaseMapping mapping;
    
    /** Field type */
    protected Class dataClass = Timestamp.class;
    protected String dataClassName = dataClass.getName();

    /** Object type */
    protected final Class objectClass = Date.class;
    protected String objectClassName = objectClass.getName();


    @Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session){
        
            try {
                DateTime sourceObject = (DateTime)(objectValue);
                return ((AbstractSession)session).getDatasourcePlatform().convertObject(sourceObject.toDate(), getDataClass());
            } catch (ConversionException e) {
                throw ConversionException.couldNotBeConverted(mapping, mapping.getDescriptor(), e);
            }
    }

    @Override
    public Object convertDataValueToObjectValue(Object dataValue,
            Session session) {
        Object attributeValue = dataValue;
        if (attributeValue != null) {
            try {
                attributeValue = ((AbstractSession)session).getDatasourcePlatform().convertObject(attributeValue, getDataClass());
            } catch (ConversionException e) {
                throw ConversionException.couldNotBeConverted(mapping, mapping.getDescriptor(), e);
            }

            try {
                attributeValue = ((AbstractSession)session).getDatasourcePlatform().convertObject(attributeValue, getObjectClass());
            } catch (ConversionException e) {
                throw ConversionException.couldNotBeConverted(mapping, mapping.getDescriptor(), e);
            }
        }

        return new DateTime(attributeValue);
    }

    @Override
    public boolean isMutable() {
       return false;
    }

    @Override
    public void initialize(DatabaseMapping mapping, Session session) {
        //do nothing special except store the mapping.
        this.mapping = mapping;
        if (mapping.isDirectToFieldMapping()) {
            AbstractDirectMapping directMapping = (AbstractDirectMapping)mapping;

            // Allow user to specify field type to override computed value. (i.e. blob, nchar)
            if (directMapping.getFieldClassification() == null) {
                directMapping.setFieldClassification(getDataClass());
            }
            
        }
    }
    
    /**
     * PUBLIC:
     * Returns the class type of the data value.
     */
    public Class getDataClass() {
        return dataClass;
    }

    /**
     * INTERNAL:
     * Return the name of the data type for the MW usage.
     */
    public String getDataClassName() {
        if ((dataClassName == null) && (dataClass != null)) {
            dataClassName = dataClass.getName();
        }
        return dataClassName;
    }

    /**
     * PUBLIC:
     * Returns the class type of the object value.
     */
    public Class getObjectClass() {
        return objectClass;
    }

    /**
     * INTERNAL:
     * Return the name of the object type for the MW usage.
     */
    public String getObjectClassName() {
        if ((objectClassName == null) && (objectClass != null)) {
            objectClassName = objectClass.getName();
        }
        return objectClassName;
    }

 
}


/*
 * $Log: DateTimeConverter.java,v $
 * Revision 1.2  2011/09/13 13:43:32  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.1  2011/06/09 22:18:56  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 */
