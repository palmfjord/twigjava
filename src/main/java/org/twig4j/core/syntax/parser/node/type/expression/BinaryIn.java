package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryIn extends Binary {
    public BinaryIn(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler.writeRaw("(org.twig4j.core.extension.Core.inFilter(");
        getLeftNode().compile(compiler);
        compiler.writeRaw(", ");
        getRightNode().compile(compiler);
        compiler.writeRaw("))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case
        return this;
    }
}
