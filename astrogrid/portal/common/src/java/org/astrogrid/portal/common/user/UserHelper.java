package org.astrogrid.portal.common.user;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import org.astrogrid.community.User;
import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;

/*
 * 
 * @author peter.shillan
 *
 * @deprecated From iteration 5 <b>should be</b> using @link org.astrogrid.community.beans.v1.Credentials
 */
public class UserHelper {
  private static final String PARAM_ACCOUNT = SessionKeys.USER; //"username";
  private static final String PARAM_GROUP = SessionKeys.COMMUNITY_ACCOUNT; //"group";
  private static final String PARAM_TOKEN = "token"; //@TODO unused
  
	public static User getCurrentUser(Parameters params, Request request, Session session) {
		ActionUtils utils = ActionUtilsFactory.getActionUtils();
		
		String fullUserid = (String)utils.getAnyParameter(UserHelper.PARAM_ACCOUNT, "", params, request, session);

		String account = fullUserid.substring( fullUserid.lastIndexOf('/')+1 );
    String group = fullUserid.substring( fullUserid.indexOf('/')+2, fullUserid.lastIndexOf('/') );
		String token = utils.getAnyParameter(UserHelper.PARAM_TOKEN, params, request, session);
		
		return new User(account, group, "not-used", token);
	}
}
