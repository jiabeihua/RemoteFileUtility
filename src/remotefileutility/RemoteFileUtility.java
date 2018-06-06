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
public class RemoteFileUtility {
    String gsUsername = "";
    String gsPassword = "";
    String gsSMB = "";
    String gsDomain = "";

    public void setRemoteFileUtility(String username, String password, String smb, String domain) {
        this.gsUsername = username;
        this.gsPassword = password;
        this.gsSMB = smb;
        this.gsDomain = domain;
        
        this.gsPassword = this.gsPassword.replace("%", "%25");
    }
    
    public String listFile() {
        String remoteShareUrl = "smb://";
        
        try {
            if (gsDomain.isEmpty()) {
                remoteShareUrl += this.gsUsername + ":" + this.gsPassword + "@" + this.gsSMB;
            }
            else {
                remoteShareUrl += this.gsDomain + ";" + this.gsUsername + ":" + this.gsPassword + "@" + this.gsSMB;
            }
            //System.out.println(remotePhotoUrl);
            SmbFile remoteFile = new SmbFile(remoteShareUrl);
        
            String[] files = remoteFile.list();
            
            StringBuilder builder = new StringBuilder();
            for (String s : files) {
                builder.append(s);
                builder.append(",");
            }
            String str = builder.toString();
            return str;
            /*
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i]);
            }
            */
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World!");

        //byte[] myfile = ReadFile.readFile();
        //System.out.println(new String(myfile));
        
        //WriteFile.writeFile();
        
        //ListFiles.listFiles();
        
        RemoteFileUtility rfu = new RemoteFileUtility();
        rfu.setRemoteFileUtility("username", "password", "ipaddress/folder/", "");
        System.out.println(rfu.listFile());
    }
}
