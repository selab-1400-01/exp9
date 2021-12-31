package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.ImmediateAddress;
import codegen.VarType;
import codegen.addresses.DirectAddress;
import semantic.symbol.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class IdPusherHandler extends AbstractSymbolHandler {

    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(1, this::checkID);
        functionalities.put(2, this::pid);
        functionalities.put(3, this::fpid);
        functionalities.put(4, this::kpid);
        functionalities.put(5, this::intpid);
        return functionalities;
    }

    private void checkID(CodeGenerationContext context) {
        context.getSymbolStack().pop();
    }

    private void pid(CodeGenerationContext context) {
        if (context.getSymbolStack().size() > 1) {
            String methodName = context.getSymbolStack().pop();
            String className = context.getSymbolStack().pop();
            try {
                Symbol s = context.getSymbolTable()
                        .get(className, methodName, context.getNextToken().getValue());
                VarType t = VarType.fromSymbolType(s.getType());
                context.getSemanticStack().push(new DirectAddress(s.getAddress(), t));
            } catch (Exception e) {
                context.getSemanticStack().push(new DirectAddress(0, VarType.NON));
            }
            context.getSymbolStack().push(className);
            context.getSymbolStack().push(methodName);
        } else {
            context.getSemanticStack().push(new DirectAddress(0, VarType.NON));
        }

        context.getSymbolStack().push(context.getNextToken().getValue());
    }

    private void fpid(CodeGenerationContext context) {
        context.getSemanticStack().pop();
        context.getSemanticStack().pop();

        Symbol s = context.getSymbolTable()
                .get(context.getSymbolStack().pop(), context.getSymbolStack().pop());
        VarType t = VarType.fromSymbolType(s.getType());
        context.getSemanticStack().push(new DirectAddress(s.getAddress(), t));
    }

    private void kpid(CodeGenerationContext context) {
        context.getSemanticStack()
                .push(context.getSymbolTable().get(context.getNextToken().getValue()));
    }

    private void intpid(CodeGenerationContext context) {
        context.getSemanticStack().push(
                new ImmediateAddress(
                        Integer.parseInt(context.getNextToken().getValue()),
                        VarType.INT));
    }
}
