package com.jsun;

import static org.junit.Assert.*;
import org.junit.Test;

import com.jsun.text.TextSearchReplace;
import com.jsun.type.SearchType;
import com.jsun.type.TypeEnum;

public class SearchReplaceTest {
  @Test
  public void testGetOutPath() {
    String path = "C:\\Tmp\\best_times.txt";
    TypeEnum typeEnum = TypeEnum.PHRASE;
    String searchString = "failure";
    
    SearchReplace searchReplace = new TextSearchReplace(path, new SearchType(typeEnum, searchString));
    
    assertEquals("The new generated file is C:\\Tmp\\best_times_out.txt",
                 searchReplace.getOutPath(path),
                 "C:\\Tmp\\best_times_out.txt");
  }
}