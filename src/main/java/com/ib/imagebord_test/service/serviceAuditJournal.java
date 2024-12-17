package com.ib.imagebord_test.service;

import java.io.*;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class serviceAuditJournal{
    private File adminlog;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public serviceAuditJournal(){
        adminlog=new File("src/main/files/adminlog.txt");
    }

    public void addBanActivityToLog(String adminname,String role, String ip, String cause, String period,boolean banadded){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            if(banadded) {
                writer.write("[" + timestamp + "] "+parseUserRole(role)+" " + adminname + " ЗАБАНИЛ пользователя с ip " + ip + " по причине " + cause + " на срок до " + period + '\n');
            }else{
                writer.write("[" + timestamp + "] "+parseUserRole(role)+" " + adminname + " РАЗБАНИЛ пользователя с ip " + ip + ", забаненного по причине " + cause + " на срок до " + period + '\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void addThreadActivityToLog(String adminname,String role,String thread_id,int action){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            switch (action) {
                case 1 -> {
                    writer.write("[" + timestamp + "] "+parseUserRole(role)+" "  + adminname + " ЗАКРЕПИЛ тред с id " + thread_id + '\n');
                }
                case 2 -> {
                    writer.write("[" + timestamp + "] "+parseUserRole(role)+" "  + adminname + " ЗАКРЫЛ для постинга тред с id " + thread_id + '\n');
                }
                case 3 ->{
                    writer.write("[" + timestamp + "] "+parseUserRole(role)+" "  + adminname + " написал в ЗАКРЫТЫЙ тред с id " + thread_id + '\n');
                }
                default -> {
                    writer.write("[" + timestamp + "] "+parseUserRole(role)+" "  + adminname + " совершил неизвестное действие с тредом с id " + thread_id + '\n');
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void addReportActivityToLog(String adminname,String role,String report_id,String status){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            writer.write("["+timestamp+"] "+parseUserRole(role)+" " +adminname+" ОБРАБОТАЛ РЕПОРТ с id "+report_id+" и поставил ему статус "+status+'\n');
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void addTextAutoFormatActivityToLog(String adminname,String role,String bornshortame,boolean status,int action){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            switch (action) {
                case 1 -> {
                    if(status) {
                        writer.write("[" + timestamp + "] " + parseUserRole(role) + " " + adminname + " ВКЛЮЧИЛ автозамену на доске " + bornshortame + '\n');
                    }else{
                        writer.write("[" + timestamp + "] " + parseUserRole(role) + " " + adminname + " ВЫКЛЮЧИЛ автозамену на доске " + bornshortame + '\n');
                    }
                }
                case 2 -> {
                    writer.write("[" + timestamp + "] "+parseUserRole(role)+" "  + adminname + " ИЗМЕНИЛ список автозамены на доске " + bornshortame + '\n');
                }
                default -> {
                    writer.write("[" + timestamp + "] "+parseUserRole(role)+" "  + adminname + " совершил неизвестное действие с автозаменой на доске "+bornshortame+ '\n');
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void addPostfilesIdWarning(){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            writer.write("["+timestamp+"] "+"ВНИМАНИЕ! Таблица postfiles скоро переполнится. Требуется сброс значений id"+'\n');
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public String getAdminLog(){
        lock.readLock().lock();
        try (FileReader reader = new FileReader(adminlog);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString();
        }catch(IOException e){
            e.printStackTrace();
            return "";
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clearJournal(){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,false)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            writer.write("");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    private String parseUserRole(String role){
        String rolestr="unknown";
        switch (role) {
            case "ROLE_ADMIN" -> {
                rolestr = "Администратор";
            }
            case "ROLE_MODERATOR" -> {
                rolestr = "Модератор";
            }
            case "ROLE_MAINADMIN" -> {
                rolestr = "Главный администратор";
            }
        }
        return rolestr;
    }
}
