package com.example.demo.controller;

import com.example.demo.model.FileInformation;
import com.example.demo.util.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class FileUploadController {


    /**
     * 1、需要修改自己的服务器的url
     */
    public static String serverUrl = "http://202.195.167.206:8080/file/";
    String operation = "index";
    String getLoadFilePicPath() {
        String pic_path = "";
        String tomcat_path = System.getProperty("user.dir");
        String bin_path = tomcat_path.substring(tomcat_path.lastIndexOf("/") + 1, tomcat_path.length());
        /**
         * 2、pic_path也需要修改
         */
        if ("bin".equals(bin_path)) {
            pic_path = tomcat_path.substring(0, System.getProperty("user.dir").lastIndexOf("/")) + "/../../ftpServer/ftprepository/";
        } else {
            pic_path = tomcat_path + "/../../ftpServer/ftprepository/";
        }
        return pic_path;
    }

    @GetMapping({"getallfile"})
    @ResponseBody
    public List getallfile() {
        FileUtils.globalPath=getLoadFilePicPath();
        ArrayList<FileInformation> arrayList = FileUtils.searchDirectoryFileName(getLoadFilePicPath());
        Collections.sort(arrayList);
        return arrayList;
    }

    @PostMapping({"deleteFile"})
    @ResponseBody
    public String deleteFile(@RequestParam("fileurl") String fileurl) {
        int idx=serverUrl.length();
        String filename=fileurl.substring(idx,fileurl.length());
        boolean flag = FileUtils.deleteFile(getLoadFilePicPath() + filename);
        if (flag)
            return "success";
        return "failed";
    }
    @PostMapping({"/uploadFile"})
    @ResponseBody
    public List uploadFile(@RequestParam("file") MultipartFile []files) {
        String pic_path = getLoadFilePicPath();
        ArrayList<String> res=new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty())
                return null;
            String fileName = file.getOriginalFilename();
            File dest = new File(pic_path + fileName);
            if (!dest.getParentFile().exists())
                dest.getParentFile().mkdirs();
            try {
                file.transferTo(dest);
                String dcmPath = pic_path + fileName;
                Runtime runtime = Runtime.getRuntime();
                // 执行linux命令 chmod 644 filepath  为文件添加644权限
                runtime.exec(" chmod 644 "+ dcmPath );
            } catch (IOException e) {
                e.printStackTrace();
            }
            res.add(serverUrl + fileName);
        }
        return res;
    }
    @GetMapping({"ftp"})
    public String uploadfile() {
        return "ftp";
    }
    @GetMapping({"index"})
    public String index() {
        return "ftp";
    }
}
