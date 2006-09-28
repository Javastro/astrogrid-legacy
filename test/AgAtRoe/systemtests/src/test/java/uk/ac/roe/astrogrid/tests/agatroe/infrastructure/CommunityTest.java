package uk.ac.roe.astrogrid.tests.agatroe.infrastructure;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;

import uk.ac.roe.astrogrid.tests.RuntimeRequiringTestCase;
import uk.ac.roe.astrogrid.tests.agatroe.TestUser;

/**
 * Checks that we can log in and out of community
 * @author jdt
 *
 */
public class CommunityTest extends RuntimeRequiringTestCase {
    private Community comm;
    public void setUp() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
        comm = (Community) getAcr().getService(Community.class);
    }
    public void tearDown() {
        comm.logout();
    }
    public void testLogin() throws SecurityException, ServiceException {
        comm.login(TestUser.USER, TestUser.PASS, TestUser.COMMUNITY);
        assertTrue(comm.isLoggedIn());
        UserInformation ui = comm.getUserInformation();
        assertEquals(TestUser.USER,ui.getName());
    }
    
    public void testUnknownUser() throws ServiceException {
        try {
            comm.login("fee","fii",TestUser.COMMUNITY);
            assertFalse(comm.isLoggedIn());
        } catch (SecurityException e) {
            // expected
            assertFalse(comm.isLoggedIn());
            return;
        } 
        fail("Community didn't reject unknown user");
    }
}
