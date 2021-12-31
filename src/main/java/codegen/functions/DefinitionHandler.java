package codegen.functions;

import codegen.CodeGenerationContext;
import codegen.Operation;
import codegen.VarType;
import codegen.addresses.Address;
import codegen.addresses.IndirectAddress;
import errorhandling.ErrorHandler;
import semantic.symbol.SymbolType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DefinitionHandler extends AbstractSymbolHandler {
    @Override
    protected Map<Integer, Consumer<CodeGenerationContext>> getFunctionalities() {
        Map<Integer, Consumer<CodeGenerationContext>> functionalities = new HashMap<>();
        functionalities.put(23, this::defClass);
        functionalities.put(24, this::defMethod);
        functionalities.put(25, this::popClass);
        functionalities.put(26, this::extend);
        functionalities.put(27, this::defField);
        functionalities.put(28, this::defVar);
        functionalities.put(29, this::methodReturn);
        functionalities.put(30, this::defParam);
        return functionalities;
    }

    public void defClass(CodeGenerationContext context) {
        context.getSs().pop();
        context.getSymbolTable().addClass(context.getSymbolStack().peek());
    }

    public void defMethod(CodeGenerationContext context) {
        context.getSs().pop();
        String methodName = context.getSymbolStack().pop();
        String className = context.getSymbolStack().pop();

        context.getSymbolTable().addMethod(className, methodName, context.getMemory().getCurrentCodeBlockAddress());

        context.getSymbolStack().push(className);
        context.getSymbolStack().push(methodName);
    }

    public void popClass(CodeGenerationContext context) {
        context.getSymbolStack().pop();
    }

    public void extend(CodeGenerationContext context) {
        context.getSs().pop();
        context.getSymbolTable().setSuperClass(context.getSymbolStack().pop(), context.getSymbolStack().peek());
    }

    public void defField(CodeGenerationContext context) {
        context.getSs().pop();
        context.getSymbolTable().addField(context.getSymbolStack().pop(), context.getSymbolStack().peek());
    }

    public void defVar(CodeGenerationContext context) {
        context.getSs().pop();

        String var = context.getSymbolStack().pop();
        String methodName = context.getSymbolStack().pop();
        String className = context.getSymbolStack().pop();

        context.getSymbolTable().addMethodLocalVariable(className, methodName, var);

        context.getSymbolStack().push(className);
        context.getSymbolStack().push(methodName);
    }

    public void methodReturn(CodeGenerationContext context) {
        String methodName = context.getSymbolStack().pop();
        Address s = context.getSs().pop();
        SymbolType t = context.getSymbolTable().getMethodReturnType(context.getSymbolStack().peek(), methodName);
        VarType temp = VarType.INT;
        switch (t) {
            case INT:
                break;
            case BOOL:
                temp = VarType.BOOL;
        }
        if (s.getVarType() != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        context.getMemory().addTripleAddressCode(
                Operation.ASSIGN,
                s,
                new IndirectAddress(
                        context.getSymbolTable().getMethodReturnAddress(context.getSymbolStack().peek(), methodName),
                        VarType.ADDRESS),
                null);
        context.getMemory().addTripleAddressCode(
                Operation.JP,
                new IndirectAddress(
                        context.getSymbolTable().getMethodCallerAddress(context.getSymbolStack().peek(), methodName),
                        VarType.ADDRESS),
                null,
                null);
    }

    public void defParam(CodeGenerationContext context) {
        context.getSs().pop();
        String param = context.getSymbolStack().pop();
        String methodName = context.getSymbolStack().pop();
        String className = context.getSymbolStack().pop();

        context.getSymbolTable().addMethodParameter(className, methodName, param);

        context.getSymbolStack().push(className);
        context.getSymbolStack().push(methodName);
    }
}
