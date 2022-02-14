package com.bank.app.controller;

import com.bank.app.common.Currency;
import com.bank.app.data.entity.CustomerAccount;
import com.bank.app.data.entity.Deposits;
import com.bank.app.domain.model.DepositForm;
import com.bank.app.repository.CustomerAccountRepository;
import com.bank.app.repository.DepositRepository;
import com.bank.app.security.CustomUserDetails;
import com.bank.app.sevice.UserService;
import com.bank.app.utils.CurrencyUtils;
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
public class DepositController {

    @Autowired
    private CustomerAccountRepository customerAccountRepository;


    @Autowired
    private DepositRepository depositRepository;


    @Autowired
    private DepositForm depositForm;

    @Autowired private UserService userService;


    @ModelAttribute("currentCustomerAccounts")
    public List<CustomerAccount> getCurrentCustomerAccounts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.findCustomerAccountById(customUserDetails.getId());
    }



    @ModelAttribute("addDepositForm")
    public DepositForm getDepositForm() {
        return depositForm;
    }

    @GetMapping("/deposit/transactions")
    public String getDepositTransactions(){
        return "deposit-transactions";
    }

    @GetMapping("/deposits/account/{id}")
    public String getCustomerTransactionsById(@PathVariable("id") long accountId, Model model) {
        List<Deposits> depositsList = depositRepository.findTransactionsByCustomerAccountId(accountId);
        model.addAttribute("depositsList", depositsList);
        depositForm.setCustomerAccountId(accountId);
        return "customer-deposit-transactions";
    }

    @PostMapping("/deposit")
    public String processDeposit(@ModelAttribute("addDepositForm") DepositForm depositForm, Model model) {
        var customerAccountId = depositForm.getCustomerAccountId();
        var amount = depositForm.getAmount();
        Currency currency = CurrencyUtils.convertStringToCurrency(depositForm.getCurrency());

        CustomerAccount customerAccount =  customerAccountRepository.findCustomerAccountById(customerAccountId);

        if(customerAccount !=null && customerAccount.getCustomerAccountBalance() > 0) {

            Deposits deposits = new Deposits(customerAccount, currency, amount);
            customerAccount.setCustomerAccountBalance(customerAccount.getCustomerAccountBalance() + amount);
            customerAccountRepository.save(customerAccount);
            depositRepository.save(deposits);

            model.addAttribute("successfulTransaction","successfulTransaction");
        } else {

            model.addAttribute("it reach the max deposit limit", "maxDepositLimitError");
        }

        return "deposit-transactions";

    }




}
