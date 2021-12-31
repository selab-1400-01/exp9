package codegen;

import codegen.functions.*;
import logging.Log;
import scanner.token.Token;
import semantic.symbol.SymbolTable;

import java.util.ArrayDeque;
import java.util.List;

public class CodeGenerator {
    private final CodeGenerationContext context;

    private final Iterable<ActionSymbolHandler> handlers;

    public CodeGenerator() {
        Memory memory = new Memory();
        this.context = new CodeGenerationContext(
                memory,
                new ArrayDeque<>(),
                new ArrayDeque<>(),
                new ArrayDeque<>(),
                new SymbolTable(memory));

        this.handlers = List.of(new ActionSymbolHandler[]{
                new ArithmeticOperatorsHandler(),
                new AssignmentHandler(),
                new CallingHandler(),
                new DefaultMethodsHandler(),
                new DefinitionHandler(),
                new GeneralActionHandler(),
                new IdPusherHandler(),
                new JumpHandler(),
                new LastTypeHandler(),
                new LogicsHandler(),
                new WhileHandler(),
        });
    }

    public void printMemory() {
        this.context.getMemory().printCodeBlock();
    }

    public void performSemanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        if (func == 0) {
            return;
        }

        context.setNextToken(next);
        for (ActionSymbolHandler handler : this.handlers) {
            if (handler.handle(func, context)) {
                return;
            }
        }

        throw new IllegalStateException("Functionality couldn't be handled.");
    }

}
