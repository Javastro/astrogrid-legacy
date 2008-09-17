/*
 * $Id: DateTimeXmlAdapter.java,v 1.1 2008/09/17 13:36:20 pah Exp $
 * 
 * Created on 14 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa;
import java.util.Date;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;

/**
 * Simple adapter to use the Joda DateTime for xs:dates in JAXB.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@XmlTransient
public class DateTimeXmlAdapter extends XmlAdapter<Date	, DateTime> {

    @Override
    public DateTime unmarshal(Date date) throws Exception {
        return new DateTime(date.getTime());
    }

    @Override
    public Date marshal(DateTime dateTime) throws Exception {
	if(dateTime != null){
        return new Date(dateTime.getMillis());
	}
	else {
	    return null;
	}
    }

}


/*
 * $Log: DateTimeXmlAdapter.java,v $
 * Revision 1.1  2008/09/17 13:36:20  pah
 * first version of JAXB objects generated from schema
 *
 * Revision 1.2  2008/08/02 13:36:58  pharriso
 * safety checkin - on vacation
 *
 * Revision 1.1  2008/05/17 21:20:46  pharriso
 * safety checkin before interop
 *
 */
