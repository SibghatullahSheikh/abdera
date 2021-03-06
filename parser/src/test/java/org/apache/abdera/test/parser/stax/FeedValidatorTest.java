/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/
package org.apache.abdera.test.parser.stax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.MimeTypeParseException;

import org.apache.abdera.model.AtomDate;
import org.apache.abdera.model.Category;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.DateTime;
import org.apache.abdera.model.Div;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Generator;
import org.apache.abdera.model.IRI;
import org.apache.abdera.model.Link;
import org.apache.abdera.model.Person;
import org.apache.abdera.model.Source;
import org.apache.abdera.model.Text;
import org.apache.abdera.parser.Parser;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;


import junit.framework.TestCase;

public class FeedValidatorTest 
  extends TestCase {

  private static URI baseURI = null;
  
  private static <T extends Element> Document<T> get(URI uri) {
    try {
      return Parser.INSTANCE.parse(uri.toURL().openStream(), uri);
    } catch (Exception e) {}
    return null;
  }
  

  @Override
  protected void setUp() throws Exception {
    baseURI = new URI("http://feedvalidator.org/testcases/atom/");
    super.setUp();
  }


  public static void testSection11BriefNoError() throws Exception {
    
    // http://feedvalidator.org/testcases/atom/1.1/brief-noerror.xml
    URI uri = baseURI.resolve("1.1/brief-noerror.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    Text title = feed.getTitleElement();
    assertNotNull(title);
    assertEquals(title.getTextType(), Text.Type.TEXT);
    String value = title.getValue();
    assertNotNull(value);
    assertEquals(value, "Example Feed");
    List<Link> links = feed.getLinks();
    assertEquals(1, links.size());
    for (Link link : links) {
      assertNull(link.getRel()); // it's an alternate link
      assertEquals(link.getHref(), new URI("http://example.org/"));
      assertNull(link.getHrefLang());
      assertNull(link.getMimeType());
      assertNull(link.getTitle());
      assertEquals(link.getLength(),-1);
    }
    links = feed.getLinks(Link.REL_ALTERNATE);
    assertEquals(links.size(),1);
    links = feed.getLinks(Link.REL_RELATED);
    assertEquals(links.size(),0);
    assertNotNull(feed.getUpdatedElement());
    DateTime dte = feed.getUpdatedElement();
    AtomDate dt = dte.getValue();
    assertNotNull(dt);
    Calendar c = dt.getCalendar();
    AtomDate cdt = new AtomDate(c);
    assertEquals(dt.getTime(), cdt.getTime());
    Person person = feed.getAuthor();
    assertNotNull(person);
    assertEquals(person.getName(), "John Doe");
    assertNull(person.getEmail());
    assertNull(person.getUri());
    IRI id = feed.getIdElement();
    assertNotNull(id);
    assertEquals(id.getValue(), new URI("urn:uuid:60a76c80-d399-11d9-b93C-0003939e0af6"));
    List<Entry> entries = feed.getEntries();
    assertEquals(entries.size(), 1);
    for (Entry entry : entries) {
      title = entry.getTitleElement();
      assertNotNull(title);
      assertEquals(title.getTextType(), Text.Type.TEXT);
      value = title.getValue();
      assertEquals(value, "Atom-Powered Robots Run Amok");
      links = entry.getLinks();
      assertEquals(1, links.size());
      for (Link link : links) {
        assertNull(link.getRel()); // it's an alternate link
        assertEquals(link.getHref(), new URI("http://example.org/2003/12/13/atom03"));
        assertNull(link.getHrefLang());
        assertNull(link.getMimeType());
        assertNull(link.getTitle());
        assertEquals(link.getLength(),-1);
      }
      links = entry.getLinks(Link.REL_ALTERNATE);
      assertEquals(links.size(),1);
      links = entry.getLinks(Link.REL_RELATED);
      assertEquals(links.size(),0);
      id = entry.getIdElement();
      assertNotNull(id);
      assertEquals(id.getValue(), new URI("urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a"));
      assertNotNull(entry.getUpdatedElement());
      dte = entry.getUpdatedElement();
      dt = dte.getValue();
      assertNotNull(dt);
      c = dt.getCalendar();
      cdt = new AtomDate(c);
      assertEquals(dt.getTime(), cdt.getTime());
      Text summary = entry.getSummaryElement();
      assertNotNull(summary);
      assertEquals(summary.getTextType(), Text.Type.TEXT);
      value = summary.getValue();
      assertEquals(value, "Some text.");
    }
  }
  
  public static void testSection11ExtensiveNoError() throws Exception {
    
    //http://feedvalidator.org/testcases/atom/1.1/extensive-noerror.xml
    URI uri = baseURI.resolve("1.1/extensive-noerror.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertNotNull(feed.getTitleElement());
    assertEquals(feed.getTitleElement().getTextType(), Text.Type.TEXT);
    assertEquals(feed.getTitleElement().getValue(), "dive into mark");
    assertNotNull(feed.getSubtitleElement());
    assertEquals(feed.getTitleElement().getTextType(), Text.Type.TEXT);
    assertNotNull(feed.getSubtitleElement().getValue());
    assertNotNull(feed.getUpdatedElement());
    assertNotNull(feed.getUpdatedElement().getValue());
    assertNotNull(feed.getUpdatedElement().getValue().getDate());
    assertNotNull(feed.getIdElement());
    assertTrue(feed.getIdElement() instanceof IRI);
    assertEquals(feed.getIdElement().getValue(), new URI("tag:example.org,2003:3"));
    List<Link> links = feed.getLinks(Link.REL_ALTERNATE);
    assertEquals(links.size(), 1);
    for (Link link : links) {
      assertEquals(link.getRel(), "alternate");
      assertEquals(link.getMimeType().toString(), "text/html");
      assertEquals(link.getHrefLang(), "en");
      assertEquals(link.getHref(), new URI("http://example.org/"));
      assertNull(link.getTitle());
      assertEquals(link.getLength(),-1);
    }
    links = feed.getLinks(Link.REL_SELF);
    assertEquals(links.size(), 1);
    for (Link link : links) {
      assertEquals(link.getRel(), "self");
      assertEquals(link.getMimeType().toString(), "application/atom+xml");
      assertEquals(link.getHref(), new URI("http://example.org/feed.atom"));
      assertNull(link.getHrefLang());
      assertNull(link.getTitle());
      assertEquals(link.getLength(),-1);
    }
    assertNotNull(feed.getRightsElement());
    assertEquals(feed.getRightsElement().getTextType(), Text.Type.TEXT);
    assertEquals(feed.getRightsElement().getValue(), "Copyright (c) 2003, Mark Pilgrim");
    assertNotNull(feed.getGenerator());
    Generator generator = feed.getGenerator();
    assertEquals(generator.getUri(), new URI("http://www.example.com/"));
    assertEquals(generator.getVersion(), "1.0");
    assertNotNull(generator.getText());
    assertEquals(generator.getText().trim(), "Example Toolkit");
    List<Entry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(entries.size(), 1);
    for (Entry entry : entries) {
      assertNotNull(entry.getTitleElement());
      assertEquals(entry.getTitleElement().getTextType(), Text.Type.TEXT);
      assertEquals(entry.getTitleElement().getValue(), "Atom draft-07 snapshot");
      links = entry.getLinks(Link.REL_ALTERNATE);
      assertEquals(links.size(), 1);
      for (Link link : links) {
        assertEquals(link.getRel(), "alternate");
        assertEquals(link.getMimeType().toString(), "text/html");
        assertEquals(link.getHref(), new URI("http://example.org/2005/04/02/atom"));
        assertNull(link.getHrefLang());
        assertNull(link.getTitle());
        assertEquals(link.getLength(),-1);
      }
      links = entry.getLinks(Link.REL_ENCLOSURE);
      assertEquals(links.size(), 1);
      for (Link link : links) {
        assertEquals(link.getRel(), "enclosure");
        assertEquals(link.getMimeType().toString(), "audio/mpeg");
        assertEquals(link.getHref(), new URI("http://example.org/audio/ph34r_my_podcast.mp3"));
        assertEquals(link.getLength(),1337);
        assertNull(link.getHrefLang());
        assertNull(link.getTitle());
      }
      assertNotNull(entry.getIdElement());
      assertEquals(entry.getIdElement().getValue(), new URI("tag:example.org,2003:3.2397"));
      assertNotNull(entry.getUpdatedElement());
      assertNotNull(entry.getPublishedElement());
      Person person = entry.getAuthor();
      assertNotNull(person);
      assertEquals(person.getName(),"Mark Pilgrim");
      assertEquals(person.getEmail(), "f8dy@example.com");
      assertNotNull(person.getUriElement());
      assertEquals(person.getUriElement().getValue(), new URI("http://example.org/"));
      List<Person> contributors = entry.getContributors();
      assertNotNull(contributors);
      assertEquals(contributors.size(),2);
      assertNotNull(entry.getContentElement());
      assertEquals(entry.getContentElement().getContentType(), Content.Type.XHTML);
      assertEquals(entry.getContentElement().getLanguage(), "en");
      assertEquals(entry.getContentElement().getBaseUri(), new URI("http://diveintomark.org/"));
    }
  }
  
  public static void testSection12MissingNamespace() throws Exception {
    //http://feedvalidator.org/testcases/atom/1.2/missing-namespace.xml
    URI uri = baseURI.resolve("1.2/missing-namespace.xml");
    Document doc = null;
    doc = get(uri);
    assertNotNull(doc);
  }
  
  public static void testSection12PrefixedNamespace() throws Exception {
    //http://feedvalidator.org/testcases/atom/1.2/prefixed-namespace.xml
    URI uri = baseURI.resolve("1.2/prefixed-namespace.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    OMElement omElement = (OMElement) feed;
    assert(omElement.getQName().getPrefix().equals("atom"));
  }
  
  public static void testSection12WrongNamespaceCase() throws Exception {
    //http://feedvalidator.org/testcases/atom/1.2/wrong-namespace-case.xml
    URI uri = baseURI.resolve("1.2/wrong-namespace-case.xml");
    Document doc = null;
    doc = get(uri);
    assertNotNull(doc);
  }

  public static void testSection12WrongNamespace() throws Exception {
    //http://feedvalidator.org/testcases/atom/1.2/wrong-namespace.xml
    URI uri = baseURI.resolve("1.2/wrong-namespace.xml");
    Document doc = null;
    doc = get(uri);
    assertNotNull(doc);
  }
  
  public static void testSection2BriefEntry() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/brief-entry-noerror.xml
    URI uri = baseURI.resolve("2/brief-entry-noerror.xml");
    Document<Entry> doc = get(uri);
    assertNotNull(doc);
    Entry entry = doc.getRoot();
    assertNotNull(entry);
    assertNotNull(entry.getTitleElement());
    assertEquals(entry.getLinks(Link.REL_ALTERNATE).size(), 1);
    assertNotNull(entry.getIdElement());
    assertNotNull(entry.getIdElement().getValue());
    assertNotNull(entry.getUpdatedElement());
    assertNotNull(entry.getUpdatedElement().getValue());
    assertNotNull(entry.getUpdatedElement().getValue().getDate());
    assertNotNull(entry.getSummaryElement());
    assertEquals(entry.getSummaryElement().getTextType(), Text.Type.TEXT);
    assertNotNull(entry.getAuthor());
    assertEquals(entry.getAuthor().getName(), "John Doe");
  }
  
  public static void testSection2InfosetAttrOrder() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-attr-order.xml
    URI uri = baseURI.resolve("2/infoset-attr-order.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Link> links = feed.getLinks(Link.REL_ALTERNATE);
    assertEquals(links.size(),2);
    for (Link link : links) {
      assertEquals(link.getRel(), "alternate");
      assertNotNull(link.getHref());
    }
  }
  
  public static void testSection2InfosetCDATA() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-cdata.xml
    URI uri = baseURI.resolve("2/infoset-cdata.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text summary = entry.getSummaryElement();
      assertNotNull(summary);
      assertEquals(summary.getTextType(), Text.Type.TEXT);
      String value = summary.getValue();
      assertEquals(value, "Some <b>bold</b> text.");
    }
  }
  
  @SuppressWarnings("deprecation")
  public static void testSection2InfosetCharRef() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-char-ref.xml
    URI uri = baseURI.resolve("2/infoset-char-ref.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      DateTime updated = entry.getUpdatedElement();
      assertNotNull(updated);
      assertNotNull(updated.getValue());
      assertNotNull(updated.getValue().getDate());
      assertEquals(updated.getValue().getDate().getYear(), 103);
    }
  }
  
  public static void testSection2InfosetElementWhitespace() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-element-whitespace.xml
    //Note: we're not doing any actual validation of the Atom right now,
    //so we're skipping this test
  }
  
  public static void testSection2InfosetEmpty1() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-empty1.xml
    //Note: we're not doing ay actual validation on the Atom right now,
    //so we're skipping this test
  }
  
  public static void testSection2InfosetEmpty2() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-empty2.xml
    //Note: validation not implemented yet
  }
  
  public static void testSection2InfosetSingleQuote() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/infoset-quote-single.xml
    URI uri = baseURI.resolve("2/infoset-quote-single.xml");
    Document doc = get(uri);
    assertNotNull(doc);
    
  }
  
  public static void testSection2InvalidXmlBase() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/invalid-xml-base.xml
    URI uri = baseURI.resolve("2/invalid-xml-base.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    try {
      feed.getBaseUri();
    } catch (Exception e) {
      assertTrue(e instanceof URISyntaxException);
    }
  }
  
  public static void testSection2InvalidXmlLang() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/invalid-xml-lang.xml
    //Note: not yet implemented
  }
  
  public static void testSection2Iri() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/iri.xml
    URI uri = baseURI.resolve("2/iri.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertNotNull(feed.getIdElement().getValue());
    assertNotNull(feed.getAuthor().getUriElement().getValue());
    assertNotNull(feed.getAuthor().getUriElement().getValue().toASCIIString());
  }
  
  public static void testSection2XmlBaseAmbiguous() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/xml-base-ambiguous.xml
    //Note: not yet implemented
  }
  
  public static void testSection2XmlBaseElemEqDoc() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/xml-base-elem-eq-doc.xml
    //Note: not yet implemented
  }
  
  public static void testSection2XmlBaseElemNeDoc() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/xml-base-elem-ne-doc.xml
    //Note: not yet implemented
  }
  
  public static void testSection2XmlBase() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/xml-base.xml
    URI uri = baseURI.resolve("2/xml-base.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Link> links = feed.getLinks();
    for (Link link : links) {
      assertEquals(link.getResolvedHref(), new URI("http://example.org/index.html"));
    }
  }
  
  public static void testSection2XmlLangBlank() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/xml-lang-blank.xml
    //Note: not implemented
  }
  
  public static void testSection2XmlLang() throws Exception {
    //http://feedvalidator.org/testcases/atom/2/xml-lang.xml
    URI uri = baseURI.resolve("2/xml-lang.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertEquals(feed.getLanguage(), "en-us");
  }
  
  public static void testSection3WsAuthorUri() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-author-uri.xml
    URI uri = baseURI.resolve("3/ws-author-uri.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    Person author = feed.getAuthor();
    try {
      author.getUriElement().getValue();
    } catch (Exception e) {
      assertTrue(e instanceof URISyntaxException);
    }
  }
  
  public static void testSection3WsCategoryScheme() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-category-scheme.xml
    URI uri = baseURI.resolve("3/ws-category-scheme.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    assertNotNull(entries);
    for (Entry entry : entries) {
      List<Category> cats = entry.getCategories();
      for (Category cat : cats) {
        try {
          cat.getScheme();
        } catch (Exception e) {
          assertTrue(e instanceof URISyntaxException);
        }
      }
    }
  }
  
  public static void testSection3WsContentSrc() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-content-src.xml
    URI uri = baseURI.resolve("3/ws-content-src.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    assertNotNull(entries);
    for (Entry entry : entries) {
      Content content = entry.getContentElement();
      assertNotNull(content);
      try {
        content.getSrc();
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection3WsEntryId() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-entry-id.xml
    URI uri = baseURI.resolve("3/ws-entry-id.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    assertNotNull(entries);
    for (Entry entry : entries) {
      IRI id = entry.getIdElement();
      assertNotNull(id);
      try {
        id.getValue();
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection3WsEntryPublished() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-entry-published.xml
    URI uri = baseURI.resolve("3/ws-entry-published.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    assertNotNull(entries);
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException); // bad date
      }
    }
  }
  
  public static void testSection3WsEntryUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-entry-updated.xml
    URI uri = baseURI.resolve("3/ws-entry-updated.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    assertNotNull(entries);
    for (Entry entry : entries) {
      try {
        entry.getUpdatedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException); // bad date
      }
    }
  }  
  
  public static void testSection3WsFeedIcon() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-feed-icon.xml
    URI uri = baseURI.resolve("3/ws-feed-icon.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getIconElement().getValue();
    } catch (Exception e) {
      assertTrue(e instanceof URISyntaxException);
    }
  }

  public static void testSection3WsFeedId() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-feed-id.xml
    URI uri = baseURI.resolve("3/ws-feed-id.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getIdElement().getValue();
    } catch (Exception e) {
      assertTrue(e instanceof URISyntaxException);
    }
  }
  
  public static void testSection3WsFeedLogo() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-feed-logo.xml
    URI uri = baseURI.resolve("3/ws-feed-logo.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getLogoElement().getValue();
    } catch (Exception e) {
      assertTrue(e instanceof URISyntaxException);
    }
  }
  
  public static void testSection3WsFeedUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-feed-updated.xml
    URI uri = baseURI.resolve("3/ws-feed-updated.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getUpdatedElement().getValue();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }  
  
  public static void testSection3WsGeneratorUri() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-generator-uri.xml
    URI uri = baseURI.resolve("3/ws-generator-uri.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    Generator gen = feed.getGenerator();
    assertNotNull(gen);
    try {
      gen.getUri();
    } catch (Exception e) {
      assertTrue(e instanceof URISyntaxException);
    }
  }
  
  public static void testSection3WsLinkHref() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-link-href.xml
    URI uri = baseURI.resolve("3/ws-link-href.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Link> links = feed.getLinks();
    for (Link link : links) {
      try {
        link.getHref();
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection3WsLinkRel() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-link-rel.xml
    //Note: validation of link rels not yet implemented
  }
  
  public static void testSection3WsXmlBase() throws Exception {
    //http://feedvalidator.org/testcases/atom/3/ws-xml-base.xml
    URI uri = baseURI.resolve("3/ws-xml-base.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getBaseUri();
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection311SummaryTypeMime() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1/summary_type_mime.xml
    URI uri = baseURI.resolve("3.1.1/summary_type_mime.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getEntries();
    } catch (Exception e) {
      assertTrue(e instanceof OMException);
    }
  }
  
  public static void testSection3111EscapedText() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.1/escaped_text.xml
    URI uri = baseURI.resolve("3.1.1.1/escaped_text.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text text = entry.getSummaryElement();
      assertNotNull(text);
      assertEquals(text.getTextType(), Text.Type.TEXT);
      String value = text.getValue();
      assertEquals(value, "Some&nbsp;escaped&nbsp;html");
    }
  }
  
  public static void testSection3111ExampleTextTitle() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.1/example_text_title.xml
    URI uri = baseURI.resolve("3.1.1.1/example_text_title.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text title = entry.getTitleElement();
      assertNotNull(title);
      assertEquals(title.getTextType(), Text.Type.TEXT);
      String value = title.getValue();
      assertEquals(value.trim(), "Less: <");
    }
  }
  
  public static void testSection3111SummaryTypeMime() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.1/summary_type_mime.xml
    URI uri = baseURI.resolve("3.1.1.1/summary_type_mime.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getEntries();
    } catch (Exception e) {
      assertTrue(e instanceof OMException);
    }
  }
  
  public static void testSection3112ExampleHtmlTitle() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.2/example_html_title.xml
    URI uri = baseURI.resolve("3.1.1.2/example_html_title.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text title = entry.getTitleElement();
      assertEquals(title.getTextType(), Text.Type.HTML);
      String value = title.getValue();
      assertEquals(value.trim(), "Less: <em> &lt; </em>");
    }
  }
  
  public static void testSection3112InvalidHtml() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.2/invalid_html.xml
    //Note: not validating the content
  }
  
  public static void testSection3112TestWithEscapedHtml() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.2/text_with_escaped_html.xml
    //Note: not validating the content
  }
  
  public static void testSection3112ValidHtml() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.2/valid_html.xml
    //Note: not validating the content
  }
  
  public static void testSection3113ExampleXhtmlSummary1() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.3/example_xhtml_summary1.xml
    URI uri = baseURI.resolve("3.1.1.3/example_xhtml_summary1.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text summary = entry.getSummaryElement();
      assertNotNull(summary);
      assertEquals(summary.getTextType(), Text.Type.XHTML);
      Div div = summary.getValueElement();
      assertNotNull(div);
    }
  }
  
  public static void testSection3113ExampleXhtmlSummary2() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.3/example_xhtml_summary2.xml
    URI uri = baseURI.resolve("3.1.1.3/example_xhtml_summary2.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text summary = entry.getSummaryElement();
      assertNotNull(summary);
      assertEquals(summary.getTextType(), Text.Type.XHTML);
      Div div = summary.getValueElement();
      assertNotNull(div);
    }
  }
  
  public static void testSection3113ExampleXhtmlSummary3() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.3/example_xhtml_summary3.xml
    URI uri = baseURI.resolve("3.1.1.3/example_xhtml_summary3.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text summary = entry.getSummaryElement();
      assertNotNull(summary);
      assertEquals(summary.getTextType(), Text.Type.XHTML);
      Div div = summary.getValueElement();
      assertNotNull(div);
    }
  }  
  
  public static void testSection3113MissingXhtmlDiv() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.3/missing_xhtml_div.xml
    URI uri = baseURI.resolve("3.1.1.3/missing_xhtml_div.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Text summary = entry.getSummaryElement();
      assertNotNull(summary);
      assertEquals(summary.getTextType(), Text.Type.XHTML);
      Div div = summary.getValueElement();
      assertNull(div);
    }
  }
  
  public static void testSection3113XhtmlNamedEntity() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.1.1.3/xhtml_named_entity.xml
    URI uri = baseURI.resolve("3.1.1.3/xhtml_named_entity.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getEntries();
    } catch (Exception e) {
      assertTrue(e instanceof OMException);
    }    
  }
  
  public static void testSection321ContainsEmail() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.1/contains-email.xml
    //Note: not validating input right now
  }
  
  public static void testSection321MultipleNames() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.1/multiple-names.xml
    //Note: not validating input right now
  }
  
  public static void testSection321NoName() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.1/no-name.xml
    //Note: not validating input right now
  }
  
  public static void testSection322InvalidUri() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.2/invalid-uri.xml
    URI uri = baseURI.resolve("3.1.1.3/missing_xhtml_div.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    for (Person person : contr) {
      try {
        person.getUriElement();
      } catch (Exception e) {
        assertFalse(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection322MultipleUris() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.2/multiple-uris.xml
    //Note: not validating input right now
  }
  
  public static void testSection322RelativeRef() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.2/relative-ref.xml
    URI uri = baseURI.resolve("3.2.2/relative-ref.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    for (Person person : contr) {
      assertEquals(person.getUriElement().getValue(), new URI("~jane/"));
      assertEquals(person.getUriElement().getResolvedValue(), uri.resolve("~jane/"));
    }
  }
  
  public static void testSection323EmailRss20Style() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.3/email-rss20-style.xml
    URI uri = baseURI.resolve("3.2.3/email-rss20-style.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    for (Person person : contr) {
      try {
        new URI(person.getEmail()); 
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection323EmailWithName() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.3/email-with-name.xml
    URI uri = baseURI.resolve("3.2.3/email-with-name.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    for (Person person : contr) {
      try {
        new URI(person.getEmail()); 
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }  
  
  public static void testSection323EmailWithPlus() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.3/email-with-plus.xml
    URI uri = baseURI.resolve("3.2.3/email-with-plus.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    for (Person person : contr) {
      new URI(person.getEmail()); 
    }
  }

  public static void testSection323InvalidEmail() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.3/invalid-email.xml
    URI uri = baseURI.resolve("3.2.3/invalid-email.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    for (Person person : contr) {
      try {
        new URI(person.getEmail());
      } catch (Exception e) {
        assertTrue(e instanceof URISyntaxException);
      }
    }
  }
  
  public static void testSection323MultipleEmails() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.2.3/multiple-emails.xml
    //Note: not implemented
  }
  
  public static void testSection33DuplicateUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/duplicate-updated.xml
    //Note: not implemented
  }
  
  public static void testSection33LowercaseUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/lowercase-updated.xml
    URI uri = baseURI.resolve("3.3/lowercase-updated.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    try {
      feed.getUpdatedElement().getValue();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
  
  public static void testSection33PublishedBadDay() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_bad_day.xml
    //Note: not yet implemented
  }
  
  public static void testSection33PublishedBadDay2() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_bad_day2.xml
    //Note: not yet implemented
  }
  
  public static void testSection33PublishedBadHours() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_bad_hours.xml
    //Note: not yet implemented
  }
  
  public static void testSecton33PublishedBadMinutes() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_bad_minutes.xml
    //Note: not yet implemented
  }
  
  public static void testSection33PublishedBadMonth() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_bad_month.xml
    //Note: not yet implemented
  }
  
  public static void testSection33PublishedBadSeconds() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_bad_seconds.xml
    //Note: not yet implemented
  }
  
  public static void testSection33PublishedDateOnly() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_date_only.xml
    URI uri = baseURI.resolve("3.3/published_date_only.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }
  
  public static void testSection33PublishedExtraSpaces() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_extra_spaces.xml
    URI uri = baseURI.resolve("3.3/published_extra_spaces.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedExtraSpaces2() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_extra_spaces2.xml
    URI uri = baseURI.resolve("3.3/published_extra_spaces2.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedExtraSpaces3() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_extra_spaces3.xml
    URI uri = baseURI.resolve("3.3/published_extra_spaces3.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedExtraSpaces4() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_extra_spaces4.xml
    URI uri = baseURI.resolve("3.3/published_extra_spaces4.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedExtraSpaces5() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_extra_spaces5.xml
    URI uri = baseURI.resolve("3.3/published_extra_spaces5.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }
  
  public static void testSection33PublishedFractionalSecond() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_fractional_second.xml
    URI uri = baseURI.resolve("3.3/published_fractional_second.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      entry.getPublishedElement().getValue();
    }
  }
  
  public static void testSection33PublishedHoursMinutes() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_hours_minutes.xml
    URI uri = baseURI.resolve("3.3/published_hours_minutes.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedNoColons() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_no_colons.xml
    URI uri = baseURI.resolve("3.3/published_no_colons.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }   
  
  public static void testSection33PublishedNoHyphens() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_no_hyphens.xml
    URI uri = baseURI.resolve("3.3/published_no_hyphens.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedNoT() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_no_t.xml
    URI uri = baseURI.resolve("3.3/published_no_t.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }
  
  public static void testSection33PublishedNoTimezoneColon() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_no_timezone_colon.xml
    URI uri = baseURI.resolve("3.3/published_no_timezone_colon.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  
  
  public static void testSection33PublishedNoYear() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_no_year.xml
    URI uri = baseURI.resolve("3.3/published_no_year.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }  

  public static void testSection33PublishedSeconds() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_seconds.xml
    URI uri = baseURI.resolve("3.3/published_seconds.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      entry.getPublishedElement().getValue();
    }
  }  
  
  public static void testSection33PublishedUtc() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_utc.xml
    URI uri = baseURI.resolve("3.3/published_utc.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      entry.getPublishedElement().getValue();
    }
  }
  
  public static void testSection33PublishedWrongFormat() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_wrong_format.xml
    URI uri = baseURI.resolve("3.3/published_wrong_format.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }
  
  public static void testSection33PublishedYearAndMonth() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_year_and_month.xml
    URI uri = baseURI.resolve("3.3/published_year_and_month.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }
  
  public static void testSection33PublishedYearOnly() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/published_year_only.xml
    URI uri = baseURI.resolve("3.3/published_year_only.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      try {
        entry.getPublishedElement().getValue();
      } catch (Exception e) {
        assertTrue(e instanceof IllegalArgumentException);
      }
    }
  }
  
  public static void testSection33UpdatedExample2() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/updated-example2.xml
    URI uri = baseURI.resolve("3.3/updated-example2.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      entry.getUpdatedElement().getValue();
    }
  }
  
  public static void testSection33UpdatedExample3() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/updated-example3.xml
    URI uri = baseURI.resolve("3.3/updated-example3.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      entry.getUpdatedElement().getValue();
    }
  }  
  
  public static void testSection33UpdatedExample4() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/updated-example4.xml
    URI uri = baseURI.resolve("3.3/updated-example4.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      entry.getUpdatedElement().getValue();
    }
  }  
  
  public static void testSection33UpdatedFuture() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/updated-future.xml
    //Note: not implemented
  }
  
  public static void testSection33UpdatedPast() throws Exception {
    //http://feedvalidator.org/testcases/atom/3.3/updated-past.xml
    //Note: not implemented
  }
  
  public static void testSection411AuthorAtEntryOnly() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/author-at-entry-only.xml
    URI uri = baseURI.resolve("4.1.1/author-at-entry-only.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNotNull(entry.getAuthor());
    }
  }
  
  public static void testSection411AuthorAtFeedAndEntry() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/author-at-feed-and-entry.xml
    URI uri = baseURI.resolve("4.1.1/author-at-feed-and-entry.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertNotNull(feed.getAuthor());
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNotNull(entry.getAuthor());
    }
  }
  
  public static void testSection411AuthorAtFeedOnly() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/author-at-feed-only.xml
    URI uri = baseURI.resolve("4.1.1/author-at-feed-only.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertNotNull(feed.getAuthor());
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNull(entry.getAuthor());
    }
  }  
  
  public static void testSection411AuthorlessWithNoEntries() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/authorless-with-no-entries.xml
    URI uri = baseURI.resolve("4.1.1/authorless-with-no-entries.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertNull(feed.getAuthor());
  }
  
  public static void testSection411AuthorlessWithOneEntry() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/authorless-with-one-entry.xml
    URI uri = baseURI.resolve("4.1.1/authorless-with-one-entry.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertNull(feed.getAuthor());
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNull(entry.getAuthor());
    }
  }
  
  public static void testSection411DuplicateEntries() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/duplicate-entries.xml
    //Note: not implemented
  }
  
  public static void testSection411LinkRelFull() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/link-rel-full.xml
    //Note: not implemented
  }
  
  public static void testSection411MisplacedMetadata() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/misplaced-metadata.xml
    //Note: not implemented
  }
  
  public static void testSection411MissingId() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/missing-id.xml
    //Note: not implemented
  }
  
  public static void testSection411MissingSelf() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/missing-self.xml
    //Note: not implemented
  }  
  
  public static void testSection411MissingTitles() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/missing-titles.xml
    //Note: not implemented
  }
  
  public static void testSection411MissingUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/missing-updated.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleAlternatesDiffering() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-alternates-differing.xml
    URI uri = baseURI.resolve("4.1.1/multiple-alternates-differing.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Link> links = feed.getLinks(Link.REL_ALTERNATE);
    assertEquals(links.size(), 2);
  }
  
  public static void testSection411MultipleAlternatesMatching() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-alternates-matching.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleAuthors() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-authors.xml
    URI uri = baseURI.resolve("4.1.1/multiple-authors.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> authors = feed.getAuthors();
    assertEquals(authors.size(),2);
  }
  
  public static void testSection411MultipleCategories() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-categories.xml
    URI uri = baseURI.resolve("4.1.1/multiple-categories.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Category> cats = feed.getCategories();
    assertEquals(cats.size(),2);
  }
  
  public static void testSection411MultipleContributors() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-contributors.xml
    URI uri = baseURI.resolve("4.1.1/multiple-contributors.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Person> contr = feed.getContributors();
    assertEquals(contr.size(),2);
  }  
  
  public static void testSection411MultipleGenerators() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-generators.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleIcons() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-icons.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleIds() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-ids.xml
    //Note: not implemented
  }  
  
  public static void testSection411MultipleLogos() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-logos.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleRelatedMatching() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-related-matching.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleRights() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-rights.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleSubtitles() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-subtitles.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleTitles() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-titles.xml
    //Note: not implemented
  }
  
  public static void testSection411MultipleUpdateds() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/multiple-updateds.xml
    //Note: not implemented
  }
  
  public static void testSection411ZeroEntries() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1/zero-entries.xml
    URI uri = baseURI.resolve("4.1.1/zero-entries.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    assertEquals(feed.getEntries().size(),0);
  }
  
  public static void testSection4111ContentSrc() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1.1/content-src.xml
    URI uri = baseURI.resolve("4.1.1.1/content-src.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      Content content = entry.getContentElement();
      assertNotNull(content.getSrc());
    }
  }
  
  public static void testSection4111EmptyContent() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1.1/empty-content.xml
    //Note: not implemented
  }
  
  public static void testSection4111EmptyTitle() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1.1/empty-title.xml
    //Note: not implemented
  }  
  
  public static void testSection4111NoContentOrSummary() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.1.1/no-content-or-summary.xml
    //Note: not implemented
  }
  
  public static void testSection412AlternateNoContent() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/alternate-no-content.xml
    URI uri = baseURI.resolve("4.1.2/alternate-no-content.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertEquals(entry.getLinks(Link.REL_ALTERNATE).size(), 1);
      assertNotNull(entry.getSummaryElement());
    }
  }
  
  public static void testSection412ContentBase64NoSummary() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/content-base64-no-summary.xml
    URI uri = baseURI.resolve("4.1.2/content-base64-no-summary.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNull(entry.getSummaryElement());
      assertNotNull(entry.getContentElement());
      Content mediaContent = entry.getContentElement();
      DataHandler dataHandler = mediaContent.getDataHandler();
      InputStream in = (ByteArrayInputStream) dataHandler.getContent();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int n = -1;
      while ((n = in.read()) > -1) { baos.write(n); }
      assertEquals(baos.toString(), "Some more text.");
    }
  }
  
  public static void testSection412ContentNoAlternate() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/content-no-alternate.xml
    URI uri = baseURI.resolve("4.1.2/content-no-alternate.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertEquals(entry.getLinks(Link.REL_ALTERNATE).size(),0);
      assertNotNull(entry.getContentElement());
    }
  }
  
  public static void testSection412ContentSrcNoSummary() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/content-src-no-summary.xml
    //Note: not implemented
  }
  
  public static void testSection412EntrySourceAuthor() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/entry-source-author.xml
    URI uri = baseURI.resolve("4.1.2/entry-source-author.xml");
    Document<Entry> doc = get(uri);
    assertNotNull(doc);
    Entry entry = doc.getRoot();
    assertNotNull(entry);
    assertNotNull(entry.getSource());
    assertNotNull(entry.getSource().getAuthor());
  }
  
  public static void testSection412LinkFullUri() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/link-full-uri.xml
    URI uri = baseURI.resolve("4.1.2/link-full-uri.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      List<Link> links = entry.getLinks("http://xmlns.com/foaf/0.1/");
      assertEquals(links.size(),1);
    }
  }
  
  public static void testSection412LinkSameRelDifferentTypes() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/link-same-rel-different-types.xml
    URI uri = baseURI.resolve("4.1.2/link-same-rel-different-types.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      List<Link> links = entry.getLinks(Link.REL_ALTERNATE);
      assertEquals(links.size(),2);
    }
  }
  
  public static void testSection412LinkSameRelTypeDifferentHreflang() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/link-same-rel-type-different-hreflang.xml
    //Note: not implemented
  }
  
  public static void testSection412LinkSameRelTypeHreflang() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/link-same-rel-type-hreflang.xml
    //Note: not implemented
  }
  
  public static void testSection412LinkSameRelTypeNoHreflang() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/link-same-rel-type-no-hreflang.xml
    //Note: not implemented
  }
  
  public static void testSection412MissingId() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/missing-id.xml
    //Note: not implemented
  }  

  public static void testSection412MissingTitle() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/missing-title.xml
    //Note: not implemented
  }  
  
  public static void testSection412MissingUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/missing-updated.xml
    //Note: not implemented
  }  
  
  public static void testSection412MultiEnclosureTest() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multi-enclosure-test.xml
    URI uri = baseURI.resolve("4.1.2/multi-enclosure-test.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      List<Link> enclosures = entry.getLinks(Link.REL_ENCLOSURE);
      assertEquals(enclosures.size(),2);
    }
  }  
  
  public static void testSection412MultipleCategories() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-categories.xml
    URI uri = baseURI.resolve("4.1.2/multiple-categories.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      List<Category> cats = entry.getCategories();
      assertEquals(cats.size(),2);
    }
  }   

  public static void testSection412MultipleContents() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-contents.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultipleContributors() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-contributors.xml
    URI uri = baseURI.resolve("4.1.2/multiple-contributors.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      List<Person> contr = entry.getContributors();
      assertEquals(contr.size(),2);
    }
  }
  
  public static void testSection412MultipleIds() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-ids.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultiplePublished() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-published.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultipleRights() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-rights.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultipleSources() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-sources.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultipleSummaries() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-summaries.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultipleTitles() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-titles.xml
    //Note: not implemented
  }   
  
  public static void testSection412MultipleUpdated() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/multiple-updated.xml
    //Note: not implemented
  }   
  
  public static void testSection412NoContentOrAlternate() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/no-content-or-alternate.xml
    //Note: not implemented
  }   
  
  public static void testSection412RelatedSameRelTypeHreflang() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/related-same-rel-type-hreflang.xml
    //Note: not implemented
  }   
      
  public static void testSection412SummaryContentBase64() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/summary-content-base64.xml
    URI uri = baseURI.resolve("4.1.2/summary-content-base64.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNotNull(entry.getSummaryElement());
      assertEquals(entry.getSummaryElement().getTextType(), Text.Type.TEXT);
      assertNotNull(entry.getContentElement());
      assertEquals(entry.getContentElement().getContentType(), Content.Type.MEDIA);
    }
  }
  
  public static void testSection412SummaryContentSrc() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.2/summary-content-src.xml
    URI uri = baseURI.resolve("4.1.2/summary-content-src.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNotNull(entry.getSummaryElement());
      assertEquals(entry.getSummaryElement().getTextType(), Text.Type.TEXT);
      assertNotNull(entry.getContentElement());
      assertEquals(entry.getContentElement().getContentType(), Content.Type.MEDIA);
      Content mediaContent = entry.getContentElement();
      assertNotNull(mediaContent.getSrc());
      assertEquals(mediaContent.getMimeType().toString(), "application/pdf");
    }
  }  
  
  public static void testSection4131TypeHtml() throws Exception {
   //http://feedvalidator.org/testcases/atom/4.1.3.1/type-html.xml
    URI uri = baseURI.resolve("4.1.3.1/type-html.xml");
    Document<Feed> doc = get(uri);
    assertNotNull(doc);
    
    Feed feed = doc.getRoot();
    assertNotNull(feed);
    List<Entry> entries = feed.getEntries();
    for (Entry entry : entries) {
      assertNotNull(entry.getContentElement());
      assertEquals(entry.getContentElement().getContentType(), Content.Type.HTML);
    }
  }
  
  public static void testSectoin413TypeMultipartAlternative() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.3.1/type-multipart-alternative.xml
    //Note: not implemented
  }
  
  public static void testSection4131TypeTextHtml() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.3.1/type-text-html.xml
     URI uri = baseURI.resolve("4.1.3.1/type-text-html.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       assertNotNull(entry.getContentElement());
       assertEquals(entry.getContentElement().getContentType(), Content.Type.MEDIA);
       Content mediaContent = entry.getContentElement();
       assertEquals(mediaContent.getMimeType().toString(), "text/html");
     }
   }  
  
  public static void testSection4131TypeText() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.3.1/type-text.xml
     URI uri = baseURI.resolve("4.1.3.1/type-text.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       assertNotNull(entry.getContentElement());
       assertEquals(entry.getContentElement().getContentType(), Content.Type.TEXT);
     }
   }
  
  public static void testSection4131TypeXhtml() throws Exception {
    //http://feedvalidator.org/testcases/atom/4.1.3.1/type-xhtml.xml
     URI uri = baseURI.resolve("4.1.3.1/type-xhtml.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       assertNotNull(entry.getContentElement());
       assertEquals(entry.getContentElement().getContentType(), Content.Type.XHTML);
     }
   }
  
   public static void testSection413TypeXml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.1/type-xhtml.xml
     URI uri = baseURI.resolve("4.1.3.1/type-xml.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     try {
       feed.getEntries();
     } catch (Exception e) {
       assertTrue(e instanceof OMException);
     }
   }
   
   public static void testSection4132ContentSrcExtraChild() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-extra-child.xml
     //Note: not implemented
   }
   
   public static void testSection4132ContentSrcExtraText() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-extra-text.xml
     //Note: not implemented
   }
   
   public static void testSection4132ContentSrcInvalidIri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-invalid-iri.xml
     URI uri = baseURI.resolve("4.1.3.2/content-src-invalid-iri.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Content content = entry.getContentElement();
       assertNotNull(content);
       try {
         content.getSrc();
       } catch (Exception e) {
         assertTrue(e instanceof URISyntaxException);
       }
     }
   }
   
   public static void testSection4132ContentSrcNoTypeNoError() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-no-type-no-error.xml
     //Note: not implemented
   }
   
   public static void testSection4132ContentSrcNoType() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-no-type.xml
     //Note: not implemented
   }
   
   public static void testSection4132ContentSrcRelativeRef() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-relative-ref.xml
     URI uri = baseURI.resolve("4.1.3.2/content-src-relative-ref.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Content content = entry.getContentElement();
       assertNotNull(content);
       assertEquals(content.getContentType(), Content.Type.MEDIA);
       assertEquals(content.getResolvedSrc(), uri.resolve("2003/12/12/atom03.pdf"));
     }
   }
   
   public static void testSection4132ContentSrcTypeHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-type-html.xml
     //Note: not implementd
   }
   
   public static void testSection4132ContentSrcTypeTextHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-type-text-html.xml
     //Note: not implemented
   }
   
   public static void testSection4132ContentSrcTypeText() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-type-text.xml
     //Note: not implemented
   }
   
   public static void testSection4132ContentSrcTypeXhtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.2/content-src-type-xhtml.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentApplicationXhtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-application-xthml.xml
     URI uri = baseURI.resolve("4.1.3.3/content-application-xthml.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Content content = entry.getContentElement();
       assertNotNull(content);
       assertEquals(entry.getContentElement().getContentType(), Content.Type.XML);
     }
   }
   
   public static void testSection4133ContentHtmlWithChildren() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-html-with-children.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentJpegInvalidBase64() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-jpeg-invalid-base64.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentJpegValidBase64() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-jpeg-valid-base64.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentNoTypeEscapedHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-no-type-escaped-html.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentNoTypeWithChildren() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-no-type-with-children.xml
     //Note: not implemented
   }   
   
   public static void testSection4133ContentPlainWithChildren() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-plain-with-children.xml
     //Note: not implemented
   }   
   
   public static void testSection4133ContentSvgMixed() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-svg-mixed.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentSvg() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-svg.xml
     URI uri = baseURI.resolve("4.1.3.3/content-svg.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Content content = entry.getContentElement();
       assertNotNull(content);
       assertEquals(entry.getContentElement().getContentType(), Content.Type.XML);
     }
   }
   
   public static void testSection4133ContentTextHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-text-html.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentTextWithChildren() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-text-with-children.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentXhtmlEscaped() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-xhtml-escaped.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentXhtmlMixed() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-xhtml-mixed.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentXhtmlNoXhtmlDiv() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-xhtml-no-xhtml-div.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentXhtmlNotmarkup() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-xhtml-notmarkup.xml
     //Note: not implemented
   }
   
   public static void testSection4133ContentXhtmlTextChildren() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.1.3.3/content-xhtml-text-children.xml
     //Note: not implemented
   }
   
   public static void testSection4221CategoryNoTerm() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.2.1/category-no-term.xml
     //Note: not implemented
   }
   
   public static void testSection4222CategoryNoScheme() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.2.2/category-no-scheme.xml
     //Note: not implemented
   }
   
   public static void testSection4222CategorySchemeInvalidIri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.2.2/category-scheme-invalid-iri.xml
     URI uri = baseURI.resolve("4.2.2.2/category-scheme-invalid-iri.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       List<Category> cats = entry.getCategories();
       for (Category cat : cats) {
         try {
           cat.getScheme();
         } catch (Exception e) {
           assertTrue(e instanceof URISyntaxException);
         }
       }
     }
   }
   
   public static void testSection4222CategorySchemeRelIri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.2.2/category-scheme-rel-iri.xml
     //Note: not implemented
   }
   
   public static void testSection4223CategoryLabelEscapedHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.2.3/category-label-escaped-html.xml
     //Note: not implemented
   }
   
   public static void testSection4223CategoryNoLabel() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.2.3/category-no-label.xml
     //Note: not implemented
   }
   
   public static void testSection424GeneratorEscapedHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.4/generator-escaped-html.xml
     //Note: not implemented
   }
   
   public static void testSection424GeneratorInvalidIri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.4/generator-invalid-iri.xml
     URI uri = baseURI.resolve("4.2.4/generator-invalid-iri.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     Generator generator = feed.getGenerator();
     assertNotNull(generator);
     try {
       generator.getUri();
     } catch (Exception e) {
       assertTrue(e instanceof URISyntaxException);
     }
   }
   
   public static void testSection424GeneratorNoText() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.4/generator-no-text.xml
     //Note: not implemented
   }
   
   public static void testSection424GeneratorWithChild() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.4/generator-with-child.xml
     //Note: not implemented
   }
   
   public static void testSection424GeneratorRelativeRef() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.4/generator_relative_ref.xml
     URI uri = baseURI.resolve("4.2.4/generator_relative_ref.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     Generator generator = feed.getGenerator();
     assertNotNull(generator);
     assertEquals(generator.getResolvedUri(), uri.resolve("misc/Colophon"));
   }
   
   public static void testSection425IconInvalidUri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.5/icon_invalid_uri.xml
     URI uri = baseURI.resolve("4.2.5/icon_invalid_uri.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     assertNotNull(feed.getIconElement());
     try {
       feed.getIconElement().getValue();
     } catch (Exception e) {
       assertTrue(e instanceof URISyntaxException);
     }
   }
   
   public static void testSection425IconRelativeRef() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.5/icon_relative_ref.xml
     URI uri = baseURI.resolve("4.2.5/icon_relative_ref.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     assertNotNull(feed);
     assertNotNull(feed.getIconElement());
     assertEquals(feed.getIconElement().getResolvedValue(), uri.resolve("favicon.ico"));
   }
   
   public static void testSection426IdDotSegments() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-dot-segments.xml
     //Note: not implemented
   }
   
   public static void testSection426IdEmptyFragmentId() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-empty-fragment-id.xml
     URI uri = baseURI.resolve("4.2.6/id-empty-fragment-id.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     feed.getIdElement().getValue();
   }
   
   public static void testSection426IdEmptyPath() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-empty-path.xml
     //Note: not implemented
   }
   
   public static void testSection426IdEmptyQuery() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-empty-query.xml
     //Note: not implemented
   }
   
   public static void testSection426IdExplicitAuthority() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-explicit-authority.xml
     //Note: not implemented
   }
   
   public static void testSection426IdExplicitDefaultPort() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-explicit-default-port.xml
     //Note: not implemented
   }
   
   public static void testSection426IdHostUppercase() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-host-uppercase.xml
     //Note: not implemented
   }
   
   public static void testSection426IdNotUri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-not-uri.xml
     URI uri = baseURI.resolve("4.2.6/id-not-uri.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     try {
       feed.getIdElement().getValue();
     } catch (Exception e) {
       assertTrue(e instanceof URISyntaxException);
     }
   }
   
   public static void testSection426IdPercentEncodedLower() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-percent-encoded-lower.xml
     //Note: not implemented
   }
   
   public static void testSection426IdPercentEncoded() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-percent-encoded.xml
     //Note: not implemented
   }
   
   public static void testSection426IdRelativeUri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-relative-uri.xml
     //Note: not implemented
   }
   
   public static void testSection426IdUppercaseScheme() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-uppercase-scheme.xml
     //Note: not implemented
   }
   
   public static void testSection426IdValidTagUris() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.6/id-valid-tag-uris.xml
     //Note: not implemented
   }
   
   public static void testSection4271LinkHrefInvalid() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.1/link-href-invalid.xml
     URI uri = baseURI.resolve("4.2.7.1/link-href-invalid.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       List<Link> links = entry.getLinks();
       for (Link link : links) {
         try {
           link.getHref();
         } catch (Exception e) {
           assertTrue(e instanceof URISyntaxException);
         }
       }
     }
   }
   
   public static void testSection427LinkHrefRelative() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.1/link-href-relative.xml
     URI uri = baseURI.resolve("4.2.7.1/link-href-relative.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       List<Link> links = entry.getLinks();
       for (Link link : links) {
         assertEquals(link.getResolvedHref(), uri.resolve("/2003/12/13/atom03"));
       }
     }
   }
   
   public static void testSection427LinkNoHref() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.1/link-no-href.xml
     //Note: not implemented
   }
   
   public static void testSection4272AbsoluteRel() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/absolute_rel.xml
     URI uri = baseURI.resolve("4.2.7.2/absolute_rel.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     assertEquals(feed.getLinks(Link.REL_ALTERNATE).size(), 1);
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       assertEquals(entry.getLinks(Link.REL_ALTERNATE).size(), 1);
     }
   }
   
   public static void testSection4272EmptyPath() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/empty-path.xml
     URI uri = baseURI.resolve("4.2.7.2/empty-path.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     List<Link> links = feed.getLinks(Link.REL_ALTERNATE);
     for (Link link : links) {
       assertEquals(link.getResolvedHref(), uri);
     }
   }
   
   public static void testSection4272LinkRelIsegmentNzNc() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/link-rel-isegment-nz-nc.xml
     //Note: not implemented
   }
   
   public static void testSection4272LinkRelRelative() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/link-rel-relative.xml
     //Note: not implemented
   }
   
   public static void testSection4272LinkRelSelfMatch() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/link-rel-self-match.xml
     //Note: not implemented
   }
   
   public static void testSection4272LinkRelSelfNoMatch() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/link-rel-self-nomatch.xml
     //Note: not implemented
   }
   
   public static void testSection4272SelfVsAlternate() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/self-vs-alternate.xml
     //Note: not implemented
   }
   
   public static void testSection4272UnregisteredRel() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.2/unregistered-rel.xml
     //Note: not implemented
   }
   
   public static void testSection4273LinkTypeInvalidMime() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.3/link-type-invalid-mime.xml
     URI uri = baseURI.resolve("4.2.7.3/link-type-invalid-mime.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       List<Link> links = entry.getLinks();
       for (Link link : links) {
         try {
           link.getMimeType();
         } catch (Exception e) {
           assertTrue(e instanceof MimeTypeParseException);
         }
       }
     }
   }
   
   public static void testSection4273LinkTypeParameters() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.3/link-type-parameters.xml
     URI uri = baseURI.resolve("4.2.7.3/link-type-parameters.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       List<Link> links = entry.getLinks();
       for (Link link : links) {
         assertEquals(link.getMimeType().getBaseType(), "text/html");
         assertEquals(link.getMimeType().getParameter("charset"), "utf-8");
       }
     }
   }
   
   public static void testSection4274LinkHreflangInvalidLanguage() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.4/link-hreflang-invalid-language.xml
     //Note: not implemented
   }
   
   public static void testSection4275LinkTitleWithBadchars() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.5/link-title-with-badchars.xml
     //Note: not implemented
   }
   
   public static void testSection4275LinkTitleWithHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.5/link-title-with-html.xml
     //Note: not implemented
   }
   
   public static void testSection4276LinkLengthNotPositive() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.7.6/link-length-not-positive.xml
     //Note: not implemented
   }
   
   public static void testSection428LogoInvalidUri() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.8/logo-invalid-uri.xml
     URI uri = baseURI.resolve("4.2.8/logo-invalid-uri.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     assertNotNull(feed.getLogoElement());
     try {
       feed.getLogoElement().getValue();
     } catch (Exception e) {
       assertTrue(e instanceof URISyntaxException);
     }
   }
   
   public static void testSection428LogoRelativeRef() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.8/logo_relative_ref.xml
     URI uri = baseURI.resolve("4.2.8/logo_relative_ref.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     assertNotNull(feed.getLogoElement());
     assertEquals(feed.getLogoElement().getResolvedValue(), uri.resolve("atomlogo.png"));
   }
   
   public static void testSection429PublishedInvalidDate() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.9/published-invalid-date.xml
     URI uri = baseURI.resolve("4.2.9/published-invalid-date.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       try {
         entry.getPublishedElement().getValue();
       } catch (Exception e) {
         assertTrue(e instanceof IllegalArgumentException);
       }
     }
   }
   
   public static void testSection4210RightsInvalidType() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.10/rights-invalid-type.xml
     // Note: not implemented
   }
   
   public static void testSection4210RightsTextWithEscapedHtml() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.10/rights-text-with-escaped-html.xml
     // Note: not implemented
   }
   
   public static void testSection4210RightsXhtmlNoXmldiv() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.10/rights-xhtml-no-xmldiv.xml
     // Note: not implemented
   }
   
   public static void testSection4211MissingId() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/missing-id.xml
     //Note: not implemented
   }
   
   public static void testSection4211MissingTitle() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/missing-title.xml
     //Note: not implemented
   }
   
   public static void testSection4211MissingUpdated() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/missing-updated.xml
     //Note: not implemented
   }
   
   public static void testSection4211MultipleAlternatesDiffering() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-alternates-differing.xml
     //Note: not implemented
   }
   
   public static void testSection4211MultipleAlternatesMatching() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-alternates-matching.xml
     //Note: not implemented
   }   
   
   public static void testSection4211MultipleAuthors() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-authors.xml
     URI uri = baseURI.resolve("4.2.11/multiple-authors.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Source source = entry.getSource();
       assertNotNull(source);
       assertEquals(source.getAuthors().size(),2);
     }
   }
   
   public static void testSection4211MultipleCategories() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-categories.xml
     URI uri = baseURI.resolve("4.2.11/multiple-categories.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Source source = entry.getSource();
       assertNotNull(source);
       assertEquals(source.getCategories().size(),2);
     }
   }   
   
   public static void testSection4211MultipleContributors() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-contributors.xml
     URI uri = baseURI.resolve("4.2.11/multiple-contributors.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       Source source = entry.getSource();
       assertNotNull(source);
       assertEquals(source.getContributors().size(),2);
     }
   }
   
   public static void testSection4211MultipleGenerators() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-generators.xml
     //Note: not implemented
   }
   
   public static void testSection4211MultipleIcons() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-icons.xml
     //Note: not implemented;
   }
   
   public static void testSection4211MultipleIds() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-ids.xml
     //Note: not implemented;
   }   
   
   public static void testSection4211MultipleLogos() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-logos.xml
     //Note: not implemented;
   }
   
   public static void testSection4211MultipleRights() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-rights.xml
     //Note: not implemented;
   }
   
   public static void testSection4211MultipleSubtitles() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-subtitles.xml
     //Note: not implemented;
   }
   
   public static void testSection4211MultipleTitles() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-titles.xml
     //Note: not implemented;
   }
   
   public static void testSection4211MultipleUpdateds() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/multiple-updateds.xml
     //Note: not implemented;
   }
   
   public static void testSection4211SourceEntry() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.11/source-entry.xml
     //Note: not implemented;
   }
   
   public static void testSection4212SubtitleBlank() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.12/subtitle-blank.xml
     //Note: not implemented;
   }
   
   public static void testSection4214TitleBlank() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.14/title-blank.xml
     //Note: not implemented;
   }
   
   public static void testSection4215UpdatedInvalidDate() throws Exception {
     //http://feedvalidator.org/testcases/atom/4.2.15/updated-invalid-date.xml
     URI uri = baseURI.resolve("4.2.15/updated-invalid-date.xml");
     Document<Feed> doc = get(uri);
     assertNotNull(doc);
     Feed feed = doc.getRoot();
     List<Entry> entries = feed.getEntries();
     for (Entry entry : entries) {
       try {
         entry.getUpdatedElement().getValue();
       } catch (Exception e) {
         assertTrue(e instanceof IllegalArgumentException);
       }
     }
   }
   
}
