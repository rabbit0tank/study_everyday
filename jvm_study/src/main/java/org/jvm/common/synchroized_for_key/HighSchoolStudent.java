package org.jvm.common.synchroized_for_key;


import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter

public class HighSchoolStudent extends Student{
    private String schoolName;
    private double score;

    public static ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<String, Object>();
    public static Map<String,Integer> countMap=new HashMap<>();

    public HighSchoolStudent(){

    }
    public HighSchoolStudent(String schoolName, double score){
        this.schoolName=schoolName;
        this.score=score;
    }


    /**
     * 实现对String的线程安全访问控制
     * schoolName
     * 1.可以直接对String进行控制，但是不通用，一旦传入的是new String，而不是字符常量，那线程安全就会失控
     * 2.即使都用字符常量，由于字符常量存储与常量池中，会与其他异步控制混淆，比如
     */
    public void submit(){
        Object lock = lockMap.computeIfAbsent(schoolName, (s)-> new Object());
        synchronized (lock){
            System.out.println(schoolName+" 上交入学考试试卷 name is"+this.getName());
            save();
            System.out.println(schoolName+" 考试成绩录入完成");
            System.out.println("[HighSchool]"+countMap);
        }
    }

    public void save(){
        Integer count = countMap.getOrDefault(schoolName,0);
        countMap.put(schoolName,++count);
    }


}
