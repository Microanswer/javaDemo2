package cn.microanswer.makeranduser;

import java.util.Date;

/**
 * 消费者类
 */
public class ProductUser extends Thread {

    // 为了记录是哪一个消费者，我们定一个姓名。
    private String mName;

    // 定义消费者要消费哪一个仓库里的产品。
    private ProductRoom productRoom;

    public ProductUser(String mName, ProductRoom productRoom) {
        this.mName = mName;
        this.productRoom = productRoom;
    }

    @Override
    public void run() {
        super.run();

        while (true) {

            try {
                // 从仓库取出消耗一个产品。

                System.out.println("[" + new Date().toLocaleString() + "] " + mName + "获取产品..");
                Object o = productRoom.takeAProduct();
                System.out.println("[" + new Date().toLocaleString() + "] " + "成功并消费了：@" + o.hashCode());

                // 每3秒就消耗一个产品。
                Thread.sleep(3 * 1000);
            } catch (Exception ignore) {/*暂时忽略异常*/}
        }
    }
}
