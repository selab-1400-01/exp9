package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.VarType;
import codegen.addresses.DirectAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GeneralActionHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(13, this::label);
        functionalities.put(14, this::save);
        return functionalities;
    }

    private void label(CodeGenerationContext context) {
        context.getSemanticStack().push(
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS));
    }

    private void save(CodeGenerationContext context) {
        context.getSemanticStack().push(
                new DirectAddress(context.getMemory().saveMemory(), VarType.ADDRESS));
    }
}
