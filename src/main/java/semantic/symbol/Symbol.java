package semantic.symbol;

public class Symbol {
    private SymbolType type;
    private int address;

    public Symbol(SymbolType type, int address) {
        this.type = type;
        this.address = address;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
