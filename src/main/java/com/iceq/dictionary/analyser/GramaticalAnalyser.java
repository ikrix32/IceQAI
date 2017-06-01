package com.iceq.dictionary.analyser;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import com.iceq.dictionary.PartOfSpeech;
import com.iceq.natural.language.Sequence;

public class GramaticalAnalyser
{
	DictionaryDatabase m_databaseDriver;
	
	public GramaticalAnalyser(){
		m_databaseDriver = new DictionaryDatabase();
	}
	
	public void analyse(String text){
		System.out.println("Analysing : "+text);
		text = Normalizer.normalize(text, Form.NFC);
		text = text.replace(',', ' ');
		text = text.replace('.', ' ');
		String[] wordsStr = text.split("\\s");
		List<Word> words = new ArrayList<>();
		
		for(int i = 0; i < wordsStr.length;i++){
			String wordStr = wordsStr[i].toLowerCase().trim();
			Word word = m_databaseDriver.getWord(wordStr);
			words.add(word);
			System.out.println(word);
		}
		composeSequence(new ArrayList<PartOfSpeech>(), words, 0);
	}
	
	public void processSentence(String text){
		System.out.println("Analysing : "+text);
		text = Normalizer.normalize(text, Form.NFC);
		text = text.replace(',', ' ');
		text = text.replace('.', ' ');
		String[] wordsStr = text.split("\\s");
		List<Word> words = new ArrayList<>();
		
		for(int i = 0; i < wordsStr.length;i++){
			String wordStr = wordsStr[i].toLowerCase().trim();
			Word word = m_databaseDriver.getWord(wordStr);
			words.add(word);
			
			System.out.println(word);
		}
		Sequence s = new Sequence(words);
		if(s.isValid())
			System.out.println("Sequence:"+s);
	}
	
	public List<PartOfSpeech> composeSequence(List<PartOfSpeech> seq,List<Word> words,int index){
		if(index < words.size()){
			Word w = words.get(index);
			for(int i = 0; i < w.getPosibleFunctions().size();i++){
				PartOfSpeech p = w.getPosibleFunctions().get(i);
				List<PartOfSpeech> seq1 = (List<PartOfSpeech>)((ArrayList<PartOfSpeech>)seq).clone();
				seq1.add(p);
				List<PartOfSpeech> result = composeSequence(seq1, words, index + 1);
			}
		}else{
			if(isValid(seq)){
				return seq;
			}
		}
		return null;
	}
	
	public boolean isValid(List<PartOfSpeech> seq){
		System.out.print("\nSequence:");
		for(int i = 0; i < seq.size();i++){
			PartOfSpeech p = seq.get(i);
			System.out.print(p.m_value+"("+p.getType()+") ");
		}
		return false;
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
		long time = System.currentTimeMillis();
		//g.analyse("Eu mă gândesc la colegii mei din liceu.");
		//g.analyse("Zilele următoare, vremea se va menține la temperaturi în limitele specifice perioadei.");
		//g.analyse("Frumos este copilul.");
		//g.analyse("Omul merge cel mai frumos.");
		//g.analyse("Omul arata ca un copil.");
		//g.analyse("Omul abia merge.");
		//g.analyse("Când fata a intrat în cameră, toți au întors privirile spre ea.");
		g.processSentence("Omul este copil.");
		float duration = (System.currentTimeMillis() - time) / 1000.0f;
		System.out.println("\nAnalized in "+duration);
	}
}
