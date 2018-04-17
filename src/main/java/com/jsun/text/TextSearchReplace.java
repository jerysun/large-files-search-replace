package com.jsun.text;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsun.SearchReplace;
import com.jsun.type.SearchType;
import com.jsun.type.TypeEnum;

/**
 * This is an implementation of the interface SearchReplace
 * for text files processing, using FileInputStream and 
 * FileOutputStream to reduce the memory consumption.
 * @author jerysun
 * @version 1.00 2018-04-08
 */
public class TextSearchReplace implements SearchReplace {
  private final String path;
  private final SearchType searchType;

  public TextSearchReplace(String path, SearchType searchType) {
    this.path = path;
    this.searchType = searchType;
  }
  
  @Override
  public boolean fSearch(InputStream in, SearchType searchType) {
    if (in == null || searchType == null)
      return false;
    
    TypeEnum typeEnum = searchType.getTypeEnum();
    
    try (Scanner sc = new Scanner(in, "UTF-8")) {
      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        
        switch (typeEnum) {
        case PHRASE:
          if(line.contains(searchType.getSearchString()))
            return true;
          break;
        case PATTERN:
          Pattern pattern = Pattern.compile(searchType.getSearchString());
          Matcher matcher = pattern.matcher(line);
          if (matcher.find())
            return true;
          break;
        case WILDCARD:
          //TODO
          return false;
        case VARIABLE:
          //TODO
          return false;
        case UNKNOWN:
          return false;
        default:
          break;
        }
      }
    } catch (Exception e) {
      //e.printStackTrace();
      return false;
    }
    return false;
  }
  
  @Override
  public void fReplace(InputStream in, SearchType searchType, String replaceString) throws IOException {
    if (in == null || searchType == null || replaceString == null || replaceString.isEmpty())
      return;
    
    TypeEnum typeEnum = searchType.getTypeEnum();
    
    try (Scanner sc = new Scanner(in, "UTF-8");
         FileOutputStream fo = new FileOutputStream(getOutPath(path))) {
      while (sc.hasNextLine()) {
        String line = sc.nextLine();

        switch (typeEnum) {
        case PHRASE:
          phraseReplace(line, searchType.getSearchString(), replaceString, fo);
          break;
        case PATTERN:
          patternReplace(line, searchType.getSearchString(), replaceString, fo);
          break;
        case WILDCARD:
          //TODO
          return;
        case VARIABLE:
          //TODO
          return;
        case UNKNOWN:
          return;
        default:
          break;
        }
      }
      fo.flush();
    }
  }
  
  public void phraseReplace(String line, String targetString, String replaceString, FileOutputStream fo) throws IOException {
    String changedString = line.replaceAll(targetString, replaceString);
    StringBuilder sb = new StringBuilder(changedString);
    sb.append(System.getProperty("line.separator"));
    byte[] lineBytes = sb.toString().getBytes();
    fo.write(lineBytes);
  }
  
  public void patternReplace(String line, String targetString, String replaceString, FileOutputStream fo) throws IOException {
    Pattern pattern = Pattern.compile(targetString);
    Matcher matcher = pattern.matcher(line);
    String changedString = matcher.replaceAll(replaceString);
    StringBuilder sb = new StringBuilder(changedString);
    sb.append(System.getProperty("line.separator"));
    byte[] lineBytes = sb.toString().getBytes();
    fo.write(lineBytes);
  }
  
  public String getPath() {
    return path;
  }

  public SearchType getSearchType() {
    return searchType;
  }
}