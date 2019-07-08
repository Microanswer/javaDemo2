package cn.microanswer;


public class Test54 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年04月20日 23:12:47
     */
    public static void main(String[] args) throws Exception {

        createClient(arg -> null);

        // 上面的效果和下面的
        createClient(new Mission() {
            @Override
            public Object d0(Object arg) {
                // TODO
                return null;
            }
        });
        // 一样

    }


    public static void createClient(Mission mission) {

        Object arg = new Object();

        mission.d0(arg);
    }

    public static interface Mission {
        Object d0 (Object arg);
    }

}
