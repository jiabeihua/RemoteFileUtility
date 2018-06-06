/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotefileutility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

/**
 *
 * @author BlueAngel
 */
public class WriteFile {
    public static void writeFile() {
        InputStream in = null;
        OutputStream out = null;

        try {
            //Get a picture
            File localFile = new File("C:/temp/test.csv");
            String remotePhotoUrl = "smb://domain;username:password@ipaddress/test/"; //The shared directory to store pictures
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS_");
            SmbFile remoteFile = new SmbFile(remotePhotoUrl + "/" + fmt.format(new Date()) + localFile.getName());
            remoteFile.connect(); //Try to connect

            in = new BufferedInputStream(new FileInputStream(localFile));
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));

            byte[] buffer = new byte[4096];
            int len = 0; //Read length
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush(); //The refresh buffer output stream
        }
        catch (Exception e) {
            String msg = "The error occurred: " + e.getLocalizedMessage();
            System.out.println(msg);
        }
        finally {
            try {
                if(out != null) {
                    out.close();
                }
                if(in != null) {
                    in.close();
                }
            }
            catch (Exception e) {}
        }
    }
}
