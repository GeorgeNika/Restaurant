package ua.george_nika.restaurant.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "request_for_membership", schema = "public", catalog = "restaurant")
public class RequestFMSEntity {

    private int idRequestFMS;
    private String info;
    private Timestamp created;
    private AccountEntity accountEntity;
    private ClientEntity clientEntity;

    @Id
    @Column(name = "id_request")
    @SequenceGenerator(name="request_seq", sequenceName="request_seq")
    @GeneratedValue(generator = "request_seq")

    public int getIdRequestFMS() {
        return idRequestFMS;
    }

    public void setIdRequestFMS(int idRequestFMS) {
        this.idRequestFMS = idRequestFMS;
    }

    @Basic
    @Column(name = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account")
    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }
}
