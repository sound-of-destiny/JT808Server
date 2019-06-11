import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.*;

public class MainListener implements PropertyChangeListener {
    private ClassWithProperty test;

    public MainListener() {
        test = new ClassWithProperty();
        test.addPropertyChangeListener(this);
        test.setupOnlineUsers();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(test.getUsersOnline());
    }

   /* public static void main(String[] args) {
        new MainListener(); // do everything in the constructor
    }*/
    private static final int NTHREADS = 5;
    private static final int NTASKS = 100;
    private static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws InterruptedException {
        final CompletionService<Long> ecs = new ExecutorCompletionService<>(exec);
        for (int i = 0; i < NTASKS ; ++i) {
            final long ret = i;
            ecs.submit(() -> ret);
        }
        for (int i = 0; i < NTASKS; ++i) {
            try {
                long l = ecs.take().get();
                System.out.println(l);
            } catch (ExecutionException e) {
                e.getCause().printStackTrace();
            }
        }
        exec.shutdownNow();
        exec.awaitTermination(50, TimeUnit.MILLISECONDS);
    }
}
