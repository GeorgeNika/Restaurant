package ua.george_nika.restaurant.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client", schema = "public", catalog = "restaurant")
public class ClientEntity implements Serializable{

    private int idClient;
    private String clientName;
    private Timestamp created;
    private Timestamp updated;
    private boolean active;
    private boolean manualCreatedGroup;

    private AccountEntity accountOwner;
    private List<AccountEntity> memberList;
    private List<RequestFMSEntity> requestFMSList;
    private List<OrderEntity> orderList;

    @Id
    @Column(name = "id_client")
    @SequenceGenerator(name="client_seq", sequenceName="client_seq")
    @GeneratedValue(generator = "client_seq")
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Basic
    @Column(name = "client_name")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
    @Column(name = "manual_created_group")
    public boolean isManualCreatedGroup() {
        return manualCreatedGroup;
    }

    public void setManualCreatedGroup(boolean manualCreatedGroup) {
        this.manualCreatedGroup = manualCreatedGroup;
    }

    @ManyToOne
    @JoinColumn(name = "id_account_owner", referencedColumnName = "id_account")
    public AccountEntity getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(AccountEntity accountOwner) {
        this.accountOwner = accountOwner;
    }

    @ManyToMany
    @JoinTable(name = "account_client",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_account"))
    public List<AccountEntity> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<AccountEntity> memberList) {
        this.memberList = memberList;
    }

    @OneToMany(mappedBy = "clientEntity")
    public List<RequestFMSEntity> getRequestFMSList() {
        return requestFMSList;
    }

    public void setRequestFMSList(List<RequestFMSEntity> requestFMSList) {
        this.requestFMSList = requestFMSList;
    }

    @OneToMany(mappedBy = "client")
    public List<OrderEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderEntity> orderList) {
        this.orderList = orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntity that = (ClientEntity) o;

        return idClient == that.idClient;

    }

    @Override
    public int hashCode() {
        return idClient;
    }

    public void addMember(AccountEntity accountEntity){
        if (memberList ==null) {
            memberList = new ArrayList<AccountEntity>();
        }
        if (!memberList.contains(accountEntity)) {
            memberList.add(accountEntity);
        }
    }

    public void deleteMember(AccountEntity accountEntity){
        if (memberList != null) {
            memberList.remove(accountEntity);
        }
    }
}
