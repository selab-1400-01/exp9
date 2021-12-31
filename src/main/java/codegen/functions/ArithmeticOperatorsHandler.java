package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.Address;

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
        this.addTripleAddressCode(
                context,
                VarType.INT,
                Operation.ADD,
                ArithmeticOperatorsHandler::checkOperands,
                "In addition two operands must be integer");
    }

    private void sub(CodeGenerationContext context) {
        this.addTripleAddressCode(
                context,
                VarType.INT,
                Operation.SUB,
                ArithmeticOperatorsHandler::checkOperands,
                "In subtract two operands must be integer");
    }

    private void mult(CodeGenerationContext context) {
        this.addTripleAddressCode(
                context,
                VarType.INT,
                Operation.MULT,
                ArithmeticOperatorsHandler::checkOperands,
                "In multiplication two operands must be integer");
    }

    private static Boolean checkOperands(Address s1, Address s2) {
        return !(s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT);
    }
}
