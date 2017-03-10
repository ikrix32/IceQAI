package com.iceq.dictionary.analyser;

import java.util.ArrayList;
import java.util.List;

public class GramaticalAnalyser
{
	DictionaryDatabase m_databaseDriver;
	
	public GramaticalAnalyser(){
		m_databaseDriver = new DictionaryDatabase();
	}
	
	public void analyse(String text){
		System.out.println("Analysing :"+text);
		text = text.replace(',', ' ');
		text = text.replace('.', ' ');
		String[] wordsStr = text.split("\\s");
		List<Word> words = new ArrayList<>();
		for(int i = 0; i < wordsStr.length;i++)
		{
			String wordStr = wordsStr[i].toLowerCase().trim();
			Word word = m_databaseDriver.getWord(wordStr);
			words.add(word);
			System.out.println(word);
		}
		System.out.println("Done");
	}
	
	public void test(){
//		List<Substantive> s = m_databaseDriver.getSubstantive("copilule");
//		System.out.println("Substantive:" + s);
//		Verb v = m_databaseDriver.getVerb("citi");
//		System.out.println("Verb:" + v);
//		Adjective a = m_databaseDriver.getAdjective("frumoasa");
//		System.out.println("Adjective:" + a);
//		System.out.println("'absolut' este adverb:" + m_databaseDriver.isAdverb("absolut"));
//		System.out.println("'acesta' este pronume:" + m_databaseDriver.isPronoun("acesta"));
//		System.out.println("'cu' este prepozitie:" + m_databaseDriver.isPreposition("cu"));
//		System.out.println("'trei' este numeral:" + m_databaseDriver.isNumeral("trei"));
//		System.out.println("'si' este conjunctie:" + m_databaseDriver.isConjunction("si"));
	}

	public static void main(String[] args){
		GramaticalAnalyser g = new GramaticalAnalyser();
		//g.test();
		//g.analyse("Eu mă gândesc la colegii mei din liceu.");
		//g.analyse("Acest cățel frumos nu este al nimănui.");
		g.analyse("Când fata a intrat în cameră, toți au întors privirile spre ea.");
	}
}
