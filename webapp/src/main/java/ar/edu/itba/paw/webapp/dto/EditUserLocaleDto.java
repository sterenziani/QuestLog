package ar.edu.itba.paw.webapp.dto;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditUserLocaleDto {
	
    @NotNull
    @Size(min = 2)
    private String locale;

    public EditUserLocaleDto() {
    	
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
