package ua.george_nika.restaurant.dao.intface;

import ua.george_nika.restaurant.dao.util.SortAndRestrictForEntity;

import java.util.List;

public interface AbstractDao<T> {

    void save(Object abstractEntity);

    void update(Object abstractEntity);

    void delete(Object abstractEntity);

    T getById(Integer id);

    List<T> getAll();

    T getSingleBy(String field, String value);
    T getSingleByObject(String field, Object value);

    List<T> getListByObject(String field, Object value);

    int getCountRecordsWith(String field, String value);

    List<T> getFilteredAndSortedList(Integer offset, Integer limit, SortAndRestrictForEntity sortAndRestrict);

    int getCountRecordsFilteredAndSortedList(SortAndRestrictForEntity sortAndRestrict);

}
