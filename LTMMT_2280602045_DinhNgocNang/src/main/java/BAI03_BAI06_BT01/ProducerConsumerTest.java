package BAI03_BAI06_BT01;

public class ProducerConsumerTest {

    // Lop kho hang (nha phan phoi)
    static class CubbyHole {
        private int maHangHoa;
        private boolean available = false; // false = kho trong, true = kho co hang

        public synchronized int get() {
            while (!available) {
                try { wait(); }
                catch (InterruptedException e) {}
            }
            System.out.println("Consumer (Khach) DA LAY hang so: " + maHangHoa);

            available = false;
            notifyAll();
            return maHangHoa;
        }

        public synchronized void put(int value) {
            while (available) {
                try { wait(); }
                catch (InterruptedException e) {}
            }
            maHangHoa = value;
            available = true;

            System.out.println("Producer (Nha SX) DA THEM hang so: " + maHangHoa);

            notifyAll();
        }
    }

    // Nha san xuat
    static class Producer extends Thread {
        private CubbyHole cubbyhole;
        private int number;

        public Producer(CubbyHole c, int number) {
            cubbyhole = c;
            this.number = number;
        }

        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    int time = (int)(Math.random() * 1500);
                    System.out.println("...Producer dang san xuat mon hang so " + i +
                                       " (mat " + time + "ms)...");
                    sleep(time);
                } catch (InterruptedException e) {}
                
                cubbyhole.put(i);
            }
        }
    }

    // Khach hang tieu thu
    static class Consumer extends Thread {
        private CubbyHole cubbyhole;
        private int number;

        public Consumer(CubbyHole c, int number) {
            cubbyhole = c;
            this.number = number;
        }

        public void run() {
            for (int i = 1; i <= 10; i++) {
                int value = cubbyhole.get();
                try { sleep((int)(Math.random() * 1000)); }
                catch (InterruptedException e) {}
            }
        }
    }

    // Ham main CHUAN
    public static void main(String[] args) {

        CubbyHole c = new CubbyHole();

        Producer p1 = new Producer(c, 1);
        Consumer c1 = new Consumer(c, 1);

        System.out.println("Bat dau mo phong quy trinh san xuat - tieu thu:");

        p1.start();
        c1.start();
    }
}
