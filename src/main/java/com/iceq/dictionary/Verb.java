package com.iceq.dictionary;

import com.iceq.dictionary.Grammar.Mood;
import com.iceq.dictionary.Grammar.Person;
import com.iceq.dictionary.Grammar.Tense;
import com.iceq.dictionary.Grammar.Number;;

public class Verb extends PartOfSpeech
{
	public class Conjugation extends StringN
	{
		public Tense	m_tense;
		public Mood		m_mood;
		public Person	m_person;
		public Number	m_number;

		public String toString()
		{
			return "[" + m_mood + ", " + m_tense + ", Persoana " + m_person + ", " + m_number + "] -> " + m_value;
		}
	}

	 
	public Conjugation[][][][] m_conjugations = new Conjugation[Mood.values().length][Tense.values().length][Number.values().length][Person.values().length];

	public Verb(){
		super(Type.Verb);
	}
	
	public void setConjugare( Mood mood, Tense tense, Number number, Person person, String s)
	{
		final int m = mood.ordinal();
		final int t = tense.ordinal();
		final int n = number.ordinal();
		final int p = person.ordinal();
		
		if(m_conjugations[m][t][n][p] == null)
			m_conjugations[m][t][n][p] = new Conjugation();
		
		m_conjugations[m][t][n][p].m_mood  = mood;
		m_conjugations[m][t][n][p].m_tense = tense;
		m_conjugations[m][t][n][p].m_number= number;
		m_conjugations[m][t][n][p].m_person= person;
		m_conjugations[m][t][n][p].m_value = s;
		m_conjugations[m][t][n][p].m_valueAscii = toAscii(s);
	}

	public String toString()
	{
		String a = "Verb->\"a " + m_value + "\"";

		for (int mood = 0; mood < Mood.values().length; mood++)
		{
			for (int tense = 0; tense < Tense.values().length; tense++)
			{
				for (int number = 0; number < Number.values().length; number++)
				{
					for (int person = 0; person < Person.values().length; person++)
					{
						if (m_conjugations[mood][tense][number][person] != null)
						{
							a += "\n\t\t" + m_conjugations[mood][tense][number][person];
						}
					}
				}
			}
		}

		return a;
	}
}
