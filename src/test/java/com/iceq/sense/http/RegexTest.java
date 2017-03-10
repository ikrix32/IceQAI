package com.iceq.sense.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest
{
	final static String WORD_PATTERN ="[@]([^@]*)[@]";
	public static void main( String args[])
	{
		// String to be scanned to find the pattern.
		String line = "@COPILÁNDRĂ,@ $copilandre,$ #s. f.# Fată în perioada de trecere de la copilărie la adolescență; codană. - Din @copilandru.@";
		String pattern = WORD_PATTERN;//"(\\b[@]\\w*[@]\\b)";// "(\b.*?\b)";

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(line);
		while (m.find())
		{
			System.out.println("Found value: " + m.group());
		}
	}
}