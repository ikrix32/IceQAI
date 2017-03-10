package com.iceq.dictionary.importer;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

import com.iceq.dictionary.Grammar.Number;
import com.iceq.dictionary.Grammar.Person;
import com.iceq.dictionary.Grammar.Tense;
import com.iceq.dictionary.sql.SQLiteDriver;
import com.iceq.dictionary.Adjective;
import com.iceq.dictionary.Grammar.Art;
import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Mood;
import com.iceq.dictionary.Substantive;
import com.iceq.dictionary.Verb;

public class DictionaryDatabaseDriver
{
	final static String	databaseName	= "data/words.db";

	SQLiteDriver		driver			= new SQLiteDriver();

	public void databaseOpen()
	{
		driver.openDatabaseConnection(databaseName, false);
	}

	public void databaseClose()
	{
		driver.closeDatabaseConnection();
	}

	public void databaseCommit()
	{
		driver.commit();
	}

	final static String	TAG_ID								= "#ID#";
	final static String	TAG_VALUE							= "#VALUE#";
	final static String	TAG_ASCII_VALUE						= "#VALUE_ASCII#";

	final static String	TAG_MOOD							= "#MOOD#";
	final static String	TAG_TENSE							= "#TENSE#";
	final static String	TAG_NUMBER							= "#NUMBER#";
	final static String	TAG_GENDER							= "#GENDER#";
	final static String	TAG_PERSON							= "#PERSON#";
	final static String	TAG_CASE							= "#CASE#";
	final static String	TAG_ART								= "#ART#";
	
	final static String	NUMERAL_INSERT_QUERY				= "insert into numerals(numeral,numeral_ascii)" 
															+ " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "');";
	
	final static String	CONJUNCTION_INSERT_QUERY			= "insert into conjunctions(conjunction,conjunction_ascii)" 
															+ " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "');";
	
	final static String	PRONOUN_INSERT_QUERY				= "insert into pronouns(pronoun,pronoun_ascii)" 
															+ " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "');";
	
	final static String	ADVERB_INSERT_QUERY					= "insert into adverbs(adverb,adverb_ascii)" 
															+ " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "');";

	
	final static String	ADJECTIVE_INSERT_QUERY				= "insert into adjectives(adjective,adjective_ascii)" 
															+ " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "');";
	final static String	ADJECTIVE_CONJUGATION_INSERT_QUERY	= "insert into "
															+ "adjective_declination(adjective_id,declination,declination_ascii,gender,number)" 
															+ " values(" + TAG_ID + ",'" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "'," + TAG_GENDER + "," + TAG_NUMBER + ");";

	final static String	VERB_INSERT_QUERY					= "insert into verbs(verb,verb_ascii)" + " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "');";
	final static String	VERB_CONJUGATION_INSERT_QUERY		= "insert into conjugations(verb_id,conj,conj_ascii,mood,tense,number,person)" + " values(" + TAG_ID + ",'" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "'," + TAG_MOOD + ","
			+ TAG_TENSE + "," + TAG_NUMBER + "," + TAG_PERSON + ");";

	final static String	SUBSTANTIVE_INSERT_QUERY			= "insert into substantives( substantive, substantive_ascii, gender)" + " values('" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "','" + TAG_GENDER + "');";
	final static String	SUBST_DECLINATION_INSERT_QUERY		= "insert into subst_declinations(substantive_id,declination,declination_ascii,'case',number,art)" + " values(" + TAG_ID + ",'" + TAG_VALUE + "','" + TAG_ASCII_VALUE + "',"
			+ TAG_CASE + "," + TAG_NUMBER + "," + TAG_ART + ");";
	
	final static String	SUBSTANTIVE_UPDATE_QUERY			= "update substantives set gender="+TAG_GENDER+" where substantive='"+TAG_VALUE+"';";
	
