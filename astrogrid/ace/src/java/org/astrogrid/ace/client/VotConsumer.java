
/*
 $Id: VotConsumer.java,v 1.1 2003/08/25 18:36:13 mch Exp $

 */


package org.astrogrid.ace.client;

import org.w3c.dom.Element;

/**
 * Applications that want to use ACE, such as Aladin, will tend to want ACE
 * to display the dialog box modally, and then go off and get the results
 * asynchronously.
 * <p>
 * This class provides that callback mechanism.  The calling application implements
 * it (or provides an implementation of it), calls ACE with a reference to it, and
 * ACE then calls the <code>consumeAceResults()</code> method with the votable as
 * a DOM element
 * <P>
 * NB - the method here is likely to be called by a separate extraction
 * thread, so watch out for synchronisation issues.
 *
 * @author M Hill
 */

public interface VotConsumer
{
   public void consumeAceResults(Element votResults);
}

/*
$Log: VotConsumer.java,v $
Revision 1.1  2003/08/25 18:36:13  mch
*** empty log message ***

Revision 1.3  2003/07/11 10:44:11  mch
Better javadoc comment

Revision 1.2  2003/06/26 19:14:59  mch
Passband stuff added

Revision 1.1  2003/06/18 16:01:41  mch
Removing circular dependency on Aladin, tidying up threading

*/

