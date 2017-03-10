package com.iceq.dictionary.analyser;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.iceq.dictionary.Adjective;
import com.iceq.dictionary.Grammar.Art;
import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Mood;
import com.iceq.dictionary.Grammar.Number;
import com.iceq.dictionary.Grammar.Person;
import com.iceq.dictionary.Grammar.Tense;
import com.iceq.dictionary.ParteDeVorbire;
import com.iceq.dictionary.ParteDeVorbire.Type;
import com.iceq.dictionary.Pronoun;
import com.iceq.dictionary.Substantive;
import com.iceq.dictionary.Verb;
import com.iceq.dictionary.sql.SQLiteDriver;

public class DictionaryDatabase
{
	final static String	databaseName	= "data/words.db";

	SQLiteDriver		driver			= new SQLiteDriver();

	public DictionaryDatabase(){
		driver = new SQLiteDriver();
		driver.openDatabaseConnection(databaseName,false);
	}

	final static String TAG_VALUE = "#VALUE#";
	String SUBSTANTIVE_QUERY=	"select s.substantive,s.substantive_ascii,s.gender,d.declination, d.declination_ascii,d.'case',d.number,d.art from substantives as s, subst_declinations as d " 
							+	"where (d.declination='#VALUE#' or d.declination_ascii='#VALUE#') and d.substantive_id = s.id";
	
