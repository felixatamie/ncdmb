package com.ncdmb.canteen.controllers;

import com.ncdmb.canteen.dtos.request.TransactionRequestDto;
import com.ncdmb.canteen.dtos.response.*;
import com.ncdmb.canteen.iservice.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tx")
@RequiredArgsConstructor

public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create-user") // need
    public ResponseEntity<?> addCadre(@RequestBody TransactionRequestDto dto)
    {
        return transactionService.createTransaction(dto);
    }
    @GetMapping("/all-staff-ncdmb/graph")
    public ResponseEntity<List<AmountAndDateDto>> getAmountsAndDatesByPeriod( @RequestParam String query)
    {
        return ResponseEntity.ok(transactionService.getAmountsAndDatesByPeriod(query));
    }

    @GetMapping("/all-staff-ncdmb") //need
    public ResponseEntity<List<StaffTransactionDto>> getAllStaffTransactionsAcrossCanteens( @RequestParam String period)
    {
        return ResponseEntity.ok(transactionService.getAllStaffTransactions(period));
    }

    @GetMapping("/all-staff-ncdmb/cadre/{cadreId}")
    public ResponseEntity<?> getAllStaffTransactionsByCadre(@PathVariable int cadreId)
    {
        return transactionService.getAllStaffTransactionsByCadre(cadreId);
    }

    @GetMapping("/all-staff-user/canteen/{canteenId}") //need
    public ResponseEntity<?> getAllStaffTransactionsByCanteen(@PathVariable int canteenId,@RequestParam String period)
    {
        return transactionService.getAllStaffTransactionsByCanteen(canteenId,period);
    }

    @GetMapping("/all-customers/canteen/{canteenId}") //need
    public ResponseEntity<List<NCDMBStaffAndNonStaffCustomersDto>> getAllTransactionsByCanteen(@PathVariable int canteenId,@RequestParam String period)
    {
        return ResponseEntity.ok(transactionService.getAllTransactionsPerCanteen(canteenId,period));
    }

    @GetMapping("/non-staff-user/canteen/{canteenId}") //need
    public ResponseEntity<?> getAllTransactionsByNonStaffCustomers(@PathVariable int canteenId)
    {
        return transactionService.getNonStaffTransactionsByCanteen(canteenId);
    }

    @GetMapping("/all-staff-ncdmb/cadre/{cadreId}/canteen/{canteenId}")
    public ResponseEntity<?> getAllStaffTransactionsByCadreAndCanteen(@PathVariable int cadreId,@PathVariable int canteenId)
    {
        return transactionService.getAllStaffTransactionsByCanteenAndCadre(cadreId,canteenId);
    }

    @GetMapping("/all-staff-ncdmb/graph/cadre/{cadreId}")
    public ResponseEntity<?> getTotalAmountsWithDateBetweenAndCadre(@PathVariable int cadreId, @RequestParam String period)
    {
        return transactionService.getTotalAmountsWithDateBetweenAndCadre(period,cadreId);
    }
    @GetMapping("/verify-user/{staffId}") // need
    public ResponseEntity<OperationalResponse> verifyIfStaffHasTransactedToday(@PathVariable String staffId)
    {
        return ResponseEntity.ok(transactionService.hasStaffTransactedToday(staffId));
    }


    @GetMapping("/total-user/canteen/{canteenId}") //need
    public ResponseEntity<TotalPerPeriod> getTotalPerPeriod(@PathVariable int canteenId, @RequestParam String period)
    {
        return ResponseEntity.ok(transactionService.getTotalAmountMadePerPeriod(period, canteenId));
    }

    @GetMapping("/total-ncdmb/canteen/{canteenId}")// need
    public ResponseEntity<TotalPerPeriod> getTotalSubsidyPerPeriodPerCanteen(@PathVariable int canteenId, @RequestParam String period)
    {
        return ResponseEntity.ok(transactionService.getTotalAmountSubsidyPerPeriodPerCanteen(period, canteenId));
    }

    @GetMapping("/ncdmb-summary")
    public ResponseEntity<SummaryDetails> getSummaryPerCanteen(@RequestParam (value = "canteenId", required = false) Integer canteenId,
                                                               @RequestParam String period)
    {
        return ResponseEntity.ok(transactionService.getSummaryDetails(canteenId,period));
    }

    @GetMapping("/details")
    public ResponseEntity<TxnResponse> getATransactionDetails(@RequestParam  Integer txnId)
    {
        return ResponseEntity.ok(transactionService.getTransactionDetails(txnId));
    }

    @GetMapping("/total-ncdmb/cadre/{cadreId}")// need
    public ResponseEntity<?> getTotalSubsidyPerPeriodPerCadreAcrossCanteens(@PathVariable int cadreId, @RequestParam String period)
    {
        return transactionService.getTotalAmountPerCadrePerPeriodAcrossAllCanteens(period, cadreId);
    }

    @GetMapping("/total-ncdmb/canteen/{canteenId}/cadre/{cadreId}")// need
    public ResponseEntity<?> getTotalSubsidyPerPeriodPerCanteenPerCadre(@PathVariable int canteenId,@PathVariable int cadreId, @RequestParam String period)
    {
        return transactionService.getTotalAmountPerCadrePerPerPeriodPerCanteen(period,cadreId, canteenId);
    }


}
