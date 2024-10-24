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

    public RefCountedObject() {
    }

    public RefCountedObject(Object reference) {
        this.reference = reference;
        addReference();
    }

    public void addReference() {
        referenceCount++;
        if (reference == null) {
            nextReference.addReference();
        }
    }


    public void setNextReference(RefCountedObject nextReference) {

        this.nextReference = nextReference;
        nextReference.addReference();
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
