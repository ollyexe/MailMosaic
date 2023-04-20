package it.edu.unito.eclientlib;


import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Util {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

     static final String memory = new File("").getAbsolutePath() +"/server/src/main/java/it/edu/unito/eserver/memory";

    public static boolean validateEmail(String email){
        return Pattern.matches("^(.+?)@gmail.com", email);
    }
}
