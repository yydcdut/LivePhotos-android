package com.yydcdut.rxmarkdown.grammar.android;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnderLineGrammarTest {

    private UnderLineGrammar underLineGrammar;

    @Before
    public void before() {
        underLineGrammar = new UnderLineGrammar(null);
    }

    @Test
    public void testIsMath1() {
        boolean match = underLineGrammar.isMatch(
                "ugh it\\u2019s close enough to the park for adventurous kids to go exploring " +
                        "any time they like. That\\u2019s if ");
        Assert.assertTrue(match);
    }

    @Test
    public void testIsMath2() {
        boolean match = underLineGrammar.isMatch(
                "ugh itu2019s close enough to the park for adventurous kids to go exploring " +
                        "any time they  if ");
        Assert.assertFalse(match);
    }

}