package br.com.itau.letscode.ialmeida.DynamoDB.exception;

public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(String msg) {
        super(msg);
    }

}
