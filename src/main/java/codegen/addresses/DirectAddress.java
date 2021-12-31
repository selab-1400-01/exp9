package codegen.addresses;

import codegen.VarType;

public class DirectAddress extends Address {
    public DirectAddress(int num, VarType varType) {
        super(num, varType);
    }

    @Override
    public String toString() {
        return Integer.toString(this.getNum());
    }
}
