/*$Id: CommunityBean.java,v 1.1 2003/11/14 00:38:30 mch Exp $
 * Created on 11-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.messagebeans;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Sep-2003
 *
 */
public class CommunityBean {

    private String userId;
    private int anotherProperty;
    private boolean flag ;
    /**
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param string
     */
    public void setUserId(String string) {
        userId = string;
    }

    /**
     * @return
     */
    public int getAnotherProperty() {
        return anotherProperty;
    }

    /**
     * @return
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param i
     */
    public void setAnotherProperty(int i) {
        anotherProperty = i;
    }

    /**
     * @param b
     */
    public void setFlag(boolean b) {
        flag = b;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        CommunityBean other = (CommunityBean)obj;
        return this.flag == other.flag && this.anotherProperty == other.anotherProperty 
        && (this.userId != null ? this.userId.equals(other.userId) : other.userId == null);
    }

}


/* 
$Log: CommunityBean.java,v $
Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.1  2003/09/11 09:30:10  nw
demonstrator for using castor to map javabeans to xml
 
*/