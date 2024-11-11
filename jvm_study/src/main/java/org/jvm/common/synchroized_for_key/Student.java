package org.jvm.common.synchroized_for_key;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String name;
    private int age;
    private String gender;


}
