package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Log.LogType;
import it.edu.unito.oModels.Request;
import javafx.application.Platform;

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
            default -> throw new IllegalArgumentException("Invalid product type: " + rq.getOpName());

        }
    }

}
