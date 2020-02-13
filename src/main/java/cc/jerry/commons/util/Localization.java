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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class Localization {
	
	private static final String systemLanguage = Locale.getDefault().toLanguageTag();
//	private static Thread listFolderThread;
	private static boolean loadedFolders;
	private static String appLanguage;
	private static String[] langFiles;
	private static Properties localFile = new Properties();
//	private static Locale[] allLocales;
	private static String langExtension;
	
//	public static void setAllLocales(Locale[] allLocales) {
//		Localization.allLocales = allLocales;
//	}
	
	/**
	 * Instantiate the localization class with a specified localization folder which contains all the localization files and is located inside the source folder, 
	 * a specified file type, the default language, and the language of the application. The default language and 
	 * the application language are both Strings, but they have to be specified language tags like "en-US". 
	 * <p>
	 * Run this as soon as the application is started (the main() method in the main class) if you wish to use this as your localization library, so you can use "get(key)" 
	 * to retrieve the String in the desired language. 
	 * <p>
	 * The default language will be used when the specified application language is not available. 
	 * 
	 * @param langFolder a String with the location of the folder containing all the localization files (format: "/package/", example: "/cc/jerry/commons/local/localizations/")
	 * @param defaultLang a String of the language tag for the default language, this specified language is used when the language in the "lang" parameter is not supported (for example: "en-US") 
	 * @param lang 	      a String of the language qtag for the language of the application, this specified language is used to retrieve the localization file for this language (for example: "zh-CN")
	 */
	public Localization(String langFolder, String defaultLang, String lang, String fileSuffix) {
		if (!fileSuffix.contains(" ") && fileSuffix.startsWith(".")) {
			appLanguage = lang;
			langExtension = fileSuffix;

			if (!langFolder.endsWith(File.separator))
				langFolder = langFolder + File.separator;

			/*
			final String localFolder = langFolder;

			listFolderThread = new Thread(() -> {
				System.out.println("Thread Started Localization: " + Thread.currentThread().getName());


				List<String> files = new ArrayList<>();

				for (Locale locale : allLocales) {
					if (defaultLang.length() >= 5) {
						if (defaultLang.contains("-")) {
							try {
								new BufferedReader(new InputStreamReader(new FileInputStream(new File(localFolder + locale.getLanguage() + "-" + locale.getCountry() + fileSuffix)), StandardCharsets.UTF_8)).close();
								files.add(locale.getLanguage() + "-" + locale.getCountry() + fileSuffix);
							} catch (NullPointerException | FileNotFoundException e) {
								//no action needed
							} catch (IOException e) {
								System.err.println("[ERROR] " + e.toString());
							}
						}
						else if (defaultLang.contains("_")) {
							try {
								new BufferedReader(new InputStreamReader(new FileInputStream(new File(localFolder + locale.getLanguage() + "_" + locale.getCountry() + fileSuffix)), StandardCharsets.UTF_8)).close();
								files.add(locale.getLanguage() + "_" + locale.getCountry() + fileSuffix);
							} catch (NullPointerException | FileNotFoundException e) {
								//no action needed
							} catch (IOException e) {
								System.err.println("[ERROR] " + e.toString());
							}
						}
						else {
							System.err.println("{ERROR] " + new IllegalArgumentException("Language tag format not supported: " + defaultLang + " (valid examples: en-US, en_US)").toString());
						}
					}
				}

				langFiles = files.toArray(new String[0]);

				 */
				/*
				loadedFolders = true;
				System.out.println("Thread Ended: " + Thread.currentThread().getName());
//				Thread.currentThread().interrupt();
			});

			if (!loadedFolders)
				listFolderThread.start();
			*/

			langFiles = new File(langFolder).list();

			if (langFiles == null)
				throw new IllegalArgumentException("Unable to locate folder: " + langFolder);

			System.out.println("[INFO] Loaded languages: " + Arrays.toString(langFiles).replaceAll("\\.lang", ""));

			try {
				localFile.load(new InputStreamReader(new FileInputStream(new File(langFolder + lang + fileSuffix)), StandardCharsets.UTF_8));
			} catch (FileNotFoundException e) {
				System.out.println("[WARNING] Language " + lang + " is not available, Loading default language...");

				try {
					appLanguage = defaultLang;
					localFile.load(new InputStreamReader(new FileInputStream(new File(langFolder + defaultLang + fileSuffix)), StandardCharsets.UTF_8));
				} catch (FileNotFoundException e1) {
					System.err.println("[ERROR] Default language " + defaultLang + " is not available");
					throw new IllegalArgumentException("Default language file not available: " + langFolder + defaultLang + fileSuffix);
				} catch (UnsupportedEncodingException e1) {
					System.err.println("[ERROR] Invalid encoding for language file: " + langFolder + defaultLang + fileSuffix);
					throw new IllegalArgumentException("Encoding for file " + langFolder + defaultLang + fileSuffix + " is not UTF-8");
				} catch (IOException e1) {
					System.out.println("[WARNING] Exception caught: " + e1.toString());
				}
			} catch (UnsupportedEncodingException e1) {
				System.err.println("[ERROR] Invalid encoding for language file: " + langFolder + lang + fileSuffix);
				throw new IllegalArgumentException("Encoding for file " + langFolder + lang + fileSuffix + " is not UTF-8");
			} catch (IOException e1) {
				System.out.println("[WARNING] Exception caught: " + e1.toString());
			}
		}
	}
	
	/**
	 * Instantiate the localization class with a specified localization folder which contains all the localization files and is located inside the source folder, 
	 * a specified file type, and the default language. The default language is a String, but it has to be a specified language tag like "en-US". 
	 * <p>
	 * Run this as soon as the application is started (the main() method in the main class) if you wish to use this as your localization library, so you can use "get(key)" 
	 * to retrieve the String in the desired language. 
	 * <p>
	 * Running this will use the user's system language as the application language, but the default language will be used when user's system language is not supported. 
	 * 
	 * @param localFolder a String with the location of the folder containing all the localization files (format: "/package/", example: "/cc/jerry/commons/local/localizations/") 
	 * //@param fileType    a String with a specified file suffix for the localization files (for example: ".txt" or ".json")
	 * @param defaultLang a String of the language tag for the default language, this specified language is used when the language in the "lang" parameter is not supported (for example: "en-US") 
	 */
	public Localization(String localFolder, String defaultLang, String fileSuffix) {
		new Localization(localFolder, defaultLang, systemLanguage, fileSuffix);
	}
	
	/**
	 * Retrieve a String with the given key. The String will be in the language specified during the instantiation of this class. 
	 * 
	 * @param key the key or identifier used to retrieve the String. 
	 * @return the String retrieved with the key. 
	 */
	public static String get(String key) {
		if (localFile.containsKey(key))
			return localFile.getProperty(key); 
		else 
			return key; 
	}
	
	/**
	 * Returns a String array with a list of languages tags ("en-US") that matches the localization files in the folder specified during the class instantiation. 
	 * 
	 * @return an array of language tags in String
	 */
	public static String[] langList() {
		List<String> list = new ArrayList<>();
		for (String item : langFiles) {
			list.add(item.replace(langExtension, ""));
		}

		String[] result = list.toArray(new String[0]);
		Arrays.sort(result);
		return result;
	}

	//Getters
//	public static boolean isListFolderThreadAlive() {
//		if (listFolderThread == null) throw new NullPointerException("Class not initialized! ");
//		else return listFolderThread.isAlive();
//	}

	public static String getAppLanguage() {
		if (appLanguage == null) throw new NullPointerException("Class not initialized! ");
		else return appLanguage;
	}
	
}
