package cn.microanswer;

public class Test14 {

    // 售票厅， 可以在售票厅里抢票。
    static class TicketHouse {

        // 记录所有的票数，被抢一张，此字段减一
        int totalCount;

        // 记录被卖出的票
        int selledCount;

        TicketHouse(int totalCount) {
            this.totalCount = totalCount;
        }

        // 抢票方法，调用该方法获取一张票。
        int getTicket() {

            // 由于一张票必须保证只能给一个人，所以，添加同步代码块
            synchronized (this) {

                // 如果已经没有票了，返回 -1 ，表示没抢到
                if (totalCount <= 0) {
                    return -1;
                }

                totalCount--;
                selledCount++;

                return 1;
            }

        }

    }

    // 通过继承 Thread 类，模拟玩家来抢票。
    static class Player extends Thread {
        TicketHouse ticketHouse;

        String playerName;
        boolean hasTicket; // 标记这位玩家是否抢到票了。

        Player (String name) {
            this.playerName = name;
        }

        @Override
        public void run() {
            super.run();
            int ticket = ticketHouse.getTicket();
            hasTicket = ticket == 1;
            System.out.println(playerName + (hasTicket ? "抢到票了 √" : "没抢到票 ×"));
        }

        // 开始抢票，
        // 既然要抢票，就要传入在哪儿抢票。 TicketHouse
        void start(TicketHouse ticketHouse) {
            this.ticketHouse = ticketHouse;
            super.start();
        }
    }

    public static void main(String[] args) {
        TicketHouse ticketHouse = new TicketHouse(2);

        Player lbw = new Player("卢本伟");
        Player xdd = new Player("小叮当");
        Player gfc = new Player("郭富城");

        lbw.start(ticketHouse);
        xdd.start(ticketHouse);
        gfc.start(ticketHouse);

    }
}
