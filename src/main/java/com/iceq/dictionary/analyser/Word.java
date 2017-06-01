package com.iceq.dictionary.analyser;

import java.util.ArrayList;
import java.util.List;

import com.iceq.dictionary.PartOfSpeech;

public class Word
{
	String m_word;
	List<PartOfSpeech> m_possibleFunctions = new ArrayList<PartOfSpeech>();
	
	public void set(String word){
		m_word = word;
	}
	
	public String get(){
		return m_word;
	}
	
	public void addPossibleFunction(PartOfSpeech p){
		m_possibleFunctions.add(p);
	}
	
	public void addPossibleFunction(List<PartOfSpeech> p){
		if(p != null && p.size() > 0)
			m_possibleFunctions.addAll(p);
	}
	
	public List<PartOfSpeech> getPosibleFunctions(){
		return m_possibleFunctions;
	}
	
	public boolean isAmbiguos(){
		return m_possibleFunctions.size() > 1;
	}
	
	public boolean isUnknown(){
		return m_possibleFunctions.size() == 0;
	}
	
	public PartOfSpeech getType(PartOfSpeech.Type t){
		for(int i = 0; i < m_possibleFunctions.size();i++){
			final PartOfSpeech p = m_possibleFunctions.get(i);
			if(p.getType() == t)
				return p;
		}
		return null;
	}
	
	public String toString(){
		String line = m_word +"\t";
		for(int i = 0; i < m_possibleFunctions.size();i++){
			PartOfSpeech p = m_possibleFunctions.get(i);
			line += "= " + p +";\n\t";
		}
	
		return line;
	}
}
