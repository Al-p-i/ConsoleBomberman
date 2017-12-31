package tick;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Ticker {
    private static final int FPS = 5;
    private static final long FRAME_TIME = 1000 / FPS;
    private Queue<Tickable> tickables = new ConcurrentLinkedQueue<>();
    private long tickNumber = 0;
    private volatile boolean stop = false;

    public void gameLoop() {
        while (!stop && !Thread.currentThread().isInterrupted()) {
            long started = System.currentTimeMillis();
            System.out.println("---------- " + tickNumber + " ----------");
            act(FRAME_TIME);
            long elapsed = System.currentTimeMillis() - started;
            if (elapsed < FRAME_TIME) {
                //log.info("All tick finish at {} ms", elapsed);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                //log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }
            //log.info("{}: tick ", tickNumber);
            tickNumber++;
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void registerTickable(Tickable tickable) {
        tickables.add(tickable);
    }

    public void unregisterTickable(Tickable tickable) {
        tickables.remove(tickable);
    }

    private void act(long elapsed) {
        tickables.forEach(tickable -> tickable.tick(elapsed));
    }

    public long getTickNumber() {
        return tickNumber;
    }
}
