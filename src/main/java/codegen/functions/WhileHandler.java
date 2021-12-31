package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.DirectAddress;

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
                context.getSemanticStack().pop().getNum(),
                Operation.JPF,
                context.getSemanticStack().pop(),
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress() + 1, VarType.ADDRESS),
                null);
        context.getMemory().addTripleAddressCode(Operation.JP, context.getSemanticStack().pop(), null, null);
    }
}
