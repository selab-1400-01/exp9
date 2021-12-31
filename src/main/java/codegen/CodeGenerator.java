package codegen;

import errorhandling.ErrorHandler;
import logging.Log;
import scanner.token.Token;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.SymbolType;

import java.util.ArrayDeque;
import java.util.Deque;

public class CodeGenerator {
    private final Memory memory = new Memory();
    private final Deque<Address> ss = new ArrayDeque<>();
    private final Deque<String> symbolStack = new ArrayDeque<>();
    private final Deque<String> callStack = new ArrayDeque<>();
    private final SymbolTable symbolTable;

    public CodeGenerator() {
        symbolTable = new SymbolTable(memory);
    }

    public void printMemory() {
        memory.pintCodeBlock();
    }

    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        switch (func) {
            case 0:
                return;
            case 1:
                checkID();
                break;
            case 2:
                pid(next);
                break;
            case 3:
                fpid();
                break;
            case 4:
                kpid(next);
                break;
            case 5:
                intpid(next);
                break;
            case 6:
                startCall();
                break;
            case 7:
                call();
                break;
            case 8:
                arg();
                break;
            case 9:
                assign();
                break;
            case 10:
                add();
                break;
            case 11:
                sub();
                break;
            case 12:
                mult();
                break;
            case 13:
                label();
                break;
            case 14:
                save();
                break;
            case 15:
                endWhile();
                break;
            case 16:
                jpfSave();
                break;
            case 17:
                jpHere();
                break;
            case 18:
                print();
                break;
            case 19:
                equal();
                break;
            case 20:
                lessThan();
                break;
            case 21:
                and();
                break;
            case 22:
                not();
                break;
            case 23:
                defClass();
                break;
            case 24:
                defMethod();
                break;
            case 25:
                popClass();
                break;
            case 26:
                extend();
                break;
            case 27:
                defField();
                break;
            case 28:
                defVar();
                break;
            case 29:
                methodReturn();
                break;
            case 30:
                defParam();
                break;
            case 31:
                lastTypeBool();
                break;
            case 32:
                lastTypeInt();
                break;
            case 33:
                defMain();
                break;
        }
    }

    private void defMain() {
        memory.addTripleAddressCode(ss.pop().num,
                Operation.JP,
                new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null,
                null);
        String methodName = "main";
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void checkID() {
        symbolStack.pop();
    }

    public void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {
                Symbol s = symbolTable.get(className, methodName, next.value);
                VarType t = getVarType(s);
                ss.push(new Address(s.address, t));
            } catch (Exception e) {
                ss.push(new Address(0, VarType.NON));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, VarType.NON));
        }

        symbolStack.push(next.value);
    }

    public void fpid() {
        ss.pop();
        ss.pop();

        Symbol s = symbolTable.get(symbolStack.pop(), symbolStack.pop());
        VarType t = getVarType(s);
        ss.push(new Address(s.address, t));
    }

    private VarType getVarType(Symbol s) {
        return switch (s.type) {
            case BOOL -> VarType.BOOL;
            case INT -> VarType.INT;
        };
    }

    public void kpid(Token next) {
        ss.push(symbolTable.get(next.value));
    }

    public void intpid(Token next) {
        ss.push(new Address(Integer.parseInt(next.value), VarType.INT, TypeAddress.IMMEDIATE));
    }

    public void startCall() {
        ss.pop();
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolTable.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);
    }

    public void call() {
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            symbolTable.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }

        VarType t = switch (symbolTable.getMethodReturnType(className, methodName)) {
            case INT -> VarType.INT;
            case BOOL -> VarType.BOOL;
        };

        Address temp = new Address(memory.getTemp(), t);
        ss.push(temp);
        memory.addTripleAddressCode(
                Operation.ASSIGN,
                new Address(temp.num, VarType.ADDRESS, TypeAddress.IMMEDIATE),
                new Address(symbolTable.getMethodReturnAddress(className, methodName), VarType.ADDRESS),
                null);
        memory.addTripleAddressCode(
                Operation.ASSIGN,
                new Address(
                        memory.getCurrentCodeBlockAddress() + 2,
                        VarType.ADDRESS,
                        TypeAddress.IMMEDIATE),
                new Address(
                        symbolTable.getMethodCallerAddress(className, methodName),
                        VarType.ADDRESS),
                null);
        memory.addTripleAddressCode(Operation.JP, new Address(symbolTable.getMethodAddress(className, methodName), VarType.ADDRESS), null, null);
    }

    public void arg() {
        String methodName = callStack.pop();
        try {
            Symbol s = symbolTable.getNextParam(callStack.peek(), methodName);
            VarType t = getVarType(s);

            Address param = ss.pop();
            if (param.varType != t) {
                ErrorHandler.printError("The argument type isn't match");
            }

            memory.addTripleAddressCode(Operation.ASSIGN, param, new Address(s.address, t), null);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);
    }

    public void assign() {
        Address s1 = ss.pop();
        Address s2 = ss.pop();
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in assign is different ");
        }
        memory.addTripleAddressCode(Operation.ASSIGN, s1, s2, null);
    }

    public void add() {
        Address temp = new Address(memory.getTemp(), VarType.INT);
        Address s2 = ss.pop();
        Address s1 = ss.pop();

        if (s1.varType != VarType.INT || s2.varType != VarType.INT) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        memory.addTripleAddressCode(Operation.ADD, s1, s2, temp);
        ss.push(temp);
    }

    public void sub() {
        Address temp = new Address(memory.getTemp(), VarType.INT);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != VarType.INT || s2.varType != VarType.INT) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        memory.addTripleAddressCode(Operation.SUB, s1, s2, temp);
        ss.push(temp);
    }

    public void mult() {
        Address temp = new Address(memory.getTemp(), VarType.INT);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != VarType.INT || s2.varType != VarType.INT) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        memory.addTripleAddressCode(Operation.MULT, s1, s2, temp);
        ss.push(temp);
    }

    public void label() {
        ss.push(new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS));
    }

    public void save() {
        ss.push(new Address(memory.saveMemory(), VarType.ADDRESS));
    }

    public void endWhile() {
        memory.addTripleAddressCode(
                ss.pop().num,
                Operation.JPF,
                ss.pop(),
                new Address(memory.getCurrentCodeBlockAddress() + 1, VarType.ADDRESS),
                null);
        memory.addTripleAddressCode(Operation.JP, ss.pop(), null, null);
    }

    public void jpfSave() {
        Address save = new Address(memory.saveMemory(), VarType.ADDRESS);
        memory.addTripleAddressCode(
                ss.pop().num,
                Operation.JPF,
                ss.pop(),
                new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null);
        ss.push(save);
    }

    public void jpHere() {
        memory.addTripleAddressCode(
                ss.pop().num,
                Operation.JP,
                new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS),
                null,
                null);
    }

    public void print() {
        memory.addTripleAddressCode(Operation.PRINT, ss.pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        memory.addTripleAddressCode(Operation.EQ, s1, s2, temp);
        ss.push(temp);
    }

    public void lessThan() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != VarType.INT || s2.varType != VarType.INT) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        memory.addTripleAddressCode(Operation.LT, s1, s2, temp);
        ss.push(temp);
    }

    public void and() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != VarType.BOOL || s2.varType != VarType.BOOL) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        memory.addTripleAddressCode(Operation.AND, s1, s2, temp);
        ss.push(temp);
    }

    public void not() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != VarType.BOOL) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        memory.addTripleAddressCode(Operation.NOT, s1, s2, temp);
        ss.push(temp);
    }

    public void defClass() {
        ss.pop();
        symbolTable.addClass(symbolStack.peek());
    }

    public void defMethod() {
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void popClass() {
        symbolStack.pop();
    }

    public void extend() {
        ss.pop();
        symbolTable.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        ss.pop();
        symbolTable.addField(symbolStack.pop(), symbolStack.peek());
    }

    public void defVar() {
        ss.pop();

        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodLocalVariable(className, methodName, var);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        String methodName = symbolStack.pop();
        Address s = ss.pop();
        SymbolType t = symbolTable.getMethodReturnType(symbolStack.peek(), methodName);
        VarType temp = VarType.INT;
        switch (t) {
            case INT:
                break;
            case BOOL:
                temp = VarType.BOOL;
        }
        if (s.varType != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        memory.addTripleAddressCode(
                Operation.ASSIGN,
                s,
                new Address(
                        symbolTable.getMethodReturnAddress(symbolStack.peek(), methodName),
                        VarType.ADDRESS,
                        TypeAddress.INDIRECT),
                null);
        memory.addTripleAddressCode(
                Operation.JP,
                new Address(
                        symbolTable.getMethodCallerAddress(symbolStack.peek(), methodName),
                        VarType.ADDRESS),
                null,
                null);
    }

    public void defParam() {
        ss.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodParameter(className, methodName, param);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void lastTypeBool() {
        symbolTable.setLastType(SymbolType.BOOL);
    }

    public void lastTypeInt() {
        symbolTable.setLastType(SymbolType.INT);
    }
}
