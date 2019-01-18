package cn.microanswer;


public class Test49 {

    /**
     * @param args 主参数
     * @author Microanswer
     * @date 2019年01月04日 16:45:13
     */
    public static void main(String[] args) throws Exception {


    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        String num1 = getNum(l1);
        String num2 = getNum(l2);
        return getLn(num1 + num2);
    }

    private String getNum(ListNode ln) {
        StringBuilder num1 = new StringBuilder(String.valueOf(ln.val));
        while (ln.next != null) {
            num1.append(ln.next.val);
            ln = ln.next;
        }
        return num1.reverse().toString();
    }

    private ListNode getLn(long num) {
        String n = String.valueOf(num);
        ListNode ds = null;
        ListNode ln = null;
        for (int i = n.length() - 1; i >= 0; i--) {
            int v = Integer.parseInt("" + n.charAt(i));
            if (ln == null) {
                ln = new ListNode(v);
                ds = ln;
            } else {
                ln.next = new ListNode(v);
                ln = ln.next;
            }
        }

        return ds;
    }
}
