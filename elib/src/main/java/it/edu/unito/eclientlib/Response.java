package it.edu.unito.eclientlib;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    ResponseName responseName;
    List<Mail> content;

    public Response(ResponseName responseName, List<Mail> content) {
        this.responseName = responseName;
        this.content = content;
    }

    public Response(){

    }

    public ResponseName getResponseName() {
        return responseName;
    }

    public List<Mail> getContent() {
        return content;
    }


}
