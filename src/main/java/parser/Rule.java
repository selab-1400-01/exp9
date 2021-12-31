package parser;

import scanner.token.Token;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private NonTerminal LHS;
    private List<GrammarSymbol> RHS;
    private int semanticAction;
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
        RHS = new ArrayList<>();
        if (split.length > 1) {
            String[] rhsList = split[1].split(" ");
            for (String s : rhsList) {
                try {
                    RHS.add(new GrammarSymbol(NonTerminal.valueOf(s)));
                } catch (Exception e) {
                    RHS.add(new GrammarSymbol(new Token(Token.getTypeFormString(s), s)));
                }
            }
        }
    }

    public NonTerminal getLHS() {
        return LHS;
    }

    public void setLHS(NonTerminal LHS) {
        this.LHS = LHS;
    }

    public List<GrammarSymbol> getRHS() {
        return RHS;
    }

    public void setRHS(List<GrammarSymbol> RHS) {
        this.RHS = RHS;
    }

    public int getSemanticAction() {
        return semanticAction;
    }

    public void setSemanticAction(int semanticAction) {
        this.semanticAction = semanticAction;
    }
}

class GrammarSymbol {
    private boolean isTerminal;
    private NonTerminal nonTerminal;
    private Token terminal;

    public GrammarSymbol(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        isTerminal = false;
    }

    public GrammarSymbol(Token terminal) {
        this.terminal = terminal;
        isTerminal = true;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Token getTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public void setTerminal(Token terminal) {
        this.terminal = terminal;
    }
}