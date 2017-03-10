package com.iceq.dictionary.dex;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iceq.dictionary.dex.Word.Type;
import com.iceq.dictionary.sql.SQLiteDriver;

public class DEXProcessor
{
	SQLiteDriver driver;

	public void initialize()
	{
		driver = new SQLiteDriver();
		driver.openDatabaseConnection("data/dex.db",false);
	}

	public void uninitialize()
	{
		driver.closeDatabaseConnection();
	}

	final static String	WORD_DEF_PATTERN		= "[@]([^@]*)[@]";
	final static String	WORD_RELATED_PATTERN	= "[$]([^$]*)[$]";
	final static String	WORD_TYPE_PATTERN		= "[#]([^#]*)[#]";

	//	private Word extractWord( String definition)
	//	{
	//		Pattern r = Pattern.compile(WORD_DEF_PATTERN);
	//		Matcher m = r.matcher(definition);
	//		if (m.find())
	//		{
	//			String text = m.group();
	//			if (!isNumberHighighlight(text))
	//			{
	//				text = text.replace("@", "");
	//				return new Word(0, text);
	//			}
	//		}
	//		return null;
	//	}

	//	private Type extractType( String definition)
	//	{
	//		Pattern r = Pattern.compile(WORD_TYPE_PATTERN);
	//		Matcher m = r.matcher(definition);
	//		if (m.find())
	//		{
	//			String w = m.group().replace("#", "");
	//			return getType(w);
	//		} else
	//		{
	//
	//		}
	//
	//		return Type.Unknown;
	//	}

	//	private void parseDef( String definition)
	//	{
	//		String def = definition;
	//
	//		Word w = extractWord(def);
	//		if (w != null)
	//		{
	//			w.setType(extractType(def));
	//			if (w.getTypes().size() == 0)
	//			{
	//				System.out.println(definition);
	//
	//				//System.out.println("word:"+w);
	//			}
	//		} else
	//			System.out.println("Can't parse definition:" + def);
	//	}

	private String extractW( String definition, Word word)
	{
		int indexArond = definition.indexOf('@', 1);
		if (indexArond > 0)
		{
			String w = definition.substring(0, indexArond + 1);
			w = w.replace("@", "");
			w = w.replace(",", "");
			word.text = w;
			return definition.substring(indexArond + 1);
		}
		return definition;
	}

	private String extractAlternate( String definition, Word word)
	{
		int indexArond = definition.indexOf(definition.charAt(0), 1);
		if (indexArond > 0)
		{
			String w = definition.substring(0, indexArond + 1);
			w = w.replace("" + definition.charAt(0), "");
			w = w.replace(",", "");
			word.addRelatedWords(w);
			return definition.substring(indexArond + 1);
		}
		return definition;
	}

	private static String[]	TYPE_TAGS	= new String[] { "num. col.", "verb.", "num. card.", "num. ord.", "loc. adj.", "conj.", "loc. adv.", "art.", "subst.", "prep.", "pron.", "interj.", "s. f.", "s. m.", "s. n.", "s. v.", "adj.", "adv.",
			"vb.", "s.", "m.", "n.", "f.", "v.", "a." };
	private static Type[]	TYPES		= new Type[] { Type.Numeral, Type.Verb, Type.Numeral, Type.Numeral, Type.Adjectiv, Type.Conjunctie, Type.Adverb, Type.Articol, Type.Substantiv, Type.Prepozitie, Type.Pronume, Type.Interjectie,
			Type.Substantiv, Type.Substantiv, Type.Substantiv, Type.Substantiv, Type.Adjectiv, Type.Adverb, Type.Verb, Type.Substantiv, Type.Substantiv, Type.Substantiv, Type.Substantiv, Type.Verb, Type.Adjectiv };

	private int getTypeIndex( String definition)
	{
		for (int i = 0; i < TYPE_TAGS.length; i++)
		{
			String x = definition.toLowerCase();
			if (x.startsWith(TYPE_TAGS[i]))
			{
				return i;
			}
		}
		return -1;
	}

	private String extractTypeW( String definition, Word word)
	{
		String typeStr = definition;
		int endIndex = 0;

		if (definition.startsWith("#"))
		{
			endIndex = definition.indexOf('#', 1) + 1;
			if (endIndex > 1)
			{
				typeStr = definition.substring(1, endIndex);
			}
		} else if (definition.startsWith("$"))
		{
			endIndex = definition.indexOf('$', 1) + 1;
			if (endIndex > 1)
			{
				typeStr = definition.substring(1, endIndex);
			}
		}

		int typeIndex = getTypeIndex(typeStr);
		if (typeIndex >= 0)
		{
			if (endIndex == 0)
				endIndex = TYPE_TAGS[typeIndex].length();

			word.useType = TYPES[typeIndex];
			return definition.substring(endIndex);
		}

		return definition;
	}

