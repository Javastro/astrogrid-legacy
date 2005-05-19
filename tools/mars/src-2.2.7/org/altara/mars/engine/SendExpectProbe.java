/* MARS Network Monitoring Engine
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars.engine;

import java.io.*;
import java.net.*;
import java.util.*;
import org.altara.mars.*;

public class SendExpectProbe extends Probe {	

	private SendExpectClient client;

	public SendExpectProbe() {
		client = new SendExpectClient();
	}

	public SendExpectProbe(Service service) {
		super(service);
		client = new SendExpectClient(
			service.getHost().getAddress(),
			service.getPort(), service.getTimeout());
	}

	public void setService(Service service) {
		super.setService(service);
		client.setServer(
			service.getHost().getAddress(),
			service.getPort(), service.getTimeout());
	}

	public void send(Object out) {
		client.send(out);
	}

	public void expect(Object in) {
		client.expect(in);
	}

	public void expect(Object pass, Object fail) {
		client.expect(pass,fail);
	}

	public String dumpScript() {
		return client.dumpScript();
	}

	protected Status doProbe() {
		return client.runScript();
	}

	public SendExpectProbe instantiatePrototype(Service service) {
		ByteArrayOutputStream byteOut = null;
		ObjectOutputStream objOut = null;
		ObjectInputStream objIn = null;

		try {
			byteOut = new ByteArrayOutputStream();
			objOut = new ObjectOutputStream(byteOut);
			objOut.writeObject(this);
			objOut.flush();
			objIn = new ObjectInputStream(
				new ByteArrayInputStream(byteOut.toByteArray()));
			SendExpectProbe out = (SendExpectProbe)objIn.readObject();
			out.setService(service);
			return out;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("failure in instantiatePrototype");
		} finally {
			try {
				objOut.close();
				objIn.close();
			} catch (Exception ignored){}
		}
	}
}
