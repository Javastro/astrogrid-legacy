/*$Id: RuleNotFoundException.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
 * Created on 04-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

/** Exception thrown to signify that the index script has computed a rule id that does not exist.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Nov-2004
 *
 */
public class RuleNotFoundException extends ScriptEngineException {

    /** Construct a new RuleNotFoundException
     * @param s
     */
    public RuleNotFoundException(String s) {
        super(s);
    }

    /** Construct a new RuleNotFoundException
     * @param s
     * @param e
     */
    public RuleNotFoundException(String s, Exception e) {
        super(s, e);
    }

}


/* 
$Log: RuleNotFoundException.java,v $
Revision 1.2  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.1.2.1  2004/11/05 16:11:26  nw
subclassed map to create rulestore.
added methods to compute index of triggers,
optimization: compiled index is cached in soft reference
location of next rule uses index
 
*/