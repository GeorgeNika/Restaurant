package ua.george_nika.restaurant.controller.lightentity;

import ua.george_nika.restaurant.entity.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// for send with ajax. Normal Entity has a many dependency
public class LightOrder {

    private Integer status;
    private Integer idOrder;
    private String orderTime;
    private String clientName;
    private Integer persons;
    private Integer positions;
    private Integer cost;
    private Timestamp created;
    private Timestamp updated;

    public LightOrder() {
    }

    public LightOrder(OrderEntity orderEntity) {
        this.setStatus(orderEntity.getStatus());
        this.setIdOrder(orderEntity.getIdOrder());
        this.setOrderTime(orderEntity.getOrderTime());
        this.setClientName(orderEntity.getClient().getClientName());

        int resultPositions = 0;
        int resultPersons = 0;
        int resultCost = 0;
        Set<AccountEntity> accountSet = new HashSet<AccountEntity>();
        Set<MenuItemEntity> menuItemSet = new HashSet<MenuItemEntity>();
        List<OrderItemEntity> orderItemList = orderEntity.getOrderItemList();
        for (OrderItemEntity tempOrderItem : orderItemList) {
            accountSet.add(tempOrderItem.getAccount());
            menuItemSet.add(tempOrderItem.getMenuItem());
            resultCost += tempOrderItem.getQuantity() * tempOrderItem.getMenuItem().getPrice();
        }

        this.persons = accountSet.size();
        this.positions = menuItemSet.size();
        if (orderEntity.isDone()) {
            this.cost = orderEntity.getCost();
        } else {
            this.cost = resultCost;
        }

        this.created = orderEntity.getCreated();
        this.updated = orderEntity.getUpdated();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getPersons() {
        return persons;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public Integer getPositions() {
        return positions;
    }

    public void setPositions(Integer positions) {
        this.positions = positions;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
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
}
