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

import org.apache.oro.text.regex.*;
import org.altara.mars.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class SendExpectClient implements Serializable {
	// for numbering child threads
	private static int nctid = 0;

	private static final char ENDSTREAM = (char)0xFFFF;

	private transient InetAddress host;
	private transient int port;
	private transient long timeout;
	private LinkedList script;

	private transient Socket sock;		
	private transient Writer writer;	
	private transient Reader reader;
	private transient StringBuffer buffer;	
	private transient int bufptr;

	private transient Perl5Matcher matcher;

	private transient boolean didTimeout;
	private transient boolean didClose;
	private transient boolean didFail;

    private transient ClientDebugger debug;

	public SendExpectClient() {
		this(null,0,0);
	}

	public SendExpectClient(InetAddress host, int port, long timeout) {
		// store all instance variables for script run
		setServer(host,port,timeout);
		// prepare an empty script
		this.script = new LinkedList();
	}

	public void setServer(InetAddress host, int port, long timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	public void clear() {
		script.clear();
	}

	public void send(Object out) {
		script.add(new SendStep(out));
	}

	public void expect(Object in) {
        try {
            if (script.getLast() instanceof SendStep)
                script.add(new FlushStep());
        } catch (NoSuchElementException nse) {}
		script.add(new ExpectStep(in));
	}

	public void expect(Object pass, Object fail) {
        try {
            if (script.getLast() instanceof SendStep)
                script.add(new FlushStep());
        } catch (NoSuchElementException nse) {}
		script.add(new ExpectStep(pass,fail));
	}

	public synchronized Status runScript() {
		// do nothing if no server
		if (host == null) return new Status(Status.PROBEFAIL);
		try {
            // open a debugger session
            debug = Debug.getCurrent().newDebugger(
                "SEC "+host.getHostName()+":"+port);

			// open a socket
			try {
				sock = new Socket(host, port);
                debug.message("connected");
			} catch (IOException ex) {
				return new Status(Status.DOWN);
			}
	
			// set up I/O on the socket
			try {
				writer = new OutputStreamWriter(sock.getOutputStream());
				reader = new InputStreamReader(sock.getInputStream());
				buffer = new StringBuffer();
				bufptr = 0;
			} catch (IOException ex) {
				return new Status(Status.FASTCLOSE);
			}
	
			// start the helper threads
			didTimeout = false;
			didClose = false;
			didFail = false;
			new ReaderThread().start();
			new ReaperThread().start();
			// get the time for response time logging
			long startTime = System.currentTimeMillis();
			// run through the script
			ListIterator scriptIter = script.listIterator();
			while (scriptIter.hasNext()) {
				// run the step
				try {
                    ((Step)scriptIter.next()).run();
                } catch (Exception ex) {
                    Status out = new Status(Status.PROBEFAIL);
                    out.setProperty("exception",ex.getClass().getName());
                    out.setProperty("exceptionMessage",ex.getMessage());
                    return out;
                }
                // check for timeout
				if (didTimeout) {
					Status out = new Status(Status.TIMEOUT);
					out.setProperty("received",bufferString());
                    debug.message("timed out");
					return out;
				}
				// check for premature closing
				if (didClose) {
					Status out = new Status(Status.FASTCLOSE);
					out.setProperty("received",bufferString());
                    debug.message("closed by remote");
					return out;
				}
				// check for overrun or expect failure
				if (didFail) {
					Status out = new Status(Status.UNEXPECTED);
					out.setProperty("received",bufferString());
                    debug.message("got bad reply");
					return out;
				}
			}
			// we made it to the end of the script, so we must be up
			Status out =
				new Status(Status.UP,System.currentTimeMillis() - startTime);
			out.setProperty("received",bufferString());
            debug.message("ok");
			return out;
		} finally {
			// close the socket no matter what
			try {
                debug.close();
				sock.close();
			} catch (Exception ignored) {}
		}
	}

    private String bufferString() {
        synchronized (buffer) {
            if (buffer.length() > 0 &&
                buffer.charAt(buffer.length() - 1) == ENDSTREAM)
                buffer.deleteCharAt(buffer.length()-1);
            return buffer.toString();
        }
    }

	public String dumpScript() {
		StringBuffer out = new StringBuffer();
		String hostName = "[prototype]";
		if (host != null) hostName = host.getHostName();
		out.append("SEC "+hostName+":"+port+" {\n");
		ListIterator scriptIter = script.listIterator();
		while (scriptIter.hasNext()) {
			// print the step
			Step nextStep = (Step)scriptIter.next();
			if (nextStep instanceof SendStep) {
				Object toSend = ((SendStep)nextStep).toSend;
				out.append("\t> "+getShortClassName(toSend.getClass())+
					" "+toSend.toString()+"\n");
			} else if (nextStep instanceof ExpectStep) {
				Object pass = ((ExpectStep)nextStep).pass;
				Object fail = ((ExpectStep)nextStep).fail;
				if (pass instanceof Perl5Pattern) {
					out.append("\t< "+getShortClassName(pass.getClass())+
						" "+((Perl5Pattern)pass).getPattern()+"\n");
				} else {
					out.append("\t< "+getShortClassName(pass.getClass())+
						" "+pass.toString()+"\n");
				}
				if (fail != null) {
					if (fail instanceof Perl5Pattern) {
						out.append("\tX "+getShortClassName(pass.getClass())+
							" "+((Perl5Pattern)fail).getPattern()+"\n");
					} else {
						out.append("\tX "+getShortClassName(fail.getClass())+
							" "+fail.toString()+"\n");
					}
				}
			} else {
				out.append("\tBAD STEP "+nextStep.getClass()+"\n");
			}
		}
		out.append("}\n");
		return out.toString();
	}

	private static String getShortClassName(Class clazz) {
		String longname = clazz.getName();
		return longname.substring(longname.lastIndexOf(".")+1);
	}

	private class ReaderThread extends Thread {
		private ReaderThread() {
			super ("SECReader "+(nctid++));
		}

		public void run() {
			try {
				while(true) {
					int c = reader.read();
					synchronized (buffer) {
						if (c == -1) {
							buffer.append(ENDSTREAM);
                            debug.message("end of stream");
							buffer.notify();
							break;
                    // } else if (buffer.length() - bufptr > MAXRECV_LEN) {
                    //		didFail = true;
						} else {
							buffer.append((char)c);
                            debug.receive(String.valueOf((char)c));
						}
						buffer.notify();
					}
				}
			} catch (IOException ex) {
				didClose = true;
				synchronized (buffer) {
					buffer.notify();
				}
			}
		}
	}

	private class ReaperThread extends Thread {
		private ReaperThread() {
			super ("SECReaper "+(nctid++));
		}

		public void run() {	
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException ignored) {}
			didTimeout = true;
			synchronized(buffer) {
				buffer.notify();
			}
		}
	}

	public class SendLocalHostname implements Serializable {
		public String toString() {
			return sock.getLocalAddress().getHostName();
		}
	}

	public static class SendRemoteHostname implements Serializable {
		private SendExpectProbe sep;

		public SendRemoteHostname(SendExpectProbe sep) {
			this.sep = sep;
		}

		public String toString() {
			if (sep.getService() == null) return "[no svc]";
			return sep.getService().getHost()
				.getAddress().getHostName();
		}
	}

	public static class SendParameter implements Serializable {
		private SendExpectProbe sep;
		private String name;

		public SendParameter(SendExpectProbe sep, String name) {
			this.sep = sep;
			this.name = name;
		}

		public String toString() {
			Service svc = sep.getService();
			if (svc == null) return "[no svc]";
			String param = svc.getParameter(name);
			if (param == null || param.length() == 0)
				param = svc.getProbeFactory().getServiceParamDefault(svc,name);
			return param;
		}
	}

    public static class ParameterPattern implements Serializable {
        private SendExpectProbe sep;
        private String name;

        public ParameterPattern(SendExpectProbe sep, String name) {
            this.sep = sep;
            this.name = name;
        }

        public Perl5Pattern toPattern() throws MalformedPatternException {
            Service svc = sep.getService();
            if (svc == null) return null;
            return svc.getParameterAsRegex(name);
        }
    }

	private abstract class Step implements Serializable {
		public abstract void run() throws Exception;
	}

	private class SendStep extends Step {
		private Object toSend;

		public SendStep(Object toSend) {
			this.toSend = toSend;
		}

		public void run() {
            // convert to string
            String out = toSend.toString();
            // do nothing for null sends
            if (out.length() == 0) return;
            // set the buffer pointer to the current buffer end
            bufptr = buffer.length();
            // send the string
            try {
                debug.send(out);
                writer.write(out);
            } catch (IOException ex) {
                didClose = true;
            }
		}
	}

    private class FlushStep extends Step {
        public FlushStep() {}

        public void run() {
            try {
                writer.flush();
            } catch (IOException ex) {
                didClose = true;
            }
        }
    }

	private class ExpectStep extends Step {
		private Object pass, fail;

		public ExpectStep(Object pass) {
			this(pass,null);
		}

		public ExpectStep(Object pass, Object fail) {
			this.pass = pass;
			this.fail = fail;
		}

		public void run() throws MalformedPatternException {
			while (true) {
				synchronized (buffer) {
					String bufarea = buffer.substring(bufptr);
					// Check for pass match
					if (checkMatch(bufarea, pass)) {
						return;
					}
					// Check for fail match
					if (fail != null && checkMatch(bufarea, fail)) {
						didFail = true;
						return;
					}
					// check for EOS
					if (bufarea.indexOf(ENDSTREAM) >= 0) {
						// end of stream found before expected string
						// set didClose flag.
						didClose = true;
						return;
					}
					if (didClose || didTimeout || didFail) return;
					try {
						buffer.wait();
					} catch (InterruptedException ex) {
						return;
					}
				}
			}
		}

		private boolean checkMatch (String bufarea, Object match) 
            throws MalformedPatternException {
			// Check for pattern match
			if (match instanceof Perl5Pattern) {
				if (matcher == null)
					matcher = new Perl5Matcher();
				if (matcher.contains(bufarea,((Perl5Pattern)match)))
					return true;
            // Check for parameter pattern match
            } else if (match instanceof ParameterPattern) {
                if (matcher == null)
                    matcher = new Perl5Matcher();
                Perl5Pattern pattern = ((ParameterPattern)match).toPattern();
                if (pattern == null) return false;
                if (matcher.contains(bufarea,pattern)) return true;
			// Check for string match
			} else {
				if (bufarea.indexOf(match.toString()) >= 0) return true;
			}
			// No match found if we made it here.
			return false;
		}
	}
}
