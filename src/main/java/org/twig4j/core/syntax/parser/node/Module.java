package org.twig4j.core.syntax.parser.node;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.compiler.Compilable;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.type.Block;
import org.twig4j.core.syntax.parser.node.type.expression.Constant;

import java.util.Map;

public class Module implements Compilable {
    protected Node bodyNode;
    protected Node parent;
    protected Map<String, Block> blocks;

    protected String fileName = "";

    public Module(Node bodyNode) {
        this.bodyNode = bodyNode;
    }

    public Module(Node bodyNode, Node parent, Map<String, Block> blocks, String fileName) {
        this.bodyNode = bodyNode;
        this.parent = parent;
        this.blocks = blocks;
        this.fileName = fileName;
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        String className = compiler.getEnvironment().getTemplateClass(this.fileName);

        compileClassHeader(compiler, className);

        compileConstructor(compiler, className);

        compileDisplay(compiler);

        compileBlocks(compiler);

        compileClassFooter(compiler);
    }

    protected void compileClassHeader(ClassCompiler compiler, String className) throws LoaderException {
        String baseClass = compiler.getEnvironment().getTemplateBaseClass();

        compiler
                .writeLine("package " + compiler.getEnvironment().getTemplatePackage() + ";\n")
                .writeLine("import org.twig4j.core.exception.Twig4jException;\n")
                .writeLine("/**")
                .writeLine(" * ".concat(this.fileName))
                .writeLine(" */")
                .write("public class ".concat(className))
                .writeLine(" extends ".concat(baseClass).concat(" {"))
                .indent();
    }

    protected void compileConstructor(ClassCompiler compiler, String className) throws LoaderException, Twig4jRuntimeException {
        compiler
            .writeLine("public " + className + "(org.twig4j.core.Environment environment) throws Twig4jException {")
            .indent()
                .writeLine("this.environment = environment;\n");

        if (blocks.entrySet().size() > 0) {
            compiler
                .writeLine("try {")
                .indent();

            for (Map.Entry block : blocks.entrySet()) {
                compiler.writeLine("blocks.put(\"" + block.getKey() + "\", new TemplateBlockMethodSet(this, this.getClass().getMethod(\"block_" + block.getKey() + "\", Context.class, java.util.Map.class)));");
            }

            compiler
                .unIndent()
                .writeLine("} catch (NoSuchMethodException e) {")
                .indent()
                .writeLine("throw new org.twig4j.core.exception.Twig4jRuntimeException(\"Could not find method for block (\\\"\" + e.getMessage() + \"\\\").\", getTemplateName(), -1, e);")
                .unIndent()
                .writeLine("}")
                .unIndent();
        }
        compiler
            .writeLine("}\n");
    }

    protected void compileClassFooter(ClassCompiler compiler) {
        compiler
                .writeLine("public String getTemplateName() {")
                .indent()
                    .write("return ")
                    .writeString(getFileName())
                    .writeRaw(";\n")
                .unIndent()
                .writeLine("}")
                .unIndent()
                .writeLine("}");
    }

    protected void compileDisplay(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler
                .writeLine("protected String doDisplay(Context context, java.util.Map<String, TemplateBlockMethodSet> blocks) throws Twig4jException {")
                    .indent();

        if (parent != null) {
            compiler
                .addDebugInfo(parent)
                .write("parent = loadTemplate(")
                .subCompile(parent)
                .writeRaw(", \"" + getFileName() + "\", 1, null);\n\n");
        }

        compiler
                    .writeLine("java.util.Map<String, Object> tmpForParent;")
                    .writeLine("StringBuilder output = new StringBuilder();");

        compiler
                    .subCompile(this.getBodyNode());

        if (parent != null) {
            compiler.addDebugInfo(parent);
            if (parent instanceof Constant) {
                compiler
                    .writeLine("java.util.Map<String, TemplateBlockMethodSet> mergedBlocks = new java.util.HashMap<>();")
                    .writeLine("mergedBlocks.putAll(this.blocks);")
                    .writeLine("mergedBlocks.putAll(blocks);")
                    .writeLine("output.append(parent.display(context, mergedBlocks));");
            } else {
                // TODO
                throw new RuntimeException("not implemented yet");
            }
        }

        compiler
                .writeLine("return output.toString();")
            .unIndent()
            .writeLine("}");
    }

    protected void compileBlocks(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        if (blocks == null) {
            return;
        }

        for (Map.Entry<String, Block> block : blocks.entrySet()) {
            block.getValue().compile(compiler);
        }
    }

    public Node getBodyNode() {
        return bodyNode;
    }

    public void setBodyNode(Node bodyNode) {
        this.bodyNode = bodyNode;
    }

    public Node getParent() {
        return parent;
    }

    public Module setParent(Node parent) {
        this.parent = parent;
        return this;
    }

    public Map<String, Block> getBlocks() {
        return blocks;
    }

    public Module setBlocks(Map<String, Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
