package ua.george_nika.restaurant.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "menu_item", schema = "public", catalog = "restaurant")
public class MenuItemEntity {
    private int idMenuItem;
    private String menuItemName;
    private Integer weight;
    private Integer price;
    private String photo;
    private Timestamp created;
    private Timestamp updated;
    private boolean active;

    private MenuGroupEntity menuGroupEntity;

    @Id
    @Column(name = "id_menu_item", nullable = false, insertable = true, updatable = true)
    @SequenceGenerator(name="menu_item_seq", sequenceName="menu_item_seq")
    @GeneratedValue(generator = "menu_item_seq")
    public int getIdMenuItem() {
        return idMenuItem;
    }

    public void setIdMenuItem(int idMenuItem) {
        this.idMenuItem = idMenuItem;
    }

    @Basic
    @Column(name = "menu_item_name", nullable = false, insertable = true, updatable = true, length = 100)
    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    @Basic
    @Column(name = "weight", nullable = true, insertable = true, updatable = true)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "price", nullable = true, insertable = true, updatable = true)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "photo", nullable = true, insertable = true, updatable = true, length = 100)
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
    @Column(name = "active", nullable = false, insertable = true, updatable = true)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ManyToOne
    @JoinColumn(name = "id_menu_group", referencedColumnName = "id_menu_group")
    public MenuGroupEntity getMenuGroupEntity() {
        return menuGroupEntity;
    }

    public void setMenuGroupEntity(MenuGroupEntity menuGroupEntity) {
        this.menuGroupEntity = menuGroupEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItemEntity that = (MenuItemEntity) o;

        return idMenuItem == that.idMenuItem;

    }

    @Override
    public int hashCode() {
        return idMenuItem;
    }
}
