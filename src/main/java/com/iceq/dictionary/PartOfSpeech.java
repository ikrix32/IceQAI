package com.iceq.dictionary;

public class PartOfSpeech extends StringN
{
	public enum Type{
		Substantive,
		Verb,
		Adjective,
		Adverb,
		Pronoun,
		Preposition,
		Numeral,
		Conjunction,
		Article,
	}
	
	protected final Type m_type;
	protected EntityType m_entityType;
	
	public PartOfSpeech(Type type){
		m_type = type;
	}
	
	public Type getType(){
		return m_type;
	}

	public void setEntityType(EntityType m_entityType) {
		this.m_entityType = m_entityType;
	}
	
	public EntityType getEntityType() {
		return m_entityType;
	}
	
	public String toString(){
		return m_type+"->\""+m_value+"\"";
	}
}
