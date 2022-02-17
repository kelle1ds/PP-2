import java.util.HashMap;
import java.util.Map;

public class Tree implements Comparable<Tree>{

    private Double value;
    private String code;
    private Tree left = null;
    private Tree right = null;

    public Tree(String c, Double v){
        this.value = v;
        this.code = c;
    }

    public Tree(Double v){
        this.value = v;
    }

    public Double getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean equals(Tree other){
        return this.getValue() == other.value;
    }


    public int compareTo(Tree other){
        if(this.equals(other)){
            return 0;
        }
        else if(getValue() > other.value){
            return 1;
        }
        else {
            return -1;
        }
    }


}
