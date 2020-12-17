package ar.edu.itba.paw.webapp.dto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ValidationErrorDto {
    private List<String> errors;
    
    public ValidationErrorDto() {}
    
    public ValidationErrorDto(String errorMessage) {
        this.errors = new ArrayList<>();
        this.errors.add(errorMessage);
    }
    
    public <T> ValidationErrorDto(Set<ConstraintViolation<T>> violations) {
        this.errors = new ArrayList<>();
        for(ConstraintViolation<T> violation : violations) {
            errors.add(violation.getPropertyPath().toString() + " : " + violation.getMessage());
        }
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}