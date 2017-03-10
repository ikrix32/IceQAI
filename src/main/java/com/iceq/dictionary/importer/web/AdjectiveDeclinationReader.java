package com.iceq.dictionary.importer.web;

import java.net.URLEncoder;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;

import com.iceq.dictionary.Adjective;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Grammar.Number;

public class AdjectiveDeclinationReader extends HtmlParser
{
	private String BASE_URL = "https://ro.wiktionary.org/wiki/";

	protected String processHtmlContent( String html)
	{
		//html = html.replace("\n", "");
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

		tt = new TagTransformation("h3");
		transformations.addTransformation(tt);

		tt = new TagTransformation("h4");
		transformations.addTransformation(tt);

		tt = new TagTransformation("meta");
		transformations.addTransformation(tt);

		tt = new TagTransformation("link");
		transformations.addTransformation(tt);

		tt = new TagTransformation("noscript");
		transformations.addTransformation(tt);

		tt = new TagTransformation("sc");
		transformations.addTransformation(tt);

		tt = new TagTransformation("span");
		transformations.addTransformation(tt);

		tt = new TagTransformation("td");
		transformations.addTransformation(tt);

		tt = new TagTransformation("font");
		transformations.addTransformation(tt);

		tt = new TagTransformation("table");
		transformations.addTransformation(tt);

		tt = new TagTransformation("tbody");
		transformations.addTransformation(tt);

		tt = new TagTransformation("tr");
		transformations.addTransformation(tt);
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

		tt = new TagTransformation("b");
		transformations.addTransformation(tt);

		tt = new TagTransformation("p");
		transformations.addTransformation(tt);

		tt = new TagTransformation("i");
		transformations.addTransformation(tt);

		tt = new TagTransformation("br");
		transformations.addTransformation(tt);

		tt = new TagTransformation("li");
		transformations.addTransformation(tt);

		tt = new TagTransformation("ul");
		transformations.addTransformation(tt);

		tt = new TagTransformation("ol");
		transformations.addTransformation(tt);

		tt = new TagTransformation("div");
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
			//System.out.println("Content:" + content);
			if (!content.isEmpty()) processContent(content);
		}

		// logNode(htmlNode);

		return true;
	}

	final static String[] TAG_GENDER = new String[] {
			"Masculin", "Feminin","Neutru"
	};

	private enum State
	{
		NotStarted,
		Processing,
		Done,
	}

	int		m_gender	= -1;
	int		m_number	= -1;
	State	m_state		= State.NotStarted;

	protected void processContent( String content)
	{
		if (m_state == State.NotStarted)
		{
			if (content.contains("Declinarea adjectivului"))
			{
				m_state = State.Processing;
			}
		} else if (m_state == State.Processing)
		{
			if (m_gender < 0)
			{
				m_gender = getGender(content);
			} else
			{
				if (processGender(m_gender, content))
				{
					if (m_gender == 2) 
						m_state = State.Done;
					m_gender = -1;
				}
			}
		}
	}

	protected int getGender( String content)
	{
		for (int i = 0; i < TAG_GENDER.length; i++)
		{
			if (TAG_GENDER[i].equals(content)) return i;
		}
		return -1;
	}

	protected boolean processGender( int gender, String content)
	{
		m_number++;

		if (content.length() > 1)
			setAdjectiveDeclination(gender, m_number, content);
		// System.out.println("->[" + TAG_CAZURI[caz] + "] [" + (m_number == 0 ?
		// "Singular" : "Plural") + "] [" + content + "]");

		if (m_number == 1)
		{
			m_number = -1;
			return true;
		}
		return false;
	}

	Adjective adj = null;

	protected void setAdjectiveDeclination( int gender, int number, String s)
	{
		
		Gender g = Gender.values()[gender];
		Number n = Number.values()[number + 1];
		s = replaceInvariable(s, adj, g,n );
	
		if (adj == null){
			adj = new Adjective();
			adj.set(s);
		}
		
		adj.set(g, n, s);
	}

	private String replaceInvariable( String s, Adjective ajective, Gender gender, Number n)
	{
		String sLowerCase = s.toLowerCase();
		if (n == Number.Plural && sLowerCase.equals("invariabil") && ajective != null)
		{
			String a = ajective.get(gender, Number.Singular);
			if (a != null && !a.contains("invariabil")) { 
				return a; 
			}
		}
		return s;
	}

	public Adjective getAdjective( String s) throws Exception
	{
		m_state = State.NotStarted;

		parse(BASE_URL + URLEncoder.encode(s, "UTF-8"));

		Adjective ad = adj;
		adj = null;

		return ad;
	}

	public static void main( String[] args)
	{
		AdjectiveDeclinationReader reader = new AdjectiveDeclinationReader();

		try
		{
			Adjective ad = reader.getAdjective("același");
			System.out.println(ad.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
