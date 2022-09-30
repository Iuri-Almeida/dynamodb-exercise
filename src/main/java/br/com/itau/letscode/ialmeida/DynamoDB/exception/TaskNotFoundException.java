package br.com.itau.letscode.ialmeida.DynamoDB.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String msg) {
        super(msg);
    }

}
