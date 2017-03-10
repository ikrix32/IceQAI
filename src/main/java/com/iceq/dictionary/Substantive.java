package com.iceq.dictionary;

import com.iceq.dictionary.Grammar.Art;
import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Number;;

public class Substantive extends ParteDeVorbire
{	
	public class Declination extends StringN
	{
		public Case 	m_case;
		public Number	m_number;
		public Art  	m_art;
		
		public String toString(){
			return "["+m_case+","+m_number+","+m_art+"] -> "+"" + m_value;
		}
	}
	
	private Declination[][][] m_declinations = new Declination[Case.values().length][Number.values().length][Art.values().length];
	private Gender m_gender;
	
	public Substantive(){
		super(Type.Substantive);	
	}
	
	public Substantive(Gender gender){
		super(Type.Substantive);
		setGender(gender);
	}
	
	public void set(Case caz, Number number, Art art, String s){
		int c = caz.ordinal();
		int n = number.ordinal();
		int a = art.ordinal();
		if(m_declinations[c][n][a] == null){
			m_declinations[c][n][a] = new Declination();
		}
		m_declinations[c][n][a].m_case = caz;
		m_declinations[c][n][a].m_number = number;
		m_declinations[c][n][a].m_art = art;
		m_declinations[c][n][a].m_value = s;
		m_declinations[c][n][a].m_valueAscii = toAscii(s);
	}
	
	public String get(Case caz,Number n,Art articol){
		Declination declination = m_declinations[caz.ordinal()][n.ordinal()][articol.ordinal()];
		if(declination != null)
			return declination.m_value;
		
		return null;
	}

	public Gender getGender() {
		return m_gender;
	}

	public void setGender(Gender m_gender) {
		this.m_gender = m_gender;
	}

	public String toString(){
		String s = super.toString();
		
		s += " gender: "+getGender();
		
		for(int i = 0; i < Case.values().length; i++){
			for(int k = 0; k < Art.values().length;k++){
				for(int j = 0; j < Number.values().length; j++){
					if(m_declinations[i][j][k] != null)
						s += "\n\t\t"+m_declinations[i][j][k];
				}
			}
		}
		
		return s;
	}
}
