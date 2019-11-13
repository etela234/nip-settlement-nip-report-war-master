/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.elixir.commons.cdi.BeanManager;
import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.ActivityLog;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.model.Flag;
import com.nibss.nipreport.model.StatusFlag;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    @PersistenceContext(unitName = "nip-report-PU")
    private EntityManager em;
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public void create(T entity, ActivityLog log) {
        getEntityManager().persist(entity);
        logActivity(entity, log, StatusFlag.CREATE);
    }

    public void edit(T entity, ActivityLog log) {
        getEntityManager().merge(entity);
        logActivity(entity, log, StatusFlag.EDIT);
    }

    public T delete(T entity, ActivityLog log) {
        T merge = getEntityManager().merge(entity);
        logActivity(entity, log, StatusFlag.DELETE);
        return merge;
    }

    public T save(T entity) {
        return getEntityManager().merge(entity);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    //*******
    public List<T> findAll(Boolean enabled) {
        String q = "SELECT b FROM " + entityClass.getSimpleName() + " b " + (enabled ? "where b.flag = " + Flag.ENABLED : "");
        return getEntityManager().createQuery(q).getResultList();
    }

    public List<Object[]> findAll(Boolean enabled, String... fields) {
        String q = "SELECT " + concatFields(fields, "b") + " FROM " + entityClass.getSimpleName() + " b " + (enabled ? " where flag = " + Flag.ENABLED : "");
        return (List<Object[]>) getEntityManager().createQuery(q).getResultList();
    }

    //******************
    public static <F> F instance(Class<F> clazz) {
        return BeanManager.INSTANCE.getReference(clazz);
    }

    public int count(Map<String, ? extends Object> filterMap) {
        String acronym = "COUNT(" + entityClass.getSimpleName().substring(0, 1).toLowerCase() + ")";
        javax.persistence.Query query = getEntityManager().createQuery(buildQuery(acronym, null, filterMap));
        return ((Long) query.getSingleResult()).intValue();
    }

//    public int count(String dateField, Map<String, ? extends Object> filterMap) {
//        String acronym = "COUNT(" + entityClass.getSimpleName().substring(0, 1).toLowerCase() + ")";
//        javax.persistence.Query query = getEntityManager().createQuery(buildQuery(acronym, null, filterMap));
//        Object fromDate = filterMap.get(dateField);
//        Object toDate = filterMap.get(dateField + "1");
//        if (fromDate instanceof Date) {
//            query.setParameter(1, (Date) fromDate, TemporalType.TIMESTAMP);
//            query.setParameter(2, toDate instanceof Date ? (Date) toDate : new Date(), TemporalType.TIMESTAMP);
//        }
//        return ((Long) query.getSingleResult()).intValue();
//    }
    public List<T> search(int first, int size, Map<String, SortOrder> sortOrderMap, Map<String, ? extends Object> filterMap, String... returnFields) {
        javax.persistence.Query query = getEntityManager().createQuery(buildQuery(concatFields(returnFields, null), sortOrderMap, filterMap));
        query.setMaxResults(size);
        query.setFirstResult(first);
        return query.getResultList();
    }

//    public List<T> search(int first, int size, String dateField, Map<String, SortOrder> sortOrderMap, Map<String, ? extends Object> filterMap, String... returnFields) {
//        javax.persistence.Query query = getEntityManager().createQuery(buildQuery(concatFields(returnFields, null), sortOrderMap, filterMap));
//        query.setMaxResults(size);
//        query.setFirstResult(first);
//        Object fromDate = filterMap.get(dateField);
//        Object toDate = filterMap.get(dateField + "1");
//        if (fromDate instanceof Date) {
//            query.setParameter(1, (Date) fromDate, TemporalType.TIMESTAMP);
//            query.setParameter(2, toDate instanceof Date ? (Date) toDate : new Date(), TemporalType.TIMESTAMP);
//        }
//        return query.getResultList();
//    }
    public List search(Map<String, SortOrder> sortOrderMap, Map<String, ? extends Object> filterMap, String... returnFields) {
        javax.persistence.Query q = getEntityManager().createQuery(buildQuery(concatFields(returnFields, null), sortOrderMap, filterMap));
        return q.getResultList();
    }

    /**
     * build jpql guery by hashmap parameter this method does not include entity
     * state in the search mainly used by date search
     *
     * @param filterMap
     * @param sortOrderMap
     * @param returnValue
     * @return
     */
    public String buildQuery(String returnValue, Map<String, SortOrder> sortOrderMap, Map<String, ? extends Object> filterMap) {

        String acronym = entityClass.getSimpleName().substring(0, 1).toLowerCase();
        if (returnValue == null || returnValue.isEmpty()) {
            returnValue = acronym;
        }
        String query = "SELECT " + returnValue + " FROM " + entityClass.getSimpleName() + " " + acronym + " ";
        boolean ordered = false;
        if (filterMap != null && !filterMap.isEmpty()) {
            int i = 0;
            String qlquery = null;
            for (Map.Entry<String, ? extends Object> entry : filterMap.entrySet()) {
                if (entry == null) {
                    continue;
                }
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null && key == null) {
                    continue;
                }
                if (value instanceof Date) {
                    qlquery = qlquery == null ? " where " : (i > 0) ? qlquery : qlquery + " and ";
                    if (filterMap.containsKey(key + "1")) {
                        qlquery = qlquery + acronym + "." + key + " BETWEEN ?1 AND ?2";
                    }
                    i = i > 0 ? 0 : 1;
                } else {
                    qlquery = qlquery == null ? " where " : qlquery + " and ";
                    if (value == null) {
                        qlquery = qlquery + key;
                    } else if (value instanceof String) {
                        qlquery = qlquery + acronym + "." + key + " Like " + "\'%" + escapeSQL(value.toString()) + "%\' ";
                    } else {
                        qlquery = qlquery + acronym + "." + key + "=" + value;
                    }
                }
            }
            query += (qlquery != null ? qlquery : "");
        }
        if (sortOrderMap != null && !sortOrderMap.isEmpty()) {
            Set<Map.Entry<String, SortOrder>> entrySet = sortOrderMap.entrySet();

            for (Map.Entry<String, SortOrder> entry : sortOrderMap.entrySet()) {
                if (entry == null) {
                    continue;
                }
                String orderField = entry.getKey();
                SortOrder sortOrder = entry.getValue();
                if (orderField != null && sortOrder != null) {
                    query = ordered ? (query + " , " + acronym + "." + orderField) : (query + " ORDER BY " + acronym + "." + orderField)
                            + (sortOrder == SortOrder.ASCENDING ? " ASC" : (sortOrder == SortOrder.DESCENDING ? " DESC" : ""));
                }
            }

        }
        AppUtil.log(this.getClass(), "Excecuting Query >>>>> " + query);
        return query;
    }

    private String concatFields(String[] fields, String def) {
        if (fields == null || fields.length == 0) {
            return def;
        }
        StringBuilder sb = null;
        for (String field : fields) {
            if (sb == null) {
                sb = new StringBuilder();
                sb.append(field);
            } else {
                sb.append(",");
                sb.append(field);
            }
        }
        return sb == null ? def : sb.toString();
    }

    private void logActivity(T entity, ActivityLog log, int statusCode) {
        //log(entity, log, statusCode, StatusFlag.ACTIVITY_TYPE);
    }

    private void log(T entity, ActivityLog log, int statusCode, String statusType) {
        if (log.getEntityID() == null && entity != null && entity instanceof com.nibss.nipreport.model.Entity) {
            Object entityId = ((com.nibss.nipreport.model.Entity) entity).getEntityId();
            log.setEntityID(entityId != null ? entityId.toString() : null);
        }
        ActivityLogFacade.instance().save(log, statusCode, statusType);
    }

    public static String escapeSQL(String search) {
        if (search == null) {
            return null;
        }
        char[] searchChars = search.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char searchChar : searchChars) {
            if (isSpecialChar(searchChar)) {
                sb.append('\\');
            }
            sb.append(searchChar);
        }
        return sb.toString();
    }

    public static String sqlLike(String name, String value) {
        return name + " Like " + "\'%" + escapeSQL(value) + "%\' ";
    }
    private static final char[] SPECIAL_CHARS = new char[]{'_', '%', '\\', '\'', '{', '}', '&', '?', '(', ')', '[', ']',
        '-', ';', '~', '|', '$', '!', '<', '*', '"'};

    public static boolean isSpecialChar(char value) {
        if (SPECIAL_CHARS == null) {
            return !Character.isLetterOrDigit(value);
        }
        for (char val : SPECIAL_CHARS) {
            if (val == value) {
                return true;
            }
        }
        return false;
    }
}
