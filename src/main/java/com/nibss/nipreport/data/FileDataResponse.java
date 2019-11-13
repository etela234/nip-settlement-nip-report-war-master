/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nibss.nipreport.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author elixir
 * @param <T>
 */
public class FileDataResponse<T extends FileData> extends DataResponse<T> {

    @XmlElement
    private List<T> files;

    public List<T> getFiles() {
        return files;
    }

    public void setFiles(List<T> files) {
        this.files = files;
    }

    public void addFile(T dataEntry) {
        if (dataEntry == null) {
            return;
        }
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(dataEntry);
    }

    public String getFileName(Map header) {
        System.out.println("header="+header);
        Object object = header.get("Content-Disposition");
        StringBuilder content = new StringBuilder();
        if(object instanceof List){
            for(Object ob : (List)object){
                if(ob!=null)
                  content.append(ob);
            }
        }else if(object instanceof String)
            content.append(object);
        
        String[] contentDisposition = content.toString().split(";");
        for (String filename : contentDisposition) {
            filename = filename.trim();
            if ((filename.startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                int index = finalFileName.lastIndexOf(File.separatorChar);
                finalFileName = (index < 0) ? finalFileName : filename.substring(index + 1);
                return finalFileName;
            }
        }
        return null;
    }

    public long saveFile(InputStream inputStream, File file) throws IOException {
        long size = 0;
        int read;
        byte[] bytes = new byte[1024];
        try (FileOutputStream outpuStream = new FileOutputStream(file)) {
            while ((read = inputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
                size += read;
            }
            outpuStream.flush();
        }
        return size;
    }
//public FileData saveFileData(InputStream inputStream, File file) {
//        long size=0;
//        FileData fileData=null;
//        try {
//            
//            int read;
//            byte[] bytes = new byte[1024];
//            try (FileOutputStream outpuStream = new FileOutputStream(file)) {
//                while ((read = inputStream.read(bytes)) != -1) {
//                    outpuStream.write(bytes, 0, read);
//                    size+=read;
//                }
//                outpuStream.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            size=0;
//        }finally{
//            if(size>0){
//                fileData=new FileData();
//                fileData.s
//            }
//        }
//        return size;
//    }

}
