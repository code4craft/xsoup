package us.codecraft.xsoup;

/**
 * @author code4crafter@gmail.com
 */
public class Task implements Comparable<Task> {

    private long addTime;

    private int priority;

    //30秒的超时时间
    private static final int TIME_OUT = 30000;

    @Override
    public int compareTo(Task o) {
        if (Math.abs(this.addTime - o.addTime) > TIME_OUT) {
            if (this.addTime > o.addTime) {
                //创建时间越早，优先级越大
                return -1;
            } else {
                return 1;
            }

        }
        if (this.priority > o.priority) {
            return 1;
        } else if (this.priority == o.priority) {
            return 0;
        } else {
            return -1;
        }
    }
}
