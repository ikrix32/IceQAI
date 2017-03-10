package com.iceq.sense.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.CommentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;
import org.htmlcleaner.TagTransformation;

public class HTMLCleanerTest
{
	public static void testPageConvert()
	{
		CleanerProperties props = new CleanerProperties();

		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		//props.setTransResCharsToNCR(true);
		props.setOmitComments(true);

		// do parsing
		TagNode tagNode = null;
		try
		{
			tagNode = new HtmlCleaner(props).clean(new URL("http://conjugare.ro/romana/conjugarea-verbului-joc"));
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrettyXmlSerializer ser = new PrettyXmlSerializer(props);
		System.out.println("XHTML:"+ser.getXmlAsString(tagNode));
	}
	
	public static void testPageTraverse()
	{
		HtmlCleaner cleaner = new HtmlCleaner();
		final String siteUrl = "http://conjugare.ro/romana/conjugarea-verbului-joc";
		 
		TagNode node = null;
		try
		{
			node = cleaner.clean(new URL(siteUrl));
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// traverse whole DOM and update images to absolute URLs
		node.traverse(new TagNodeVisitor() {
		    public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
		        if (htmlNode instanceof TagNode) {
		            TagNode tag = (TagNode) htmlNode;
		            String tagName = tag.getName();
		            System.out.println("TagName:"+tagName+"->"+tag.toString());
		            if ("img".equals(tagName)) {
		                String src = tag.getAttributeByName("src");
		            }
		        } else if (htmlNode instanceof CommentNode) {
		            CommentNode comment = ((CommentNode) htmlNode); 
		            //comment.getContent().append(" -- By HtmlCleaner");
		        }
		        // tells visitor to continue traversing the DOM tree
		        return true;
		    }
		});
		 
		PrettyXmlSerializer ser = new PrettyXmlSerializer(cleaner.getProperties());
		System.out.println("XHTML:"+ser.getXmlAsString(node));
	}
	
	public static void testPageCleanup()
	{
		HtmlCleaner cleaner = new HtmlCleaner();
		final String siteUrl = "http://conjugare.ro/romana/conjugarea-verbului-copil";
		 
		CleanerTransformations transformations = 
		    new CleanerTransformations();
		 
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
		
		tt = new TagTransformation("script");
		transformations.addTransformation(tt);
		
		tt = new TagTransformation("noscript");
		transformations.addTransformation(tt);
		
		tt = new TagTransformation("sc");
		transformations.addTransformation(tt);
		
		tt = new TagTransformation("style");
		transformations.addTransformation(tt);
		
		//tt = new TagTransformation("span");
		//transformations.addTransformation(tt);
		
		tt = new TagTransformation("a");
		transformations.addTransformation(tt);
		
		tt = new TagTransformation("img");
		transformations.addTransformation(tt);
		
		cleaner.getProperties().setCleanerTransformations(transformations);
		cleaner.getProperties().setOmitComments(true);
		cleaner.getProperties().setOmitCdataOutsideScriptAndStyle(true);
		cleaner.getProperties().setOmitDoctypeDeclaration(true);
		cleaner.getProperties().setOmitXmlDeclaration(true);
		cleaner.getProperties().setOmitHtmlEnvelope(true);
		TagNode node = null;
		try
		{
			node = cleaner.clean(new URL(siteUrl));
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrettyXmlSerializer ser = new PrettyXmlSerializer(cleaner.getProperties());
		System.out.println("XHTML:"+ser.getXmlAsString(node));
	}
	
	public static void main( String[] args){
		//testPageConvert();
		//testPageTraverse();
		testPageCleanup();
	}
}
