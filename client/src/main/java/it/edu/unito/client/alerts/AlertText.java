package it.edu.unito.client.alerts;

public enum AlertText {
    INVALID_RECIPIENTS("Invalid recipients address, some of them may not exists."),
    MESSAGE_SENT("Email sent with success!"),
    MESSAGE_DELETED("Email deleted with success!"),
    OP_ERROR("Ops, something went wrong."),
    NEW_EMAILS("New emails available!"),
    NO_CONNECTION("Connection not found."),
    ILLEGAL_PARAMS("Invalid parameters.");

    public final String text;

    AlertText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
