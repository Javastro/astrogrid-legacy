/* Altara Utility Classes
   Copyright (c) 2001,2002 Brian H. Trammell

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.
	
	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, it is available at
	http://www.gnu.org/copyleft/lesser.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
	Boston, MA  02111-1307  USA
*/

package org.altara.util;

import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class IconService {

	String iconPath;
	Class classBase;
	Map iconCache;
	static IconService current;

	private IconService (Class classBase, String iconPath) {
		this.classBase = classBase;
		this.iconPath = iconPath;
		this.iconCache = new HashMap();
		current = this;
	}

	public static void initialize(Class classBase, String iconPath) {
		new IconService(classBase, iconPath);
	}

	public static ImageIcon getIcon(String iconName) {
		try {
			return current.getIconImpl(iconName);
		} catch (NullPointerException ex) {
			throw new IconServiceNotInitializedException();
		}
	}

	private synchronized ImageIcon getIconImpl(String iconName) {
		// Try the cache first
		ImageIcon icon;
		icon = (ImageIcon)iconCache.get(iconName);
		if (icon == null) {
			// cache miss - load icon
			URL iconUrl =
				classBase.getResource(iconPath+"/"+iconName);
			if (iconUrl == null) {
				throw new IconNotFoundException(iconName);
			}
			icon = new ImageIcon(iconUrl);
			iconCache.put(iconName,icon);
		}
		return icon;
	}

	public static class IconNotFoundException
			extends IconServiceException {
		IconNotFoundException(String msg) {
			super(msg);
		}
	}

	public static class IconServiceNotInitializedException
			extends IconServiceException {
		IconServiceNotInitializedException() {
			super("Attempt to call getIcon() before initialize()");
		}
	}

	public static class IconServiceException extends RuntimeException {
		IconServiceException(String msg) {
			super(msg);
		}
	}
}
