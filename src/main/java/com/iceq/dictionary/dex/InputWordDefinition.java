package com.iceq.dictionary.dex;

public class InputWordDefinition
{
	public int		dbId;
	public int		dbLexemId;
	public String	lexem;
	public String	definition;
	
	public String toString()
	{
		return "[" + dbLexemId + "][" + dbId + "] -> [" + lexem + "][" + definition + "]";
	}
}
