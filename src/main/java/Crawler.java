/**
 * Created by Uchqun on 30.06.14.
 */
public class Crawler {

    public static int subNetwork = 0;

    public static void main(String... arg){

        int start = 1;
        int stop = 250;
        int length = 25;
        int threadCount = 10;
        /*CrawlerThread t = new CrawlerThread(254, 255, subNetwork);
        Thread thread = new Thread(t);
        thread.start();*/
        CrawlerThread[] threads = new CrawlerThread[threadCount];
        for(int i = 0; i < threadCount; i++){
            threads[i] = new CrawlerThread(start + i * length, start + (i+1)*length, subNetwork);
        }
        for (CrawlerThread thread : threads){
            Thread t = new Thread(thread);
            t.start();
        }
    }


}