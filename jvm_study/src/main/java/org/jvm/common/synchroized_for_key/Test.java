package org.jvm.common.synchroized_for_key;

import java.util.concurrent.CompletableFuture;

public class Test {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            HighSchoolStudent student = new HighSchoolStudent();
            student.setSchoolName("清华");
            student.setName("张三");
            student.submit();
        });
        Thread thread1 = new Thread(()->{
            HighSchoolStudent student = new HighSchoolStudent();
            student.setSchoolName("清华");
            student.setName("李四");

            student.submit();
        });
        Thread thread2 = new Thread(()->{
            HighSchoolStudent student = new HighSchoolStudent();
            student.setSchoolName("北大");
            student.setName("王五");

            student.submit();
        });
        workInCollage();
        thread.start();
        thread1.start();
        thread2.start();


        System.out.println(HighSchoolStudent.countMap);
    }

    public static void workInCollage(){
        Thread thread = new Thread(()->{
            CollageStudent student = new CollageStudent();
            student.setCollageName("清华");
            student.setName("张三");
            student.submit();
        });
        Thread thread1 = new Thread(()->{
            CollageStudent student = new CollageStudent();
            student.setCollageName("清华");
            student.setName("李四");

            student.submit();
        });
        Thread thread2 = new Thread(()->{
            CollageStudent student = new CollageStudent();
            student.setCollageName("北大");
            student.setName("王五");

            student.submit();
        });
        CompletableFuture.runAsync(()->{
            thread.start();
            thread1.start();
            thread2.start();

        }).thenRun(()->{
            System.out.println(HighSchoolStudent.countMap);
            System.out.println(CollageStudent.countMap);
        });



    }
}
