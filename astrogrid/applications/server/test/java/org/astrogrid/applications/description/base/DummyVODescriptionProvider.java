/*$Id: DummyVODescriptionProvider.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 02-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.base;

import org.astrogrid.applications.component.ProvidesVODescription;
import org.astrogrid.registry.beans.resource.ContactType;
import org.astrogrid.registry.beans.resource.CurationType;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.beans.resource.ResourceReferenceType;
import org.astrogrid.registry.beans.resource.ResourceType;
import org.astrogrid.registry.beans.resource.SummaryType;
import org.astrogrid.registry.beans.resource.VODescription;


public class DummyVODescriptionProvider implements ProvidesVODescription {
    private final String AUTH_ID;
    private final String RES_KEY;
    public DummyVODescriptionProvider(String AUTH_ID, String RES_KEY) {
        super();
        this.AUTH_ID = AUTH_ID;
        this.RES_KEY = RES_KEY;
    }
    public VODescription getVODescription() throws Exception {
        // right, this is the minimal valid vodescription. hope the schema doesn't change again soon!
        VODescription desc = new VODescription();
        ResourceType resource = new ResourceType();
        desc.addResource(resource);
        IdentifierType id = new IdentifierType();
        resource.setIdentifier(id);
        id.setAuthorityID(AUTH_ID);
        id.setResourceKey(RES_KEY);
        resource.setTitle("test resource");
        SummaryType summary = new SummaryType();
        resource.setSummary(summary);
        summary.setDescription("");
        summary.setReferenceURL("");
        CurationType curation = new CurationType();
        resource.setCuration(curation);
        ContactType contact = new ContactType();
        contact.setEmail("");
        contact.setIdentifier(id);
        contact.setName("");
        curation.setContact(contact);
        ResourceReferenceType pubRef = new ResourceReferenceType();
        pubRef.setDescription("");
        pubRef.setIdentifier(id);
        pubRef.setReferenceURL("");
        pubRef.setTitle("");
        curation.setPublisher(pubRef);
        return desc;
    }
}

/* 
$Log: DummyVODescriptionProvider.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/