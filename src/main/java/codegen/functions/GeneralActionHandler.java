package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.DirectAddress;
import codegen.VarType;

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

    public void label(CodeGenerationContext context) {
        context.getSs().push(
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS));
    }

    public void save(CodeGenerationContext context) {
        context.getSs().push(
                new DirectAddress(context.getMemory().saveMemory(), VarType.ADDRESS));
    }
}
