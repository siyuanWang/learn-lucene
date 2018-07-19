package com.wsy.learn.lucene;

public class Test {
    public void a() {
        System.out.println("step1");
        {
            System.out.println("step2");
        }
        System.out.println("step3");
        {
            System.out.println("step4");
        }
        {
            System.out.println("step5");
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.a();
    }
}
