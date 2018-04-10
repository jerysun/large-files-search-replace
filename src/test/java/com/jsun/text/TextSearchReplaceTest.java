package com.jsun.text;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jsun.type.SearchType;
import com.jsun.type.TypeEnum;

public class TextSearchReplaceTest {

	@Test
	public void testGetSearchType() {
		String path = "C:\\Tmp\\HRC200Reasons.txt";
		TypeEnum typeEnum = TypeEnum.PHRASE;
		String searchString = "failure";
		
		TextSearchReplace searchReplace = new TextSearchReplace(path, new SearchType(typeEnum, searchString));
		
		int i = 0;
		if (searchReplace.getSearchType().getTypeEnum() == TypeEnum.PHRASE) {
			i = 1;
		}
		
		assertEquals("The search type must be PHRASE", 1, i);
	}

}
