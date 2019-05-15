package example2;

import java.util.List;

public class MyPattern {
    private int sizePattern;
    private List<Integer> pattern;
    private List<Integer> mementoQueue;

    public boolean match(){
        for (int i = 0; i < sizePattern; i++) {
            if(pattern.get(i) != mementoQueue.get(i)){
                return false;
            }
        }
        return true;
    }

    public boolean validateSize() {
        if (pattern.size() == sizePattern
                && mementoQueue.size() == sizePattern
                && mementoQueue.size() != pattern.size()) {
            return true;
        } else {
            return false;
        }
    }

}
