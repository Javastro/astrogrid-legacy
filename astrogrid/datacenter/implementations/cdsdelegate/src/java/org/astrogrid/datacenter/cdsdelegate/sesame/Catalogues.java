/*$Id: Catalogues.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.sesame;

/** Constants class that represents catalogues to search in
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class Catalogues {

    /**
     * 
     */
    private Catalogues(String code) {
        this.code = code;
    }
    private String code;
    
    protected String getCode() {
        return code;
    }
    /** search in the simbad catalogue */
    public final static Catalogues SIMBAD = new Catalogues("S");
    /** search in the ned catalogue */
    public final static Catalogues NED = new Catalogues("N");
    /** search in the vizier catalogue */
    public final static Catalogues VIZIER = new Catalogues("V");
    /** search in all catalogues */
    public final static Catalogues ALL = new Catalogues("A");
    
    /** search in two catalogues
     * 
     * @param a one catalogue to search in 
     * @param b another catalogue to search in.
     * @return code to search in both catalogues
     */
    public static Catalogues  BOTH(Catalogues a,Catalogues b) {
        return new Catalogues(a.getCode() + b.getCode() + ALL.getCode());
    }
    
    /** search in a primary catalogue. If no result found, try in a fallback catalogue
     * 
     * @param primary first catalogue to search
     * @param secondary seconf catalogue to search
     * @return
     */
    public static Catalogues FALLBACK(Catalogues primary,Catalogues secondary) {
        return new Catalogues(primary.getCode() + secondary.getCode());
    }

}


/* 
$Log: Catalogues.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/