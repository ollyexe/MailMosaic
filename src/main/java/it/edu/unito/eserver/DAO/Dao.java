package it.edu.unito.eserver.DAO;

import it.edu.unito.oModels.Mail;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Dao {
    private final String memory = new File("").getAbsolutePath() +"/src/main/java/it/edu/unito/eserver/memory/";

    public Dao() {
    }
    private  String findEmailPath(Mail mail, String user){
        int id = mail.getId();
        System.out.println(id);
        File f = new File(memory + "/" + user +  "/" +  id + ".txt");
        return f.getAbsolutePath();
    }
    public   boolean save(Mail mail, String user){
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

        File[] emailsFiles = new File(memory + "/" +
                user +  "/").listFiles();
        FileInputStream fin;
        ObjectInputStream obj;
        List<Mail> emails = new ArrayList<>();

        try {
            for (File file : Objects.requireNonNull(emailsFiles)) {
                fin = new FileInputStream(file);
                obj = new ObjectInputStream(fin);
                emails.add((Mail) obj.readObject());
                obj.close();
                fin.close();
            }


        } catch (ClassNotFoundException | IOException |
                 NullPointerException e) {
            e.printStackTrace();
            emails = null;
        }

        return emails;
    }

    public void read(Mail mail, String user){
        File emailsFiles = new File(findEmailPath(mail, user));
        FileInputStream fin;
        ObjectInputStream obj;
        Mail actEmail;

        try {

                fin = new FileInputStream(Objects.requireNonNull(emailsFiles));
                obj = new ObjectInputStream(fin);
                actEmail=(Mail) obj.readObject();

                actEmail.setRead(true);
                save(actEmail,user);//hash code chages but doenst change the id --> first hach code when just created
                obj.close();
                fin.close();



        } catch (ClassNotFoundException | IOException |
                 NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void delete(Mail mail, String user){

        Optional<File> file= Optional.of(new File(findEmailPath(mail, user)));

        if (file.isEmpty()) {
            System.err.println("File nort found");
        } else {
            file.get().delete();;
        }

    }
}
