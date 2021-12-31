package parser;


import codegen.CodeGenerator;
import errorhandling.ErrorHandler;
import logging.Log;
import scanner.LexicalAnalyzer;
import scanner.token.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class Parser {
    private final List<Rule> rules;
    private final Deque<Integer> parsStack;
    private ParseTable parseTable;
    private LexicalAnalyzer lexicalAnalyzer;
    private final CodeGenerator cg;

    public Parser() {
        parsStack = new ArrayDeque<>();
        parsStack.push(0);
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rules = new ArrayList<>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                rules.add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cg = new CodeGenerator();
    }

    public void startParse(java.util.Scanner sc) {
        lexicalAnalyzer = new LexicalAnalyzer(sc);
        Token lookAhead = lexicalAnalyzer.getNextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                Log.print(lookAhead.toString() + "\t" + parsStack.peek());
                currentAction = parseTable.getActionTable(parsStack.peek(), lookAhead);
                Log.print(currentAction.toString());

                switch (currentAction.getAction()) {
                    case SHIFT:
                        parsStack.push(currentAction.getNumber());
                        lookAhead = lexicalAnalyzer.getNextToken();

                        break;
                    case REDUCE:
                        Rule rule = rules.get(currentAction.getNumber());
                        for (int i = 0; i < rule.getRHS().size(); i++) {
                            parsStack.pop();
                        }

                        Log.print(parsStack.peek() + "\t" + rule.getLHS());
                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.getLHS()));
                        Log.print(parsStack.peek() + "");
                        try {
                            cg.semanticFunction(rule.getSemanticAction(), lookAhead);
                        } catch (Exception e) {
                            Log.print("Code Generator Error");
                        }
                        break;
                    case ACCEPT:
                        finish = true;
                        break;
                }
                Log.print("");

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

        }
        if (!ErrorHandler.isHasError())
            cg.printMemory();
    }
}
