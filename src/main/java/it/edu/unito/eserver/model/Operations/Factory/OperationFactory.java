package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.*;

public class OperationFactory {

    Request rq;

    public OperationFactory(Request rq) {
        this.rq = rq;
    }

    public Operation produce(){
        switch (rq.getOpName()){
            case GET -> {
                return new Fetch(rq);
            }
            case POST -> {
                return new Send(rq);
            }
            case PUT -> {

                return new Read(rq);
            }
            case DELETE -> {

                return new Delete(rq);
            }
            default -> throw new IllegalArgumentException("Invalid product type: " + rq.getOpName());

        }
    }

}
