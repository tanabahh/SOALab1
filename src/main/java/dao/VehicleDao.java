package dao;

import exception.BadRequestException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Subquery;
import model.GroupByCreationDateDto;
import org.hibernate.query.Query;
//import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.Coordinates;
import model.FuelType;
import model.QueryAdditions;
import model.TableQueryAdditions;
import model.Vehicle;
import model.VehicleType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

public class VehicleDao {

    public Vehicle findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Vehicle.class, id);
    }

    public void update(Vehicle vehicle) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(vehicle);
        tx1.commit();
        session.close();
    }

    public void delete(Vehicle vehicle) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(vehicle);
        tx1.commit();
        session.close();
    }

    public void deleteWhereEnginePower(Long enginePower) {
        Transaction transaction = null;
        try {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> criteriaQuery = criteriaBuilder.createQuery(Vehicle.class);
            Root<Vehicle> root = criteriaQuery.from(Vehicle.class);
            CriteriaQuery<Vehicle> query = criteriaQuery
                .where(criteriaBuilder.equal(root.get("enginePower"), enginePower)); //поменять
            List<Vehicle> vehicles = session.createQuery(query).getResultList();

            vehicles.forEach(session::delete);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<GroupByCreationDateDto> groupByCreationDate() {
        Transaction transaction = null;
        List<GroupByCreationDateDto> result = null;
        try {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<GroupByCreationDateDto> criteriaQuery = criteriaBuilder.createQuery(GroupByCreationDateDto.class);
            Root<Vehicle> root = criteriaQuery.from(Vehicle.class);
            criteriaQuery.multiselect(
                root.get("creationDate").as(java.sql.Date.class),
                criteriaBuilder.count(root)
            );

            criteriaQuery.groupBy(root.get("creationDate").as(java.sql.Date.class));

            TypedQuery<GroupByCreationDateDto> query = session.createQuery(criteriaQuery);
            result = query.getResultList();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }


        return result;
    }

    public Vehicle getWithMaxCreatingDate() {
        Transaction transaction = null;
        Vehicle vehicle = null;
        try {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> criteriaQuery = criteriaBuilder.createQuery(Vehicle.class);
            Root<Vehicle> root = criteriaQuery.from(Vehicle.class);
            CriteriaQuery<Vehicle> selectQuery = criteriaQuery.select(root);

            QueryAdditions queryAdditions = new QueryAdditions(TableQueryAdditions.VEHICLE, "creationDate", false);
            List<QueryAdditions> sortAdditions = new ArrayList<>();
            sortAdditions.add(queryAdditions);

            addAdditionsToQuery(sortAdditions, root, true, criteriaQuery, criteriaBuilder);
            List<Vehicle> vehicles = session.createQuery(selectQuery).getResultList();
            vehicle = vehicles.get(0);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return vehicle;
    }

    public List<Vehicle> findAll(
        Integer page,
        Integer perPage,
        List<QueryAdditions> sortAdditions,
        List<QueryAdditions> filterAdditions
    ) throws BadRequestException {
        List<Vehicle> vehicles = null;
        Transaction transaction = null;
        try {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> query = criteriaBuilder.createQuery(Vehicle.class);
            Root<Vehicle> root = query.from(Vehicle.class);
            CriteriaQuery<Vehicle> selectQuery = query.select(root);
            if (sortAdditions.size() > 0) {
                addAdditionsToQuery(sortAdditions, root, true, query, criteriaBuilder);
            }
            if (filterAdditions.size() > 0) {
                addAdditionsToQuery(filterAdditions, root, false, query, criteriaBuilder);
            }
            if (page != null && perPage != null) {
                vehicles = session.createQuery(selectQuery).setFirstResult((page - 1) * perPage).setMaxResults(perPage).getResultList();
            } else {
                vehicles = session.createQuery(selectQuery).getResultList();
            }
            transaction.commit();
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        return vehicles;
    }

    private void addAdditionsToQuery(
        List<QueryAdditions> additions,
        Root<Vehicle> root,
        Boolean isSort,
        CriteriaQuery<Vehicle> resultQuery,
        CriteriaBuilder criteriaBuilder
    ) throws BadRequestException {
        Join<Vehicle, Coordinates> coordinatesJoin = root.join("coordinates");
        ArrayList<Order> orders = new ArrayList<>(Collections.emptyList());
        List<Predicate> conditions = new ArrayList<>(Collections.emptyList());
        for(QueryAdditions addition: additions) {
            Path<String> path;
            if (addition.table == TableQueryAdditions.VEHICLE) {
                path = root.get(addition.column);
            } else if (addition.table == TableQueryAdditions.COORDINATES) {
                path = coordinatesJoin.get(addition.column);
            } else {
                throw new BadRequestException(String.format("%s не соответствует ни одному названию таблиц", addition.table));
            }

            try {
                if (isSort) {
                    if (addition.isAsc) {
                        orders.add(criteriaBuilder.asc(path));
                    } else {
                        orders.add(criteriaBuilder.desc(path));
                    }
                } else {
                    if (addition.column.equals("creationDate")) {

                        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        conditions.add(criteriaBuilder.equal(
                            criteriaBuilder.function("DATE_TRUNC", java.sql.Date.class,
                                criteriaBuilder.literal("day"), path),
                            criteriaBuilder.function("TO_DATE", java.sql.Date.class,
                                criteriaBuilder.literal(addition.firstValue),
                                criteriaBuilder.literal("yyyy-mm-dd"))));
//
//                        conditions.add(criteriaBuilder.equal(
//                            criteriaBuilder.function("TRUNC", Date.class, path),
//                            criteriaBuilder.function("TO_DATE", String.class,
//                                criteriaBuilder.parameter(String.class, addition.firstValue),
//                                criteriaBuilder.literal("yyyy-mm-dd"))));

                        //conditions.add(criteriaBuilder.equal(path, LocalDate.parse(addition.firstValue, formatter)));
                    } else if (addition.column.equals("type")) {
                        conditions.add(
                            criteriaBuilder.equal(path, VehicleType.valueOf(addition.firstValue)));
                    } else if (addition.column.equals("fuelType")) {
                        conditions.add(
                            criteriaBuilder.equal(path, FuelType.valueOf(addition.firstValue)));
                    } else {
                        conditions.add(criteriaBuilder.equal(path, addition.firstValue));
                    }
                }
            } catch (Exception ex) {
                throw new BadRequestException("${addition.firstValue} -- неправильный формат данных для столбца ${addition.column} таблицы ${addition.table} ");
            }
        }
        if (isSort) {
            resultQuery.orderBy(orders);
        } else {
            resultQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));
        }
    }

    public void save(Vehicle vehicle) {
        Transaction transaction = null;
        try {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            if (transaction == null || session == null) {
                return;
            }
            session.save(vehicle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
