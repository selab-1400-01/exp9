package codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
    public int num;
    public TypeAddress type;
    public VarType varType;

    public Address(int num, VarType varType, TypeAddress Type) {
        this.num = num;
        this.type = Type;
        this.varType = varType;
    }

    public Address(int num, VarType varType) {
        this.num = num;
        this.type = TypeAddress.Direct;
        this.varType = varType;
    }
    public String toString(){
        switch (type){
            case Direct:
                return num+"";
            case Indirect:
                return "@"+num;
            case Imidiate:
                return "#"+num;
        }
        return num+"";
    }
}
