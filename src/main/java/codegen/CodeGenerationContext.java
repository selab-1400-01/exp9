package codegen;

import codegen.addresses.Address;
import scanner.token.Token;
import semantic.symbol.SymbolTable;

import java.util.Deque;

public class CodeGenerationContext {
    private final Memory memory;
    private final Deque<Address> semanticStack;
    private final Deque<String> symbolStack;
    private final Deque<String> callStack;
    private final SymbolTable symbolTable;

    private Token nextToken;

    public CodeGenerationContext(Memory memory,
                                 Deque<Address> semanticStack,
                                 Deque<String> symbolStack,
                                 Deque<String> callStack,
                                 SymbolTable symbolTable) {
        this.memory = memory;
        this.semanticStack = semanticStack;
        this.symbolStack = symbolStack;
        this.callStack = callStack;
        this.symbolTable = symbolTable;
    }

    public Memory getMemory() {
        return memory;
    }

    public Deque<Address> getSemanticStack() {
        return semanticStack;
    }

    public Deque<String> getSymbolStack() {
        return symbolStack;
    }

    public Deque<String> getCallStack() {
        return callStack;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Token getNextToken() {
        return nextToken;
    }

    public void setNextToken(Token nextToken) {
        this.nextToken = nextToken;
    }
}
