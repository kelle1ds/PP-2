import java.util.HashMap;
import java.util.Map;

public class Tree implements Comparable<Tree>{

    private Double frequency;
    private String symbol;
    private Tree left;
    private Tree right;

    //Constructor for first node)
    public Tree(String c, Double v){
        this.frequency = v;
        this.symbol = c;
        left = null;
        right = null;
    }

    public Tree(Tree leftSide, Tree rightSide){
        this.symbol = "";
        this.frequency = rightSide.getFrequency() + leftSide.getFrequency();
        left = leftSide;
        right = rightSide;
    }

   public Tree(Double v){
        this.symbol = "";
        this.frequency = v;
        //this.value = "";
        left = null;
        right = null;
    }

    public Double getFrequency() {
        return frequency;
    }

    public String getCode() {
        return symbol;
    }

    public void setFrequency(Double value) {
        this.frequency = frequency;
    }

    public void setCode(String code) {
        this.symbol = code;
    }

    public boolean equals(Tree other){
        return this.getFrequency() == other.getFrequency();
    }

    public Tree getLeft() {
        return left;
    }

    public void setLeft(Tree left) {
        this.left = left;
    }

    public Tree getRight() {
        return right;
    }

    public void setRight(Tree right) {
        this.right = right;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String value) {
        this.symbol = symbol;
    }

    //Comparator
    public int compareTo(Tree other){
        if(this.equals(other)){
            return 0;
        }
        else if(getFrequency() > other.frequency){
            return 1;
        }
        else {
            return -1;
        }
    }

}
