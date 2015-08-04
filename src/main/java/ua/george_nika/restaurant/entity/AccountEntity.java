package ua.george_nika.restaurant.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "account", schema = "public", catalog = "restaurant")
public class AccountEntity implements Serializable{
    private int idAccount;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private Timestamp created;
    private Timestamp updated;
    private boolean active;
    private boolean emailVerified;
    private String phone;
    private boolean admin;

    private List<ClientEntity> ownerClientList; // where i owner
    private List<ClientEntity> memberClientList; // where i a member
    private List<RequestFMSEntity> requestFMSList; // my request for membership

    private List<OrderItemEntity> orderItemList; // my orders


    @Id
    @Column(name = "id_account")
    @SequenceGenerator(name="account_seq", sequenceName="account_seq")
    @GeneratedValue(generator = "account_seq")
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "updated")
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Basic
    @Column(name = "active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "email_verified")
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "admin")
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    @OneToMany(mappedBy = "accountOwner")
    public List<ClientEntity> getOwnerClientList() {
        return ownerClientList;
    }

    public void setOwnerClientList(List<ClientEntity> myClientList) {
        this.ownerClientList = myClientList;
    }

    @ManyToMany(mappedBy = "memberList")
    public List<ClientEntity> getMemberClientList() {
        return memberClientList;
    }

    public void setMemberClientList(List<ClientEntity> memberClientList) {
        this.memberClientList = memberClientList;
    }

    @OneToMany(mappedBy = "accountEntity")
    public List<RequestFMSEntity> getRequestFMSList() {
        return requestFMSList;
    }

    public void setRequestFMSList(List<RequestFMSEntity> requestFMSList) {
        this.requestFMSList = requestFMSList;
    }

    @OneToMany(mappedBy = "account")
    public List<OrderItemEntity> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemEntity> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "idAccount=" + idAccount +
                ", login='" + login + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity that = (AccountEntity) o;

        return idAccount == that.idAccount;

    }

    @Override
    public int hashCode() {
        return idAccount;
    }
}
