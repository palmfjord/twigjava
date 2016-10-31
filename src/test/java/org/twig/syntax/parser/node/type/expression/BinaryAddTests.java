package org.twig.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

import static org.mockito.Mockito.*;

public class BinaryAddTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        Node left = new Node(1);
        Node right = new Node(2);
        BinaryAdd concatNode = new BinaryAdd(left, right, 1);

        when(compilerStub.writeRaw(anyString())).thenReturn(compilerStub);
        when(compilerStub.subCompile(anyObject())).thenReturn(compilerStub);

        concatNode.compile(compilerStub);

        verify(compilerStub).subCompile(left);
        verify(compilerStub).writeRaw(" + ");
        verify(compilerStub).subCompile(right);
    }
}
