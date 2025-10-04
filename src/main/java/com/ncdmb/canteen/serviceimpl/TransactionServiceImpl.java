package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.dtos.request.MealLineItemRequestDto;
import com.ncdmb.canteen.dtos.request.TransactionRequestDto;
import com.ncdmb.canteen.dtos.response.*;
import com.ncdmb.canteen.entity.*;
import com.ncdmb.canteen.iservice.TransactionService;
import com.ncdmb.canteen.repository.*;
import com.ncdmb.canteen.util.DateRange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ncdmb.canteen.util.DateRange.getDateRange;

@Service
@RequiredArgsConstructor

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final StaffTransactionRepository staffTransactionRepository;
    private final NonStaffTransactionRepository nonStaffTransactionRepository;
    private final MealLineItemRepository mealLineItemRepository;
    private final CanteenUserRepository canteenUserRepository;
    private final NCDMBStaffRepository ncdmbStaffRepository;
    private final MealRepository mealRepository;
    private final CadreRepository cadreRepository;
    private final CanteenRepository canteenRepository;

    @Override
    @Transactional
    public ResponseEntity<?> createTransaction(TransactionRequestDto dto) {

        BigDecimal totalAmount = BigDecimal.ZERO; // Represents 0
        Transaction transaction = new Transaction();
        BigDecimal staffPaid = BigDecimal.ZERO;
        BigDecimal ncdmbPaid = BigDecimal.ZERO;
        transaction.setTotalAmount(dto.getTotalAmount());


        transaction.setTicketId(dto.getTicketId());
        transaction.setCanteen(canteenRepository.findById(dto.getCanteenId()).get());
        transaction.setOperator(canteenUserRepository.findById(dto.getOperatorId()).get());
        Optional<NCDMBStaff> staff;

        if(canteenRepository.findById(dto.getCanteenId()).isEmpty())
        {
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Canteen not found").build());
        }

        if(canteenUserRepository.findByIdAndCanteen_Id(dto.getOperatorId(),dto.getCanteenId()).isEmpty())
        {
            return ResponseEntity.ok(OperationalResponse.builder().message("Operator not found").success(false).build());
        }

        if (dto.getIsStaffTransaction())
        {
            staff = ncdmbStaffRepository.findByStaffId(dto.getStaffId());
            if(staff.isEmpty())
            {
                return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Staff not found").build());
            }
        }

        transaction = transactionRepository.save(transaction);

        for (MealLineItemRequestDto itemDto : dto.getMealLineItems()) {
            MealLineItem lineItem = new MealLineItem();
            lineItem.setTransaction(transaction);
            lineItem.setMeal(mealRepository.findById(itemDto.getMealId())
                    .orElseThrow(() -> new IllegalArgumentException("Meal not found"))); // Or fetch full Meal entity if you need validation
            lineItem.setQuantity(itemDto.getQuantity());
            lineItem.setPricePerUnit(itemDto.getPricePerUnit());
            BigDecimal lineItemTotal = itemDto.getPricePerUnit().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            lineItem.setTotalAmount(lineItemTotal);
            totalAmount = totalAmount.add(lineItemTotal);
            mealLineItemRepository.save(lineItem);
        }

        // Handle Staff or NonStaff Transaction
        if (Boolean.TRUE.equals(dto.getIsStaffTransaction())) {

            NCDMBStaff ncdmbStaff = ncdmbStaffRepository.findByStaffId(dto.getStaffId()).get();
            Cadre cadre = cadreRepository.findById(ncdmbStaff.getCadre().getId()).get();
            StaffTransaction staffTx = new StaffTransaction();
            staffTx.setTransaction(transaction);

            if(!hasStaffTransactedToday(dto.getStaffId()).isSuccess())
            {
                staffPaid = totalAmount.multiply(cadre.getStaffPercent().multiply(BigDecimal.valueOf(0.01)));
                ncdmbPaid = totalAmount.multiply(cadre.getNcdmbPercent().multiply(BigDecimal.valueOf(0.01)));
                staffTx.setStaffPaid(staffPaid);
                staffTx.setNcdmbPaid(ncdmbPaid);
                staffTx.setStaff(ncdmbStaff);
            }else{

                staffPaid = totalAmount;
                staffTx.setStaffPaid(totalAmount);
                staffTx.setNcdmbPaid(BigDecimal.ZERO);
                staffTx.setStaff(ncdmbStaff);
            }

            staffTransactionRepository.save(staffTx);
        } else {
            NonStaffTransaction nonStaffTx = new NonStaffTransaction();
            nonStaffTx.setTransaction(transaction);
            nonStaffTransactionRepository.save(nonStaffTx);
        }

        // Build Response
        TransactionResponseDto response = new TransactionResponseDto();
        response.setTransactionId(transaction.getId());
        response.setTotalAmount(transaction.getTotalAmount());
        response.setTicketId(transaction.getTicketId());
        response.setCreatedAt(transaction.getCreatedAt());
        //
        response.setIsStaffTransaction(dto.getIsStaffTransaction());
        if(dto.getIsStaffTransaction())
        {
            response.setStaffId(dto.getStaffId());
            response.setStaffPaid(staffPaid);
            response.setNcdmbPaid(ncdmbPaid);
        }

        //

        response.setMealLineItems(
                dto.getMealLineItems().stream().map(item -> {
                    MealLineItemResponseDto lineItemResp = new MealLineItemResponseDto();

                    lineItemResp.setMealName(mealRepository.findById(item.getMealId()).get().getName()); // If you fetch full Meal entity
                    lineItemResp.setQuantity(item.getQuantity());
                    lineItemResp.setPricePerUnit(item.getPricePerUnit());
                    lineItemResp.setTotalAmount(item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())));
                    return lineItemResp;
                }).collect(Collectors.toList())
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public List<AmountAndDateDto> getAmountsAndDatesByPeriod(String period) {

        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        switch (period.toLowerCase()) {
            case "today" -> startDate = LocalDate.now().atStartOfDay();
            case "yesterday" -> {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                startDate = yesterday.atStartOfDay();
                endDate = yesterday.atTime(LocalTime.MAX);
            }
            case "this_week" -> startDate = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
            case "last_week" -> {
                LocalDate mondayLastWeek = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1);
                startDate = mondayLastWeek.atStartOfDay();
                endDate = mondayLastWeek.plusDays(6).atTime(LocalTime.MAX);
            }
            case "this_month" -> startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();

            case "last_month" -> {
                LocalDate firstDayLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                startDate = firstDayLastMonth.atStartOfDay();
                endDate = firstDayLastMonth.withDayOfMonth(firstDayLastMonth.lengthOfMonth()).atTime(LocalTime.MAX);
            }
            case "this_year" -> startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1).atStartOfDay();
            case "last_year" -> {
                int lastYear = LocalDate.now().getYear() - 1;
                startDate = LocalDate.of(lastYear, 1, 1).atStartOfDay();
                endDate = LocalDate.of(lastYear, 12, 31).atTime(LocalTime.MAX);
            }
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        }

        return transactionRepository.findTotalAmountsWithDateBetween(startDate, endDate);
    }

    @Override
    public List<StaffTransactionDto> getAllStaffTransactions(String period) {
        DateRange range = getDateRange(period);
       return transactionRepository.findAllStaffTransactions( range.getStart(), range.getEnd());

    }

    @Override
    public List<NCDMBStaffAndNonStaffCustomersDto> getAllTransactionsPerCanteen(Integer canteenId,String period) {
        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        ArrayList<NCDMBStaffAndNonStaffCustomersDto> result = new ArrayList<>();
        if(canteenOptional.isEmpty()){
            return List.of();
        }
        DateRange range = getDateRange(period);
        List<StaffTransactionDto> staffTransactionDto = transactionRepository.findAllStaffTransactionsByCanteenId(canteenId,range.getStart(),range.getEnd());
        NCDMBStaffAndNonStaffCustomersDto field;
        for(StaffTransactionDto dto: staffTransactionDto){
            field = new NCDMBStaffAndNonStaffCustomersDto(dto.totalAmount(),dto.transactionDate(), dto.staffName(), dto.staffPaid(),dto.ncdmbPaid(), dto.canteenName(),dto.id());
            result.add(field);
        }
        List<Transaction> nonCustomerstxn = transactionRepository.findNonStaffTransactionsByCanteenId(canteenId,range.getStart(),range.getEnd());
        for(Transaction txn: nonCustomerstxn){
            field = new NCDMBStaffAndNonStaffCustomersDto(txn.getTotalAmount(),txn.getCreatedAt(), "NON-NCDMB-CUSTOMER", null,null, txn.getCanteen().getName(), txn.getId());
            result.add(field);
        }
        return result;
    }

    @Override
    public ResponseEntity<?> getAllStaffTransactionsByCadre(Integer cadreId) {
        Optional<Cadre> cadreOptional = cadreRepository.findById(cadreId);
        if(cadreOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Cadre not found").build());
        return ResponseEntity.ok(transactionRepository.findAllStaffTransactionsByCadre(cadreId));
    }

    @Override
    public ResponseEntity<?> getAllStaffTransactionsByCanteen(Integer canteenId,String period) {
        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if(canteenOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Canteen not found").build());
        DateRange range = getDateRange(period);
        return ResponseEntity.ok(transactionRepository.findAllStaffTransactionsByCanteen(canteenId,range.getStart(),range.getEnd()));
    }

    @Override
    public ResponseEntity<?> getAllStaffTransactionsByCanteenAndCadre(Integer cadreId, Integer canteenId) {

        Optional<Cadre> cadreOptional = cadreRepository.findById(cadreId);
        if(cadreOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Cadre not found").build());

        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if(canteenOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Canteen not found").build());

        return ResponseEntity.ok(transactionRepository.findAllStaffTransactionsByCanteenAndCadre(cadreId,canteenId));
    }

    @Override
    public ResponseEntity<?> getTotalAmountsWithDateBetweenAndCadre(String period, Integer cadreId) {
        Optional<Cadre> cadreOptional = cadreRepository.findById(cadreId);

        if(cadreOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Cadre not found").build());

        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        switch (period.toLowerCase()) {
            case "today" -> startDate = LocalDate.now().atStartOfDay();
            case "yesterday" -> {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                startDate = yesterday.atStartOfDay();
                endDate = yesterday.atTime(LocalTime.MAX);
            }
            case "this_week" -> startDate = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
            case "last_week" -> {
                LocalDate mondayLastWeek = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1);
                startDate = mondayLastWeek.atStartOfDay();
                endDate = mondayLastWeek.plusDays(6).atTime(LocalTime.MAX);
            }
            case "this_month" -> startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            case "last_month" -> {
                LocalDate firstDayLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                startDate = firstDayLastMonth.atStartOfDay();
                endDate = firstDayLastMonth.withDayOfMonth(firstDayLastMonth.lengthOfMonth()).atTime(LocalTime.MAX);
            }
            case "this_year" -> startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1).atStartOfDay();
            case "last_year" -> {
                int lastYear = LocalDate.now().getYear() - 1;
                startDate = LocalDate.of(lastYear, 1, 1).atStartOfDay();
                endDate = LocalDate.of(lastYear, 12, 31).atTime(LocalTime.MAX);
            }
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        }

        return ResponseEntity.ok(transactionRepository.findTotalAmountsWithDateBetweenAndCadre(startDate,endDate,cadreId));
    }

    @Override
    public OperationalResponse hasStaffTransactedToday(String staffId) {

        Optional<NCDMBStaff> staffOptional = ncdmbStaffRepository.findByStaffId(staffId);
        if(staffOptional.isEmpty())
            return OperationalResponse.builder().message("staff id: " + staffId + " not found").success(false).build();
        if(transactionRepository.hasStaffTransactedToday(staffId))
            return OperationalResponse.builder().success(true).message("staff with id: "+ staffId+ " has transacted today").build();

        return OperationalResponse.builder().success(false).message("staff with id: "+ staffId+ " has not transacted today").build();

    }

    @Override
    public ResponseEntity<?> getNonStaffTransactionsByCanteen(Integer canteenId) {
        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if(canteenOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Canteen not found").build());

        List<Transaction> nonCustomerstxn = transactionRepository.findNonStaffTransactionsByCanteen(canteenId);
        List<TransactionDto> dtos = new ArrayList<>();
        for(Transaction transaction: nonCustomerstxn)
        {
            TransactionDto dto = new TransactionDto();
            dto.setCanteenName(transaction.getCanteen().getName());
            dto.setId(transaction.getId());
            dto.setCreatedAt(transaction.getCreatedAt());
            dto.setTotalAmount(transaction.getTotalAmount());
            dto.setOperatorId(transaction.getOperator().getId());
            dtos.add(dto);
        }
       return ResponseEntity.ok(dtos);
    }

    @Override
    public TotalPerPeriod getTotalAmountMadePerPeriod(String period, Integer canteenId) {

        DateRange range = getDateRange(period);
        BigDecimal total = transactionRepository.getTotalAmountByCanteenAndPeriod(
                canteenId,
                range.getStart(),
                range.getEnd()
        );
        return TotalPerPeriod.builder().total(total).period(period).build();
    }

    @Override
    public TotalPerPeriod getTotalAmountSubsidyPerPeriodPerCanteen(String period, Integer canteenId) {

        DateRange range = getDateRange(period);
        BigDecimal total = transactionRepository.getStaffTransactionsTotalByCanteenAndPeriod(
                canteenId,
                range.getStart(),
                range.getEnd()
        );
        return TotalPerPeriod.builder().total(total).period(period).build();
    }

    @Override
    public SummaryDetails getSummaryDetails(Integer canteenId, String period) {
        List<Cadre> cadreList = (List<Cadre>) cadreRepository.findAll();
        ArrayList<SummaryResponse> summaryResponses = new ArrayList<>();
        DateRange range = getDateRange(period);
        BigDecimal grandTotal = BigDecimal.ZERO;

        if(canteenId != null){
            for(Cadre cadre: cadreList){

                BigDecimal total = transactionRepository.sumNcdmbPaidByCadreAndCanteenAndPeriod(cadre.getId(),canteenId,range.getStart(),range.getEnd());
                if(total != null){
                    SummaryResponse result = SummaryResponse.builder().total(total).cadreName(cadre.getName()).build();
                    summaryResponses.add(result);
                    grandTotal = grandTotal.add(total);
                }

            }
        }else {

            for(Cadre cadre: cadreList){
                BigDecimal total = transactionRepository.sumNcdmbPaidByCadreAndPeriod(cadre.getId(),range.getStart(),range.getEnd());
                if(total != null){
                    SummaryResponse result = SummaryResponse.builder().total(total).cadreName(cadre.getName()).build();
                    summaryResponses.add(result);
                    grandTotal = grandTotal.add(total);
                }
            }
        }
        return SummaryDetails.builder().grandTotalPerPeriod(grandTotal).summaryResponseList(summaryResponses).build();
    }

    @Override
    public TxnResponse getTransactionDetails(Integer txnId) {

        TxnResponse result = new TxnResponse();
        Optional<Transaction> txnOptional = transactionRepository.findById(txnId);
        Optional<StaffTransaction> staffTransactionOptional = staffTransactionRepository.findById(txnId);
        Optional<NonStaffTransaction>nonStaffTransactionOptional = nonStaffTransactionRepository.findById(txnId);

        if(txnOptional.isEmpty())
            return null;

        List<MealLineItem> mealLineItems = mealLineItemRepository.findByTransactionId(txnId);
        ArrayList<MealLineItemResponseDto> meals = new ArrayList<>();

        Transaction txn = txnOptional.get();
        result.setTransactionId(txn.getId());
        result.setTotalAmount(txn.getTotalAmount());
        result.setCanteenName(txn.getCanteen().getName());
        result.setCreatedAt(txn.getCreatedAt());
        result.setCanteenOperatorName(txn.getOperator().getUsername());
        MealLineItemResponseDto mealLineItemResponseDto;
        for(MealLineItem item: mealLineItems){

            mealLineItemResponseDto = new MealLineItemResponseDto(item.getMeal().getName(), item.getQuantity(), item.getPricePerUnit(),item.getTotalAmount());
            meals.add(mealLineItemResponseDto);

        }
        result.setMealLineItems(meals);
        if(nonStaffTransactionOptional.isEmpty()){
            StaffTransaction staff = staffTransactionOptional.get();
            StaffObj obj = new StaffObj();
            obj.setCadre(staff.getStaff().getCadre().getName());
            obj.setNCDMBStaffName(staff.getStaff().getName());
            result.setTransactedBy(obj);
        }else {
            result.setTransactedBy("NON_NCDMB_STAFF_CUSTOMER");
        }

        return result;
    }

    @Override
    public ResponseEntity<?> getTotalAmountPerCadrePerPeriodAcrossAllCanteens(String period, Integer cadreId) {

        Optional<Cadre> cadreOptional = cadreRepository.findById(cadreId);
        if(cadreOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Cadre not found").build());
        DateRange range = getDateRange(period);
        BigDecimal total = transactionRepository.sumNcdmbPaidByCadreAndPeriod(cadreId,range.getStart(),range.getEnd());

        return ResponseEntity.ok(TotalPerPeriod.builder().total(total).period(period).build());
    }

    @Override
    public ResponseEntity<?> getTotalAmountPerCadrePerPerPeriodPerCanteen(String period, Integer cadreId, Integer canteenId) {

        Optional<Cadre> cadreOptional = cadreRepository.findById(cadreId);
        if(cadreOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Cadre not found").build());

        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if(canteenOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Canteen not found").build());

        DateRange range = getDateRange(period);
        BigDecimal total = transactionRepository.sumNcdmbPaidByCadreAndCanteenAndPeriod(cadreId,canteenId,range.getStart(),range.getEnd());

        return ResponseEntity.ok(TotalPerPeriod.builder().total(total).period(period).build());
    }

}
