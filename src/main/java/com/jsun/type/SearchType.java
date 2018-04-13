package com.jsun.type;

/**
 * This is a basic search unit
 * @author jerysun
 * @version 1.00 2018-04-08
 */
public final class SearchType {
  private final TypeEnum typeEnum;
  private final String searchString;
  
  public SearchType(TypeEnum typeEnum, String searchString) {
    this.typeEnum = typeEnum;
    this.searchString = searchString;
  }
  
  public TypeEnum getTypeEnum() {
    return typeEnum;
  }
  
  public String getSearchString() {
    return searchString;
  }
}