package ua.george_nika.restaurant.controller.lightentity;


import ua.george_nika.restaurant.entity.OrderEntity;
import ua.george_nika.restaurant.entity.OrderItemEntity;

import java.sql.Timestamp;
import java.util.*;

public class AjaxOrder {

    private Integer status;
    private Integer idOrder;
    private String owner;
    private Integer myCost;
    private Integer myPositions;
    private Integer allCost;
    private Integer allPositions;
    private String orderTime;
    private Timestamp created;
    private Timestamp updated;
    private LinkedList<LightOrderItem> entityList;

    public AjaxOrder() {
    }

    public AjaxOrder(OrderEntity orderEntity, Integer idAccount) {
        this.status = orderEntity.getStatus();
        this.idOrder = orderEntity.getIdOrder();
        this.owner = orderEntity.getClient().getClientName();
        this.orderTime = orderEntity.getOrderTime();
        this.created = orderEntity.getCreated();
        this.updated = orderEntity.getUpdated();

        // 1. get full linkedList and tempMenuGroupNameMap
        LinkedList<LightOrderItem> fullOrderItemList = new LinkedList<LightOrderItem>();
        Map<Integer, String> tempMenuGroupNameMap = new HashMap<Integer, String>();
        for (OrderItemEntity tempOrderItem : orderEntity.getOrderItemList()) {
            LightOrderItem tempLightOrderItem = new LightOrderItem(tempOrderItem, idAccount);
            fullOrderItemList.add(tempLightOrderItem);
            tempMenuGroupNameMap.put(tempOrderItem.getMenuItem().getMenuGroupEntity().getIdMenuGroup(),
                    tempOrderItem.getMenuItem().getMenuGroupEntity().getMenuGroupName());
        }

        // 2. all same orderItem take together
        Map<Integer, LightOrderItem> existMenuItem = new HashMap<Integer, LightOrderItem>();
        for (LightOrderItem tempOrderItem : fullOrderItemList){
            if (existMenuItem.containsKey(tempOrderItem.getIdMenuItem())) {
                // exist menuItem
                LightOrderItem oldLightOrderItem = existMenuItem.get(tempOrderItem.getIdMenuItem());
                tempOrderItem.setMyQuantity(tempOrderItem.getMyQuantity() + oldLightOrderItem.getMyQuantity());
                tempOrderItem.setAllQuantity(tempOrderItem.getAllQuantity() + oldLightOrderItem.getAllQuantity());
            }else{
                // new menuItem

            }
            existMenuItem.put(tempOrderItem.getIdMenuItem(), tempOrderItem);
        }

        // 3 get list from map
        LinkedList<LightOrderItem> singleOrderItemList = new LinkedList<LightOrderItem>(existMenuItem.values());

        // 4. sort
        Collections.sort(singleOrderItemList, new Comparator<LightOrderItem>() {
            public int compare(LightOrderItem o1, LightOrderItem o2) {
                // sort by idGroup
                return o1.getIdGroup() - o2.getIdGroup();
            }
        });

        // 5. add group to List and calculate cost and position
        Integer resultMyCost = 0;
        Integer resultMyPositions = 0;
        Integer resultAllCost = 0;
        Integer resultAllPositions = 0;
        Integer tempIdGroup = 0;
        ListIterator<LightOrderItem> iterator = singleOrderItemList.listIterator();
        while (iterator.hasNext()) {
            LightOrderItem tempLightOrderItem = iterator.next();
            if (tempLightOrderItem.getIdGroup() > tempIdGroup) {
                tempIdGroup = tempLightOrderItem.getIdGroup();
                LightOrderItem newGroup = new LightOrderItem();
                newGroup.setIdGroup(tempIdGroup);
                newGroup.setMenuItemName(tempMenuGroupNameMap.get(tempIdGroup));
                newGroup.setMenuGroup(true);
                iterator.previous();
                iterator.add(newGroup);
            } else {
                if (tempLightOrderItem.getMyQuantity() != 0) {
                    resultMyPositions += 1;
                    resultMyCost += tempLightOrderItem.getMyQuantity() * tempLightOrderItem.getPrice();
                }
                resultAllPositions += 1;
                resultAllCost += tempLightOrderItem.getAllQuantity() * tempLightOrderItem.getPrice();
            }
        }

        this.myPositions = resultMyPositions;
        this.allPositions = resultAllPositions;
        if (orderEntity.isDone()) {
            this.allCost = orderEntity.getCost();
        } else {
            this.allCost = resultAllCost;
        }
        this.myCost = resultMyCost;
        setEntityList(singleOrderItemList);

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getMyCost() {
        return myCost;
    }

    public void setMyCost(Integer myCost) {
        this.myCost = myCost;
    }

    public Integer getMyPositions() {
        return myPositions;
    }

    public void setMyPositions(Integer myPositions) {
        this.myPositions = myPositions;
    }

    public Integer getAllCost() {
        return allCost;
    }

    public void setAllCost(Integer allCost) {
        this.allCost = allCost;
    }

    public Integer getAllPositions() {
        return allPositions;
    }

    public void setAllPositions(Integer allPositions) {
        this.allPositions = allPositions;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
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

    public LinkedList<LightOrderItem> getEntityList() {
        return entityList;
    }

    public void setEntityList(LinkedList<LightOrderItem> entityList) {
        this.entityList = entityList;
    }
}
