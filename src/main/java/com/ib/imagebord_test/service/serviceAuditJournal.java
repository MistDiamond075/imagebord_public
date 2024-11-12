package com.ib.imagebord_test.service;

import java.io.*;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class serviceAuditJournal{
    private File adminlog;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public serviceAuditJournal(){
        adminlog=new File("src/main/resources/adminlog.txt");
    }

    public void addBanActivityToLog(String adminname, String ip, String cause, String period,boolean banadded){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            if(banadded) {
                writer.write("[" + timestamp + "] " + adminname + " ЗАБАНИЛ пользователя с ip " + ip + " по причине " + cause + " на срок до " + period + '\n');
            }else{
                writer.write("[" + timestamp + "] " + adminname + " РАЗБАНИЛ пользователя с ip " + ip + ", забаненного по причине " + cause + " на срок до " + period + '\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void addThreadActivityToLog(String adminname,String thread_id,int action){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            switch (action) {
                case 1 -> {
                    writer.write("[" + timestamp + "] " + adminname + " ЗАКРЕПИЛ тред с id " + thread_id + '\n');
                    break;
                }
                case 2 -> {
                    writer.write("[" + timestamp + "] " + adminname + " ЗАКРЫЛ для постинга тред с id " + thread_id + '\n');
                    break;
                }
                default -> {
                    writer.write("[" + timestamp + "] " + adminname + " совершил неизвестное действие с тредом с id " + thread_id + '\n');
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void addReportActivityToLog(String adminname,String report_id,String status){
        lock.writeLock().lock();
        try(FileWriter writer=new FileWriter(adminlog,true)){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            writer.write("["+timestamp+"] "+adminname+" ОБРАБОТАЛ РЕПОРТ с id "+report_id+" и поставил ему статус "+status+'\n');
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
}
