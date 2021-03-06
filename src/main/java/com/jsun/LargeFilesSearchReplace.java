package com.jsun;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.jsun.text.TextSearchReplace;
import com.jsun.type.TypeEnum;
import com.jsun.xml.XMLPartEnum;
import com.jsun.xml.XMLSearchReplace;
import com.jsun.type.SearchType;
import com.jsun.type.FileType;

/**
 * This is a command line utility to demonstrate the usage of
 * the search and replace functionality applied to text and
 * xml files.
 * @author jerysun
 * @version 1.00 2018-04-08
 */
public class LargeFilesSearchReplace {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    try (Scanner sc = new Scanner(System.in)) {
      String path = "";
      String searchString = "";
      int type = 0;
      FileType fileType = FileType.UNKNOWN;
      XMLPartEnum xmlPartEnum = XMLPartEnum.UNKNOWN;
      
      System.out.println("Please input the file path:");
      int idx = -1;
      while(true) {
        if ((path = sc.next().trim()).isEmpty() || 
            (idx = path.lastIndexOf('.')) == -1 ||
            idx == path.length() - 1) {
          System.out.println("Please input the file path:");
          continue;
        } else {
          break;
        }
      }
      
      String extensionName = path.substring(++idx);
      if (extensionName.toLowerCase().equals("txt")) {
        fileType = FileType.TEXT;
      } else if (extensionName.toLowerCase().equals("xml")) {
        fileType = FileType.XML;
        
        System.out.println("Please input the XML part type(1: element name 2. attribute 3. text node), just input the number:");
        while(true) {
          try {
            type = sc.nextInt();
          } catch (InputMismatchException e) {
            System.out.println("Error! You must input a number: 1 or 2 or 3!");
            return;
          }
          
          if (type == 1 || type == 2 || type == 3) {
            switch (type) {
            case 1:
              xmlPartEnum = XMLPartEnum.ELEMENT_NAME;
              break;
            case 2:
              xmlPartEnum = XMLPartEnum.ATTRIBUTE;
              break;
            case 3:
              xmlPartEnum = XMLPartEnum.TEXT_NODE;
              break;
            default:
              break;
            }
            break;
          } else {
            System.out.println("Please input the XML part type(1: element name 2. attribute 3. text node), just input the number:");
          }
        }
      } else {
        fileType = FileType.TEXT;//TODO, now just set it as default if it's not xml
      }
      
      System.out.println("Please input the string you want to search:");
      searchString = sc.next();
      searchString = searchString.trim();
      
      switch (xmlPartEnum) {
      case ELEMENT_NAME:
        while (!searchString.contains("<")) {
          System.out.println("Error! The xml element name must be prefixed by a <");
          System.out.println("Please input the string you want to search:");
          searchString = sc.next().trim();
        }
        
        if (searchString.charAt(searchString.length()-1) == '>') { //user friendly for open tag
          searchString = searchString.substring(0, searchString.length()-1);
        }
        break;
      case ATTRIBUTE:
        while (!searchString.contains("=")) {
          System.out.println("Error! The xml attribute must contain a =");
          System.out.println("Please input the string you want to search:");
          searchString = sc.next().trim();
        }
        break;
      default:
        break;
      } //end of switch

      System.out.println("Please input the search type(1: Text phrease 2. Pattern), just input the number:");
      while(true) {
        try {
          type = sc.nextInt();
        } catch (InputMismatchException e) {
          System.out.println("Error! You must input a number either 1 or 2!");
          return;
        }
        
        if (type == 1 || type == 2) {
          break;
        } else {
          System.out.println("Please input the search type(1: Text phrease 2. Pattern), just input the number:");
        }
      }
      //System.out.println("Path: " + path + ", searchString: " + searchString + ", type: " + type);
      
      SearchType searchType = new SearchType(type == 1 ? TypeEnum.PHRASE : TypeEnum.PATTERN, searchString);
      
      System.out.println("Please input the replacement string(if you input no, that means you skip the replace operation):");
      String replaceString = sc.next().trim();
      if (!replaceString.equals("no")) {
        switch (xmlPartEnum) {
        case ELEMENT_NAME:
          while (!replaceString.contains("<")) {
            System.out.println("Error! The xml element name must be prefixed by a <");
            System.out.println("Please input the replacement string if you want:");
            replaceString = sc.next().trim();
          }
          
          if (replaceString.charAt(replaceString.length()-1) == '>') { //user friendly for open tag
             replaceString = replaceString.substring(0, replaceString.length()-1);
          }
          break;
        case ATTRIBUTE:
          while (!replaceString.contains("=")) {
            System.out.println("Error! The xml attribute must contain a =");
            System.out.println("Please input the replacement string if you want:");
            replaceString = sc.next().trim();
          }
          break;
        default:
          break;
        }
      }
      
      SearchReplace searchReplace = null;
      if (fileType == FileType.TEXT) {
        searchReplace = new TextSearchReplace(path, searchType);
      } else if (fileType == FileType.XML) {
        searchReplace = new XMLSearchReplace(path, searchType, xmlPartEnum);
      } else { //JSUN
        return;
      }
        
      boolean found = false;
      try (InputStream in = new FileInputStream(path)) {
        if (found = searchReplace.fSearch(in, searchType)) {
          System.out.println("\"" + searchType.getSearchString() + "\" is found in file: " + path);
        } else {
          System.out.println("\"" + searchType.getSearchString() + "\" is NOT found in file: " + path);
        }
      }
      
      if (found && !replaceString.equals("no")) {
        try (InputStream in = new FileInputStream(path)) {
          searchReplace.fReplace(in, searchType, replaceString);
          System.out.println("Replacement is done, please check the new generated file: " + searchReplace.getOutPath(path));
        }
      }
    }
  }
}