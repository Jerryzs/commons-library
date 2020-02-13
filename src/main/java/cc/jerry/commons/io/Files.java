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

package cc.jerry.commons.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Files {

    /**
     * Download a file from an URL (for example "http://example.com/file.txt") 
     * to a directory, and keep the same filename. 
     * 
     * @param url       an absolute URL of the file
     * @param directory a directory, must include "/" at the end. 
     */
    public void downloadFile(String url, String directory){
        URL fileURL;
        URLConnection downloadCon;
        DataInputStream downloadDIS; 
        FileOutputStream downloadFOS; 
        byte[] fileData;  
        try {
            fileURL = new URL(url); //File Location goes here
            downloadCon = fileURL.openConnection(); // open the url connection.
            downloadDIS = new DataInputStream(downloadCon.getInputStream());
            fileData = new byte[downloadCon.getContentLength()]; 
            for (int q = 0; q < fileData.length; q++) { 
                fileData[q] = downloadDIS.readByte();
            }
            downloadDIS.close(); // close the data input stream
            downloadFOS = new FileOutputStream(new File(directory + fileURL.getFile())); //FILE Save Location goes here
            downloadFOS.write(fileData);  // write out the file we want to save.
            downloadFOS.close(); // close the output stream writer
        }
        catch(IOException m) {
            System.out.println("Failed to download file (please check your internet connection)");
            System.out.println(m);
        }
    }
    
    /**
     * Download a file from an URL (for example "http://example.com/file.txt")
     * to a directory, and rename the file to a new filename. 
     * 
     * @param url       an absolute URL of the file
     * @param directory a directory, must include "/" at the end
     * @param filename  a filename, must include file type (for example "file.txt")
     */
    public void downloadFile(String url, String directory, String filename){
        URL fileURL;
        URLConnection downloadCon;
        DataInputStream downloadDIS; 
        FileOutputStream downloadFOS; 
        byte[] fileData;  
        try {
            fileURL = new URL(url); //File Location goes here
            downloadCon = fileURL.openConnection(); // open the url connection.
            downloadDIS = new DataInputStream(downloadCon.getInputStream());
            fileData = new byte[downloadCon.getContentLength()]; 
            for (int q = 0; q < fileData.length; q++) { 
                fileData[q] = downloadDIS.readByte();
            }
            downloadDIS.close(); // close the data input stream
            downloadFOS = new FileOutputStream(new File(directory + filename)); //FILE Save Location goes here
            downloadFOS.write(fileData);  // write out the file we want to save.
            downloadFOS.close(); // close the output stream writer
        }
        catch(IOException m) {
            System.out.println("Failed to download file (please check your internet connection)");
            System.out.println(m);
        }
        
        
    }
    
    public enum ValueType {
    	PATH, CLASSPATH, URL, FILE;
    }
    
    /**
     * Read the content inside a file containing a JSON Object, and return the JSON Object as a String. 
     * 
     * @param value        the value associated with the "type" parameter
     * @param type         the type of value you entered in the "value" parameter (can be ValueType.PATH, ValueType.CLASSPATH, ValueType.URL or ValueType.FILE)
     * @param currentClass the class used to call this method, only for use when require static
     * @return a String of a JSON Object
     */
	public static String readJSON(String value, ValueType type, Class<?> currentClass) {
    	String returnValue = null;
    	switch (type) {
    	case PATH: 
			try {
				BufferedReader reader1 = new BufferedReader(new FileReader(new File(value)));
				String content1 = readAll(reader1);
	            returnValue = content1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            break;
    	case URL: 
    		try (InputStream is2 = new URL(value).openStream()) {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2, Charset.forName("UTF-8")));
                String content2 = readAll(reader2);
                returnValue = content2;
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
            }
    		break;
    	case CLASSPATH: 
    		try {
	    		InputStream is3 = currentClass.getResourceAsStream(value);
	            InputStreamReader isr3 = new InputStreamReader(is3);
	            BufferedReader reader3 = new BufferedReader(isr3);
	            String content3 = readAll(reader3);
	            returnValue = content3;
    		} catch (IOException e) {
    			e.printStackTrace(); 
    		}
    		break;
    	default: 
    		returnValue = null;
    		break; 
    	}
    	return returnValue;
    }
    
	/**
     * Read the content inside a file containing a JSON Object, and return the JSON Object.
     * 
     * @param value        the value associated with the "type" parameter
     * @param type         the type of value you entered in the "value" parameter (can be ValueType.PATH, ValueType.CLASSPATH, ValueType.URL or ValueType.FILE)
     * @param currentClass the class used to call this method, only for use when require static
     * @return a JSON Object
     */
    public static JSONObject parseJSON(String value, ValueType type, Class<?> currentClass) {
    	try {
			return new JSONObject(readJSON(value, type, currentClass));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * Read the content inside a file containing a JSON Object, and return the JSON Object as a String. 
     * 
     * @param value        the value associated with the "type" parameter
     * @param type         the type of value you entered in the "value" parameter (can be ValueType.PATH, ValueType.CLASSPATH, ValueType.URL or ValueType.FILE)
     * @return a String of a JSON Object
     */
    public String readJSON(String value, ValueType type) {
    	try {
			return readJSON(value, type, getClass());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * Read the content inside a file containing a JSON Object, and return the JSON Object. 
     * 
     * @param value        the value associated with the "type" parameter
     * @param type         the type of value you entered in the "value" parameter (can be ValueType.PATH, ValueType.CLASSPATH, ValueType.URL or ValueType.FILE)
     * @return a JSON Object
     */
    public JSONObject parseJSON(String value, ValueType type) {
    	try {
			return new JSONObject(readJSON(value, type, getClass()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    /**
     * Delete a file or a directory. 
     * 
     * @param file a file or directory
     */
    public static void removeFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeFile(aFile);
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }
    
    public String getHTML(String url) throws Exception {
        StringBuilder result = new StringBuilder();
        URL URL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
           result.append(line);
        }
        rd.close();
        return result.toString();
    }
    
    public String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        
        return null;
    }
    
    /**
     * Write or replace the content of a file with a JSON Object. The file will be created if it doesn't exist. 
     * 
     * @param obj  a JSON Object
     * @param file a file
     */
    public void writeJsonFile(JSONObject obj, File file) {
    	writeFile(obj.toString(), file);
    }
    
    /**
     * Write or replace the content of a file with a String. The file will be created if it doesn't exist. 
     * 
     * @param content  a String
     * @param file a file
     */
    public void writeFile(String content, File file) {
    	file.mkdirs();
    	if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
        try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
        } catch (IOException ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
