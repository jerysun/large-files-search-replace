package com.jsun;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jsun.text.TextSearchReplace;
import com.jsun.type.SearchType;
import com.jsun.type.TypeEnum;

public class SearchReplaceTest {

	@Test
	public void testGetOutPath() {
		String path = "C:\\Tmp\\HRC200Reasons.txt";
		TypeEnum typeEnum = TypeEnum.PHRASE;
		String searchString = "failure";
		
		SearchReplace searchReplace = new TextSearchReplace(path, new SearchType(typeEnum, searchString));
		
		int i = 0;
		
		if (searchReplace.getOutPath(path).equals("C:\\Tmp\\HRC200Reasons_out.txt")) {
			i = 1;
		}
		
		assertEquals("The new generated file is C:\\Tmp\\HRC200Reasons_out.txt", 1, i);
		
	}

}
