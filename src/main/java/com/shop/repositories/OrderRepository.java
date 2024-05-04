package com.shop.repositories;

import com.shop.models.ColorProduct;
import com.shop.models.Order;
import com.shop.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
    @Query("SELECT o FROM orders o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR o.user.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword%)  and " +
            "(:status IS NULL OR :status = '' OR o.status = :status) " +
            "order by o.id desc ")
    Page<Order> searchOrderByKeyword(@Param("keyword") String keyword, @Param("status") OrderStatus status, Pageable pageable);
    @Query("SELECT count(o.id) as count FROM orders o WHERE " +
            "date(o.orderDate) = :date ")
    List<Object> statisticalCountOrderByDay(@Param("date") Date date);
    @Query("SELECT date(o.orderDate) as date,sum(o.totalMoney) as total FROM orders o WHERE " +
            "date(o.orderDate) = :date " +
            "group by  date(o.orderDate)")
    List<Object> statisticalEarningByDay(@Param("date") Date date);
    @Query("SELECT day(o.orderDate) as day,sum(o.totalMoney) as total FROM orders o WHERE " +
            "month(o.orderDate) = :month and year(o.orderDate) = :year " +
            "group by day(o.orderDate)")
    List<Object> statisticalEarningByMonth(@Param("month") String month,@Param("year") String year);
    @Query("SELECT month(o.orderDate) as month,sum(o.totalMoney) as total FROM orders o WHERE " +
            "year(o.orderDate) = :year " +
            "group by  month(o.orderDate) " +
            "order by month(o.orderDate) asc")
    List<Object> statisticalEarningByYear(@Param("year") String year);
    @Query(" select year(o.orderDate),month(o.orderDate) from orders o " +
            "group by year(o.orderDate),month(o.orderDate)\n" +
            "order by month(o.orderDate) asc")
    List<Object> getMonthOfYear();


}
