package cn.microanswer;


import java.util.ArrayList;
import java.util.List;

public class Test30 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月20日 09:10:36
     */
    public static void main(String[] args) throws Exception {

        // 要了解啥是接口，下面用简单的白话结合代码。
        // 书面和老师教学都不够浅显易懂。甚至会用接口这个词语本身来解释什么接口。
        // 这是不正确的引导教学方法。

        // 接口：interface。
        // 我们先不把它称为接口， 咱们把它称为：规则。对，先就叫规则。
        // 咱们就认为： interface ≈≈ 规则。
        // 这样一来看下面的代码。


        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Student s = new Student((int) Math.round(Math.random() * 1000));
            students.add(s);
        }

        // 进行排序
        sort(students);

        // 输出
        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i).getInfo());
        }
    }


    // 定义一个方法，这个方法可以完成排序功能。但是问题来了。我要的排序功能是要兼容对各种不同的类对象进行
    // 排序哦，那到底要怎么做到可以让不同的对象都能使用我这个方法排序呢。
    // 经过思考，咱们有了一套规则,这个规则告诉具体的类要怎么进行排序，是正序反序就由这套规则来决定。
    // 这套规则叫：SortAble，只要是满足这个规则的类，就都可以使用本方法进行排序了。
    public static <O extends SortAble> void sort(List<O> sortAble) {

        // 使用循环。拿到每一个对象，调用规则中的方法确定他们的排序先后。
        for (int i = 0; i < sortAble.size(); i++) {
            O value = sortAble.get(i);
            for (int j = i + 1; j < sortAble.size(); j++) {
                O other = sortAble.get(j);
                int than = value.than(other);

                if (than >= 1) { // 大于 1 认为 value 比 other 大， 大的排后面。这里进行交换。
                    sortAble.set(j, value);
                    sortAble.set(i, other);
                } else if (than == 0) { // 等于 0 认为 value 和 other 相等。
                    // 相等不用处理。
                } else { // 小于 0 认为 value 比 other 小。 不用交换。
                    // 小， 不用处理。
                }
            }
        }
    }


    // 定义出这套规则。 如果不统一这个规则，那么每个人都写一个类都自己定义那个比较方法
    // 我的排序方法中怎么知道你的比较的方法是哪一个，所以，统一定义一个排序比较规则。
    interface SortAble {

        // 定义出要比较的方法，这就相当于为这个规则添加了一条具体的比较大小方案。
        int than(Object other);
    }


    // 测试学生类。 Student， 要对学生类进行排序，那么排序方法就要知道你这个学生类
    // 要怎么排序噻， 所以为了在排序方法中知道学生类怎么排序。那么实现方法规定的那个规则
    // 将要排序的规则写在实现方法里。这样结合排序方法，才能较好的实现对学生的排序。
    static class Student implements SortAble {

        int age;

        Student(int age) {
            this.age = age;
        }


        @Override
        public int than(Object other) {

            // 按年龄比较。

            Student s = (Student) other;
            if (s.age > age) {
                return 1;
            } else if (s.age == age) {
                return 0;
            } else {
                return -1;
            }
        }

        public String getInfo() {
            return "Student(" + age + ")";
        }
    }

}
