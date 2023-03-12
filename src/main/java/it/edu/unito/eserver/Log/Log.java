package it.edu.unito.eserver.Log;

public class Log {

    String content;
    LogType type;

    public Log(String content, LogType type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public LogType getType() {
        return type;
    }

    @Override
    public String toString() {
        return content ;
    }
}
