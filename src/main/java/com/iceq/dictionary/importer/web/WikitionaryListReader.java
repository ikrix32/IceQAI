package com.iceq.dictionary.importer.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;

public class WikitionaryListReader extends HtmlParser
{
	private enum State
	{
		NotStarted,
		Running,
		NextPageLink,
		NextPageTag,
		Ended,
	}

	private String		tagListStart	= "pagina următoare";
	private String		tagListEnd		= "pagina anterioară";
	private String		tagNextPage		= "pagina următoare";

	private ItemPage	page;
	private State		m_state			= State.NotStarted;

	public void setTags( String listStart, String listEnd, String nextPage)
	{
		tagListStart = listStart;
		tagListEnd = listEnd;
		tagNextPage = nextPage;
	}

	protected String processHtmlContent( String html)
	{
		//html = html.replace("\n", "");
		//html = html.replace(" ", "");
		
		return html;
	}

	protected void setupCleanProperties( CleanerProperties properties)
	{
		CleanerTransformations transformations = new CleanerTransformations();

		TagTransformation tt = new TagTransformation("head");
		transformations.addTransformation(tt);

		tt = new TagTransformation("title");
		transformations.addTransformation(tt);

		tt = new TagTransformation("b");
		transformations.addTransformation(tt);
		
		tt = new TagTransformation("h1");
		transformations.addTransformation(tt);

		tt = new TagTransformation("h2");
		transformations.addTransformation(tt);

		tt = new TagTransformation("h3");
		transformations.addTransformation(tt);

		tt = new TagTransformation("meta");
		transformations.addTransformation(tt);

		// tt = new TagTransformation("link");
		// transformations.addTransformation(tt);

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
		//System.out.println("HTML NODE:"+htmlNode);
		if (htmlNode instanceof ContentNode)
		{
			String content = ((ContentNode) htmlNode).getContent();
			content = contentClean(content);

			if (!content.isEmpty())
				processContent(content);
		} else if (htmlNode instanceof TagNode)
		{
			if (m_state == State.NextPageLink)
			{
				TagNode node = (TagNode) htmlNode;
				if ("a".equals(node.getName()))
				{
					try
					{
						if (page != null)
						{
							page.nextPage = URLDecoder.decode(node.getAttributeByName("href"), "UTF-8");
							page.nextPage = page.nextPage.replace("&amp;", "&");
							if (page.nextPage.contains("https://"))
								page.nextPage = null;
							m_state = State.NextPageTag;
						}
					} catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		// logNode(htmlNode);

		return true;
	}

	protected String contentClean( String content)
	{
		content = content.replaceAll("^[\n\\s]*$", "");
		// content = content.replaceAll("", "");
		return content;
	}

	protected void processContent( String content)
	{
		if (m_state == State.NotStarted)
		{
			if (content.contains(tagListStart))
			{
				m_state = State.Running;
			}
		} else if (m_state == State.Running)
		{
			if (content.contains(tagListEnd))
			{
				m_state = State.NextPageLink;
			} else
			{
				if (content.length() > 1)
					processListItem(content);
			}
		} else if (m_state == State.NextPageTag)
		{
			if (content.contains(tagNextPage))
			{
				m_state = State.Ended;
			} else
			{// link incorect go back to link search
				if (page != null)
					page.nextPage = null;
				m_state = State.NextPageLink;
			}
		}
	}

	void processListItem( String item)
	{
		if(!item.contains("Utilizator:")){
			if (page == null)
				page = new ItemPage();
	
			page.add(item);
		}
	}

	public ItemPage getPage( String pageUrl) throws Exception
	{
		// System.out.println("List page:"+pageUrl);

		m_state = State.NotStarted;
		String url = pageUrl;
		parse(url);

		ItemPage p = page;

		page = null;

		return p;
	}

	public static void main( String[] args)
	{
		SubstantiveDeclinationReader sr = new SubstantiveDeclinationReader();
		WikitionaryListReader c = new WikitionaryListReader();
		c.setTags("pagina următoare", "pagina anterioară", "pagina următoare");

		try
		{
			String SERVER_URL = "https://ro.wiktionary.org";
			//String VERBS_LIST_PAGE = "/wiki/Categorie:Verbe_în_română";
			String SUBSTANTIVES_LIST_PAGE = "/wiki/Categorie:Conjuncții_în_română";

			String page = SUBSTANTIVES_LIST_PAGE;
			while (page != null)
			{
				ItemPage wp = c.getPage(SERVER_URL + page);
				if (wp != null)
				{
					for (int i = 0; i < wp.itemCount(); i++)
					{
						System.out.println("-" + wp.get(i));
//						Substantive su = sr.getSubstantive(wp.get(i));
//						System.out.println(su.toString());
					}
					page = wp.nextPage;
				} else
					page = null;

			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
