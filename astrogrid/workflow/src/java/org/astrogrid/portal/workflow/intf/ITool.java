/*$Id: ITool.java,v 1.2 2004/02/25 10:57:43 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;
import java.util.Iterator;
import java.util.List;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface ITool extends XMLSerializable{
    public abstract String getName();
    public abstract String getDocumentation();
    public abstract Iterator getInputParameters();
    public abstract Iterator getOutputParameters();
    /**
       */
    public abstract void setDocumentation(String string);
    /**
       */
    public abstract void setInputParameters(List list);
    /**
       */
    public abstract void setName(String string);
    /**
       */
    public abstract void setOutputParameters(List list);
}
/* 
$Log: ITool.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/