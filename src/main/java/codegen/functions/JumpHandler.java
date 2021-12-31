package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.Address;
import codegen.addresses.DirectAddress;

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
                context.getSemanticStack().pop().getNum(),
                Operation.JPF,
                context.getSemanticStack().pop(),
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null);
        context.getSemanticStack().push(save);
    }

    public void jpHere(CodeGenerationContext context) {
        context.getMemory().addTripleAddressCode(
                context.getSemanticStack().pop().getNum(),
                Operation.JP,
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null,
                null);
    }
}
