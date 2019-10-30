package cn.microanswer.makeranduser;

public class Tester {
    public static void main(String[] args) {

        // 创建一个仓库
        ProductRoom room = new ProductRoom();

        // 创建2个生产者
        ProductMaker maker = new ProductMaker("生产者A", room);
        ProductMaker maker2 = new ProductMaker("生产者B", room);

        // 创建一个消费者
        ProductUser user = new ProductUser("消费者Z", room);

        // 开启生产和消费
        maker.start();
        maker2.start();
        user.start();

    }
}
