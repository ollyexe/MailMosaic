package it.edu.unito.client;

import it.edu.unito.eclientlib.OperationName;
import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Response;

public class Test {
    public static void main(String[] args) {
        System.out.println(new Client().fetch());
    }
}
