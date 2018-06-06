/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remotefileutility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

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
    
    public String testRemote() {
        Boolean canWrite = false;
        Boolean canRead = false;
        Boolean canList = false;
        Boolean canDelete = false;
        
        String sReturn = "";
        
        String remoteShareUrl = "smb://";
        OutputStream out = null;
        String sTestFilename = "";
        String sTestContent = "This is a test file.";
        
        sReturn = "Test Write Access:\n";
        
        if (gsDomain.isEmpty()) {
            remoteShareUrl += this.gsUsername + ":" + this.gsPassword + "@" + this.gsSMB;
        }
        else {
            remoteShareUrl += this.gsDomain + ";" + this.gsUsername + ":" + this.gsPassword + "@" + this.gsSMB;
        }
        
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            sTestFilename = "TEST" + fmt.format(new Date()) + ".txt";
            String writeURL = remoteShareUrl + sTestFilename;
            //System.out.println(writeURL);
            SmbFile remoteWriteFile = new SmbFile(writeURL);
            remoteWriteFile.connect();
            
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteWriteFile));
            
            byte[] buffer = sTestContent.getBytes();
            
            out.write(buffer);
            out.flush();
        }
        catch (Exception e) {
            sReturn += "  Failed: " + e.getLocalizedMessage() + "\n";
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                    canWrite = true;
                    sReturn += "  Success.\n";
                }
            }
            catch (Exception e) {}
        }
        
        sReturn += "Test List Folder Access:\n";
        try {
            SmbFile remoteListFile = new SmbFile(remoteShareUrl);
        
            String[] files = remoteListFile.list();
            
            StringBuilder builder = new StringBuilder();
            for (String s : files) {
                builder.append("    " + s);
                builder.append("\n");
            }
            String str = builder.toString();
            canList = true;
            sReturn += "  Success: \n" + str;
        }
        catch (Exception e) {
            sReturn += "  Failed: " + e.getLocalizedMessage() + "\n";
        }
        
        sReturn += "Test Read Access:\n";
        InputStream in = null;
        ByteArrayOutputStream output = null;
        String readURL = remoteShareUrl + sTestFilename;

        try {
            SmbFile remoteReadFile = new SmbFile(readURL);
            remoteReadFile.connect();
            in = new BufferedInputStream(new SmbFileInputStream(remoteReadFile));
            output = new ByteArrayOutputStream((int)remoteReadFile.length());
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = in.read(buffer, 0, buffer.length)) != - 1) {
                output.write(buffer, 0, len);
            }

            output.flush();
            canRead = true;
            sReturn += "  Success: " + output.toString() + "\n";
        }
        catch (Exception e) {
            sReturn += "  Failed: " + e.getLocalizedMessage() + "\n";
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (output != null) {
                    output.close();
                }
            }
            catch (Exception e) {}
        }
        
        sReturn += "Test Delete Access:\n";
        try {
            SmbFile remoteDeleteFile = new SmbFile(readURL);
            remoteDeleteFile.connect();
            if (remoteDeleteFile.isFile() && remoteDeleteFile.exists()) {
                remoteDeleteFile.delete();
                sReturn += "  Success.\n";
            }
        }
        catch (Exception e) {
            sReturn += "  Failed: " + e.getLocalizedMessage() + "\n";
        }
        
        return sReturn;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //System.out.println("Hello World!");

        //byte[] myfile = ReadFile.readFile();
        //System.out.println(new String(myfile));
        
        //WriteFile.writeFile();
        
        //ListFiles.listFiles();
        
        /*
        System.out.println(args.length);
        for (String s : args) {
            System.out.println(s);
        }
        */
        
        RemoteFileUtility rfu = new RemoteFileUtility();
        rfu.setRemoteFileUtility(args[0], args[1], args[2], "");
        //System.out.println(rfu.listFile());
        System.out.print(rfu.testRemote());
    }
}
