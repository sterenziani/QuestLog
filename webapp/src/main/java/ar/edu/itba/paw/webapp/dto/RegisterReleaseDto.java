package ar.edu.itba.paw.webapp.dto;

public class RegisterReleaseDto {
    public RegisterReleaseDto() {}

    Long locale;
    String date;

    public void setLocale(Long locale) {
        this.locale = locale;
    }

    public Long getLocale() {
        return locale;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }


}
