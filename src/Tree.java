import java.util.HashMap;
import java.util.Map;

public class Tree implements Comparable<Tree>{

    private Double frequency;
    private String code;


    private String value;

    private Tree left;
    private Tree right;

    public Tree(String c, Double v){
        this.frequency = v;
        this.code = c;
        this.value = "";
        left = null;
        right = null;
    }

    public Tree(Double v){
        this.frequency = v;
        this.value = "";
        left = null;
        right = null;
    }

    public Double getFrequency() {
        return frequency;
    }

    public String getCode() {
        return code;
    }

    public void setFrequency(Double value) {
        this.frequency = frequency;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
