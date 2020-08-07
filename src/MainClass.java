import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        CyclicBarrier cb=new CyclicBarrier(MainClass.CARS_COUNT+1);//Т.к. 4- Thread отвечают за машины
                                                                          //5-TreadMain
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),cb);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            cb.await();//Все машины подготовятся одновременно
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            cb.await();//Данный await нужен чтобы дать всем потокам отпечатать "Гонка началась"
                        //Часть потоков может отпечатать Гонка началась и Участник начал этап
                        //и другая часть отпечатает только Гонка началась
                        //В результате напечатается Гонка началаст->участник начал гонку->Гонка началась
            cb.await();//Когда все машины доедут до финиша одновременно для всех напечатается Гонка закончилась
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }


    }
}

