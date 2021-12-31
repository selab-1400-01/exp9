package codegen;

public class IndirectAddress extends Address {
    public IndirectAddress(int num, VarType varType) {
        super(num, varType);
    }

    @Override
    public String toString() {
        return "@" + this.getNum();
    }
}
