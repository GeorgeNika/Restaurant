package ua.george_nika.restaurant.entity;

import ua.george_nika.restaurant.util.RestaurantConstant;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "order_book", schema = "public", catalog = "restaurant")
public class OrderEntity {
    private int idOrder;
    private String orderTime;
    private Timestamp created;
    private Timestamp updated;
    private int status;
    private int cost;

    private ClientEntity client;
    private List<OrderItemEntity> orderItemList;


    @Id
    @Column(name = "id_order", nullable = false, insertable = true, updatable = true)
    @SequenceGenerator(name="order_book_seq", sequenceName="order_book_seq")
    @GeneratedValue(generator = "order_book_seq")
    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    @Basic
    @Column(name = "order_time", nullable = true, insertable = true, updatable = true)
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Basic
    @Column(name = "created", nullable = false, insertable = true, updatable = true)
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "updated", nullable = true, insertable = true, updatable = true)
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = true)
    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @OneToMany(mappedBy = "order")
    public List<OrderItemEntity> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemEntity> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    @Transient
    public Boolean isDone(){
        if (status == RestaurantConstant.ORDER_STATUS_DONE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntity that = (OrderEntity) o;

        return idOrder == that.idOrder;

    }

    @Override
    public int hashCode() {
        return idOrder;
    }
}
