package ua.george_nika.restaurant.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_item", schema = "public", catalog = "restaurant")
public class OrderItemEntity {
    private int idOrderItem;
    private int quantity;
    private int price;
    private Timestamp created;
    private Timestamp updated;

    private AccountEntity account;
    private OrderEntity order;
    private MenuItemEntity menuItem;

    @Id
    @Column(name = "id_order_item", nullable = false, insertable = true, updatable = true)
    @SequenceGenerator(name="order_item_seq", sequenceName="order_item_seq")
    @GeneratedValue(generator = "order_item_seq")
    public int getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(int idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    @Basic
    @Column(name = "quantity", nullable = false, insertable = true, updatable = true)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = true)
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account")
    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id_order")
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name = "id_menu_item", referencedColumnName = "id_menu_item")
    public MenuItemEntity getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemEntity menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemEntity that = (OrderItemEntity) o;

        return idOrderItem == that.idOrderItem;

    }

    @Override
    public int hashCode() {
        return idOrderItem;
    }
}
