package org.jvm.common.instantiation;

import lombok.Getter;

@Getter
public class Programer extends Person{
    private double height;
    private static final String JOB ="程序员";
    private static String skill;
    private String name="默认程序员";

    static {
        System.out.println(skill);

    }
    static {
        skill="Java";
        System.out.println("执行子类static方法，Job为"+getJob());
        Programer person=new Programer();
        System.out.println(person.getName());
    }
    static {
        System.out.println(skill);

    }

    public Programer() {
        super(2);
        System.out.println("执行子类无参构造函数，name为"+getName());
        //此时可以使用类里里面的函数
    }

    public Programer(int height) {
        System.out.println("执行子类有参构造函数，name为"+getName());
        this.height = height;
    }
    public static String getJob() {
        return JOB;
    }
}
