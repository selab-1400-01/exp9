package codegen;

import codegen.addresses.Address;

public class ImmediateAddress extends Address {
    public ImmediateAddress(int num, VarType varType) {
        super(num, varType);
    }

    @Override
    public String toString() {
        return "#" + this.getNum();
    }
}
