package cn.microanswer.makeranduser;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品仓库
 */
public class ProductRoom {

    // 同步锁对象。
    private final Object lock = new Object();

    // 所有产品都放置在这个集合里。
    private List<Object> products;

    public ProductRoom () {
        products = new ArrayList<>();
    }


    // 从仓库中取一个产品出来。
    public Object takeAProduct() throws Exception{

        // 使用同步代码块进行同步，保证每个产品不会被多次获取。
        synchronized (lock) {

            // 只要仓库里没有产品，就在这儿等着。
            while (products.isEmpty()) {
                lock.wait();
            }

            // 能运行到这里，说明必然是有产品的。
            return products.remove(0);
        }
    }

    // 放一个产品到仓库里面。
    public void putAProduct(Object obj) {
        synchronized (lock) {
            products.add(obj);

            // 放了一个产品进去后，有可能有些消费者还在等着获取产品呢。
            // 所以，这里就调一次唤醒，让等待的消费者能继续获取产品。
            lock.notifyAll();
        }
    }

}
