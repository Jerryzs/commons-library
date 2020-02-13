/*
 * Copyright 2018 Siyuan "Jerry" Zhang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.jerry.commons.util;

public class OS {
	/**
	 * Returns the name of the operating system of the user in a String. The String could be "windows", "macos", and "unix", returns null if the user's 
	 * operating system isn't any of the three mentioned. 
	 * 
	 * @return the String of the name of a operating system, or null 
	 */
	public static String osName() {
		final String os = java.lang.System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) 
			return "windows"; 
		if (os.contains("mac")) 
			return "macos"; 
		if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
			return "unix";
		return null; 
	}
	
	/**
	 * Returns the operating system of the user as SystemType. The result could be SystemType.Windows, SystemType.Mac, SystemType.Unix or SystemType.Unknown
	 * if the user's operating system isn't any of the three mentioned. 
	 * 
	 * @return a SystemType that matches the name of the operating system of the user 
	 */
	public static SystemType os() {
		final String os = osName();
		switch (os) {
		case "windows":
			return SystemType.WINDOWS;
		case "macos": 
			return SystemType.MACOS;
		case "unix":
			return SystemType.UNIX;
		default: 
			return SystemType.UNKNOWN;
		}
	}
	
	public enum SystemType {
		WINDOWS, MACOS, UNIX, UNKNOWN;
	}
}
