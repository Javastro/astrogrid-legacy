package org.astrogrid.portal.registry;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.astrogrid.portal.generated.registry.client.RegistryInterface;

public class RegistryServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			     HttpServletResponse response)
		throws IOException, ServletException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			     HttpServletResponse response)
		throws IOException, ServletException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
				    HttpServletResponse response)
		throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		session.setAttribute("foo", "bar");
		response.sendRedirect("/Registry/servlets/Registry.jsp");
	}
}
