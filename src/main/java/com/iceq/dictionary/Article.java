package com.iceq.dictionary;

import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Number;

public class Article extends PartOfSpeech{
	public Gender m_gender;
	public Number m_number;
	public Case	  m_case;
	
	public Article() {
		super(Type.Article);
	}

	public Gender getGender() {
		return m_gender;
	}

	public void setGender(Gender m_gender) {
		this.m_gender = m_gender;
	}

	public Number getNumber() {
		return m_number;
	}

	public void setNumber(Number m_number) {
		this.m_number = m_number;
	}

	public Case getCase() {
		return m_case;
	}

	public void setCase(Case m_case) {
		this.m_case = m_case;
	}

	public String toString(){
		String s = super.toString();
		
		s += "["+m_gender+","+m_number+","+m_case+"] -> ";
		
		return s;
	}
}
