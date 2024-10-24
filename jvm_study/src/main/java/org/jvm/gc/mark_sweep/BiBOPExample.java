package org.jvm.gc.mark_sweep;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Block {
    int size;
    boolean isFree;
    Block next;

    Block(int size) {
        this.size = size;
        this.isFree = true;
        this.next = null;
    }
}

class BinnedBlockObjectPool {
    // 每个块的大小
    private final int BLOCK_SIZE = 64;
    // 最大桶数
    private final int MAX_BUCKETS = 10;
    private List<LinkedList<Block>> freeLists;

    public BinnedBlockObjectPool() {
        freeLists = new ArrayList<>(MAX_BUCKETS);
        for (int i = 0; i < MAX_BUCKETS; i++) {
            freeLists.add(new LinkedList<>());
        }
    }

    // 初始化内存块
    public void initialize(int numberOfBlocks) {
        for (int i = 0; i < numberOfBlocks; i++) {
            Block block = new Block(BLOCK_SIZE);
            // 所有块初始放在第一个桶
            freeLists.getFirst().add(block);
        }
    }

    // 分配块
    public Block allocate(int size) {
        int bucketIndex = size / BLOCK_SIZE -1;
        if (bucketIndex >= MAX_BUCKETS) {
            throw new IllegalArgumentException("Requested size is too large.");
        }

        // 查找合适的空闲块
        LinkedList<Block> freeList = freeLists.get(bucketIndex);
        if (!freeList.isEmpty()) {
            Block block = freeList.removeFirst();
            block.isFree = false;
            return block;
        }

        // 没有找到合适的块，返回 null
        return null;
    }

    // 释放块
    public void release(Block block) {
        if(block!=null){
            int bucketIndex = block.size / BLOCK_SIZE-1;
            LinkedList<Block> freeList = freeLists.get(bucketIndex);
            block.isFree = true;
            freeList.add(block);
        }
        //为null，说明该实体已被回收

    }

    // 简单的内存状态显示
    public void printMemoryState() {
        for (int i = 0; i < MAX_BUCKETS; i++) {
            System.out.println("Bucket " + i + ": " + freeLists.get(i).size() + " free blocks");
        }
    }
}

public class BiBOPExample {
    public static void main(String[] args) {
        BinnedBlockObjectPool pool = new BinnedBlockObjectPool();
        // 初始化 20 个内存块
        pool.initialize(20);

        System.out.println("Initial memory state:");
        pool.printMemoryState();

        // 分配内存块
        Block block1 = pool.allocate(64);
        Block block2 = pool.allocate(64);

        System.out.println("\nMemory state after allocations:");
        pool.printMemoryState();

        // 释放内存块
        pool.release(block1);
        pool.release(block2);

        System.out.println("\nMemory state after releases:");
        pool.printMemoryState();
    }
}