package com.jsun.xml;

/**
 * For searching and replacing a specific part in XML file
 * @author jerysun
 *
 */
public enum XMLPartEnum {
	ELEMENT_NAME,   //Must be prefixed by <, for example, <Server
	ATTRIBUTE,//Must be in the format x="y"
	TEXT_NODE,
	UNKNOWN;
}
