package ua.george_nika.restaurant.entity;

import javax.persistence.*;

@Entity
@Table(name = "email_verification", schema = "public", catalog = "restaurant")
public class EmailVerificationEntity {
    private int idAccount;
    private String code;
    private AccountEntity account;

    @Id
    @Column(name = "id_account")
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @OneToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account", nullable = false, insertable =false, updatable = false)
    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailVerificationEntity that = (EmailVerificationEntity) o;

        return idAccount == that.idAccount;

    }

    @Override
    public int hashCode() {
        return idAccount;
    }
}
