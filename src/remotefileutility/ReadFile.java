/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotefileutility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 *
 * @author BlueAngel
 */
public class ReadFile {
    public static byte[] readFile() {
        InputStream in = null ;
        ByteArrayOutputStream out = null ;
        try {
            //Create a remote file object
            String username = "username";
            String password = "password";
            //String remotePhotoUrl = "smb://username:password@192.168.1.3/jiabeihua/test.txt";
            //String remotePhotoUrl = "smb://domain;" + username + ":" + password + "@ipaddress/home/xxx.inp";
            String remotePhotoUrl = "smb://domain;" + username + ":" + password + "@ipaddress/test/test.csv";
            //String remotePhotoUrl = "smb://domain;username:password@ipaddress/test/test.csv";
            System.out.println(remotePhotoUrl);
            SmbFile remoteFile = new SmbFile(remotePhotoUrl);
            System.out.println("Connecting...");
            remoteFile.connect(); //Try to connect
            //Create a file stream
            in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
            out = new ByteArrayOutputStream((int)remoteFile.length());
            //Read the contents of the documents
            System.out.println("Reading file...");
            byte[] buffer = new byte[4096];
            int len = 0; //Read length
            while ((len = in.read(buffer, 0, buffer.length)) != - 1) {
                out.write(buffer, 0, len);
            }

            out.flush(); //The refresh buffer output stream
            return out.toByteArray();
        }
        catch (Exception e) {
            String msg = "Download a remote file error: " + e.getLocalizedMessage();
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
        return null;
    }
}
