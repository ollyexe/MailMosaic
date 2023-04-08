package it.edu.unito.eserver.model.DAO;


import it.edu.unito.eclientlib.*;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.LogManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Dao {
    private final String memory = new File("").getAbsolutePath() +"/server/src/main/java/it/edu/unito/eserver/memory";
    private static  Dao instance = new Dao();
    private Dao() {
    }

    public static Dao getInstance(){
        if (instance == null) {
            instance = new Dao() ;
        }
        return instance;
    }
    private  String findEmailPath(Mail mail, String user){
        int id = mail.getId();//hash code chages but doenst change the id --> first hach code when just created

        File f = new File(memory + "/" + user +  "/" +  id + ".txt");
        return f.getAbsolutePath();
    }
    public  boolean save(Mail mail, String user){
        boolean flag;
        FileOutputStream fout;
        try {
            String path = findEmailPath(mail, user);
            fout = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(mail);
            out.flush();
            out.close();
            fout.close();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }
    public  List<Mail> fetch(String user){
        FileInputStream fin;
        ObjectInputStream obj;
        List<Mail> emails = new ArrayList<>();
        File[] emailsFiles = new File(memory + "/" +
                user +  "/").listFiles();


        try {
            for (File file : emailsFiles) {
                fin = new FileInputStream(file);
                obj = new ObjectInputStream(fin);
                emails.add((Mail) obj.readObject());
                obj.close();
                fin.close();
            }


        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return emails;
    }

    public boolean read(Mail mail, String user)  {

        File emailsFiles = new File(findEmailPath(mail, user));
        FileInputStream fin ;
        ObjectInputStream obj ;
        Mail actEmail;

        try {

                fin = new FileInputStream(Objects.requireNonNull(emailsFiles));
                obj = new ObjectInputStream(fin);
                actEmail=(Mail) obj.readObject();
            fin.close();
            obj.close();
                if (mail.getReceivers().contains(user)){
                    actEmail.setRead(true);
                    return save(actEmail,user);
                }
                else
                    throw new Exception("Illegal reading try");






        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Mail mail, String user) throws IOException {

        Optional<File> file= Optional.of(new File(findEmailPath(mail, user)));

        if (file.isEmpty()) {
            return false;
        } else {
            LockSystem lockSys;
            lockSys = ServerApp.unifier.getLockSystem();
            ReentrantReadWriteLock.WriteLock lock = lockSys.getLock(user).writeLock();

            lock.lock();
            boolean b = Files.deleteIfExists(Path.of(file.get().getAbsolutePath()));
            lock.unlock();
        }

        return !(new File(findEmailPath(mail, user)).exists());

    }


    //controlla se l utente essite controllando se esiste una cartella con l username dell utente
    public boolean checkUser(String receiver) {
        String[] dirs = new File(memory).list(
                (current, name) -> new File(current, name)
                        .isDirectory());
        return dirs != null && dirs.length != 0 &&
                Arrays.stream(dirs).toList().contains(receiver);
    }
}
