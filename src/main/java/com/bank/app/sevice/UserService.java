package com.bank.app.sevice;


import com.bank.app.data.entity.CustomerAccount;
import com.bank.app.data.entity.Deposits;
import com.bank.app.data.entity.User;
import com.bank.app.data.entity.Withdrawals;
import com.bank.app.domain.model.UserCreateForm;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUsername(String username);

    List<CustomerAccount> findCustomerAccountById(long id);

    User registerUser(UserCreateForm userCreateForm);

    Optional<User> findUserByEmail(String email);

    List<Withdrawals> findTransactionsByCustomerAccountId(long id);

    List<Deposits> findTransactionsByAccountId(long id);

    boolean hasValidPassword(User user, String pwd);

}
