/*$Id: MostSuitableBuilder.java,v 1.7 2006/08/02 13:29:19 nw Exp $
 * Created on 07-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.builders;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.w3c.dom.Element;

/** aggregating builder which chooses the most suitable builder from a set.
  @todo rework this in hivemind pattern?? - or maybe replace altogether.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
 * * @deprecated part of the obsolete registry infrastructure
 */
public class MostSuitableBuilder implements InformationBuilder {

    /** tester class */
    private static class ApplicablePredicate implements Predicate {
        private CachedXPathAPI xpath;
        private Element el;

        public void setUp(CachedXPathAPI xpath, Element el) {          
            this.xpath = xpath;
            this.el = el;
        }

        public boolean evaluate(Object arg0) {
            InformationBuilder b = (InformationBuilder)arg0;
            return b.isApplicable(xpath,el);
        }
    }

    /** Construct a new MostSuitableBuilder
     * 
     */
    public MostSuitableBuilder() {
        super();
        builders.add(new TabularDatabaseInformationBuilder());
        builders.add(new ConeInformationBuilder());
        builders.add(new VizierConeInformationBuilder());
        builders.add(new SiapInformationBuilder());
        builders.add(new ApplicationInformationBuilder());
        builders.add(new SkyNodeInformationBuilder());
        builders.add(new ResourceInformationBuilder()); // fallback builder.
    }
    private final ApplicablePredicate test = new ApplicablePredicate();
    private final Collection builders = new ArrayList();
    
    public synchronized boolean isApplicable(final CachedXPathAPI xpath, final Element el) {
        test.setUp(xpath,el);
        return CollectionUtils.exists(builders,test);
    }

    public synchronized ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
       test.setUp(xpath,element);
       InformationBuilder b = (InformationBuilder)CollectionUtils.find(builders,test);
       return b.build(xpath,element);
       //@todo later, if first attempt at building fails, need to fall back to another builder.
    }

}


/* 
$Log: MostSuitableBuilder.java,v $
Revision 1.7  2006/08/02 13:29:19  nw
marked all as obsolete.

Revision 1.6  2006/06/27 10:25:10  nw
findbugs tweaks

Revision 1.5  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/03/13 18:28:08  nw
special case of a cone builder from vizier reg entries.

Revision 1.3  2006/02/24 15:22:29  nw
integration necessary for skynode

Revision 1.2  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/