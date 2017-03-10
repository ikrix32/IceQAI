package com.iceq.dictionary.importer.web;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;

import com.iceq.dictionary.Grammar.Mood;
import com.iceq.dictionary.Grammar.Tense;
import com.iceq.dictionary.Grammar.Number;
import com.iceq.dictionary.Grammar.Person;
import com.iceq.dictionary.Verb;
import com.iceq.dictionary.importer.DictionaryDatabaseDriver;

public class VerbConjugationReaderConjugareRo extends HtmlParser
{
	private String		BASE_URL					= "http://conjugare.ro/romana/conjugarea-verbului-";

	private String[]	MODE_TAGS					= { "Infinitiv", "Infinitiv Lung", "Participiu", "Gerunziu", "Imperativ persoana a doua", };

	private Mood[]		MOOD_TAG_ID_TO_MOOD			= new Mood[] { Mood.Infinitiv, Mood.InfinitivLung, Mood.Participiu, Mood.Gerunziu, Mood.Imperativ };

	private String[]	NUMBER_TAGS					= { "singular", "plural", };

	private String[]	MOOD_TENSE_TAGS				= { "Prezent", "Imperfect", "Perfect Simplu", "Perfectul Compus", "Mai Mult Ca Perfectul", "Viitor", "Viitor Anterior", "Conjunctiv Prezent", "Conjunctiv Perfect", "Condițional Prezent",
			"Condițional Perfect", };

	private Tense[]		MOOD_TENSE_TAG_ID_TO_TENSE	= new Tense[] { Tense.Prezent, Tense.Imperfect, Tense.PerfectSimplu, Tense.PerfectCompus, Tense.MaiMultCaPerfectul, Tense.Viitor, Tense.ViitorAnterior, Tense.Prezent, Tense.Perfect,
			Tense.Prezent, Tense.Perfect, };

	private Mood[]		MOOD_TENSE_TAG_ID_TO_MOOD	= new Mood[] { Mood.Indicativ, Mood.Indicativ, Mood.Indicativ, Mood.Indicativ, Mood.Indicativ, Mood.Indicativ, Mood.Indicativ, Mood.Conjunctiv, Mood.Conjunctiv, Mood.Conditional,
			Mood.Conditional, };

	private String[]	PERSON_TAGS					= { "eu", "tu", "el/ea", "noi", "voi", "ei/ele" };

	// private String TAG_NOT_FOUND = "Verbul nu a fost găsit.";

	protected String processHtmlContent( String html)
	{
		// html = html.replace("\n", "");
		// html= html.replace(" ", "");
		return html;
	}

	protected void setupCleanProperties( CleanerProperties properties)
	{
		CleanerTransformations transformations = new CleanerTransformations();

		TagTransformation tt = new TagTransformation("head");
		transformations.addTransformation(tt);

		tt = new TagTransformation("title");
		transformations.addTransformation(tt);

		tt = new TagTransformation("h1");
		transformations.addTransformation(tt);

		tt = new TagTransformation("h2");
		transformations.addTransformation(tt);

		tt = new TagTransformation("meta");
		transformations.addTransformation(tt);

		tt = new TagTransformation("link");
		transformations.addTransformation(tt);

		// tt = new TagTransformation("noscript");
		// transformations.addTransformation(tt);

		tt = new TagTransformation("sc");
		transformations.addTransformation(tt);

		// tt = new TagTransformation("style");
		// transformations.addTransformation(tt);

		// tt = new TagTransformation("div");
		// tt.addAttributeTransformation("align");
		// tt.addAttributeTransformation("style");
		// tt.addAttributeTransformation("id");
		// transformations.addTransformation(tt);

		tt = new TagTransformation("form");
		transformations.addTransformation(tt);

		tt = new TagTransformation("option");
		transformations.addTransformation(tt);

		tt = new TagTransformation("input");
		transformations.addTransformation(tt);

		tt = new TagTransformation("a");
		transformations.addTransformation(tt);

		tt = new TagTransformation("img");
		transformations.addTransformation(tt);

		properties.setCleanerTransformations(transformations);
		properties.setOmitComments(true);
		properties.setOmitCdataOutsideScriptAndStyle(true);
		properties.setOmitDoctypeDeclaration(true);
		properties.setOmitXmlDeclaration(true);
		properties.setPruneTags("script,style");
	}

