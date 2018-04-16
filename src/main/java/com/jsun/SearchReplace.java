package com.jsun;

import java.io.IOException;
import java.io.InputStream;
import com.jsun.type.SearchType;;

/**
 * This interface is the abstract of the search and replace function,
 * it adopts the InputStream aiming to reduce the memory consumption to
 * hundreds of mega bytes when processing the large file which could be
 * up to several giga bytes.
 * @author jerysun
 * @version 1.00 2018-04-08
 */
public interface SearchReplace {
  public boolean fSearch(InputStream in, SearchType searchType);
  
  public void fReplace(InputStream in, SearchType searchType, String replaceString) throws IOException;
  
  public default String getOutPath(String path) {
    int idx = path.lastIndexOf('.');
    
    StringBuilder sb = new StringBuilder(path.substring(0, idx));
    sb.append("_out");
    sb.append(path.substring(idx));
    
    return sb.toString();
  }
}