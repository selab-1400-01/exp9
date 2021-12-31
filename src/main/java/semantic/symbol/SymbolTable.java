package semantic.symbol;


import codegen.ImmediateAddress;
import codegen.Memory;
import codegen.VarType;
import codegen.addresses.Address;
import errorhandling.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final Map<String, ProgramClass> programClassMap;
    private final Map<String, Address> keyWords;
    private final Memory mem;
    private SymbolType lastType;

    public SymbolTable(Memory memory) {
        mem = memory;
        programClassMap = new HashMap<>();
        keyWords = new HashMap<>();
        keyWords.put("true", new ImmediateAddress(1, VarType.BOOL));
        keyWords.put("false", new ImmediateAddress(0, VarType.BOOL));
    }

    public void setLastType(SymbolType type) {
        lastType = type;
    }

    public void addClass(String className) {
        if (programClassMap.containsKey(className)) {
            ErrorHandler.printError("This class already defined");
        }
        programClassMap.put(className, new ProgramClass());
    }

    public void addField(String fieldName, String className) {
        programClassMap.get(className).fields.put(fieldName, new Symbol(lastType, mem.getDateAddress()));
    }

    public void addMethod(String className, String methodName, int address) {
        if (programClassMap.get(className).methods.containsKey(methodName)) {
            ErrorHandler.printError("This method already defined");
        }
        programClassMap.get(className).methods.put(methodName, new Method(address, lastType));
    }

    public void addMethodParameter(String className, String methodName, String parameterName) {
        programClassMap.get(className).methods.get(methodName).addParameter(parameterName);
    }

    public void addMethodLocalVariable(String className, String methodName, String localVariableName) {
        if (programClassMap.get(className).methods.get(methodName).localVariable.containsKey(localVariableName)) {
            ErrorHandler.printError("This variable already defined");
        }
        programClassMap.get(className)
                .methods.get(methodName).localVariable
                .put(localVariableName, new Symbol(lastType, mem.getDateAddress()));
    }

    public void setSuperClass(String superClass, String className) {
        programClassMap.get(className).programClass = programClassMap.get(superClass);
    }

    public Address get(String keywordName) {
        return keyWords.get(keywordName);
    }

    public Symbol get(String fieldName, String className) {
        return programClassMap.get(className).getField(fieldName);
    }

    public Symbol get(String className, String methodName, String variable) {
        Symbol res = programClassMap.get(className).methods.get(methodName).getVariable(variable);
        if (res == null)
            res = get(variable, className);
        return res;
    }

    public Symbol getNextParam(String className, String methodName) {
        return programClassMap.get(className).methods.get(methodName).getNextParameter();
    }

    public void startCall(String className, String methodName) {
        programClassMap.get(className).methods.get(methodName).reset();
    }

    public int getMethodCallerAddress(String className, String methodName) {
        return programClassMap.get(className).methods.get(methodName).callerAddress;
    }

    public int getMethodReturnAddress(String className, String methodName) {
        return programClassMap.get(className).methods.get(methodName).returnAddress;
    }

    public SymbolType getMethodReturnType(String className, String methodName) {
        return programClassMap.get(className).methods.get(methodName).returnType;
    }

    public int getMethodAddress(String className, String methodName) {
        return programClassMap.get(className).methods.get(methodName).codeAddress;
    }


    class ProgramClass {
        private final Map<String, Symbol> fields;
        private final Map<String, Method> methods;
        private ProgramClass programClass;

        public ProgramClass() {
            fields = new HashMap<>();
            methods = new HashMap<>();
        }

        public Symbol getField(String fieldName) {
            if (fields.containsKey(fieldName)) {
                return fields.get(fieldName);
            }

            return programClass.getField(fieldName);
        }
    }

    class Method {
        private final List<String> orderedParameters;
        private final int codeAddress;
        private final Map<String, Symbol> parameters;
        private final Map<String, Symbol> localVariable;
        private final int callerAddress;
        private final int returnAddress;
        private final SymbolType returnType;
        private int index;

        public Method(int codeAddress, SymbolType returnType) {
            this.codeAddress = codeAddress;
            this.returnType = returnType;
            this.orderedParameters = new ArrayList<>();
            this.returnAddress = mem.getDateAddress();
            this.callerAddress = mem.getDateAddress();
            this.parameters = new HashMap<>();
            this.localVariable = new HashMap<>();
        }

        public Symbol getVariable(String variableName) {
            if (parameters.containsKey(variableName))
                return parameters.get(variableName);
            if (localVariable.containsKey(variableName))
                return localVariable.get(variableName);
            return null;
        }

        public void addParameter(String parameterName) {
            parameters.put(parameterName, new Symbol(lastType, mem.getDateAddress()));
            orderedParameters.add(parameterName);
        }

        private void reset() {
            index = 0;
        }

        private Symbol getNextParameter() {
            return parameters.get(orderedParameters.get(index++));
        }

    }
}