	protected boolean visitNode( HtmlNode htmlNode, TagNode tagNode)
	{
		if (htmlNode instanceof ContentNode)
		{
			String content = ((ContentNode) htmlNode).getContent();
			content = trimNewLineAndSpaces(content);

			boolean emptyTag = content.replace(" ", "").equals("-");
			if (!content.isEmpty() && !emptyTag)
				processContent(content);
		}
		// logNode(htmlNode);

		return true;
	}

	private enum State
	{
		NotStarted,
		Processing,
		Done,
	}

	State	m_state		= State.NotStarted;

	Mood	crtMode		= Mood.None;
	Number	crtNumber	= Number.None;

	Tense	crtTense	= Tense.None;
	Person	crtPerson	= Person.None;

	String	prep		= "";
	Verb	verb		= null;

	protected void processContent( String content)
	{
		if (m_state != State.Processing)
			return;

		if (updateMoodAndTense(content))
		{
			resetPersonAndNumber();
		}else if( crtMode != Mood.None && crtTense != Tense.None)
		{
			processMoodAndTense(content);
		}
	}

	protected void processMoodAndTense( String content)
	{
		switch (crtMode)
		{
			case Infinitiv:
			case InfinitivLung:
			case Participiu:
			case Gerunziu:
			{
				if (verb == null)
					verb = new Verb();

				verb.setConjugare(crtMode, Tense.Prezent, Number.None, Person.None, content.trim());
				if (crtMode == Mood.Infinitiv){
					verb.set(content.trim());
					verb.setConjugare(crtMode, Tense.Perfect, Number.None, Person.None, "fi " + content.trim());
				}
			}
			break;
			default:
			{
				if (!updatePersonAndNumber(content) && crtNumber != Number.None && crtPerson != Person.None)
				{
					if (verb == null)
						verb = new Verb();
					
					String c = prep + content;
					verb.setConjugare(crtMode, crtTense, crtNumber, crtPerson, c.trim());
					prep = "";
				}
			}
			break;
		}
	}

	protected boolean updateMoodAndTense( String content)
	{
		for (int i = 0; i < MODE_TAGS.length; i++)
		{
			if (MODE_TAGS[i].equals(content))
			{
				crtMode = MOOD_TAG_ID_TO_MOOD[i];
				crtTense = Tense.Prezent;
				
				return true;
			}
		}

		for (int i = 0; i < MOOD_TENSE_TAGS.length; i++)
		{
			if (MOOD_TENSE_TAGS[i].equals(content))
			{
				crtMode = MOOD_TENSE_TAG_ID_TO_MOOD[i];
				crtTense = MOOD_TENSE_TAG_ID_TO_TENSE[i];
				return true;
			}
		}
		return false;
	}

	protected boolean updatePersonAndNumber( String content)
	{
		for (int i = 0; i < NUMBER_TAGS.length; i++)
		{
			if (NUMBER_TAGS[i].equals(content))
			{
				crtPerson = Person.Doua;
				crtNumber = Number.values()[1 + i];
				prep = "";
				return true;
			}
		}

		for (int i = 0; i < PERSON_TAGS.length; i++)
		{
			if (content.startsWith(PERSON_TAGS[i]))
			{
				crtNumber = Number.values()[1 + i / 3];
				crtPerson = Person.values()[1 + i % 3];

				prep = content.replace(PERSON_TAGS[i], "");
				return true;
			}
		}
		return false;
	}
	
	private void resetMoodAndTense(){
		crtMode = Mood.None;
		crtTense= Tense.None;
	}
	
	private void resetPersonAndNumber(){
			crtNumber= Number.None;
			crtPerson = Person.None;
	}

	public Verb getVerbConjugation( String s)
	{
		m_state = State.Processing;
		resetMoodAndTense();
		resetPersonAndNumber();
		
		try
		{
			parse(BASE_URL + s);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		Verb v = verb;
		verb = null;

		return v;
	}

	public static void main( String[] args)
	{
		VerbConjugationReaderConjugareRo c = new VerbConjugationReaderConjugareRo();
		DictionaryDatabaseDriver driver = new DictionaryDatabaseDriver();
		try
		{
			driver.databaseOpen();
			String text = "abunda";
			Verb v = c.getVerbConjugation(text);
			if (v != null)
			{
				System.out.println("" + v);
				// driver.insertVerb(v);
			} else
			{
				System.out.println("Verbul nu a fost gasit.");
			}
			driver.databaseClose();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
