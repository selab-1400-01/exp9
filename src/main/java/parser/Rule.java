package parser;

import scanner.token.Token;

import java.util.ArrayList;

/**
 * Created by mohammad hosein on 6/25/2015.
 */
public class Rule {
    public Rule(String stringRule) {
        int index = stringRule.indexOf("#");
        if (index != -1) {
            try {
                semanticAction = Integer.parseInt(stringRule.substring(index + 1));
            } catch (NumberFormatException ex) {
                semanticAction = 0;
            }
            stringRule = stringRule.substring(0, index);
        } else {
            semanticAction = 0;
        }
        String[] split = stringRule.split("->");
        LHS = NonTerminal.valueOf(split[0]);
        RHS = new ArrayList<GrammarSymbol>();
        if (split.length > 1) {
            String[] RHSs = split[1].split(" ");
            for (String s : RHSs) {
                try {
                    RHS.add(new GrammarSymbol(NonTerminal.valueOf(s)));
                } catch (Exception e) {
                    RHS.add(new GrammarSymbol(new Token(Token.getTypeFormString(s), s)));
                }
            }
        }
    }

    public NonTerminal LHS;
    public ArrayList<GrammarSymbol> RHS;
    public int semanticAction;
}

class GrammarSymbol {
    public boolean isTerminal;
    public NonTerminal nonTerminal;
    public Token terminal;

    public GrammarSymbol(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        isTerminal = false;
    }

    public GrammarSymbol(Token terminal) {
        this.terminal = terminal;
        isTerminal = true;
    }
}