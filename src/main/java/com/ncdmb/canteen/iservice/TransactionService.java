package com.ncdmb.canteen.iservice;

import com.ncdmb.canteen.dtos.request.TransactionRequestDto;
import com.ncdmb.canteen.dtos.response.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface TransactionService {

   ResponseEntity<?> createTransaction(TransactionRequestDto dto);
   List<AmountAndDateDto> getAmountsAndDatesByPeriod(String period);
   List<StaffTransactionDto> getAllStaffTransactions(String period);
   List<NCDMBStaffAndNonStaffCustomersDto> getAllTransactionsPerCanteen(Integer canteenId,String period);


   ResponseEntity<?> getAllStaffTransactionsByCadre(Integer cadreId);

   ResponseEntity<?> getAllStaffTransactionsByCanteen( Integer canteenId,String period);

   ResponseEntity<?> getAllStaffTransactionsByCanteenAndCadre( Integer cadreId, Integer canteenId);

  ResponseEntity<?> getTotalAmountsWithDateBetweenAndCadre(String period, Integer cadreId);
   OperationalResponse hasStaffTransactedToday( String staffId);

  ResponseEntity<?> getNonStaffTransactionsByCanteen(Integer canteenId);

  TotalPerPeriod getTotalAmountMadePerPeriod(String period, Integer canteenId);
  TotalPerPeriod getTotalAmountSubsidyPerPeriodPerCanteen(String period, Integer canteenId);

  ResponseEntity<?> getTotalAmountPerCadrePerPeriodAcrossAllCanteens(String period, Integer cadreId);
  ResponseEntity<?> getTotalAmountPerCadrePerPerPeriodPerCanteen(String period, Integer cadreId,Integer canteenId);
  SummaryDetails getSummaryDetails(Integer canteenId,String period);

  TxnResponse getTransactionDetails(Integer transaction);



}
