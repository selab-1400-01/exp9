package codegen;

public abstract class Address {
    private final int num;
    private final VarType varType;

    public Address(int num, VarType varType) {
        this.num = num;
        this.varType = varType;
    }

    public int getNum() {
        return num;
    }

    public VarType getVarType() {
        return varType;
    }
}
