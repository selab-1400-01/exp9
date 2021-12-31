package codegen.functions;

import codegen.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JumpHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(16, this::jpfSave);
        functionalities.put(17, this::jpHere);
        return functionalities;
    }

    public void jpfSave(CodeGenerationContext context) {
        Address save = new DirectAddress(context.getMemory().saveMemory(), VarType.ADDRESS);
        context.getMemory().addTripleAddressCode(
                context.getSs().pop().getNum(),
                Operation.JPF,
                context.getSs().pop(),
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null);
        context.getSs().push(save);
    }

    public void jpHere(CodeGenerationContext context) {
        context.getMemory().addTripleAddressCode(
                context.getSs().pop().getNum(),
                Operation.JP,
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null,
                null);
    }
}
