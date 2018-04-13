package com.jsun.xml;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.jsun.SearchReplace;
import com.jsun.type.SearchType;
import com.jsun.type.TypeEnum;

public class XMLSearchReplaceTest {
  @Test
  public void testFSearch() throws FileNotFoundException {
    String path = "C:\\Tmp\\server.xml";
    TypeEnum typeEnum = TypeEnum.PHRASE;
    String searchString = "<Logger";
    XMLPartEnum xmlPartEnum = XMLPartEnum.ELEMENT_NAME;
    
    InputStream in = new FileInputStream(path);
    SearchType searchType = new SearchType(typeEnum, searchString);
    SearchReplace searchReplace = new XMLSearchReplace(path, searchType, xmlPartEnum);
    
    assertEquals("<Logger is found in C:\\Tmp\\server.xml",
                 searchReplace.fSearch(in, searchType),
                 true);
  }

  @Test
  public void testFReplace() throws IOException {
    String path = "C:\\Tmp\\server.xml";
    TypeEnum typeEnum = TypeEnum.PHRASE;
    String searchString = "<Logger";
    String replaceString = "<Mogger";
    XMLPartEnum xmlPartEnum = XMLPartEnum.ELEMENT_NAME;
    
    InputStream in = new FileInputStream(path);
    SearchType searchType = new SearchType(typeEnum, searchString);
    SearchReplace searchReplace = new XMLSearchReplace(path, searchType, xmlPartEnum);
    
    searchReplace.fReplace(in, searchType, replaceString);
    
    path = "C:\\Tmp\\server_out.xml";
    in = new FileInputStream(path);
    assertEquals("<Logger is NOT found in C:\\Tmp\\server_out.xml",
        searchReplace.fSearch(in, searchType),
        false);
    
    in = new FileInputStream(path);
    searchType = new SearchType(typeEnum, replaceString);
    assertEquals("<Mogger is found in C:\\Tmp\\server_out.xml",
                 searchReplace.fSearch(in, searchType),
                 true);
  }
}