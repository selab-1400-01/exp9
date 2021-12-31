package codegen;

import semantic.symbol.SymbolType;

public enum VarType {
    INT,
    BOOL,
    NON,
    ADDRESS;

    public static VarType fromSymbolType(SymbolType symbolType) {
        return switch (symbolType) {
            case BOOL -> VarType.BOOL;
            case INT -> VarType.INT;
        };
    }
}
