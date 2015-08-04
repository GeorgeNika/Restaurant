package ua.george_nika.restaurant.form;

import ua.george_nika.restaurant.entity.AccountEntity;
import ua.george_nika.restaurant.errors.UserWrongInputException;

public class EditAccountForm {

    private String password;
    private String confirmPassword;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;

    public EditAccountForm() {
    }

    public EditAccountForm(AccountEntity accountEntity) {
        this.password = accountEntity.getPassword();
        this.confirmPassword = accountEntity.getPassword();
        this.email = accountEntity.getEmail();
        this.firstName = accountEntity.getFirstName();
        this.lastName = accountEntity.getLastName();
        this.middleName = accountEntity.getMiddleName();
        this.phone = accountEntity.getPhone();
    }

    public void updateAccount(AccountEntity accountEntity) {
        if (!password.isEmpty()){
            if (!password.equals(confirmPassword)) {
                throw new UserWrongInputException("Password not the same");
            }
            accountEntity.setPassword(getPassword());
        }
        if (!getEmail().equals(accountEntity.getEmail())) {
            accountEntity.setEmail(getEmail());
            accountEntity.setEmailVerified(false);
        }
        accountEntity.setFirstName(getFirstName());
        accountEntity.setLastName(getLastName());
        accountEntity.setMiddleName(getMiddleName());
        accountEntity.setPhone(getPhone());
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

