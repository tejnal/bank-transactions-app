package com.bank.app.controller;

import com.bank.app.common.Currency;
import com.bank.app.data.entity.CustomerAccount;
import com.bank.app.data.entity.User;
import com.bank.app.domain.model.CustomerAccountForm;
import com.bank.app.repository.CustomerAccountRepository;
import com.bank.app.security.CustomUserDetails;
import com.bank.app.sevice.UserService;
import com.bank.app.utils.CurrencyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private CustomerAccountForm customerAccountForm;

        // it injects data objects in model before JSP loads
    @ModelAttribute("currentCustomerAccounts")
    public List<CustomerAccount> getCurrentCustomerAccounts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.findCustomerAccountById(customUserDetails.getId());
    }

    @ModelAttribute("customerAccountForm")
    public CustomerAccountForm getCustomerAccountForm() {
        return customerAccountForm;

    }

  @GetMapping("/")
  public String index() {

    return "index";
  }

    @PostMapping("/add-customerAccount")
    public String processCustomerAccount(@ModelAttribute("customerAccountForm") CustomerAccountForm customerAccountForm,
                                         @ModelAttribute("currentUser") CustomUserDetails customUserDetails,
                                         Model model){

        Currency currency = CurrencyUtils.convertStringToCurrency(customerAccountForm.getCurrency());
        var username = customUserDetails.getUsername();
        User user = userService.findUserByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User with username=%s was not found", username)));

        CustomerAccount customerAccount = new CustomerAccount(user, customerAccountForm.getCustomerAccountBalance(), currency);
        customerAccountRepository.save(customerAccount);
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);

        }
        return "redirect:/login";
    }
}
