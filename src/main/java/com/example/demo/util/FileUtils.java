package com.example.demo.util;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipOutputStream;

import cn.hutool.json.JSONObject;
import com.example.demo.model.FileInformation;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import static com.example.demo.controller.FileUploadController.serverUrl;

public class FileUtils {

    private static final int buffer = 2048;
    public static void delFolder(String folderPath) {
        try {
            delAllFileInFolder(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFileInFolder(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists())
            return flag;
        if (!file.isDirectory())
            return flag;
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile())
                temp.delete();
            if (temp.isDirectory()) {
                delAllFileInFolder(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (!file.exists())
            return flag;
        if (file.isFile())
            return deleteFile(sPath);
        return deleteDirectory(sPath);
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator))
            sPath = sPath + File.separator;
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory())
            return false;
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete())
            return true;
        return false;
    }
    public static String globalPath="";
    public static ArrayList<FileInformation> searchDirectoryFileName(String sPath) {
        if (!sPath.endsWith(File.separator))
            sPath = sPath + File.separator;
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory())
            return null;
        ArrayList<FileInformation> filenames=new ArrayList();
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()){
                filenames.addAll(searchDirectoryFileName(files[i].getPath()));
            }else {
                int idx=globalPath.length();
                String path=sPath.substring(idx,sPath.length());
                File file=files[i];
                FileInformation fileInformation=new FileInformation();
                fileInformation.setFilename(file.getName());
                fileInformation.setFileurl(serverUrl+path+file.getName());
                Long lastModified = file.lastModified();
                Date date = new Date(lastModified);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String modtime = format.format(date);
                fileInformation.setModifyTime(modtime);
                fileInformation.setDictionary(path);
                filenames.add(fileInformation);
            }
        }
        return filenames;
    }

    @Deprecated
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists())
            pathFile.mkdirs();
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration<ZipEntry> entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            outPath = new String(outPath.getBytes("utf-8"), "ISO8859-1");
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists())
                file.mkdirs();
            if ((new File(outPath)).isDirectory())
                continue;
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0)
                out.write(buf1, 0, len);
            in.close();
            out.close();
        }
    }

    public static void compress(String source, String destinct) throws IOException {
        List<File> fileList = loadFilename(new File(source));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(destinct)));
        byte[] buffere = new byte[8192];
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            zos.putNextEntry((ZipEntry)new ZipEntry(getEntryName(source, file)));
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while (true) {
                int length = bis.read(buffere);
                if (length == -1)
                    break;
                zos.write(buffere, 0, length);
            }
            bis.close();
            zos.closeEntry();
        }
        zos.close();
    }

    private static List loadFilename(File file) {
        List<File> filenameList = new ArrayList();
        if (file.isFile())
            filenameList.add(file);
        if (file.isDirectory())
            for (File f : file.listFiles())
                filenameList.addAll(loadFilename(f));
        return filenameList;
    }

    private static String getEntryName(String base, File file) {
        File baseFile = new File(base);
        String filename = file.getPath();
        if (baseFile.getParentFile().getParentFile() == null)
            return filename.substring(baseFile.getParent().length());
        return filename.substring(baseFile.getParent().length() + 1);
    }

    public static void unZip(String path, String savepath) {
        int count = -1;
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        (new File(savepath)).mkdir();
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(path, "gbk");
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                byte[] buf = new byte[2048];
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String filename = entry.getName();
                boolean ismkdir = false;
                if (filename.lastIndexOf("/") != -1)
                    ismkdir = true;
                filename = savepath + filename;
                if (entry.isDirectory()) {
                    file = new File(filename);
                    file.mkdirs();
                    continue;
                }
                file = new File(filename);
                if (!file.exists() &&
                        ismkdir)
                    (new File(filename.substring(0, filename.lastIndexOf("/")))).mkdirs();
                file.createNewFile();
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, 2048);
                while ((count = is.read(buf)) > -1)
                    bos.write(buf, 0, count);
                bos.flush();
                bos.close();
                fos.close();
                is.close();
            }
            zipFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();
                if (fos != null)
                    fos.close();
                if (is != null)
                    is.close();
                if (zipFile != null)
                    zipFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
