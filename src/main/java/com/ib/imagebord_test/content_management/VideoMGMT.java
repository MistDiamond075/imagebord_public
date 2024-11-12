package com.ib.imagebord_test.content_management;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class VideoMGMT {
    private String saveAsWebmCompress(String filepath,MultipartFile file,String name){
        try(InputStream vid_stream = file.getInputStream()){
            Path fullpath=Path.of(filepath+name+".webm");
            File inputTempFile = File.createTempFile("tempvid", ".mp4");
            Files.copy(vid_stream, inputTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FFmpeg ffmpeg = FFmpeg.atPath();
            FFmpegResult result = ffmpeg
                    .addInput(UrlInput.fromPath(inputTempFile.toPath()))
                    .addOutput(UrlOutput.toPath(fullpath))
                            .addArguments("-c:v","libvpx")
                            .addArguments("-c:a","libvorbis")
                            .addArguments("-qmin", "42")
                            .addArguments("-qmax", "45")
                            .addArguments("-b:v", "950K")
                            .addArguments("-threads", "2")
                    .execute();
            String retname="";
            if(result!=null) {
                retname = name+".webm";
                Files.deleteIfExists(inputTempFile.toPath());
               // Files.deleteIfExists(outputTempFile.toPath());
            }
            if(!checkType(Files.probeContentType(fullpath))){
                System.out.println("potentially dangerous file");
                if (!new File(fullpath.toString()).delete()) {
                    System.out.println("delete failed");
                } else {
                    System.out.println("deleted");
                }
                return null;
            }
            return retname;
        }catch (IOException | RuntimeException e){throw new RuntimeException(e);}
    }

    private String saveAsOriginal(String filepath,MultipartFile file,String name) {
        try {
            Path fullpath = Path.of(filepath,name);
            file.transferTo(fullpath);
            if(!checkType(Files.probeContentType(fullpath))){
                System.out.println("potentially dangerous file");
                if (!new File(fullpath.toString()).delete()) {
                    System.out.println("delete failed");
                } else {
                    System.out.println("deleted");
                }
                return null;
            }
            return name;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkType(String filetype){
        return filetype.startsWith("video/");
    }

    public List<String> saveVideos(String filepath, List<MultipartFile> attached_files,Long reply_id, Integer thread_postcount,String filepathfrontend){
        List<String> vidpath=new ArrayList<>();
        if(!attached_files.isEmpty()){
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
            for(MultipartFile file:attached_files){
                String pathtovid=null;
               /* if(file.getSize()/(1024*1024)>9) {
                     pathtovid= saveAsWebmCompress(filepath, file, "vid" + reply_id + "_" + thread_postcount + "_" + s);
                }else{*/
                    if(file.getContentType()!=null) {
                        pathtovid = saveAsOriginal(filepath, file, "vid" + reply_id + "_" + thread_postcount + "_" + s + "." + file.getContentType().substring(file.getContentType().indexOf('/') + 1));
                    }
                //}
                if(pathtovid!=null) {
                    vidpath.add(filepathfrontend + "/" + pathtovid);
                }
                s++;
            }
        }
        return vidpath;
    }
}
