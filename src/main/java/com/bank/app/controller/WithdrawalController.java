package com.bank.app.controller;

import com.bank.app.data.entity.CustomerAccount;
import com.bank.app.data.entity.Withdrawals;
import com.bank.app.domain.model.WithdrawalForm;
import com.bank.app.repository.CustomerAccountRepository;
import com.bank.app.repository.WithdrawalRepository;
import com.bank.app.security.CustomUserDetails;
import com.bank.app.sevice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class WithdrawalController {
  @Autowired private UserService userService;

  @Autowired private CustomerAccountRepository customerAccountRepository;

  @Autowired private WithdrawalRepository withdrawalRepository;

  @Autowired private WithdrawalForm withdrawalForm;

  @ModelAttribute("currentCustomerAccounts")
  public List<CustomerAccount> getCurrentCustomerAccounts(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    return userService.findCustomerAccountById(customUserDetails.getId());
  }

  @ModelAttribute("addWithdrawalForm")
  public WithdrawalForm getWithdrawalForm() {
    return withdrawalForm;
  }

  @GetMapping("/withdrawal/transactions")
  public String getWithdrawalTransactions() {
    return "withdrawal-transactions";
  }

  @GetMapping("/withdrawals/account/{id}")
  public String getWithdrawalAccountIdTransactions(
      @PathVariable("id") long accountId, Model model) {
    List<Withdrawals> withdrawalsList =
        withdrawalRepository.findTransactionsByCustomerAccountId(accountId);
    model.addAttribute("withdrawalsList", withdrawalsList);
    withdrawalForm.setCustomerAccountId(accountId);
    return "customer-withdrawal-transactions";
  }

  @PostMapping("/withdrawal")
  public String processWithdrawal(
      @ModelAttribute("addWithdrawalForm") WithdrawalForm withdrawalForm, Model model) {

    long customerAccountId = withdrawalForm.getCustomerAccountId();
    var iban = withdrawalForm.getIban();
    var totalBalanceSum = withdrawalForm.getCustomerAccountBalance();

    var purpose = withdrawalForm.getPurpose();

    CustomerAccount customerAccount =
        customerAccountRepository.findCustomerAccountById(customerAccountId);

    if (customerAccount != null && customerAccount.getCustomerAccountBalance() >= totalBalanceSum) {
      Withdrawals withdrawals = new Withdrawals(customerAccount, iban, totalBalanceSum, purpose);
      customerAccount.setCustomerAccountBalance(
          customerAccount.getCustomerAccountBalance() - totalBalanceSum);

      customerAccountRepository.save(customerAccount);
      withdrawalRepository.save(withdrawals);

      model.addAttribute("successfulTransaction", "successfulTransaction");
    } else {

      model.addAttribute("insufficientFundsError", "insufficientFundsError");
    }

    return "withdrawal-transactions";
  }
}
