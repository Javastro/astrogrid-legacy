/*$Id: DefaultHtmlTransformer.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service.conversion;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;

/** Default implementaiton of transformer to html -  works ok-ish - not good for nested object trees.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class DefaultHtmlTransformer implements Transformer {

    /** Construct a new DefaultHtmlTransformer
     *
     */
    private DefaultHtmlTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body>")
            .append(ReflectionToStringBuilder.toString(arg0,htmlStyle))
            .append("</body></html>");
        return sb;
    }
    
    protected final static StandardToStringStyle htmlStyle = new StandardToStringStyle();
    static {
        htmlStyle.setArraySeparator("</tr><tr>");
        htmlStyle.setArrayEnd("</tr></table>");
        htmlStyle.setArrayStart("<table><tr>");
        htmlStyle.setContentEnd("</td></tr></table>");
        htmlStyle.setContentStart("<table><tr><td>");
        htmlStyle.setFieldSeparator("</td><td>");
        htmlStyle.setUseClassName(false);
        htmlStyle.setUseFieldNames(false);
        htmlStyle.setUseIdentityHashCode(false);
        htmlStyle.setArrayContentDetail(true);
        htmlStyle.setDefaultFullDetail(true);       
    }    

    public static final Transformer getInstance() {
        return theInstance;
    }
    
    private static final Transformer theInstance = new DefaultHtmlTransformer();
    
}


/* 
$Log: DefaultHtmlTransformer.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/