package taskone;

import java.util.List;
import java.util.ArrayList;

class StringList {

    List<String> strings = new ArrayList<String>();

    public void add(String str) {
        int pos = strings.indexOf(str);
        if (pos < 0) {
            strings.add(str);
        }
    }

    public boolean contains(String str) {
        return strings.indexOf(str) >= 0;
    }

    public int size() {
        return strings.size();
    }

    public String toString() {
        return strings.toString();
    }

    // New methods
    public void clear() {
        strings.clear();
    }

    public int indexOf(String str) {
        return strings.indexOf(str);
    }

    public void remove(int index) {
        if (index >= 0 && index < strings.size()) {
            strings.remove(index);
        }
    }

    public void set(int index, String str) {
        if (index >= 0 && index < strings.size()) {
            strings.set(index, str);
        }
    }

    // New method to get an element at a specific index
    public String get(int index) {
        if (index >= 0 && index < strings.size()) {
            return strings.get(index);
        } else {
            return null;
        }
    }
}
