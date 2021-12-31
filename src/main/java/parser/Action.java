package parser;

public class Action {
    private Act action;
    private int number;

    public Action(Act action, int number) {
        this.action = action;
        this.number = number;
    }

    public String toString() {
        switch (action) {
            case ACCEPT:
                return "acc";
            case SHIFT:
                return "s" + number;
            case REDUCE:
                return "r" + number;
        }
        return action.toString() + number;
    }

    public Act getAction() {
        return action;
    }

    public void setAction(Act action) {
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

enum Act {
    SHIFT,
    REDUCE,
    ACCEPT
}
