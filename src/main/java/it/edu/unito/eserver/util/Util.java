package it.edu.unito.eserver.util;


import java.util.List;
import java.util.regex.Pattern;

public class Util {

    public static boolean validateEmail(String email){
        return Pattern.matches("^(.+?)@unito.it", email);
    }

    public static String receiversToString(List<String> list) {
        StringBuilder s = new StringBuilder();
        for (String address: list ) {
            s.append(", ").append(address);
        }
        return s.toString();
    }



}
