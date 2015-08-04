package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.AccountEntity;

import java.sql.Timestamp;

// for send with ajax. Normal Entity has a many dependency
public class LightAccount {

    private int idAccount;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private Timestamp created;
    private Timestamp updated;
    private boolean emailVerified;
    private boolean active;
    private String phone;


    public LightAccount() {
    }

    public LightAccount(AccountEntity accountEntity) {
        this.setIdAccount(accountEntity.getIdAccount());
        this.setLogin(accountEntity.getLogin());
        this.setEmail(accountEntity.getEmail());
        this.setFirstName(accountEntity.getFirstName());
        this.setLastName(accountEntity.getLastName());
        this.setMiddleName(accountEntity.getMiddleName());
        this.setCreated(accountEntity.getCreated());
        this.setUpdated(accountEntity.getUpdated());
        this.setPhone(accountEntity.getPhone());
        this.setActive(accountEntity.isActive());
        this.setEmailVerified(accountEntity.isEmailVerified());
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