	public List<Substantive> getSubstantive(String text){
		ResultSet resultSet = driver.runQuery(SUBSTANTIVE_QUERY.replace(TAG_VALUE, text));
		
		List<Substantive> substantives = new ArrayList<Substantive>();
		
		if(resultSet != null )
		{
			try{
				while(resultSet.next()){
					String subst = resultSet.getString("substantive");
					Substantive substantive = null;
					for(int i = 0; i < substantives.size() && substantive == null;i++){
						if(subst.equals(substantives.get(i).m_value)){
							substantive = substantives.get(i);
						}
					}
					
					if(substantive == null){
						substantive = new Substantive();
						substantive.set(subst);
						substantive.setGender(Gender.values()[resultSet.getInt("gender")]);
						substantives.add(substantive);
					}
					
					String declination = resultSet.getString("declination");
					Case c = Case.values()[resultSet.getInt("case")];
					Number number = Number.values()[resultSet.getInt("number")];
					Art art = resultSet.getInt("art") != 0 ? Art.Articulat : Art.Nearticulat;
					substantive.set(c, number, art, declination);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return substantives;
	}
	
	String VERB_QUERY=	"select v.verb,v.verb_ascii,c.conj,c.conj_ascii,c.mood,c.tense,c.person,c.number from verbs as v,conjugations as c "
					 +	"where (conj='#VALUE#' or conj_ascii='#VALUE#') and v.id = c.verb_id";
	
	public List<Verb> getVerb(String text){
		ResultSet resultSet = driver.runQuery(VERB_QUERY.replace(TAG_VALUE, text));
		
		List<Verb> verbs = new ArrayList<Verb>();
		
		if(resultSet != null )
		{
			try{
				while(resultSet.next()){
					String verbTxt = resultSet.getString("verb");
					Verb verb = null;
					for(int i = 0; i < verbs.size() && verb == null;i++){
						if(verbTxt.equals(verbs.get(i).m_value)){
							verb = verbs.get(i);
						}
					}
					
					if(verb == null){
						verb = new Verb();
						verb.set(verbTxt);
						verbs.add(verb);
					}
					
					String conjugation = resultSet.getString("conj");
					Tense tense = Tense.values()[resultSet.getInt("tense")];
					Mood mood = Mood.values()[resultSet.getInt("mood")];
					Person person = Person.values()[resultSet.getInt("person")];
					Number number = Number.values()[resultSet.getInt("number")];
					verb.setConjugare(mood, tense, number, person, conjugation);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return verbs;
	}
	
	String ADJECTIVE_QUERY	=	"select a.adjective,a.adjective_ascii,d.declination,d.declination_ascii,d.gender,d.number from adjectives as a,adjective_declination as d "
							+	"where (d.declination='#VALUE#' or d.declination_ascii='#VALUE#') and a.id = d.adjective_id";

	public List<Adjective> getAdjective(String text){
		ResultSet resultSet = driver.runQuery(ADJECTIVE_QUERY.replace(TAG_VALUE, text));
		
		List<Adjective> adjectives = new ArrayList<Adjective>();
		
		if(resultSet != null )
		{
			try{
				while(resultSet.next()){
					String adjectiveTxt = resultSet.getString("adjective");
					Adjective adjective = null;
					for(int i = 0; i < adjectives.size() && adjective == null;i++){
						if(adjectiveTxt.equals(adjectives.get(i).m_value)){
							adjective = adjectives.get(i);
						}
					}
					if(adjective == null){
						adjective = new Adjective();
						adjective.set(adjectiveTxt);
						adjectives.add(adjective);
					}
					
					String declination = resultSet.getString("declination");
					Gender gender = Gender.values()[resultSet.getInt("gender")];
					Number number = Number.values()[resultSet.getInt("number")];
					adjective.set(gender, number, declination);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return adjectives;
	}
	
	final static String ADVERBS_QUERY = "select * from adverbs where adverb='#VALUE#' or adverb_ascii='#VALUE#'"; 
	public List<ParteDeVorbire> getAdverb(String text)
	{
		ResultSet resultSet = driver.runQuery(ADVERBS_QUERY.replace(TAG_VALUE, text));
		
		List<ParteDeVorbire> adverbs = new ArrayList<ParteDeVorbire>();
		if(resultSet != null ){
			try{
				while(resultSet.next()){
					String adverbTxt = resultSet.getString("adverb");
					ParteDeVorbire adverb = null;
					for(int i = 0; i < adverbs.size() && adverb == null;i++){
						if(adverbTxt.equals(adverbs.get(i).m_value)){
							adverb = adverbs.get(i);
						}
					}
					
					if(adverb == null){
						adverb = new ParteDeVorbire(Type.Adverb);
						adverb.set(adverbTxt);
						adverbs.add(adverb);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return adverbs;
	}
	
	final static String PRONOUNS_QUERY = "select * from pronouns where pronoun= '#VALUE#' or pronoun_ascii= '#VALUE#'"; 
	public List<Pronoun> getPronoun(String text)
	{
		ResultSet resultSet = driver.runQuery(PRONOUNS_QUERY.replace(TAG_VALUE, text));
		
		List<Pronoun> pronouns = new ArrayList<Pronoun>();
		if(resultSet != null ){
			try{
				while(resultSet.next()){
					String pronounTxt = resultSet.getString("pronoun");
					
					Pronoun pronoun = null;
					for(int i = 0; i < pronouns.size() && pronoun == null;i++){
						if(pronounTxt.equals(pronouns.get(i).m_value)){
							pronoun = pronouns.get(i);
						}
					}
					
					if(pronoun == null){
						pronoun = new Pronoun();
						pronoun.set(pronounTxt);
						
						//String declination = resultSet.getString("declination");
						pronoun.setPerson(Person.values()[resultSet.getInt("person")]);
						pronoun.setNumber(Number.values()[resultSet.getInt("number")]);
						pronoun.setGender(Gender.values()[resultSet.getInt("gender")]);
						pronoun.setCase(Case.values()[resultSet.getInt("case")]);
						
						pronouns.add(pronoun);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return pronouns;
	}
	
	final static String PREPOSITIONS_QUERY = "select * from prepositions where preposition= '#VALUE#' or preposition_ascii= '#VALUE#'"; 
	public List<ParteDeVorbire> getPreposition(String text)
	{
		ResultSet resultSet = driver.runQuery(PREPOSITIONS_QUERY.replace(TAG_VALUE, text));
		
		List<ParteDeVorbire> prepositions = new ArrayList<ParteDeVorbire>();
		if(resultSet != null ){
			try{
				while(resultSet.next()){
					String prepositionTxt = resultSet.getString("preposition");
					ParteDeVorbire preposition = null;
					for(int i = 0; i < prepositions.size() && preposition == null;i++){
						if(prepositionTxt.equals(prepositions.get(i).m_value)){
							preposition = prepositions.get(i);
						}
					}
					
					if(preposition == null){
						preposition = new ParteDeVorbire(Type.Preposition);
						preposition.set(prepositionTxt);
						prepositions.add(preposition);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return prepositions;
	}
	
	final static String NUMERALS_QUERY = "select * from numerals where numeral='#VALUE#' or numeral_ascii='#VALUE#'"; 
	public List<ParteDeVorbire> getNumeral(String text)
	{
		ResultSet resultSet = driver.runQuery(NUMERALS_QUERY.replace(TAG_VALUE, text));
		
		List<ParteDeVorbire> numerals = new ArrayList<ParteDeVorbire>();
		if(resultSet != null ){
			try{
				while(resultSet.next()){
					String numeralTxt = resultSet.getString("numeral");
					ParteDeVorbire numeral = null;
					for(int i = 0; i < numerals.size() && numeral == null;i++){
						if(numeralTxt.equals(numerals.get(i).m_value)){
							numeral = numerals.get(i);
						}
					}
					
					if(numeral == null){
						numeral = new ParteDeVorbire(Type.Numeral);
						numeral.set(numeralTxt);
						numerals.add(numeral);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return numerals;
	}

	final static String CONJUNCTIONS_QUERY = "select * from conjunctions where conjunction='#VALUE#' or conjunction_ascii='#VALUE#'"; 
	public List<ParteDeVorbire> getConjunction(String text)
	{
		ResultSet resultSet = driver.runQuery(CONJUNCTIONS_QUERY.replace(TAG_VALUE, text));
		
		List<ParteDeVorbire> conjunctions = new ArrayList<ParteDeVorbire>();
		if(resultSet != null ){
			try{
				while(resultSet.next()){
					String conjunctionTxt = resultSet.getString("conjunction");
					ParteDeVorbire conjunction = null;
					for(int i = 0; i < conjunctions.size() && conjunction == null;i++){
						if(conjunctionTxt.equals(conjunctions.get(i).m_value)){
							conjunction = conjunctions.get(i);
						}
					}
					
					if(conjunction == null){
						conjunction = new ParteDeVorbire(Type.Conjunction);
						conjunction.set(conjunctionTxt);
						conjunctions.add(conjunction);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			driver.dropResults();
		}
		return conjunctions;
	}
	
	public void databaseClose()
	{
		driver.closeDatabaseConnection();
	}

	public Word getWord(String word){
		Word w = new Word();
		w.set(word);
		
		List<Substantive> substantives = getSubstantive(word);
		for(int i = 0; i < substantives.size();i++){
			w.addPossibleFunction(substantives.get(i));
		}
		List<Verb> verbs = getVerb(word);
		for(int i = 0; i < verbs.size();i++){
			w.addPossibleFunction(verbs.get(i));
		}
		List<Adjective> adjectives = getAdjective(word);
		for(int i = 0; i < adjectives.size();i++){
			w.addPossibleFunction(adjectives.get(i));
		}
		List<ParteDeVorbire> adverbs = getAdverb(word);
		for(int i = 0; i < adverbs.size();i++){
			w.addPossibleFunction(adverbs.get(i));
		}
		List<Pronoun> pronouns = getPronoun(word);
		for(int i = 0; i < pronouns.size();i++){
			w.addPossibleFunction(pronouns.get(i));
		}
		List<ParteDeVorbire> prepositions = getPreposition(word);
		for(int i = 0; i < prepositions.size();i++){
			w.addPossibleFunction(prepositions.get(i));
		}
		List<ParteDeVorbire> numerals = getNumeral(word);
		for(int i = 0; i < numerals.size();i++){
			w.addPossibleFunction(numerals.get(i));
		}
		List<ParteDeVorbire> conjunctions = getConjunction(word);
		for(int i = 0; i < conjunctions.size();i++){
			w.addPossibleFunction(conjunctions.get(i));
		}
		return w;
	}
}
