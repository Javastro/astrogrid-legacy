/*
 * $Id: DateTimeXmlAdapter.java,v 1.2 2011/09/13 13:43:32 pah Exp $
 * 
 * Created on 14 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jaxb;
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
public class DateTimeXmlAdapter extends XmlAdapter<String, DateTime> {

    @Override
    public DateTime unmarshal(String date) throws Exception {
        if(date != null){
        return new DateTime(date);
        }
        else {
            return null;
        }
    }

    @Override
    public String marshal(DateTime dateTime) throws Exception {
	if(dateTime != null){
        return dateTime.toString();
	}
	else {
	    return null;
	}
    }

}


/*
 * $Log: DateTimeXmlAdapter.java,v $
 * Revision 1.2  2011/09/13 13:43:32  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.2  2011/06/15 13:55:51  pah
 * writing to DB almost works - seems to be a bug in eclipselink @OneToMany inside @Embeddable does not work....
 *
 * Revision 1.1.2.1  2011/06/01 15:23:41  pah
 * first pass at adding JPA for rdb as well as xml representation.
 *
 * Revision 1.2.2.1  2011/03/15 13:00:55  pah
 * jaxb now seems to supply xsd:date as a string....
 *
 * Revision 1.2  2008/09/22 22:16:56  pah
 * make deal with nulls
 *
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
