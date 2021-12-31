package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.ImmediateAddress;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.Address;
import codegen.addresses.DirectAddress;
import errorhandling.ErrorHandler;
import semantic.symbol.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CallingHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(6, this::startCall);
        functionalities.put(7, this::call);
        functionalities.put(8, this::arg);
        return functionalities;
    }

    public void startCall(CodeGenerationContext context) {
        context.getSemanticStack().pop();
        context.getSemanticStack().pop();
        String methodName = context.getSymbolStack().pop();
        String className = context.getSymbolStack().pop();
        context.getSymbolTable().startCall(className, methodName);
        context.getCallStack().push(className);
        context.getCallStack().push(methodName);
    }

    public void call(CodeGenerationContext context) {
        String methodName = context.getCallStack().pop();
        String className = context.getCallStack().pop();
        try {
            context.getSymbolTable().getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException ignored) {
        }

        VarType t = switch (context.getSymbolTable().getMethodReturnType(className, methodName)) {
            case INT -> VarType.INT;
            case BOOL -> VarType.BOOL;
        };

        Address temp = new DirectAddress(context.getMemory().getTemp(), t);
        context.getSemanticStack().push(temp);
        context.getMemory().addTripleAddressCode(
                Operation.ASSIGN,
                new ImmediateAddress(temp.getNum(), VarType.ADDRESS),
                new DirectAddress(
                        context.getSymbolTable().getMethodReturnAddress(className, methodName),
                        VarType.ADDRESS),
                null);
        context.getMemory().addTripleAddressCode(
                Operation.ASSIGN,
                new ImmediateAddress(
                        context.getMemory().getCurrentCodeBlockAddress() + 2,
                        VarType.ADDRESS),
                new DirectAddress(
                        context.getSymbolTable().getMethodCallerAddress(className, methodName),
                        VarType.ADDRESS),
                null);
        context.getMemory().addTripleAddressCode(Operation.JP,
                new DirectAddress(
                        context.getSymbolTable().getMethodAddress(className, methodName),
                        VarType.ADDRESS),
                null,
                null);
    }

    public void arg(CodeGenerationContext context) {
        String methodName = context.getCallStack().pop();
        try {
            Symbol s = context.getSymbolTable().getNextParam(context.getCallStack().peek(), methodName);
            VarType t = VarType.fromSymbolType(s.getType());

            Address param = context.getSemanticStack().pop();
            if (param.getVarType() != t) {
                ErrorHandler.printError("The argument type isn't match");
            }

            context.getMemory().addTripleAddressCode(Operation.ASSIGN, param, new DirectAddress(s.getAddress(), t), null);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        context.getCallStack().push(methodName);
    }
}
