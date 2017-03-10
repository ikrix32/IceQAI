package com.iceq.dictionary.importer.web;

import java.net.URLEncoder;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;

import com.iceq.dictionary.Grammar.Number;
import com.iceq.dictionary.Grammar.Art;
import com.iceq.dictionary.Grammar.Case;
import com.iceq.dictionary.Grammar.Gender;
import com.iceq.dictionary.Substantive;

public class SubstantiveDeclinationReader extends HtmlParser
{
	private String BASE_URL = "https://ro.wiktionary.org/wiki/";

	protected String processHtmlContent( String html)
	{
		//html = html.replace("\n", "");
		//html= html.replace(" ", "");
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
		
		//tt = new TagTransformation("div");
		//tt.addAttributeTransformation("align");
		//tt.addAttributeTransformation("style");
		//tt.addAttributeTransformation("id");
		//transformations.addTransformation(tt);

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
			//System.out.println("Content: "+content);
			
			if (!content.isEmpty())
				processContent(content);
		}

		// logNode(htmlNode);

		return true;
	}

	final static String[] TAG_CAZURI = new String[] { "Nominativ-Acuzativ", "Articulat", "Dativ-Genitiv", "Vocativ" };

	private enum State
	{
		NotStarted,
		Processing,
		Done,
	}

	int		m_caz		= -1;
	int		m_number	= -1;
	State	m_state		= State.NotStarted;

	boolean hasGender = false;
	protected void processContent( String content)
	{
		if (m_state == State.NotStarted)
		{
			if (content.contains("Declinarea substantivului"))
			{
				m_state = State.Processing;
			}
		} else if (m_state == State.Processing)
		{
			if(!hasGender){
				if(content.startsWith("m.") || content.startsWith("M.")){
					hasGender = true;
					subst.setGender(Gender.Masculin);
				}else if(content.startsWith("f.") || content.startsWith("F.")){
					hasGender = true;
					subst.setGender(Gender.Feminin);
				}else if(content.startsWith("n.") || content.startsWith("N.")  || content.equalsIgnoreCase("Singular")){
					hasGender = true;
					subst.setGender(Gender.Neutru);
				}
			}else if (m_caz < 0)
			{
				m_caz = getCaz(content);
			} else
			{
				if (processCaz(m_caz, content))
				{
					if (m_caz == 3)
						m_state = State.Done;
					m_caz = -1;
				}
			}
		}
	}

	protected int getCaz( String content)
	{
		for (int i = 0; i < TAG_CAZURI.length; i++)
		{
			if (TAG_CAZURI[i].equals(content))
				return i;
		}
		return -1;
	}

	protected boolean processCaz( int caz, String content)
	{
		m_number++;

		if (content.length() > 1)
			setSubstaniveDeclination(caz, m_number, content);
		// System.out.println("->[" + TAG_CAZURI[caz] + "] [" + (m_number == 0 ?
		// "Singular" : "Plural") + "] [" + content + "]");

		if (m_number == 1)
		{
			m_number = -1;
			return true;
		}
		return false;
	}

	Substantive subst = null;

	protected void setSubstaniveDeclination( int caz, int number, String s)
	{
		if(caz < 2 && number == 0)
			subst.set(s);
		
		if (caz == 0)
		{
			s = replaceInvariable(s, subst, Case.Nominativ, Number.values()[number + 1], Art.Nearticulat);
			subst.set(Case.Nominativ, Number.values()[number + 1], Art.Nearticulat, s);
			subst.set(Case.Acuzativ, Number.values()[number + 1], Art.Nearticulat, s);
		} else if (caz == 1)
		{
			s = replaceInvariable(s, subst, Case.Nominativ, Number.values()[number + 1], Art.Articulat);
			subst.set(Case.Nominativ, Number.values()[number + 1], Art.Articulat, s);
			subst.set(Case.Acuzativ, Number.values()[number + 1], Art.Articulat, s);
		} else if (caz == 2)
		{
			s = replaceInvariable(s, subst, Case.Dativ, Number.values()[number + 1], Art.Nearticulat);
			subst.set(Case.Dativ, Number.values()[number + 1], Art.Nearticulat, s);
			subst.set(Case.Genitiv, Number.values()[number + 1], Art.Nearticulat, s);
		} else if (caz == 3)
		{
			s = replaceInvariable(s, subst, Case.Vocativ, Number.values()[number + 1], Art.Nearticulat);
			subst.set(Case.Vocativ, Number.values()[number + 1], Art.Nearticulat, s);
		}
	}

	private String replaceInvariable( String s, Substantive subst, Case caz, Number n, Art art)
	{
		String sLowerCase = s.toLowerCase();
		if (n == Number.Plural && sLowerCase.equals("invariabil"))
		{
			String a = subst.get(caz, Number.Singular, art);
			if (a != null && !a.contains("invariabil"))
			{
				return a;
			}
		}
		return s;
	}

	public Substantive getSubstantive( String s) throws Exception
	{
		m_state = State.NotStarted;

		subst = new Substantive();
		subst.set(s);
		hasGender = false;
		
		parse(BASE_URL + URLEncoder.encode(s, "UTF-8"));

		return subst;
	}

	public static void main( String[] args)
	{
		SubstantiveDeclinationReader s = new SubstantiveDeclinationReader();

		try
		{
			Substantive su = s.getSubstantive("azure");
			System.out.println(su.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
