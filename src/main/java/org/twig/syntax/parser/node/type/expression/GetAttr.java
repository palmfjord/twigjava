package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;

public class GetAttr extends Expression {
    public GetAttr(Integer line) {
        super(line);
    }

    public GetAttr(Expression node, Expression attribute, Expression arguments, String type, Integer line) {
        super(line);
        addNode(0, node);
        addNode(1, attribute);
        addNode(2, arguments);

        putAttribute("type", type);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
        compiler
                .writeRaw("getAttribute(")
                .subCompile(getNode(0)) // The "name" node
                .writeRaw(", ")
                .subCompile(getNode(1)) // The "item" (attribute)
                .writeRaw(", ")
                .subCompile(getNode(2)) // The arguments
                .writeRaw(", ")
                .representValue(getAttribute("type")) // ie "method"
                .writeRaw(")");
    }
}