package guestbook.core.repository;

public class RepositoryException extends Exception{

    public RepositoryException(String message, Throwable t){
        super(message, t);
    }
}
