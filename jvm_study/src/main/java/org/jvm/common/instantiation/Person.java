package org.jvm.common.instantiation;

import lombok.Getter;

@Getter
public class Person {
    private int age;
    private static final String category="哺乳类";
    private static String motherland;
    private String country="China";

    static {
        System.out.println(motherland);

    }
    static {
        motherland="地球";
        System.out.println("执行static方法，category为"+getCategory());
        Person person=new Person();
        System.out.println(person.getCountry());
    }
    static {
        System.out.println(motherland);

    }

    public Person() {
        System.out.println("执行无参构造函数，country为"+country);
        //此时可以使用类里里面的函数
    }

    public Person(int age) {
        System.out.println("执行有参构造函数，country为"+country);

        this.age = age;
    }
    public static String getCategory() {
        return category;
    }

    public static void main(String[] args) {
        System.out.println("测试开始");
        Person p = new Person();
    }
}
