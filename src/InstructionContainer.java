
public class InstructionContainer {

    private int labelNum;

    private String byteWord;

    private String instructionText;

    private boolean labeled;

    public void setLabel(int labelNum) {
        this.labelNum = labelNum;
    }

    public void setByteWord(String byteWord) {
        this.byteWord = byteWord;
    }

    public void setInstructionText(String instructionText) {
        this.instructionText = instructionText;
    }

    public void setNeedsLabel(boolean labeled) {
        this.labeled = labeled;
    }

    public int getLabelNum() {
        return labelNum;
    }

    public String getByteWord() {
        return byteWord;
    }

    public String getInstructionText() {
        return instructionText;
    }

    public boolean isLabeled() {
        return labeled;
    }
}