/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotefileutility;

import jcifs.smb.SmbFile;

/**
 *
 * @author BlueAngel
 */
public class ListFiles {
    public static void listFiles() {
        try {
            String username = "username";
            String password = "password";
            String remotePhotoUrl = "smb://" + username + ":" + password + "@ipaddress/folder/";
            //System.out.println(remotePhotoUrl);
            SmbFile remoteFile = new SmbFile(remotePhotoUrl);
        
            String[] files = remoteFile.list();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i]);
            }
        }
        catch (Exception e) {
            
        }
    }
}
