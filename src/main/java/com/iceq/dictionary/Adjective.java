package com.iceq.dictionary;

import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Number;

public class Adjective extends PartOfSpeech
{
	public class Declination extends StringN
	{
		public Gender m_gender;
		public Number m_number;
		
		public String toString(){
			return "["+m_gender+","+m_number+"] -> " + m_value;
		}
	}
	
	private Declination[][] m_declinations = new Declination[Gender.values().length][Number.values().length];

	public Adjective(){
		super(Type.Adjective);
	}
	
	public void set( Gender gender, Number number, String s)
	{
		int g = gender.ordinal();
		int n = number.ordinal();
		if(m_declinations[g][n] == null){
			m_declinations[g][n] = new Declination();
		}
		m_declinations[g][n].m_gender = gender;
		m_declinations[g][n].m_number = number;
		m_declinations[g][n].m_value = s;
		m_declinations[g][n].m_valueAscii = toAscii(s);
	}

	public String get( Gender g, Number n)
	{
		final Adjective.Declination declination =  m_declinations[g.ordinal()][n.ordinal()];
		
		if(declination != null)
			return  m_declinations[g.ordinal()][n.ordinal()].m_value;
		
		return null;
	}

	public String toString()
	{
		String s = super.toString();

		for (int i = 0; i < Gender.values().length; i++)
		{
			for (int j = 0; j < Number.values().length; j++)
			{
				if (m_declinations[i][j] != null) s += "\n\t\t[" + Gender.values()[i] + "][" + Number.values()[j] + "] =>\"" + m_declinations[i][j] + "\"]";
			}
		}

		return s;
	}

}
