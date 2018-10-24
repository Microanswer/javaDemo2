package cn.microanswer;

public class Test {

    // main 方法程序执行入口。
    public static void main(String[] args) {


        // 创建一个 点。
        Point p = new Point(4, 20);

        Circle c1 = new Circle(new Point(2, 5), 2.7D);
        Circle c2 = new Circle(new Point(20, 18), 6.3D);

        // 打印两个圆与这个点的位置关系。
        c1.printPosition(p);
        c2.printPosition(p);

        // 打印两个圆的关系。
        c1.printPosition(c2);

        // 打印共创建了多少个点
        System.out.println("  共创建了 Point 对象：" + Point.i + "个");
    }

    // 创建 Point 类。
    static class Point {

        /**
         * 用于记录 Point 对象个数，将此变量定义为静态。
         * 注意：一但类中有静态变量，那么此类必须声明为 静态类， 观察此类的定义，你会看到 static 的身影。
         * 初始化此变量值为 0。
         */
        static int i = 0;

        /**
         * x 坐标
         */
        private int x;

        /**
         * y 坐标
         */
        private int y;

        /**
         * 构造函数。
         * 要求创建 Point 时需要初始化坐标，所以提供两个参数，进行坐标的初始化。
         * @param x x 坐标。
         * @param y y 坐标。
         */
        public Point (int x, int y) {

            // 注意： 此处 this 的使用是为了区别出哪个 x 是类里的变量，哪个是方法上定义的变量。
            //       具体的 this 含义及用法，请自行 百度一下。

            this.x = x;
            this.y = y;

            // 创建了一个 Point 对象则应该将 记录个数的 i 进行加 1。
            Point.i++;
        }

        /**
         * 获取该点 与 传入点的距离。
         * 注意： 要获取两点的距离，必须要传入另一个点，才能获取 其距离，不然一个点和谁获取距离。
         * @param point 要获取距离的另一个点。
         * @return 距离值。
         */
        public double getDistance (Point point) {

            /**
             * 根据 初中二年级 学习的两点间距离公式计算距离： |AB| = √((x1-x2)^2 + (y1-y2)^2)。
             */

            int x1 = this.getX();
            int x2 = point.getX();

            int y1 = this.getY();
            int y2 = point.getY();

            // 使用 Math.sqrt() 函数进行开根号, 使用 Math.abs() 函数获取绝对值, 使用 Math.pow 进行幂运算。
            return Math.abs(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 -y2, 2)));
        }

        /**
         * 由于 坐标声明成了 private 模式， 提供 公共的 get 方法，让外部可以获取。 下同。
         * 注意：由于此 Point 是在 Test 类的内部， 故 提供这个公共方法显得多余。
         *      但考虑到规范性，建议写上。
         * @return 返回 x 坐标值。
         */
        public int getX() { return x; }

        /**
         * 同 getX 方法。
         * @return 返回 y 坐标值。
         */
        public int getY() { return y;}
    }

    // 创建圆类。
    static class Circle {

        /**
         * 圆的圆心。
         */
        private Point o;

        /**
         * 圆的半径。
         */
        private double r;

        /**
         * 构造圆时要求初始化圆心和半径，故需要定义两个参数。
         * @param o 圆心。
         * @param r 半径。
         */
        public Circle (Point o, double r) {
            this.o = o;
            this.r = r;
        }

        /**
         * 判断某点与该圆的位置关系，并打印关系结果。
         * @param point 要判断的点。
         */
        public void printPosition (Point point) {

            /**
             * 思考： 要判断这个点在该圆的某位置，直接判断这个点与圆心的距离是否大于等于或小于圆心即可。
             */

            double distance = this.o.getDistance(point);

            System.out.print("  此点(" + point.getX() + ", " + point.getY() + "), 在圆" + toString());

            if (distance < r) { // 点与圆心的距离小于半径。
                System.out.println(" 内。");
            } else if (distance == r) {
                System.out.println(" 上。");
            } else {
                System.out.println(" 外。");
            }
        }

        /**
         * 判断某圆与该圆的位置关系。
         * @param circle 要判断的圆。
         */
        public void printPosition(Circle circle) {
            /**
             * 思考： 要判断两个圆的位置关系，需要参考中学知识，如图：
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             */

            // 两圆的圆心距离。
            double d = this.getO().getDistance(circle.getO());

            // 两圆的半径之和。
            double length = this.getR() + circle.getR();

            System.out.print("  圆" + toString() + ", 与圆" + circle.toString() + "的关系为：" );

            if (d > length) { // 两圆心距离大于两圆半径之和。
                System.out.println("外离。");

            } else if (d == length) { // 两圆心距离与两圆半径之和相等。
                System.out.println("外切。");

            } else { // 两圆心距离小于两圆半径之和，这时有几种情况。

                double r1 = this.getR();
                double r2 = circle.getR();

                if (Math.abs(r1 - r2) < d && d < Math.abs(r1 + r2)) {
                    System.out.println("相交。");

                } else if (d == Math.abs(r1 - r2)) {
                    System.out.println("内切。");

                } else if ( d < Math.abs(r1 - r2)) {
                    System.out.println("内含。");

                } else {
                    System.out.println("未知。");
                }
            }
        }

        /**
         * 获取圆心。
         * @return 返回该圆圆心。
         */
        private Point getO () { return o; }

        /**
         * 获取半径。
         * @return 返回半径。
         */
        private double getR () { return r;}

        /**
         * 重写 toString 方法，在打印圆信息时，以显示优美。
         * @return 返回圆信息。按字符串显示。
         */
        @Override
        public String toString() {
            return "(" + this.o.getX() + ", " + this.o.getY() + ", r" + this.r + ")";
        }
    }
}
