package com.iceq.dictionary;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

public class StringN
{
	public String m_value;
	public String m_valueAscii;
	
	public void set(String s){
		m_value = s;
		m_valueAscii = toAscii(s);
	}
	
	protected String toAscii( String s)
	{
		String s1 = Normalizer.normalize(s, Normalizer.Form.NFKD);
		String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

		String s2 = s;
		try
		{
			s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"), "ascii");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return s2;
	}
}
