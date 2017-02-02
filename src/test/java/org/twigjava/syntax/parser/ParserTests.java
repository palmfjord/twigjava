package org.twigjava.syntax.parser;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.Token;
import org.twigjava.syntax.TokenStream;
import org.twigjava.syntax.parser.node.Module;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.type.PrintExpression;
import org.twigjava.syntax.parser.node.type.expression.Constant;
import org.twigjava.syntax.parser.node.type.expression.Name;

public class ParserTests {
    @Test
    public void canParseText() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.TEXT, "Hello world!", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 2));

        Parser parser = new Parser(new Environment());
        Module module = parser.parse(tokenStream);

        Node bodyNode = module.getBodyNode();
        Assert.assertEquals("Module node 1 should be of type text", "Hello world!", bodyNode.getAttribute("data"));
    }

    @Test
    public void canParseStringPrint() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.STRING, "foo", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());

        Constant stringConstant = new Constant("foo", 1);
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "First node should be of type PrintExpression",
                PrintExpression.class,
                module.getBodyNode().getClass()
        );
        Assert.assertEquals(
                "PrintExpession should contain constant",
                stringConstant.getAttribute("data"),
                module.getBodyNode().getNode(0).getAttribute("data")
        );
    }

    @Test
    public void canParseVariable() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "foo", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());

        Name name = new Name("foo", 1);
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "Body should be af type PrintExpression",
                PrintExpression.class,
                module.getBodyNode().getClass()
        );
        Assert.assertEquals(
                "Variable should be of type Name",
                Name.class,
                module.getBodyNode().getNode(0).getClass()
        );
        Assert.assertEquals(
                "Variable name should be correct",
                name.getAttribute("name"),
                module.getBodyNode().getNode(0).getAttribute("name")
        );
    }

    @Test
    public void canParseInteger() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "1", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "Body should be af type PrintExpression",
                PrintExpression.class,
                module.getBodyNode().getClass()
        );
        Assert.assertEquals(
                "Printed expression should be of type Constant",
                Constant.class,
                module.getBodyNode().getNode(0).getClass()
        );
        Assert.assertEquals(
                "Integer value should be correct",
                1,
                module.getBodyNode().getNode(0).getAttribute("data")
        );
    }

    @Test
    public void canParseBoolean() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "true", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "Body should be af type PrintExpression",
                PrintExpression.class,
                module.getBodyNode().getClass()
        );
        Assert.assertEquals(
                "Printed expression should be of type Constant",
                Constant.class,
                module.getBodyNode().getNode(0).getClass()
        );
        Assert.assertEquals(
                "Boolean value should be correct",
                true,
                module.getBodyNode().getNode(0).getAttribute("data")
        );
    }

    @Test
    public void canParseNull() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "null", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "Body should be af type PrintExpression",
                PrintExpression.class,
                module.getBodyNode().getClass()
        );
        Assert.assertEquals(
                "Printed expression should be of type Constant",
                Constant.class,
                module.getBodyNode().getNode(0).getClass()
        );
        Assert.assertEquals(
                "null value should be correct",
                null,
                module.getBodyNode().getNode(0).getAttribute("data")
        );
    }
}