package com.ncdmb.canteen.repository;

import com.ncdmb.canteen.dtos.response.AmountAndDateDto;
import com.ncdmb.canteen.dtos.response.StaffTransactionDto;
import com.ncdmb.canteen.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.StaffTransactionDto(
        t.id,
        t.totalAmount,
        t.createdAt as transactionDate,
        s.name as staffName,
        st.staffPaid,
        st.ncdmbPaid,
        c.name as cadreName,
        ct.name as canteenName,
        s.staffId
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN t.canteen ct
    JOIN st.staff s
    JOIN s.cadre c
    WHERE st.ncdmbPaid > 0
      AND (:startDate IS NULL OR :endDate IS NULL OR t.createdAt BETWEEN :startDate AND :endDate)
    """)
    List<StaffTransactionDto> findAllStaffTransactions( @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);


    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.StaffTransactionDto(
        t.id,
        t.totalAmount,
        t.createdAt as transactionDate,
        s.name as staffName,
        st.staffPaid,
        st.ncdmbPaid,
        c.name as cadreName,
        ct.name as canteenName,
        s.staffId
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN t.canteen ct
    JOIN st.staff s
    JOIN s.cadre c
    WHERE c.id = :cadreId
    ORDER BY t.createdAt DESC
    """)
    List<StaffTransactionDto> findAllStaffTransactionsByCadre(@Param("cadreId") Integer cadreId);


    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.StaffTransactionDto(
        t.id,
        t.totalAmount,
        t.createdAt as transactionDate,
        s.name as staffName,
        st.staffPaid,
        st.ncdmbPaid,
        c.name as cadreName,
        ct.name as canteenName,
        s.staffId
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN t.canteen ct
    JOIN st.staff s
    JOIN s.cadre c
    WHERE ct.id = :canteenId
      AND st.ncdmbPaid > 0
      AND t.createdAt BETWEEN :startDate AND :endDate
    ORDER BY t.createdAt DESC
    """)
    List<StaffTransactionDto> findAllStaffTransactionsByCanteen(@Param("canteenId") Integer canteenId,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.StaffTransactionDto(
        t.id,
        t.totalAmount,
        t.createdAt as transactionDate,
        s.name as staffName,
        st.staffPaid,
        st.ncdmbPaid,
        c.name as cadreName,
        ct.name as canteenName,
        s.staffId
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN t.canteen ct
    JOIN st.staff s
    JOIN s.cadre c
    WHERE ct.id = :canteenId
      AND st.ncdmbPaid >= 0
      AND t.createdAt BETWEEN :startDate AND :endDate
    ORDER BY t.createdAt DESC
    """)
    List<StaffTransactionDto> findAllStaffTransactionsByCanteenId(@Param("canteenId") Integer canteenId,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT n.transaction
    FROM NonStaffTransaction n
    JOIN n.transaction t
    WHERE t.canteen.id = :canteenId
          AND t.createdAt BETWEEN :startDate AND :endDate
    ORDER BY t.createdAt DESC
    """)
    List<Transaction> findNonStaffTransactionsByCanteenId(@Param("canteenId") Integer canteenId,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT n.transaction
    FROM NonStaffTransaction n
    JOIN n.transaction t
    WHERE t.canteen.id = :canteenId
    ORDER BY t.createdAt DESC
    """)
    List<Transaction> findNonStaffTransactionsByCanteen(@Param("canteenId") Integer canteenId);


    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.StaffTransactionDto(
        t.id,
        t.totalAmount,
        t.createdAt as transactionDate,
        s.name as staffName,
        st.staffPaid,
        st.ncdmbPaid,
        c.name as cadreName,
        ct.name as canteenName,
        s.staffId
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN t.canteen ct
    JOIN st.staff s
    JOIN s.cadre c
    WHERE (:cadreId IS NULL OR c.id = :cadreId) AND (:canteenId IS NULL OR t.canteen.id = :canteenId)
    ORDER BY t.createdAt DESC
    """)
    List<StaffTransactionDto> findAllStaffTransactionsByCanteenAndCadre( @Param("cadreId") Integer cadreId,
                                                                  @Param("canteenId") Integer canteenId);

    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.AmountAndDateDto(
        t.totalAmount,
        t.createdAt
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    WHERE t.createdAt BETWEEN :startDate AND :endDate
    ORDER BY t.createdAt DESC
    """)
    List<AmountAndDateDto> findTotalAmountsWithDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT new com.ncdmb.canteen.dtos.response.AmountAndDateDto(
        t.totalAmount,
        t.createdAt
    )
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN st.staff s
    JOIN s.cadre c
    WHERE t.createdAt BETWEEN :startDate AND :endDate
      AND c.id = :cadreId
    ORDER BY t.createdAt DESC
    """)
    List<AmountAndDateDto> findTotalAmountsWithDateBetweenAndCadre(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("cadreId") Integer cadreId
    );

    @Query("""
    SELECT COUNT(st) > 0
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN st.staff s
    WHERE s.staffId = :staffId
      AND DATE(t.createdAt) = CURRENT_DATE
    """)
    boolean hasStaffTransactedToday(@Param("staffId") String staffId);

    @Query("""
    SELECT COUNT(st)
    FROM StaffTransaction st
    JOIN st.transaction t
    JOIN st.staff s
    WHERE s.staffId = :staffId
      AND DATE(t.createdAt) = CURRENT_DATE
    """)
    long countStaffTransactionsToday(@Param("staffId") String staffId);

    @Query("""
    SELECT SUM(t.totalAmount)
    FROM Transaction t
    WHERE (:canteenId IS NULL OR t.canteen.id = :canteenId)
      AND (:startDate IS NULL OR :endDate IS NULL OR t.createdAt BETWEEN :startDate AND :endDate)
    """)
    BigDecimal getTotalAmountByCanteenAndPeriod(
            @Param("canteenId") Integer canteenId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT COALESCE(SUM(t.totalAmount), 0)
    FROM StaffTransaction st
    JOIN st.transaction t
    WHERE (:canteenId IS NULL OR t.canteen.id = :canteenId)
      AND t.createdAt BETWEEN :startDate AND :endDate
      AND st.ncdmbPaid > 0
      """)
    BigDecimal getStaffTransactionsTotalByCanteenAndPeriod(
            @Param("canteenId") Integer canteenId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT SUM(st.ncdmbPaid)
    FROM StaffTransaction st
    JOIN st.staff s
    JOIN s.cadre c
    JOIN st.transaction t
    JOIN t.canteen cn
    WHERE c.id = :cadreId
      AND t.createdAt BETWEEN :startDate AND :endDate
      """)
    BigDecimal sumNcdmbPaidByCadreAndPeriod(
            @Param("cadreId") Integer cadreId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query("""
    SELECT SUM(st.ncdmbPaid)
    FROM StaffTransaction st
    JOIN st.staff s
    JOIN s.cadre c
    JOIN st.transaction t
    JOIN t.canteen cn
    WHERE c.id = :cadreId
      AND cn.id = :canteenId
      AND t.createdAt BETWEEN :startDate AND :endDate
      """)
    BigDecimal sumNcdmbPaidByCadreAndCanteenAndPeriod(
            @Param("cadreId") Integer cadreId,
            @Param("canteenId") Integer canteenId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
