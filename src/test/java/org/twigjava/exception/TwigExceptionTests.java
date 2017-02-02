package org.twigjava.exception;

import org.junit.Assert;
import org.junit.Test;

public class TwigExceptionTests {
    @Test
    public void canCreateWithPlainMessage() {
        TwigException e = new TwigException("error");

        Assert.assertEquals(e.getMessage(), "error");
    }

    @Test
    public void canCreateWithTemplateNameAndLineNumberAndTrailingDot() {
        TwigException e = new TwigException("Error.", "foo.html.twigjava", 1337);

        Assert.assertEquals(e.getMessage(), "Error in \"foo.html.twigjava\" at line 1337.");
    }

    @Test
    public void canCreateWithTemplateNameAndLineNumberAndTrailingQuestionMark() {
        TwigException e = new TwigException("Error?", "foo.html.twigjava", 1337);

        Assert.assertEquals(e.getMessage(), "Error in \"foo.html.twigjava\" at line 1337?");
    }
}