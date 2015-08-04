package ua.george_nika.restaurant.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_group", schema = "public", catalog = "restaurant")
public class MenuGroupEntity {
    private int idMenuGroup;
    private String menuGroupName;
    private List<MenuItemEntity> menuItemList;

    @Id
    @Column(name = "id_menu_group", nullable = false, insertable = true, updatable = true)
    public int getIdMenuGroup() {
        return idMenuGroup;
    }

    public void setIdMenuGroup(int idMenuGroup) {
        this.idMenuGroup = idMenuGroup;
    }

    @Basic
    @Column(name = "menu_group_name", nullable = false, insertable = true, updatable = true, length = 50)
    public String getMenuGroupName() {
        return menuGroupName;
    }

    public void setMenuGroupName(String menuGroupName) {
        this.menuGroupName = menuGroupName;
    }

    @OneToMany(mappedBy = "menuGroupEntity")
    public List<MenuItemEntity> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItemEntity> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuGroupEntity menuGroupEntity = (MenuGroupEntity) o;

        return idMenuGroup == menuGroupEntity.idMenuGroup;

    }

    @Override
    public int hashCode() {
        return idMenuGroup;
    }
}
