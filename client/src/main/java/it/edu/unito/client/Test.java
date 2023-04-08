package it.edu.unito.client;

import it.edu.unito.eclientlib.*;

import java.time.LocalDateTime;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println(Client.getInstance().delete(new Mail("gionni@gmail.com", List.of("gionni@gmail.com"),"gionni@gmail.com","", LocalDateTime.parse("07/04/2023 01:32:33", Util.formatter))).getResponseName());
    }
}