	public void insertNumeral( String numeral)
	{
		try
		{
			
			if (numeral == null)
			{
				System.out.println("Invalid numeral:" + numeral);
				return;
			}

			String query = NUMERAL_INSERT_QUERY.replace(TAG_VALUE, numeral);
			query = query.replace(TAG_ASCII_VALUE, toAscii(numeral));
			driver.runInsertOrUpdate(query);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertConjunction( String conjunction)
	{
		try
		{
			
			if (conjunction == null)
			{
				System.out.println("Invalid pronoun:" + conjunction);
				return;
			}

			String query = CONJUNCTION_INSERT_QUERY.replace(TAG_VALUE, conjunction);
			query = query.replace(TAG_ASCII_VALUE, toAscii(conjunction));
			driver.runInsertOrUpdate(query);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertPronoun( String pronoun)
	{
		try
		{
			
			if (pronoun == null)
			{
				System.out.println("Invalid pronoun:" + pronoun);
				return;
			}

			String query = PRONOUN_INSERT_QUERY.replace(TAG_VALUE, pronoun);
			query = query.replace(TAG_ASCII_VALUE, toAscii(pronoun));
			driver.runInsertOrUpdate(query);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertAdverb( String adverb)
	{
		try
		{
			if (adverb == null)
			{
				System.out.println("Invalid adverb:" + adverb);
				return;
			}

			String query = ADVERB_INSERT_QUERY.replace(TAG_VALUE, adverb);
			query = query.replace(TAG_ASCII_VALUE, toAscii(adverb));
			driver.runInsertOrUpdate(query);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertAdjective( Adjective adj)
	{
		try
		{
			String adjective = adj.get(Gender.Masculin, Number.Singular);

			if (adjective == null)
			{
				System.out.println("Invalid adjective:" + adjective);
				return;
			}

			String query = ADJECTIVE_INSERT_QUERY.replace(TAG_VALUE, adjective);
			query = query.replace(TAG_ASCII_VALUE, toAscii(adjective));
			int id = driver.runInsertOrUpdate(query);

			for (int gender = 0; gender < Gender.values().length; gender++)
			{
				for (int number = 0; number < Number.values().length; number++)
				{
					String declination = adj.get(Gender.values()[gender], Number.values()[number]);
					if (declination != null)
					{
						String sql = ADJECTIVE_CONJUGATION_INSERT_QUERY.replace(TAG_ID, "" + id);
						sql = sql.replace(TAG_VALUE, declination);
						sql = sql.replace(TAG_ASCII_VALUE, toAscii(declination));
						sql = sql.replace(TAG_GENDER, "" + gender);
						sql = sql.replace(TAG_NUMBER, "" + number);

						driver.runInsertOrUpdate(sql);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void insertVerb( Verb v)
	{
		try
		{
			String verb = v.m_value;

			if (verb == null)
			{
				System.out.println("Verb ");
			}
			String query = VERB_INSERT_QUERY.replace(TAG_VALUE, verb);
			query = query.replace(TAG_ASCII_VALUE, toAscii(verb));
			int id = driver.runInsertOrUpdate(query);

			for (int mood = 0; mood < Mood.values().length; mood++)
			{
				for (int tense = 0; tense < Tense.values().length; tense++)
				{
					for (int number = 0; number < Number.values().length; number++)
					{
						for (int person = 0; person < Person.values().length; person++)
						{
							if (v.m_conjugations[mood][tense][number][person] != null)
							{
								String text = v.m_conjugations[mood][tense][number][person].m_value;
								String sql = VERB_CONJUGATION_INSERT_QUERY.replace(TAG_ID, "" + id);
								sql = sql.replace(TAG_VALUE, text);
								sql = sql.replace(TAG_ASCII_VALUE, toAscii(text));
								sql = sql.replace(TAG_MOOD, "" + mood);
								sql = sql.replace(TAG_TENSE, "" + tense);
								sql = sql.replace(TAG_NUMBER, "" + number);
								sql = sql.replace(TAG_PERSON, "" + person);

								driver.runInsertOrUpdate(sql);
							}
						}
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void insertSubstantive( Substantive s)
	{
		try
		{
			String substantive = s.get(Case.Nominativ, Number.Singular, Art.Nearticulat);

			if (substantive == null)
			{
				System.out.println("Invalid subst:" + s);
				return;
			}

			String query = SUBSTANTIVE_INSERT_QUERY.replace(TAG_VALUE, substantive);
			query = query.replace(TAG_ASCII_VALUE, toAscii(substantive));
			query = query.replace(TAG_ASCII_VALUE, "" + s.getGender().ordinal());
			int id = driver.runInsertOrUpdate(query);

			for (int caz = 0; caz < Case.values().length; caz++)
			{
				for (int number = 0; number < Number.values().length; number++)
				{
					for (int art = 0; art < Art.values().length; art++)
					{
						String subst = s.get(Case.values()[caz], Number.values()[number], Art.values()[art]);
						if (subst != null)
						{
							String sql = SUBST_DECLINATION_INSERT_QUERY.replace(TAG_ID, "" + id);
							sql = sql.replace(TAG_VALUE, subst);
							sql = sql.replace(TAG_ASCII_VALUE, toAscii(subst));
							sql = sql.replace(TAG_CASE, "" + caz);
							sql = sql.replace(TAG_NUMBER, "" + number);
							sql = sql.replace(TAG_ART, "" + art);

							driver.runInsertOrUpdate(sql);
						}
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateSubstantive( Substantive s)
	{
		try
		{
			String substantive = s.get(Case.Nominativ, Number.Singular, Art.Nearticulat);
			if (substantive == null){
				System.out.println("Invalid subst:" + s);
				return;
			}
			String updateQuery = SUBSTANTIVE_UPDATE_QUERY.replace(TAG_VALUE, substantive);
			updateQuery = updateQuery.replace(TAG_GENDER, "" + s.getGender().ordinal());
			driver.runInsertOrUpdate(updateQuery);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String toAscii( String s)
	{
		String s1 = Normalizer.normalize(s, Normalizer.Form.NFKD);
		String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

		String s2 = s;
		try
		{
			s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"), "ascii");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return s2;
	}

	public static void main( String[] args)
	{
		DictionaryDatabaseDriver driver = new DictionaryDatabaseDriver();
		driver.databaseOpen();
		//Verb v = new Verb();
		//v.setConjugare(Mood.Infinitiv, Tense.Prezent, Number.None, Person.None, "mânca");
		//driver.insertVerb(v);
		Substantive subst = new Substantive();
		subst.set(Case.Nominativ, Number.Singular, Art.Nearticulat, "mâncare");
		driver.insertSubstantive(subst);
		driver.databaseCommit();
		driver.databaseClose();
	}
}
