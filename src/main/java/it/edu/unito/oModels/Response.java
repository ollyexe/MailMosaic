package it.edu.unito.oModels;

import java.util.List;

public class Response {
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
