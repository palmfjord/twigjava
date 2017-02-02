package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

import static org.mockito.Mockito.*;

public class BinarySubtractTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        Node left = new Node(1);
        Node right = new Node(2);
        BinarySubtract subtractNode = new BinarySubtract(left, right, 1);

        when(compilerStub.writeRaw(anyString())).thenReturn(compilerStub);
        when(compilerStub.subCompile(anyObject())).thenReturn(compilerStub);

        subtractNode.compile(compilerStub);

        verify(compilerStub).subCompile(left);
        verify(compilerStub).writeRaw(" - ");
        verify(compilerStub).subCompile(right);
    }
}