package com.th.querylog;

import static org.junit.Assert.*;

import org.junit.Test;

public class SuggestTest {

  @Test
  public void testSuggest() {
    String text = Suggest.getInstance().suggest("h");
    assertNotNull(text);
    System.out.println(text);
  }

}