	private String removeNumberHighlight( String definition)
	{
		Pattern r = Pattern.compile("[(][@][\\d\\.\\,\\sI\\-VraA@]*[@][)]");//"[@](\\d\\.*)[@]");
		Matcher m = r.matcher(definition);
		while (m.find())
		{
			String text = m.group();
			definition = definition.replace(text, "");
		}
		r = Pattern.compile("[@]([\\dI\\.\\,]*)[@]");
		m = r.matcher(definition);
		while (m.find())
		{
			String text = m.group();
			definition = definition.replace(text, "");
		}
		return definition;
	}

	//HashMap<Integer, Word>	words	= new HashMap<Integer, Word>();
	private Word parseDefinition( InputWordDefinition definition)
	{
		String tempDefinition = definition.definition;

		tempDefinition = removeNumberHighlight(tempDefinition);

		tempDefinition = tempDefinition.replace("$#", "$ #");
		tempDefinition = tempDefinition.replace("$@ @", "");
		tempDefinition = tempDefinition.replace("@ @", " ");
		tempDefinition = tempDefinition.replace("@,", "@");
		tempDefinition = tempDefinition.replace("#pers.# 3", "");
		tempDefinition = tempDefinition.replace("#pers.#3", "");
		tempDefinition = tempDefinition.replace("pers. 3", "");
		tempDefinition = tempDefinition.replace("pers.3", "");
		tempDefinition = tempDefinition.replace("#pers.# 4", "");
		tempDefinition = tempDefinition.replace("pers. 4", "");
		tempDefinition = tempDefinition.replace("#pl.#", "");
		tempDefinition = tempDefinition.replace("(#Mold.#)", "");
		tempDefinition = tempDefinition.replace("(dans)", "");
		tempDefinition = tempDefinition.replace("(arbore)", "");
		tempDefinition = tempDefinition.replace("(plantă)", "");
		tempDefinition = tempDefinition.replace("(așezare)", "");
		tempDefinition = tempDefinition.replace("(amanet)", "");

		tempDefinition = tempDefinition.replace("(@#)", "#");
		tempDefinition = tempDefinition.replace("(rar)", "");
		tempDefinition = tempDefinition.replace("(, rar)", "");
		tempDefinition = tempDefinition.replace("( rar)", "");
		tempDefinition = tempDefinition.replace("(rar, )", "");
		tempDefinition = tempDefinition.replace("(zool.)", "");
		tempDefinition = tempDefinition.replace("(#pop.#)", "");
		tempDefinition = tempDefinition.replace("$ și", "$");

		//parseDef(definition);

		Word word = null;// words.get(lexemId);
		if (word == null)
		{
			word = new Word(definition.dbLexemId, definition.dbId);
			//words.put(lexemId, word);
		}

		tempDefinition = extractW(tempDefinition, word);

		while (tempDefinition.startsWith(" ") || tempDefinition.startsWith(",") || tempDefinition.startsWith("~"))
		{
			tempDefinition = tempDefinition.substring(1);
		}

		if (tempDefinition.startsWith("@"))
		{
			tempDefinition = extractAlternate(tempDefinition, word);
		}

		while (tempDefinition.startsWith(" ") || tempDefinition.startsWith(",") || tempDefinition.startsWith("~"))
		{
			tempDefinition = tempDefinition.substring(1);
		}

		boolean typeExtracted = false;
		int loopCount = 0;
		while (tempDefinition.startsWith("$") && loopCount < 7)
		{
			int typeIndex = getTypeIndex(tempDefinition.substring(1));
			if (!typeExtracted && typeIndex >= 0)
			{
				tempDefinition = extractTypeW(tempDefinition, word);
				typeExtracted = true;
			} else
				tempDefinition = extractAlternate(tempDefinition, word);

			while (tempDefinition.startsWith(" ") || tempDefinition.startsWith(",") || tempDefinition.startsWith("~"))
			{
				tempDefinition = tempDefinition.substring(1);
			}
			loopCount++;
		}

		if (!typeExtracted)
			tempDefinition = extractTypeW(tempDefinition, word);

		word.definition = tempDefinition;
		//if (word.getTypes().size() == 0)
		{
			//			System.out.println("==>" + word.toString());
			//			System.out.println(tempDefinition);
			//			System.out.println(definition);
			//			System.out.println();
			//xCount++;
		}

		/*String newDef = "";
		String def = definition.substring(1);
		int indexVirgula = def.indexOf(',');
		int indexArond = def.indexOf('@');
		
		if(indexVirgula > 0 && indexVirgula < indexArond)
		{
			while(indexVirgula > 0 && indexVirgula < indexArond)
			{
				newDef += "[" + def.substring(0,indexVirgula) + "]";
				def = def.substring(indexVirgula + 1);
				indexVirgula = def.indexOf(',');
			}
		}else if(indexArond > 0){
			newDef += "[" + def.substring(0,indexArond) + "]";
			def = def.substring(indexArond + 1);
		}
		System.out.println(newDef+"==>"+def);
		System.out.println(definition);*/
		return word;
	}

