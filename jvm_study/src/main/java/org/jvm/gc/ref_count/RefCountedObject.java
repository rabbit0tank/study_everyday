package org.jvm.gc.ref_count;


import lombok.Setter;

/**
 * @author liqh
 */
@Setter
public class RefCountedObject {
    private int referenceCount = 0;
    private Object reference = null;
    private RefCountedObject nextReference;
    private static final int MAX_REF_COUNT = Integer.MAX_VALUE - 1; // 防止溢出

    public RefCountedObject() {
    }

    public RefCountedObject(Object reference) {
        this.reference = reference;
        addReference();
    }

    public void addReference() {
        if (referenceCount < MAX_REF_COUNT) {
            referenceCount++;
        } else {
            markForGC();
        }
        if (reference == null) {
            nextReference.addReference();
        }
    }


    public void setNextReference(RefCountedObject nextReference) {

        this.nextReference = nextReference;
        nextReference.addReference();
    }

    private void markForGC() {
        // 标记该对象为待清理
        System.out.println("Object marked for GC due to reference count overflow.");
        // 可以在这里触发标记-清除过程
        System.gc();
    }

    public void removeReference() {
        referenceCount--;
        if(nextReference != null) {
            nextReference.removeReference();
        }
        if (referenceCount == 0) {
            // 释放资源
            cleanUp();
        }
    }

    private void cleanUp() {
        reference=null;
        System.out.println("Cleaning up resources...");
        // 释放对象占用的资源
    }
}
