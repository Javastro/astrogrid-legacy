/*$Id: InterestingBean.java,v 1.1 2003/11/14 00:38:30 mch Exp $
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Sep-2003
 *
 */
public class InterestingBean {

    private Date aDate;
    private int[] someInts;
    protected CommunityBean communityBean ;
    private List list;

    public void setADate(Date aDate) {
        this.aDate = aDate;
    }

    public Date getADate() {
        return aDate;
    }

    public void setSomeInts(int[] someInts) {
        this.someInts = someInts;
    }

    public int[] getSomeInts() {
        return someInts;
    }
    /**
     * @return
     */
    public CommunityBean getCommunityBean() {
        return communityBean;
    }

    /**
     * @param bean
     */
    public void setCommunityBean(CommunityBean bean) {
        communityBean = bean;
    }

    public void setList(List foobleList) {
        this.list = foobleList;
    }


    public List getList() {
        return list;
    }
    
    public Object getList(int i) {
        return list.get(i);
    }
    
    public void setList(int i, Object o) {
        if (list == null) {
            list = new ArrayList();
        }
        if (list.size() == i) {
            list.add(o);
        }
        list.set(i,o);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        InterestingBean other = (InterestingBean)obj;
        return (this.aDate != null ? this.aDate.equals(other.aDate) : other.aDate == null) 
            && (this.communityBean != null ? this.communityBean.equals(other.communityBean) : other.communityBean == null)
            && Arrays.equals(this.someInts,other.someInts)
            && (this.list != null ? this.list.equals(other.list) : other.list == null);
    }

}


/* 
$Log: InterestingBean.java,v $
Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.2  2003/09/17 14:53:02  nw
tidied imports

Revision 1.1  2003/09/11 09:30:10  nw
demonstrator for using castor to map javabeans to xml
 
*/