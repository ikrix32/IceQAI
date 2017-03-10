package com.iceq.dictionary.importer.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.htmlcleaner.CData;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CommentNode;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.DoctypeToken;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

public abstract class HtmlParser
{
	private final static boolean	USE_HTML_PAGE_PROCESS	= true;

	private HtmlCleaner				cleaner					= new HtmlCleaner();

	protected HtmlCleaner getHtmlCleaner()
	{
		return cleaner;
	}

	public String getHTML( String urlToRead) throws Exception
	{
		HttpURLConnection conn = null;
		BufferedReader rd = null;
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);

		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(1000 * 10);

		rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null)
		{
			result.append(line);
		}
		rd.close();
		conn.disconnect();

		return result.toString();
	}

	protected abstract String processHtmlContent( String html);

	protected abstract void setupCleanProperties( CleanerProperties properties);

	protected abstract boolean visitNode( HtmlNode htmlNode, TagNode tagNode);

	protected void parse( String url) throws Exception
	{
		CleanerProperties properties = cleaner.getProperties();
		setupCleanProperties(properties);

		TagNode node = null;

		if (USE_HTML_PAGE_PROCESS)
		{
			String content = getHTML(url);
			String htmlContent = processHtmlContent(content);
			node = cleaner.clean(htmlContent);
		} else
		{
			node = cleaner.clean(new URL(url));
		}

		node.traverse(new TagNodeVisitor()
		{
			public boolean visit( TagNode tagNode, HtmlNode htmlNode)
			{
				return visitNode(htmlNode, tagNode);
			}
		});
	}

	protected String getXHTML( String url) throws Exception
	{
		CleanerProperties properties = cleaner.getProperties();
		setupCleanProperties(properties);

		TagNode node = null;
		if (USE_HTML_PAGE_PROCESS)
		{
			String htmlContent = processHtmlContent(getHTML(url));
			node = cleaner.clean(htmlContent);
		} else
		{
			node = cleaner.clean(new URL(url));
		}

		PrettyXmlSerializer ser = new PrettyXmlSerializer(properties);
		return ser.getAsString(node);
	}

	protected void logNode( HtmlNode htmlNode)
	{
		if (htmlNode instanceof TagNode)
		{
			TagNode tag = (TagNode) htmlNode;
			// String tagName = tag.getName();
			System.out.println("TagNode : " + tag.toString());
			// if ("img".equals(tagName)) {
			// String src = tag.getAttributeByName("src");
			// if (src != null) {
			// //tag.setAttribubte("src", Utils.fullUrl(siteUrl, src));
			// }
			// }
		} else if (htmlNode instanceof CommentNode)
		{
			CommentNode comment = ((CommentNode) htmlNode);
			System.out.println("CommentNode: " + comment.toString());
			// comment.getContent().append(" -- By HtmlCleaner");
		} else if (htmlNode instanceof ContentNode)
		{
			ContentNode content = ((ContentNode) htmlNode);
			System.out.println("ContentNode:\"" + content.getContent() + "\"");
			// comment.getContent().append(" -- By HtmlCleaner");
		} else if (htmlNode instanceof DoctypeToken)
		{
			DoctypeToken content = ((DoctypeToken) htmlNode);
			System.out.println("DoctypeToken: " + content.toString());
			// comment.getContent().append(" -- By HtmlCleaner");
		} else if (htmlNode instanceof CData)
		{
			CData content = ((CData) htmlNode);
			System.out.println("CData: " + content.toString());
			// comment.getContent().append(" -- By HtmlCleaner");
		}
	}

	protected String trimNewLineAndSpaces( String content)
	{
		content = content.replaceAll("^[\n\\s]*$", "");
		// content = content.replaceAll("", "");
		return content;
	}
}
