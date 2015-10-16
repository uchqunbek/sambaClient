import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.net.MalformedURLException;

/**
 * Created by Uchqun on 01.07.14.
 */
public class CrawlerThread implements Runnable {

    public int start;
    public int stop;
    public int subNetwork;

    public CrawlerThread(int start, int stop, int subNetwork){
        this.start = start;
        this.stop = stop;
        this.subNetwork = subNetwork;
    }

    @Override
    public void run() {
        for (int i = start; i < stop; i++){
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, null, null);
            try {
                SmbFile file = new SmbFile("smb://192.168."+subNetwork+"."+i+"/", auth);
                if (file.exists()){
                    String[] list = file.list();
                    System.out.println("List of \\\\192.168."+subNetwork+"."+i+"\\ content:");
                    for(String str : list){
                        System.out.print(str + ", ");
                    }
                    System.out.println();
                }
            } catch (MalformedURLException e) {
                System.out.println("Cannot connect to \\\\192.168."+subNetwork+"."+i+"\\: "+e.getMessage());
            } catch (SmbException e) {
                System.out.println("Cannot connect to \\\\192.168."+subNetwork+"."+i+"\\: "+e.getMessage());
            }
        }
    }
}
