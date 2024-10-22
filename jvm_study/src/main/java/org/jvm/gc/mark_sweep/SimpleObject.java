package org.jvm.gc.mark_sweep;

import java.util.ArrayList;
import java.util.List;

public class SimpleObject {
    String name;
    List<SimpleObject> references;

    SimpleObject(String name) {
        this.name = name;
        this.references = new ArrayList<>();
    }

    void addReference(SimpleObject obj) {
        references.add(obj);
    }

    @Override
    public String toString() {
        return name;
    }
}
