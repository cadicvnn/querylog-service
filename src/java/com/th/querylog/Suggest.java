package com.th.querylog;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.jackson.map.ObjectMapper;

public class Suggest {

  private static Suggest instance;
  private final HttpSolrServer solr;
  private final SolrQuery query;

  public static class Suggestion {
    private List<Item> items;

    public Suggestion(List<Item> items) {
      this.items = items;
    }
    
    public List<Item> getItems() {
      return items;
    }

    public void setItems(List<Item> items) {
      this.items = items;
    }
  }

  public static class Item {
    private String item;
    
    public Item(String item) {
      this.item = item;
    }

    public String getItem() {
      return item;
    }

    public void setItem(String item) {
      this.item = item;
    }
  }

  private Suggest() {
    solr = new HttpSolrServer("http://localhost:8983/solr");
    query = new SolrQuery();
  }

  public static Suggest getInstance() {
    if (instance == null) {
      instance = new Suggest();
    }
    return instance;
  }

  public String suggest(String prefix) {
    query.setQuery(prefix);
    QueryResponse response;
    try {
      response = solr.query(query);
      SolrDocumentList sdl = response.getResults();
      
      
      List<Item> items = new ArrayList<Suggest.Item>();
      for(SolrDocument doc : sdl) {
        String query = doc.getFieldValue("query").toString();
        items.add(new Item(query));
      }
      Suggestion suggestion = new Suggestion(items);
      
      StringWriter sw = new StringWriter();
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(sw, suggestion);
      
      return sw.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
