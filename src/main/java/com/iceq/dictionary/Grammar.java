package com.iceq.dictionary;

public class Grammar
{
	public enum Number
	{
		None,
		Singular,
		Plural,
	};
	
	public enum Person
	{
		None,
		Intai,
		Doua,
		Treia,
	};

	public enum Mood
	{
		None,
		Indicativ,
		Conjunctiv,
		Conditional,
		Infinitiv,
		InfinitivLung,
		Participiu,
		Gerunziu,
		Imperativ,
	};
	
	public enum Tense
	{
		None,
		Prezent,
		Perfect,
		Imperfect,
		PerfectSimplu,
		PerfectCompus,
		MaiMultCaPerfectul,
		Viitor,
		ViitorAnterior,
	}
	
	public enum Case{
		Nominativ,
		Acuzativ,
		Genitiv,
		Dativ,
		Vocativ,
	}
	
	public enum Gender{
		Masculin,
		Feminin,
		Neutru,
	}
	
	public enum Art{
		Nearticulat,
		Articulat
	}
}
