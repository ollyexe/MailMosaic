package it.edu.unito.eserver.model.DAO;

import it.edu.unito.oModels.Mail;

import java.io.*;
import java.util.*;

public class Dao {
    private final String memory = new File("").getAbsolutePath() +"/src/main/java/it/edu/unito/eserver/memory/";

    private static  Dao instance = new Dao();
    private Dao() {
    }

    public static Dao getInstance(){
        if (instance == null) {
            instance = new Dao() ;
            System.out.println("new instance");
        }
        System.out.println("already existing instance");
        return instance;
    }
    private  String findEmailPath(Mail mail, String user){
        int id = mail.getId();//hash code chages but doenst change the id --> first hach code when just created
        System.out.println(id);
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

    public boolean read(Mail mail, String user){

        File emailsFiles = new File(findEmailPath(mail, user));
        FileInputStream fin;
        ObjectInputStream obj;
        Mail actEmail;

        try {

                fin = new FileInputStream(Objects.requireNonNull(emailsFiles));
                obj = new ObjectInputStream(fin);
                actEmail=(Mail) obj.readObject();

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

    public void delete(Mail mail, String user){

        Optional<File> file= Optional.of(new File(findEmailPath(mail, user)));

        if (file.isEmpty()) {
            System.err.println("File nort found");
        } else {
            file.get().delete();;
        }

    }

    public boolean checkUser(String receiver) {
        String[] dirs = new File(memory).list(
                (current, name) -> new File(current, name)
                        .isDirectory());
        return dirs != null && dirs.length != 0 &&
                Arrays.stream(dirs).toList().contains(receiver);
    }
}
