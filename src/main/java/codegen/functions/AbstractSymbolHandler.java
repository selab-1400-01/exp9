package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.Address;
import codegen.addresses.DirectAddress;
import errorhandling.ErrorHandler;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class AbstractSymbolHandler implements ActionSymbolHandler {
    private final Map<Integer, Consumer<CodeGenerationContext>> functions;

    public AbstractSymbolHandler() {
        this.functions = this.getFunctionalities();
    }

    public boolean handle(int func, CodeGenerationContext context) {
        Consumer<CodeGenerationContext> functionality =
                this.functions.getOrDefault(func, null);
        if (functionality == null) {
            return false;
        }

        functionality.accept(context);
        return true;
    }

    protected abstract Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities();

    protected void addTripleAddressCode(
            CodeGenerationContext context,
            VarType varType,
            Operation operation,
            BiFunction<Address, Address, Boolean> operandsChecker,
            String errorMessage) {
        Address temp = new DirectAddress(context.getMemory().getTemp(), varType);
        Address s2 = context.getSemanticStack().pop();
        Address s1 = context.getSemanticStack().pop();
        if (!operandsChecker.apply(s1, s2)) {
            ErrorHandler.printError(errorMessage);
        }
        context.getMemory().addTripleAddressCode(operation, s1, s2, temp);
        context.getSemanticStack().push(temp);
    }
}
