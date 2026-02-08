/**
 * 
 * @author mike802
 *
 * brand_aware
 * ??? - 2019
 * 
 */
package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import encrypter.StringConverter;

public class IOScreen extends StringConverter{
	
	Properties properties;

	public IOScreen(Properties p) {
		properties = p;
	}
	
	public ArrayList<String> loadAllTitles() throws IOException {
		File file = new File(properties.getEntryTitlesPath());
		if(!file.exists()) {
			file.createNewFile();
			ArrayList<String> list = new ArrayList<String>();
			list.add("<empty>");
			return list;
		}
		boolean empty = true;
		BufferedReader reader = new BufferedReader(new FileReader(properties.getEntryTitlesPath()));
		ArrayList<String> titles = new ArrayList<String>();
		while(reader.ready()) {
			empty = false;
			String line = reader.readLine();
			line = decryptString(line);
			titles.add(line);
		}
		reader.close();
		if(!empty) {
			return titles;
		}
		return null;
	}
	
	public ConcurrentHashMap<String, String> loadAllEntries() throws IOException{
		ConcurrentHashMap<String, String> entryMap = new ConcurrentHashMap<String, String>();
		File file = new File(properties.getEntriesPath());
		if(!file.exists()) {
			file.createNewFile();
			ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
			map.put("<empty>", "<tbd>");
			saveAllEntries(map);
			saveAllTitles(map);
			return map;
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		boolean empty = true;
		while(reader.ready()) {
			String line = reader.readLine();
			if(line.compareTo(properties.getNullEntryString()) != 0) {
				empty = false;
				line = decryptString(line);
				String[] lineSplit = line.split(properties.getSeperatorString());
				entryMap.put(lineSplit[0], lineSplit[1]);
			}
		}
		reader.close();
		if(!empty) {
			return entryMap;
		}
		return null;
	}
	
	public String[] loadFilters() throws Exception{
		File file = new File(properties.getFiltersPath());
		if(!file.exists()) {
			System.exit(0);
		}
		String[] list = new String[3];
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int position = 0;
		while(reader.ready()) {
			String line = reader.readLine();
			line = decryptString(line);
			list[position] = line;
			position++;
		}
		reader.close();
		return list;
	}
	
	public void saveAllEntries(ConcurrentHashMap<String, String> entryMap) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(properties.getEntriesPath()));
		Iterator<String> titles = entryMap.keySet().iterator();
		String buffer = "";
		while(titles.hasNext()) {
			String title = titles.next();
			if(title.compareTo("<empty>") != 0 || entryMap.size() == 1) {
				String entry = entryMap.get(title);
				String line = title + properties.getSeperatorString() + entry;
				line = encryptString(line);
				buffer += line + "\n";
			}
		}
		if(buffer.contains("\n")) {
			buffer = buffer.substring(0, buffer.lastIndexOf("\n"));
		}
		writer.write(buffer);
		writer.close();
	}
	
	public void saveAllTitles(ConcurrentHashMap<String, String> entryMap) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(properties.getEntryTitlesPath()));
		Iterator<String> titles = entryMap.keySet().iterator();
		ArrayList<String> list = new ArrayList<String>();
		while(titles.hasNext()) {
			String title = titles.next();
			if(title.compareTo("<empty>") != 0 || entryMap.size() == 1) {
				list.add(title);
			}
		}
		for(int x = 0; x < list.size(); x++) {
			for(int y = 0; y < list.size(); y++) {
				String temp = list.get(y);
				if(list.get(x).compareTo(temp) > 0) {
					list.set(y, list.get(x));
					list.set(x, temp);
				}
			}
		}
		String buffer = "";
		for(int x = 0; x < list.size(); x++) {
			String line = list.get(x);
			line = encryptString(line);
			buffer += line + "\n";
		}
		if(buffer.contains("\n")) {
			buffer = buffer.substring(0, buffer.lastIndexOf("\n"));
		}
		writer.write(buffer);
		writer.close();
	}
}
