package com.ib.imagebord_test.content_management;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageMGMT{
    private String img_format="jpg";

    protected String saveAndCompress(MultipartFile imgfile, String filepath, String filename){
        try{
            String fullpath=filepath+filename;
            String fullthumbpath=filepath+"thumb"+filename;
            File infile=new File(fullpath);
            File thumb=new File(fullthumbpath);
            try(InputStream img_stream = imgfile.getInputStream()) {
                Thumbnails.of(img_stream).scale(1).outputFormat(img_format).toFile(infile);
            }
            if(!checkType(Files.probeContentType(Path.of(infile.getPath(),".jpg")))){
                System.out.println("potentially dangerous file");
                if(infile.delete()){
                    System.out.println("deleted");
                }else{
                    System.out.println("delete failed");
                }
                return null;
            }
            try(InputStream img_stream = imgfile.getInputStream()) {
                Thumbnails.of(img_stream).size(120, 120).outputFormat(img_format).toFile(thumb);
            }
            return thumb.getName()+"|"+infile.getName();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private boolean checkType(String filetype){
        return filetype.startsWith("image/");
    }

    public List<String> saveImages(String filepath, List<MultipartFile> attached_files, Long reply_id, Integer thread_postcount, String filepathfrontend){
            List<String> imgpath = new ArrayList<>();
            if(!attached_files.isEmpty()) {
            File filesdir = new File(filepath.substring(0, filepath.length() - 1));
            try {
                if (!filesdir.exists()) {
                    if (!filesdir.mkdirs()) {
                        throw new IOException("failed to create directory for post files");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int s = 1;
            for (MultipartFile files : attached_files) {
                String pathtimages = saveAndCompress(files, filepath, reply_id + "_" + thread_postcount + "_" + s);
                if(pathtimages!=null) {
                    imgpath.add(filepathfrontend + "/" + pathtimages.substring(0, pathtimages.indexOf('|')) + "." + img_format);
                    imgpath.add(filepathfrontend + "/" + pathtimages.substring(pathtimages.indexOf('|') + 1) + "." + img_format);
                }
                s++;
            }
        }
        return imgpath;
    }
}
