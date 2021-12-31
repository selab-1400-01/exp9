package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.DirectAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultMethodsHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(33, this::defMain);
        functionalities.put(18, this::print);
        return functionalities;
    }

    private void defMain(CodeGenerationContext context) {
        context.getMemory().addTripleAddressCode(context.getSemanticStack().pop().getNum(),
                Operation.JP,
                new DirectAddress(context.getMemory().getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null,
                null);
        String methodName = "main";
        String className = context.getSymbolStack().pop();

        context.getSymbolTable()
                .addMethod(className, methodName, context.getMemory().getCurrentCodeBlockAddress());

        context.getSymbolStack().push(className);
        context.getSymbolStack().push(methodName);
    }

    public void print(CodeGenerationContext context) {
        context.getMemory().addTripleAddressCode(Operation.PRINT, context.getSemanticStack().pop(), null, null);
    }
}
