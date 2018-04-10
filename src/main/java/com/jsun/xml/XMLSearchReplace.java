package com.jsun.xml;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsun.type.SearchType;
import com.jsun.type.TypeEnum;
import com.jsun.text.TextSearchReplace;

/**
 * This is an implementation of the interface SearchReplace
 * for XML files processing, using FileInputStream and 
 * FileOutputStream to reduce the memory consumption, using
 * XMLPartEnum to guarantee only the proper part will be
 * searched and replaced.
 * @author jerysun
 * @version 1.00 2018-04-08
 */
public class XMLSearchReplace extends TextSearchReplace {
	private final XMLPartEnum xmlPartEnum;
	
	public XMLSearchReplace(String path, SearchType searchType, XMLPartEnum xmlPartEnum) {
		super(path, searchType);
		this.xmlPartEnum = xmlPartEnum;
	}
	
	@Override
	public boolean fSearch(InputStream in, SearchType searchType) {
		if (xmlPartEnum == XMLPartEnum.UNKNOWN) {
			return false;
		}
		
		switch (xmlPartEnum) {
		case ELEMENT_NAME:
			if (!searchType.getSearchString().contains("<")) {
				return false;
			}
			break;
		case ATTRIBUTE:
			if (!searchType.getSearchString().contains("=")) {
				return false;
			}
			break;
		default:
			break;
		}
		
		return super.fSearch(in, searchType);
	}
	
	@Override
	public void fReplace(InputStream in, SearchType searchType, String replaceString) throws IOException {
		if (in == null || searchType == null || replaceString == null ||
				replaceString.isEmpty() || xmlPartEnum == XMLPartEnum.UNKNOWN)
			return;
		
		String newEndTag = null;
		String oldEndTag = null;
		switch (xmlPartEnum) {
		case ELEMENT_NAME:
			if (!searchType.getSearchString().contains("<") || !replaceString.contains("<")) {
				return;
			}
			StringBuilder sb = new StringBuilder(replaceString);
			sb.insert(1, '/');
			newEndTag = sb.toString();
			sb = new StringBuilder(searchType.getSearchString());
			sb.insert(1, '/');
			oldEndTag = sb.toString();
			break;
		case ATTRIBUTE:
			if (!searchType.getSearchString().contains("=") || !replaceString.contains("=")) {
				return;
			}
			break;
		default:
			break;
		}
		
		//System.out.println("oldEndTag: " + oldEndTag + ", newEndTag: " + newEndTag);
		
		TypeEnum typeEnum = searchType.getTypeEnum();
		
		try(Scanner sc = new Scanner(in, "UTF-8");
				FileOutputStream fo = new FileOutputStream(getOutPath(getPath()))) {
			Pattern pattern = null;
			Matcher matcher = null;
			
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				switch (typeEnum) {
				case PHRASE:
					if (line.contains(searchType.getSearchString())) {
						phraseReplace(line, searchType.getSearchString(), replaceString, fo);
					} else if (oldEndTag != null && newEndTag != null &&
							line.contains(oldEndTag)) {
						phraseReplace(line, oldEndTag, newEndTag, fo);
					} else {
						lineReplace(line, fo);
					}
					break;
				case PATTERN:
					pattern = Pattern.compile(searchType.getSearchString());
					matcher = pattern.matcher(line);
					if (matcher.find()) {
						patternReplace(line, searchType.getSearchString(), replaceString, fo);
					} else if (oldEndTag != null && newEndTag != null) {
						pattern = Pattern.compile(oldEndTag);
						matcher = pattern.matcher(line);
						if (matcher.find()) {
							patternReplace(line, oldEndTag, newEndTag, fo);
						} else {
							lineReplace(line, fo);
						}
					} else {
						lineReplace(line, fo);
					}
					break;
				case WILDCARD:
					//TODO
					return;
				case VARIABLE:
					//TODO
					return;
				default:
					break;
				}
			}
			fo.flush();
		}
	}
	
	private void lineReplace(String line, FileOutputStream fo) throws IOException {
		StringBuilder sb = new StringBuilder(line);
		sb.append(System.getProperty("line.separator"));
		byte[] lineBytes = sb.toString().getBytes();
		fo.write(lineBytes);
	}
	
	public XMLPartEnum getXmlPartEnum() {
		return xmlPartEnum;
	}
}
