package kz.iitu.springlab.membership;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(long id) {
        super("Member " + id + " not found");
    }
}
