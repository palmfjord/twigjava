package org.twigjava.syntax.parser.node.type;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class ForLoop extends Node {
    public ForLoop(Integer line) {
        super(line);
    }

    public ForLoop(Integer line, String tag) {
        super(line);
        setTag(tag);

        // TODO change attributes to false when we find out where it's changed to true in another place (probably a node visitor)
        putAttribute("with_loop", true);
        putAttribute("ifExpr", true);
        putAttribute("else", true);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        if (((Boolean)getAttribute("else"))) {
            compiler.writeLine("context.put(\"_iterated\", true);");
        }

        if (((Boolean)getAttribute("with_loop"))) {
            compiler
                    .writeLine("((org.twigjava.util.HashMap)context.get(\"loop\"))")
                    .indent()
                        .writeLine(".put(\"index\", ((Integer)((org.twigjava.util.HashMap)context.get(\"loop\")).get(\"index\")) + 1)")
                        .writeLine(".put(\"index0\", ((Integer)((org.twigjava.util.HashMap)context.get(\"loop\")).get(\"index0\")) + 1)")
                        .writeLine(".put(\"first\", false);")
                    .unIndent();
        }

        if (((Boolean)getAttribute("ifExpr"))) {
            compiler
                    .writeLine("if (((org.twigjava.util.HashMap)context.get(\"loop\")).containsKey(\"length\")) {")
                    .indent()
                        .writeLine("((org.twigjava.util.HashMap)context.get(\"loop\")).put(\"revindex0\", ((Integer)((org.twigjava.util.HashMap)context.get(\"loop\")).get(\"revindex0\")) - 1);")
                        .writeLine("((org.twigjava.util.HashMap)context.get(\"loop\")).put(\"revindex\", ((Integer)((org.twigjava.util.HashMap)context.get(\"loop\")).get(\"revindex\")) - 1);")
                        .writeLine("((org.twigjava.util.HashMap)context.get(\"loop\")).put(\"last\", ((Integer)((org.twigjava.util.HashMap)context.get(\"loop\")).get(\"revindex0\")) == 0);")
                    .unIndent()
                    .writeLine("}");
        }
    }
}
