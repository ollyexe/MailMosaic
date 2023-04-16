package it.edu.unito.eclientlib;


import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Util {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

     static final String memory = new File("").getAbsolutePath() +"/server/src/main/java/it/edu/unito/eserver/memory";

    //controlla se l utente essite controllando se esiste una cartella con l username dell utente
    public static boolean checkUser(String receiver) {
        String[] dirs = new File(memory).list(
                (current, name) -> new File(current, name)
                        .isDirectory());
        return dirs != null && dirs.length != 0 &&
                Arrays.stream(dirs).toList().contains(receiver);
    }
    public static boolean validateEmail(String email){
        return Pattern.matches("^(.+?)@gmail.com", email)&&checkUser(email);
    }
}
