package codegen.functions;

import codegen.CodeGenerationContext;

public interface ActionSymbolHandler {
    boolean handle(int func, CodeGenerationContext context);
}
