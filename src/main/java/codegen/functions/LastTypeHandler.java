package codegen.functions;

import codegen.CodeGenerationContext;
import semantic.symbol.SymbolType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LastTypeHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(31, this::lastTypeBool);
        functionalities.put(32, this::lastTypeInt);
        return functionalities;
    }

    private void lastTypeBool(CodeGenerationContext context) {
        context.getSymbolTable().setLastType(SymbolType.BOOL);
    }

    private void lastTypeInt(CodeGenerationContext context) {
        context.getSymbolTable().setLastType(SymbolType.INT);
    }
}