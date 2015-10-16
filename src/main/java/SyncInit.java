import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFilenameFilter;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Uchqun on 10.02.2015.
 */
public class SyncInit {

    public static final Logger logger = Logger.getLogger("SambaClient");

    static {
        logger.setLevel(Level.ALL);
    }

    //private static String[] extensions = {"mp3"};
    private static String[] extensions = {"asf","avi", "divx", "mpe", "mkv", "mpg", "mpg4", "mov", /*"rm", */"vob", "wmv", "xvid"};
    //private static String[] extensions = {".apk"};
    //private static String[] extensions = {"Jun10.exe"};
    private static int stop = 255;
    static class SuperThread implements Runnable{

        @Override
        public void run() {
            for (;;) {
                int c = getCounter();
                if (c == -1) return;
                try {
                    SmbFile file = new SmbFile("smb://192.168." + subNetwork + "." + c + "/", getAuthentication());
                    scanDir(file);
                } catch (MalformedURLException e) {
                    logger.log(Level.INFO,"Cannot connect to \\\\192.168." + subNetwork + "." + c + "\\: " + e.getMessage());
                }
            }
        }

        public void scanDir(SmbFile file){
            try {
                if (file.exists()){
                    SmbFile[] files = file.listFiles();
                    for (SmbFile smbFile : files){
                        if (smbFile.getType() == 16 || smbFile.getType() == 32 || smbFile.getType() == 64) continue;
                        if (smbFile.isDirectory()) scanDir(smbFile);
                        if (smbFile.isFile()) {
                            /*String[] parts = smbFile.getName().split("\\.");
                            String extension = parts[parts.length - 1];*/
                            for (String ext : extensions)
                                if (smbFile.getName().endsWith(ext)) {
                                    String path = "\\\\" + smbFile.getCanonicalPath().substring(6).replace("/", "\\");
                                    logger.log( Level.INFO, path);
                                }
                        }
                    }
                }
            } catch (Exception e) {
                logger.log(Level.INFO, e.getMessage() + "::::::" + file.getCanonicalPath());
            }
        }
    }

    private static int counter = 1;
    private static String subNetwork = "";

    private static NtlmPasswordAuthentication authentication;

    public static String getSubNetwork() {
        return subNetwork;
    }

    public static void setSubNetwork(String subNetwork) {
        SyncInit.subNetwork = subNetwork;
    }

    public static NtlmPasswordAuthentication getAuthentication() {
        return authentication;
    }

    public static void setAuthentication(NtlmPasswordAuthentication authentication) {
        SyncInit.authentication = authentication;
    }

    public synchronized static int getCounter(){
        if (counter != stop)
            return counter++;
        else return -1;
    }

    public static void main(String[] args) {
        setAuthentication(new NtlmPasswordAuthentication(null, null, null));
        setSubNetwork("0");
        for (int i = 0; i < 10; i++){
            (new Thread(new SuperThread())).start();
        }
    }
}
