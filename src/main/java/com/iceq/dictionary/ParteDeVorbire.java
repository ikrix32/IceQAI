package com.iceq.dictionary;

public class ParteDeVorbire extends StringN
{
	public enum Type{
		Substantive,
		Verb,
		Adjective,
		Adverb,
		Pronoun,
		Preposition,
		Numeral,
		Conjunction
	}
	
	protected final Type m_type;
	
	public ParteDeVorbire(Type type){
		m_type = type;
	}
	
	public String toString(){
		return m_type+"->\""+m_value+"\"";
	}
}
