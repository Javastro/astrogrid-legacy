/*$Id: AladinImageTest.java,v 1.3 2004/10/16 09:48:24 mch Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.deployment.vizier.aladinimage;

import junit.framework.TestCase;
import org.astrogrid.datacenter.impl.cds.generated.aladinimage.AladinImage;
import org.astrogrid.datacenter.impl.cds.generated.aladinimage.AladinService;
import org.astrogrid.datacenter.impl.cds.generated.aladinimage.AladinServiceLocator;
import javax.xml.rpc.ServiceException;

/** Not currently running - CDS have just documented this web service, and want people to send an email before using it - probably
 * because it loads the servers more. hence, it seems unfair to thrash it in a unit test. will leave for now.
 * @todo check the wsdl has not changed, write a delegate to wrap it nicely.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class AladinImageTest extends TestCase {

    /**
     * Constructor for AladinImageTest.
     * @param arg0
     */
    public AladinImageTest(String arg0) {
        super(arg0);
    }

    public void testItsThere() throws ServiceException {
      AladinService service = new AladinServiceLocator();
       AladinImage imageClient = service.getAladinImage();
       
       //see comment above - don't test yet
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AladinImageTest.class);
    }

}


/*
$Log: AladinImageTest.java,v $
Revision 1.3  2004/10/16 09:48:24  mch
Added test skeleton

Revision 1.2  2004/10/05 17:08:22  mch
Fixed package names and imports

Revision 1.1  2004/10/05 17:06:53  mch
Moved service implementation tests from unit tests to integration tests

Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/
