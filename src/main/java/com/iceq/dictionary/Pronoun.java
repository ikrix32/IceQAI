package com.iceq.dictionary;

import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Number;
import com.iceq.dictionary.Grammar.Person;

public class Pronoun extends ParteDeVorbire{
	public Person m_person;
	public Gender m_gender;
	public Number m_number;
	public Case	  m_case;
	
	public Pronoun() {
		super(Type.Pronoun);
	}
	
	public Person getPerson() {
		return m_person;
	}

	public void setPerson(Person m_person) {
		this.m_person = m_person;
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
		
		s += "["+m_person+","+m_gender+","+m_number+","+m_case+"] -> ";
		
		return s;
	}
}
