package com.iceq.dictionary.analyser;

import java.util.ArrayList;
import java.util.List;

import com.iceq.dictionary.ParteDeVorbire;

public class Word
{
	String m_word;
	List<ParteDeVorbire> m_possibleFunctions = new ArrayList<ParteDeVorbire>();
	
	public void set(String word){
		m_word = word;
	}
	
	public void addPossibleFunction(ParteDeVorbire p){
		m_possibleFunctions.add(p);
	}
	
	public void addPossibleFunction(List<ParteDeVorbire> p){
		if(p != null && p.size() > 0)
			m_possibleFunctions.addAll(p);
	}
	
	public String toString(){
		String line = m_word +"\t";
		for(int i = 0; i < m_possibleFunctions.size();i++){
			ParteDeVorbire p = m_possibleFunctions.get(i);
			line += "= " + p +";\n\t";
		}
	
		return line;
	}
}
