package codegen;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private final List<TripleAddressCode> codeBlock;
    private int lastTempIndex;
    private int lastDataAddress;

    public Memory() {
        codeBlock = new ArrayList<>();
        lastTempIndex = 500;
        lastDataAddress = 200;
    }

    public int getTemp() {
        int tempSize = 4;
        lastTempIndex += tempSize;
        return lastTempIndex - tempSize;
    }

    public int getDateAddress() {
        int dataSize = 4;
        lastDataAddress += dataSize;
        return lastDataAddress - dataSize;
    }

    public int saveMemory() {
        codeBlock.add(new TripleAddressCode());
        return codeBlock.size() - 1;
    }

    public void addTripleAddressCode(Operation op, Address opr1, Address opr2, Address opr3) {
        codeBlock.add(new TripleAddressCode(op, opr1, opr2, opr3));
    }

    public void addTripleAddressCode(int i, Operation op, Address opr1, Address opr2, Address opr3) {
        codeBlock.remove(i);
        codeBlock.add(i, new TripleAddressCode(op, opr1, opr2, opr3));
    }


    public int getCurrentCodeBlockAddress() {
        return codeBlock.size();
    }

    public void pintCodeBlock() {
        System.out.println("Code Block");
        for (int i = 0; i < codeBlock.size(); i++) {
            System.out.println(i + " : " + codeBlock.get(i).toString());
        }
    }
}

class TripleAddressCode {
    private Operation operation;
    private Address Operand1;
    private Address Operand2;
    private Address Operand3;

    public TripleAddressCode() {

    }

    public TripleAddressCode(Operation op, Address opr1, Address opr2, Address opr3) {
        operation = op;
        Operand1 = opr1;
        Operand2 = opr2;
        Operand3 = opr3;
    }

    public String toString() {
        if (operation == null) return "";
        StringBuffer res = new StringBuffer("(");
        res.append(operation.toString()).append(",");
        if (Operand1 != null)
            res.append(Operand1);
        res.append(",");
        if (Operand2 != null)
            res.append(Operand2);
        res.append(",");
        if (Operand3 != null)
            res.append(Operand3);
        res.append(")");

        return res.toString();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Address getOperand1() {
        return Operand1;
    }

    public void setOperand1(Address operand1) {
        Operand1 = operand1;
    }

    public Address getOperand2() {
        return Operand2;
    }

    public void setOperand2(Address operand2) {
        Operand2 = operand2;
    }

    public Address getOperand3() {
        return Operand3;
    }

    public void setOperand3(Address operand3) {
        Operand3 = operand3;
    }
}
