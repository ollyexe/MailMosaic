package it.edu.unito.eclientlib;

public enum ResponseName {
    SUCCESS(null),
    ILLEGAL_PARAMS("Invalid parameters."),
    INVALID_RECIPIENTS("Invalid recipients address, some of them may not exists."),
    OP_ERROR("Ops, something went wrong.");

    public final String text;

    ResponseName(String text) {
        this.text = text;
    }
}
