package cn.microanswer.makeranduser;

import java.util.Date;

/**
 * 生产者。
 */
public class ProductMaker extends Thread{

    // 为了识别生产者，我们给生产者一个名字。
    private String mName;

    // 仓库。
    private ProductRoom productRoom;

    // 构造时，指定将生产的产品放到对应的仓库里。
    public ProductMaker (String name, ProductRoom productRoom) {
        this.mName = name;
        this.productRoom = productRoom;
    }

    @Override
    public void run() {
        super.run();

        // 生产者只要已启动，就不停的每5秒生产一个产品。
        while (true) {
            try {
                // 每五秒生产一个，只需要sleep5秒钟，来模拟耗时5秒钟才生产完成。
                Thread.sleep(5 * 1000);
            }catch (Exception ignore) {/* 暂时忽略异常 */}

            Object o = new Object();
            // 生产了一个。就放到仓库里
            productRoom.putAProduct(o);
            // 顺便打印一个提示。
            System.out.println("[" + new Date().toLocaleString() + "] " + this.mName + "生产了一个产品:@" + o.hashCode());
        }

    }

    public String getmName() {
        return mName;
    }
}
