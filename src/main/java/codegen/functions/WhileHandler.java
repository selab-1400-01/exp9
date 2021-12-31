package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.DirectAddress;
import codegen.Operation;
import codegen.VarType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WhileHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(15, this::endWhile);
        return functionalities;
    }

    public void endWhile(CodeGenerationContext context) {
        context.getMemory().addTripleAddressCode(
                context.getSs().pop().getNum(),
                Operation.JPF,
                context.getSs().pop(),
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress() + 1, VarType.ADDRESS),
                null);
        context.getMemory().addTripleAddressCode(Operation.JP, context.getSs().pop(), null, null);
    }
}
