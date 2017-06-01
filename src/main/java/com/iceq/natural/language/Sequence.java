package com.iceq.natural.language;

import java.util.List;

import com.iceq.dictionary.EntityType;
import com.iceq.dictionary.PartOfSpeech;
import com.iceq.dictionary.Substantive;
import com.iceq.dictionary.Grammar.Art;
import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Number;
import com.iceq.dictionary.PartOfSpeech.Type;
import com.iceq.dictionary.analyser.Word;

public class Sequence {
	List<Word> 	m_words;
	boolean 	m_isValid;
	
	public Sequence(List<Word> seq){
		m_words = seq;
		validate();
	}
	
	public boolean isValid(){
		return m_isValid;
	}
	
	private void validate(){
		for(Word word : m_words){
			if(word.isAmbiguos()){
				System.out.println("Ambiguos word:"+word.get());
				return;
			}
			
			if(word.isUnknown()){
				System.out.println("Unknown word:"+word.get());
				return;
			}
			
			PartOfSpeech p = word.getPosibleFunctions().get(0);
			if(p.getType() == Type.Verb){
				if(!"fi".equals(p.m_value)){
					System.out.println("Unsupported verb:"+p.m_value);
					return;
				}
				p.setEntityType(EntityType.Relation);
			}
			
			if(p.getType() == Type.Substantive){
				Substantive subst = (Substantive)p;
				String str = subst.get(Case.Nominativ, Number.Singular, Art.Articulat);
				if(str == null)
					str = subst.get(Case.Nominativ, Number.Plural, Art.Articulat);
				
				if(str != null){
					p.setEntityType(EntityType.EntityMain);
				}else{
					p.setEntityType(EntityType.EntitySecondary);
				}
			}
			
			if(p.getType() == Type.Adjective){
				p.setEntityType(EntityType.Attribute);
			}
		}
		m_isValid = true;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(Word word: m_words){
			EntityType t = word.getPosibleFunctions().get(0).getEntityType();
			s += " ["+t+"]"+ word.get();
		}
		return s;
	}
}
