package in.arula.myorder.validator;


public interface Validation {

    String getErrorMessage();

    boolean isValid(String text);

}