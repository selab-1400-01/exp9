package codegen.functions;

import codegen.CodeGenerationContext;

import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractSymbolHandler implements ActionSymbolHandler {
    private final Map<Integer, Consumer<CodeGenerationContext>> functions;

    public AbstractSymbolHandler() {
        this.functions = this.getFunctionalities();
    }

    public boolean handle(int func, CodeGenerationContext context) {
        Consumer<CodeGenerationContext> functionality =
                this.functions.getOrDefault(func, null);
        if (functionality == null) {
            return false;
        }

        functionality.accept(context);
        return true;
    }

    protected abstract Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities();
}
