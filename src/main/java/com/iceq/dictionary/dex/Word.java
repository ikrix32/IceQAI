package com.iceq.dictionary.dex;

import java.util.ArrayList;
import java.util.List;

class Word
{
	public enum Type
	{
		Unknown,
		//Flexibile
		Substantiv, //copil, fata, stilou, ploaie;
		Articol, // un copil, al copilului, cel rau;
		Adjectiv, // luminos, trist, negru, lenes;
		Numeral, // noua, inzecit, tuspatru, cate trei;
		Pronume, // tu, ai tai, acesta, cine, fiecare, nimic;
		Verb, // a face, a privi, a trece, a iubi;
		//Neflexibile:
		Adverb, // aici, acolo, apoi, astfel, putin, mult;
		Prepozitie, // din, pe, pe langa, gratie, contra;
		Conjunctie, // si, desi, ca sa, daca, fiindca;
		Interjectie,// of! valeu! poc! bis! pac!
	}
	//new definition table
	public int 			dbLexemId;
	public int 			dbDefinitionId;
	public Type			useType = Type.Unknown;
	public String		text;
	public List<String>	relatedWords	= new ArrayList<String>();
	public String		definition;
	
	public Word(int lexemId,int definitionId)
	{
		dbLexemId = lexemId;
		dbDefinitionId = definitionId;
	}
	
	public void addRelatedWords(String str){
		int i = str.indexOf(',');
		while(i > 0){
			String w = str.substring(0, i);
			relatedWords.add(w);
			str = str.substring(i+1);
		}
		relatedWords.add(str);
	}

	public String toString()
	{
		String str = "[" + dbLexemId + "][" + dbDefinitionId + "] -> [" + text + "][" + useType + "]";
		for(int i = 0; i < relatedWords.size();i++){
			str += "("+relatedWords.get(i)+")";
		}
		str += "=>["+definition+"]";
		return str;
	}
}
