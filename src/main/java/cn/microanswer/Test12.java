package cn.microanswer;


public class Test12 {

    /**
     *
     * @param args 主参数
     * @author Microanswer
     * @date 2018年11月310日 18:08:03
     */
    public static void main(String[] args) throws Exception {
        new a().start();
        for (int i = 0; i < 100; i++) {
            Thread.currentThread().setPriority(1);
            System.out.println(Thread.currentThread().getName() + "----" + i);
        }
    }


    static class a extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "----" + i);
            }
        }
    }
}
