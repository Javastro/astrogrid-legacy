package org.astrogrid.portal.common.user;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import org.astrogrid.community.User;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;

/*
 * 
 * @author peter.shillan
 *
 * @deprecated From iteration 5 <b>should be</b> using @link org.astrogrid.community.beans.v1.Credentials
 */
public class UserHelper {
  private static final String PARAM_ACCOUNT = "username";
  private static final String PARAM_GROUP = "group";
  private static final String PARAM_TOKEN = "token";
  
	public static User getCurrentUser(Parameters params, Request request, Session session) {
		ActionUtils utils = ActionUtilsFactory.getActionUtils();
		
		String account = utils.getAnyParameter(UserHelper.PARAM_ACCOUNT, params, request, session);
		String group = utils.getAnyParameter(UserHelper.PARAM_GROUP, params, request, session);
		String token = utils.getAnyParameter(UserHelper.PARAM_TOKEN, params, request, session);
		
		return new User(account, group, token);
	}
}
