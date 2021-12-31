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

public class LogicsHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(19, this::equal);
        functionalities.put(20, this::lessThan);
        functionalities.put(21, this::and);
        functionalities.put(22, this::not);
        return functionalities;
    }

    public void equal(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.BOOL);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();
        if (s1.getVarType() != s2.getVarType()) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        context.getMemory().addTripleAddressCode(Operation.EQ, s1, s2, temp);
        context.getSs().push(temp);
    }

    public void lessThan(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.BOOL);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        context.getMemory().addTripleAddressCode(Operation.LT, s1, s2, temp);
        context.getSs().push(temp);
    }

    public void and(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.BOOL);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();
        if (s1.getVarType() != VarType.BOOL || s2.getVarType() != VarType.BOOL) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        context.getMemory().addTripleAddressCode(Operation.AND, s1, s2, temp);
        context.getSs().push(temp);
    }

    public void not(CodeGenerationContext context) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), VarType.BOOL);
        Address s2 = context.getSs().pop();
        Address s1 = context.getSs().pop();
        if (s1.getVarType() != VarType.BOOL) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        context.getMemory().addTripleAddressCode(Operation.NOT, s1, s2, temp);
        context.getSs().push(temp);
    }
}
