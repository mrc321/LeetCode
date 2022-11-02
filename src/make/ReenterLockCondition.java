package make;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLockCondition implements Runnable {

    public static ReentrantLock lock = new   java.util.concurrent.locks.ReentrantLock();
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        lock.lock();
        try {
            condition.await();
            System.out.println("Thread is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLockCondition t = new ReenterLockCondition();
        Thread thread = new Thread(t);
        thread.start();
        Thread.sleep(2000);
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
