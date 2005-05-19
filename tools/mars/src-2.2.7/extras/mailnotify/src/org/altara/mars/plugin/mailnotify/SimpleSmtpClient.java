/* MARS Mail Notification Plugin
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

package org.altara.mars.plugin.mailnotify;

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.net.*;
import java.io.*;
import org.apache.oro.text.regex.*;

public class SimpleSmtpClient extends SendExpectClient {

	private static final long SMTP_TIMEOUT = 120000;
	private static final int SMTP_PORT = 25;

	protected static Perl5Compiler recompiler = new Perl5Compiler();
    private Perl5Pattern failpat; 

	public SimpleSmtpClient(InetAddress server) {
		super(server,SMTP_PORT,SMTP_TIMEOUT);

        try {
            failpat = (Perl5Pattern)recompiler.compile("^[45]\\d{2}");
        } catch (MalformedPatternException ex) {
            throw new RuntimeException("error in smtp client: "+
                "regex compile failed");
        }
	}

	public Status sendMessage(String from, String to,
							String subject, String message) {
		// clear the script
		clear();
		// say hello
		expect("220",failpat);
		send("HELO ");
		send(new SendLocalHostname());
		send("\r\n");
		expect("250",failpat);
		// set from address
		send("MAIL From: <"+from+">\r\n");
		expect("250",failpat);
        // send recipient list
        StringTokenizer totok = new StringTokenizer(to,",");
        while (totok.hasMoreTokens()) {
            String nextto = totok.nextToken();
		    send("RCPT To: <"+nextto+">\r\n");
		    expect("250",failpat);
        }
		// send message
		send("DATA\r\n");
		expect("354",failpat);
		send("From: "+from+"\r\n");
		send("To: "+to+"\r\n");
		send("Subject: "+subject+"\r\n\r\n");
		send(message);
		send("\r\n.\r\n");
		expect("250",failpat);
		// clean up
		send("QUIT\r\n");
		expect("221",failpat);
		// now run the script
		return runScript();
	}
}
