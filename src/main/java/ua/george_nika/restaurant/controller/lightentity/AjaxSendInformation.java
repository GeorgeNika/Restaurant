package ua.george_nika.restaurant.controller.lightentity;

import java.util.List;

public class AjaxSendInformation {

    private Integer page;
    private String sort;
    private String idLike;
    private String nameLike;
    private List<? extends Object> entityList;

    public AjaxSendInformation() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIdLike() {
        return idLike;
    }

    public void setIdLike(String idLike) {
        this.idLike = idLike;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public List<? extends Object> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<? extends Object> entityList) {
        this.entityList = entityList;
    }
}
