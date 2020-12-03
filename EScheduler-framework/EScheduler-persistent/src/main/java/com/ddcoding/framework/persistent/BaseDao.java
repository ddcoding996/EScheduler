package com.ddcoding.framework.persistent;


import com.ddcoding.framework.persistent.entity.AbstractEntity;

import java.util.List;

/**
 */
public interface BaseDao {

	<T> String save(T entity);

	<T> void update(T entity);

	<T> void delete(T entity);

	<T> List<String> save(List<T> entityList);

	<T> void update(List<T> entityList);

	<T> void delete(List<T> entityList);

	<T> List<T> getAll(Class<T> clazz);

	<T> T get(Class<T> clazz, String id);

	<T> List<T> getList(Class<T> clazz, T entity);

	<T> List<T> getList(Class<T> clazz, T entity, boolean useLike);

	<T> T getUnique(Class<T> clazz, T entity);

	<T> T getUnique(Class<T> clazz, T entity, boolean useLike);

	<T extends AbstractEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity, boolean useLike);

}
