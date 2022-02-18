package projet.mi.exception;

public class IllegalSyntax extends Exception {
    private String message;
    public IllegalSyntax(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
