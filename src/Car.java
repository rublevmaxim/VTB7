import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static Object monitor=new Object();
    static boolean haveWinner=false;
    static AtomicInteger ai =new AtomicInteger();

    static {
        CARS_COUNT = 0;
    }
    private CyclicBarrier cb;
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrier cb) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cb=cb;
    }
    @Override
    public void run() {
        try {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cb.await();//Для синхронизации
            cb.await();//с await в Main
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

            cb.await();//Для синхронизации с await в Main. Машина доехала

            //Определение победителя
            //Вариант №1 Монитор
           /* synchronized (monitor){
                if (!haveWinner) {
                    haveWinner = true;
                    System.out.println("Победитель "+name);
                }
            }*/

            //Вариант №2 с помощью атомарных типов
            if (ai.incrementAndGet()==1){
                System.out.println("Победитель "+name);
            }
            //Вариант №3 Можно организовать защёлку lock.trylock
            //но она останется навсегда защёлкнутой(не очень)

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

