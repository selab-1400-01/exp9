package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.addresses.Address;
import errorhandling.ErrorHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AssignmentHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(9, this::assign);
        return functionalities;
    }

    public void assign(CodeGenerationContext context) {
        Address s1 = context.getSs().pop();
        Address s2 = context.getSs().pop();
        if (s1.getVarType() != s2.getVarType()) {
            ErrorHandler.printError("The type of operands in assign is different ");
        }
        context.getMemory().addTripleAddressCode(Operation.ASSIGN, s1, s2, null);
    }
}
