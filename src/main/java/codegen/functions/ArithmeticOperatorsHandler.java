package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.Address;
import codegen.addresses.DirectAddress;
import errorhandling.ErrorHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ArithmeticOperatorsHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(10, this::add);
        functionalities.put(11, this::sub);
        functionalities.put(12, this::mult);
        return functionalities;
    }

    private void add(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.INT);
        Address s2 = context.getSemanticStack().pop();
        Address s1 = context.getSemanticStack().pop();

        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        context.getMemory().addTripleAddressCode(Operation.ADD, s1, s2, temp);
        context.getSemanticStack().push(temp);
    }

    private void sub(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.INT);
        Address s2 = context.getSemanticStack().pop();
        Address s1 = context.getSemanticStack().pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        context.getMemory().addTripleAddressCode(Operation.SUB, s1, s2, temp);
        context.getSemanticStack().push(temp);
    }

    private void mult(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.INT);
        Address s2 = context.getSemanticStack().pop();
        Address s1 = context.getSemanticStack().pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        context.getMemory().addTripleAddressCode(Operation.MULT, s1, s2, temp);
        context.getSemanticStack().push(temp);
    }
}
