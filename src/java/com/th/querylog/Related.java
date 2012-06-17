package com.th.querylog;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.codehaus.jackson.map.ObjectMapper;

public class Related {

  private static Related instance;
  private final HttpSolrServer solr;
  private final SolrQuery query;

  public static class Relation {
    private List<Item> items;

    public Relation(List<Item> items) {
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

  private Related() {
    solr = new HttpSolrServer("http://localhost:8984/solr");
    query = new SolrQuery();
  }

  public static Related getInstance() {
    if (instance == null) {
      instance = new Related();
    }
    return instance;
  }

  public String related(String queryText) {
    query.setQuery("\"" + queryText + "\"");
    try {
      QueryResponse response = solr.query(query);
      FacetField queryField = response.getFacetField("query");
      
      List<Item> items = new ArrayList<Item>();
      for(Count facetCount : queryField.getValues()) {
        String query = facetCount.getName();
        if(query.equals(queryText)) continue;
        items.add(new Item(query));
      }
      Relation relateds = new Relation(items);
      
      StringWriter sw = new StringWriter();
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(sw, relateds);
      
      return sw.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