	final static String KEYWORD_TAG = "#KEY_WORD#";
	
	String DEFINITION_QUERY = "select definitions.id as defId,lexems.id as lexId,lexems.word as lex,definitions.definition as def " 
							+ "from lexems,definitions " + "where lexems.id = definitions.lex_id "
							+ "and lexems.word like '"+KEYWORD_TAG+"' order by lexems.word asc";

	public InputWordDefinition getWordDefinition(ResultSet resultSet)
	{
		InputWordDefinition word = null;
		try
		{
			if (resultSet != null && resultSet.next())
			{
				word = new InputWordDefinition();
				String definitionId = resultSet.getString("defId");
				word.dbId = Integer.parseInt(definitionId);
				String lexemId = resultSet.getString("lexId");
				word.dbLexemId = Integer.parseInt(lexemId);
				word.lexem = resultSet.getString("lex");
				word.definition = resultSet.getString("def");
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return word;
	}
	
	public void doTheDo()
	{
		initialize();
		String keyword = "%";
		ResultSet result = driver.runQuery(DEFINITION_QUERY.replace(KEYWORD_TAG, keyword));
		if (result != null)
		{
			InputWordDefinition definition = getWordDefinition(result);
			while (definition != null)
			{
				Word word = parseDefinition(definition);

				System.out.println("Word: " + word);
				
				/*driver.insertWord(word);
				xCount++;
				if (xCount > 1000)
				{
					driver.commit();
					xCount = 0;
				}*/
				definition = getWordDefinition(result);
			}
			driver.dropResults();
			//driver.commit();
		}
		uninitialize();
	}
	
	String DICTIONARY_QUERY	= "select lexems.word as lex,dictionary.word as word,dictionary.type as type "
							+ "from lexems,dictionary where dictionary.word like '" + KEYWORD_TAG 
							+ "' and lexems.id = dictionary.lexem_id";
	
	private DictionaryWord getDictionaryWord(ResultSet resultSet)
	{
		DictionaryWord result = null;
		try
		{
			if (resultSet != null && resultSet.next())
			{
				result = new DictionaryWord();
				
				result.lexem = resultSet.getString("lex");
				result.text = resultSet.getString("word");
				String typeStr = resultSet.getString("type");
				int tId = Integer.parseInt(typeStr);
				Word.Type t = Word.Type.values()[tId];
				result.addType(t);
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}
	
	public DictionaryWord getWord(String text){
		DictionaryWord result = null;
		ResultSet resultSet = driver.runQuery(DICTIONARY_QUERY.replace(KEYWORD_TAG, text));
		
		if(resultSet != null)
		{
			DictionaryWord word = getDictionaryWord(resultSet);
			while (word != null)
			{
				if(result == null){
					result = word;
				}else{
					result.addType(word.types.get(0));
				}
				word = getDictionaryWord(resultSet);
			}
			driver.dropResults();
		}
		return result;
	}
	
	public void analyse(String text){
		initialize();
		String[] words = text.split("\\s");
		for(int i = 0; i < words.length;i++)
		{
			DictionaryWord word = getWord(words[i]);
			if(word != null)
				System.out.println(word.toString());
			else
				System.out.println("Can't find "+words[i]);
		}
		uninitialize();
	}

	public static void main( String args[])
	{
		DEXProcessor processor = new DEXProcessor();
		//processor.doTheDo();
		processor.analyse("copil");
	}
}
