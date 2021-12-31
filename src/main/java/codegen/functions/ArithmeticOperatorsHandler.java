package codegen.functions;

import codegen.*;
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

    public void add(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.INT);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();

        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        context.getMemory().addTripleAddressCode(Operation.ADD, s1, s2, temp);
        context.getSs().push(temp);
    }

    public void sub(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.INT);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        context.getMemory().addTripleAddressCode(Operation.SUB, s1, s2, temp);
        context.getSs().push(temp);
    }

    public void mult(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.INT);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        context.getMemory().addTripleAddressCode(Operation.MULT, s1, s2, temp);
        context.getSs().push(temp);
    }
}