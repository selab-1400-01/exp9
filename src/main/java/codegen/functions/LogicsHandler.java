package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;

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

    private void equal(CodeGenerationContext context) {
        this.addTripleAddressCode(
                context,
                VarType.BOOL,
                Operation.EQ,
                (s1, s2) -> s1.getVarType() == s2.getVarType(),
                "The type of operands in equal operator is different");
    }

    private void lessThan(CodeGenerationContext context) {
        this.addTripleAddressCode(
                context,
                VarType.BOOL,
                Operation.LT,
                (s1, s2) -> !(s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT),
                "The type of operands in less than operator is different");
    }

    private void and(CodeGenerationContext context) {
        this.addTripleAddressCode(
                context,
                VarType.BOOL,
                Operation.AND,
                (s1, s2) -> !(s1.getVarType() != VarType.BOOL || s2.getVarType() != VarType.BOOL),
                "In and operator the operands must be boolean");
    }

    private void not(CodeGenerationContext context) {
        this.addTripleAddressCode(
                context,
                VarType.BOOL,
                Operation.NOT,
                (s1, s2) -> s1.getVarType() == VarType.BOOL,
                "In not operator the operand must be boolean");
    }
}
